package com.yicj.study.reactor.service;

import com.yicj.study.reactor.model.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class HelloService {

    @Autowired
    private WebClient webClient ;

    public Mono<UserInfo> findById(Integer userId){
        return webClient.get().uri("/users/"+ userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(cr -> cr.bodyToMono(UserInfo.class)) ;
    }

}
