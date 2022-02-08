package com.github.jander99.webclientbuilder.autoconfigure;

import com.github.jander99.webclientbuilder.autoconfigure.properties.WebClientProperties;
import com.github.jander99.webclientbuilder.autoconfigure.properties.WebClientsProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.context.GenericReactiveWebApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Slf4j
@Configuration
@ConditionalOnClass(WebClient.class)
@EnableConfigurationProperties(WebClientsProperties.class)
public class WebClientBuilderAutoConfiguration {

    private static final int CONNECTION_TIMEOUT_MILLIS = 5000;
    private static final int RESPONSE_TIMEOUT_MILLIS = 30000;

    private GenericReactiveWebApplicationContext context;

    public WebClientBuilderAutoConfiguration(GenericReactiveWebApplicationContext context,
                                             WebClientsProperties webClientsProperties) {
        this.context = context;
        initializeWebclients(webClientsProperties);
    }

    private void initializeWebclients(WebClientsProperties webClientsProperties) {

        log.info("Initializing WebClients");
        webClientsProperties.getWebClients().forEach(webClientProperties -> {
            log.info("Building WebClient: {}", webClientProperties.getName());

            // Initialize a WebCLient builder
            WebClient.Builder builder = WebClient.builder();

            // Set the base url
            builder.baseUrl(webClientProperties.getBaseUrl());

            // Set the client connector
            builder.clientConnector(buildClientHttpConnector(webClientProperties));

            // Register the WebClient Bean
            context.registerBean(webClientProperties.getName(), WebClient.class, builder::build);
        });

        log.info("WebClients {} initialized", Arrays.toString(context.getBeanNamesForType(WebClient.class)));
    }

    private ClientHttpConnector buildClientHttpConnector(WebClientProperties webClientProperties) {

        HttpClient httpClient = HttpClient.create();

        // Set Proxy settings if proxy settings are provided
        if (Objects.nonNull(webClientProperties.getProxy())) {
            httpClient.proxy(proxy -> {
                proxy.type(ProxyProvider.Proxy.HTTP)
                        .host(webClientProperties.getProxy().getHost())
                        .port(webClientProperties.getProxy().getPort())
                        .connectTimeoutMillis(Objects
                                .requireNonNullElse(webClientProperties.getConnectionTimeout(),
                                        CONNECTION_TIMEOUT_MILLIS));
            });
        }

        // Set the connect timeout
        httpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Objects
                .requireNonNullElse(webClientProperties.getConnectionTimeout(),
                        CONNECTION_TIMEOUT_MILLIS));

        // Set the response timeout
        httpClient.responseTimeout(Duration.ofMillis(Objects
                .requireNonNullElse(webClientProperties.getResponseTimeout(), RESPONSE_TIMEOUT_MILLIS)));

        // Set the read timeout
        httpClient.doOnConnected(connection -> connection
                .addHandler(new ReadTimeoutHandler(webClientProperties.getReadTimeout(),
                        TimeUnit.MILLISECONDS)));

        // Default wiretap to be true
        httpClient.wiretap(true);

        return new ReactorClientHttpConnector(httpClient);
    }
}
