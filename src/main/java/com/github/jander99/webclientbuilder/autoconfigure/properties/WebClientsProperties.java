package com.github.jander99.webclientbuilder.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties("spring.webflux")
public class WebClientsProperties {

    private List<WebClientProperties> webClients;
}

