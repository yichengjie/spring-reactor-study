package com.yicj.study.reactor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MyConfig {

    @Bean
    public WebClient webClient(){
        return WebClient.create("http://localhost:8081") ;
    }
}
