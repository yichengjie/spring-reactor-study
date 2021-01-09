package com.yicj.study.user.config;

import com.yicj.study.user.handler.UserHandlerFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class MyConfig {

    @Bean
    public RouterFunction<?> helloRouteFunction(UserHandlerFunction handlerFunction){
        return route(GET("/users/{id}"), handlerFunction::hello) ;
    }
}
