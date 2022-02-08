package com.github.jander99.webclientbuilder.autoconfigure.properties;

import lombok.Data;

@Data
public class OAuthProperties {

    private String factory;

    private String clientId;

    private String clientSecret;

    private String tokenUrl;

}
