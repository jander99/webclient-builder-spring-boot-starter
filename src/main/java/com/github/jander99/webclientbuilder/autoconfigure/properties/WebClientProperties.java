package com.github.jander99.webclientbuilder.autoconfigure.properties;

import lombok.Data;


@Data
public class WebClientProperties {

    private String name;

    private String baseUrl;

    private OAuthProperties oauth;

    private ProxyProperties proxy;

    private int maxRetryAttempts;

    private int backoffDelay;

    private int connectionTimeout;

    private int readTimeout;

    private int responseTimeout;
}
