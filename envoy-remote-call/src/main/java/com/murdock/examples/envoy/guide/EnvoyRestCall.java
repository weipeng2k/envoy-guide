package com.murdock.examples.envoy.guide;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * @author weipeng2k 2018年03月20日 下午22:15:54
 */
@RestController
@ConfigurationProperties(prefix = "httpbin")
public class EnvoyRestCall {

    private RestTemplate template = new RestTemplate();

    private String backendHost;

    private int backendPort;

    @RequestMapping(value = "/ip", method = RequestMethod.GET, produces = "text/plain")
    public String execute() throws UnknownHostException {
        String backendServiceUrl = String.format("http://%s:%d/ip", backendHost, backendPort);
        Map<String, String> object = template.getForObject(backendServiceUrl, Map.class);
        InetAddress localHost = InetAddress.getLocalHost();
        object.put("localhost", localHost.getHostAddress());

        return object.toString();
    }

    public void setBackendHost(String backendHost) {
        this.backendHost = backendHost;
    }

    public void setBackendPort(int backendPort) {
        this.backendPort = backendPort;
    }
}
