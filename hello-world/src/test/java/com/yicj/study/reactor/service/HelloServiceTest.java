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
import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HelloWorldApplication.class)
public class HelloServiceTest {

    @Autowired
    private HelloService helloService ;

    /**
     * 同步方式调用
     */
    @Test
    public void findByIdBlock(){
        Integer userId = 11 ;
        Mono<UserInfo> byId = helloService.findById(userId);
        UserInfo userInfo = byId.block();
        log.info("=====> user info : {}", userInfo);
    }

    /**
     * 异步方式调用
     * @throws InterruptedException
     */
    @Test
    public void findByIdAsync() throws InterruptedException {
        Integer userId = 11 ;
        Mono<UserInfo> byId = helloService.findById(userId);
        final CountDownLatch latch = new CountDownLatch(1);
        byId.subscribe(userInfo -> {
            log.info("=====> user info : {}", userInfo);
            latch.countDown();
        }, Throwable::printStackTrace) ;
        latch.await();
    }

}
