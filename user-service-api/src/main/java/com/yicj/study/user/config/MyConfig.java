package com.yicj.study.user.config;

import com.yicj.study.user.handler.UserHandlerFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class MyConfig {
    @Bean
    public RouterFunction<?> helloRouteFunction(UserHandlerFunction handlerFunction){
        return route(GET("/hello"), this::hello)
                .andRoute(GET("/users/{id}"), handlerFunction::findById) ;
    }

    private Mono<ServerResponse> hello(ServerRequest request){
        return ServerResponse.ok().body(Mono.just("hello world"), String.class) ;
    }
}
