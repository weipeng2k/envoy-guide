# 微服务2.0技术栈

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;技术发展非常快！眼前很新的技术，转眼间就过时了。当我们谈论微服务架构时，我们已经发现团队的组织结构会极大地影响系统的架构设计。当我们真正开始实施微服务架构时，我们会发现我们需要思考太多的分布式系统相关的问题，需要储备这些领域的相关知识。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;我最近观察到的是：我们正在寻找新的方法来改进和完善实践微服务的技术。换句话说，我们正在做的并不是什么新东西，也许只是为老问题寻找更优雅的解决方案？

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在构建微服务时，我们将深入一个拥有40年研究背景且根植于复杂的自适应系统理论的学科--分布式系统。这么说有些复杂，但是它广泛涉及的概念我们或多或少都听过，而它解决的问题我们应该都不陌生：

* 部署（deployment）
* 交付（delivery）
* 应用程序接口（APIs）
* 版本（versioning）
* 契约（contracts）
* 伸缩/自动伸缩（scaling/autoscaling）
* 服务发现（service discovery）
* 负载均衡（load balancing）
* 路由/适应性路由（routing / adaptive routing）
* 健康检查（health checking）
* 配置（configuration）
* 断路器（circuit breaking）
* 隔水舱（bulk-heads）
* 存活控制（TTL/deadlining）
* 延时追踪（latency tracing）
* 分布式服务追踪（service causal tracing）
* 分布式日志（distributed logging）
* 运行指标暴露和收集（metrics exposure, collection）

> TTL是TCP中Time to live，防止网络包在节点上无线的转发，路由器在处理包时会将其减一，当这个值为零时会被抛弃，其在微服务中对应的概念有些类似超时控制

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**Netflix** 和其他互联网公司在这方面做了不少令人惊讶的工作（比如：开源它们的产品或者发表论文）。它们所实现的东西在前人没有工作的场景下进行，而它们的工作或多或少有重复的内容，这被解释为 “我们的产品和对方有一点点不同”。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;我真正激动的是这些被发明出来的技术中有一些属于未来的技术，能够看到它们以更加优雅的方式解决了之前提到的问题。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;例如，Kubernetes是谷歌和红帽构建的平台，它旨在使用容器技术，为云原生应用提供一组应用级别的运维操作的平台。在此之前，它经历了若干次的开发迭代，使它能够做到极大的简化诸如：服务发现、伸缩和部署相关的问题处理。这些事情并不简单，而超过1000个Committer的产品更是让人臣服于它的热度，如果它能够在5年前推出，就不会有这么多微服务框架尝试解决它已经解决的服务发现、部署、失败转移和负载均衡诸如此类的工作了。

<center>
<img src="https://github.com/weipeng2k/envoy-guide/raw/master/resource/chapter1-2.png" />
</center>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;另一个例子是类似断路器。任何人都可以写一个断路器（而且不在少数），Netflix甚至开源了其断路器（Hystrix库）实现，并作为OSS的一部分发布。Hystrix能够让应用程序具备在进行网络请求时充分保护自己的能力，当下游出现问题，它能够控制故障的范围。缺点也是很明显的（服务发现、跟踪以及Metrics都是如此），它需要开发人员使用正确的库，并正确配置并加以使用，这真的很难。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;我真的很喜欢以不同的方式来解决这个问题。我们需要的是简单并且正确的使用它们，而不用将应用搞的更复杂，比如依赖一大堆框架或者限定在某些语言。

<center>
<img src="https://github.com/weipeng2k/envoy-guide/raw/master/resource/chapter1-3.png" />
</center>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;“更优雅”的解决这个问题，是将这些东西放在客户端代理中，与应用程序一起部署，这种模式称为“sidecar”。Lyft的Envoy项目就是用来实现这种模式，在这个伟大的项目中，提供了一个非常小巧的C++客户端。该客户端用于处理断路器、隔水舱、服务发现、Metrics统计以及分布式跟踪等，这意味着一个Envoy代理会与每个应用程序一起部署（一比一的形式）。这使得应用程序无论使用哪种编程语言都可以利用此功能。应用通过“localhost”与其他服务进行通信，而代理会将请求反向代理给实际的服务端，代理知道如何找到后端服务，并进行自适应路由、重试、跟踪以及限流等。作为一名开发人员，保持自己的应用程序代码干净，依赖简单轻量，并免费获得所有这些便利功能，是一个让人向往的事情。

<center>
<img src="https://github.com/weipeng2k/envoy-guide/raw/master/resource/chapter1-1.jpg" />
</center>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;当前使用REST构建微服务是事实标准，启动一个服务，公开一个REST端点并将其用于服务之间的所有交互。在交互方面，新的趋势和技术不仅仅是显示了新，更重要的是将交互方式变得更加优雅。在REST服务中，对于不兼容的服务变更、服务数据格式的类型安全以及性能（至少基于HTTP 1.1）都比基于二进制的RPC服务要弱。而基于非阻塞的通信框架类似RxJava、Vert.x不断出现，甚至像gRPC这样的RPC框架，让服务的交互变得更加优雅。

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;开源社区以及这些新工具，将更强大的功能以更简单的方式提供给了用户，让我们能用更简单的方式构建更好的应用。
