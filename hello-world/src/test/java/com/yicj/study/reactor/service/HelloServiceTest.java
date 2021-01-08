package com.yicj.study.reactor.service;

import com.yicj.study.reactor.HelloWorldApplication;
import com.yicj.study.reactor.model.vo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HelloWorldApplication.class)
public class HelloServiceTest {

    @Autowired
    private HelloService helloService ;

    @Test
    public void findById(){
        Integer userId = 11 ;
        Mono<UserInfo> byId = helloService.findById(userId);
        byId.subscribe(userInfo -> {
            log.info("user info : {}", userInfo);
        }, throwable -> throwable.printStackTrace()) ;
    }

}
