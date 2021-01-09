package com.yicj.study.user.handler;

import com.yicj.study.user.model.vo.UserInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserHandlerFunction {

    public Mono<ServerResponse> findById(ServerRequest request){
        String id = request.pathVariable("id");
        UserInfo userInfo = UserInfo.builder()
                .id(Integer.parseInt(id))
                .username("yicj")
                .addr("bjs").build();
        return ServerResponse.ok().body(Mono.just(userInfo), UserInfo.class) ;
    }

}
