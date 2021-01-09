package com.yicj.study.reactor.service;

import com.yicj.study.reactor.model.vo.Player;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


// flatMap的转换Function要求返回Publisher，这个Publisher代表一个作用于元素的异步转换操作
// map是同步的元素转换操作
@Slf4j
public class MapAndFlatMapTest {

    // 转换操作异步操作
    @Test
    public void flatMap() throws InterruptedException {
        Flux.just(1,2,3)
            .log()
            .flatMap(e -> {
                return Flux.just(e *2).delayElements(Duration.ofSeconds(1)) ;
            }).subscribe(e -> log.info("get : {}", e)) ;
        TimeUnit.SECONDS.sleep(10);
    }

    // 转换操作同步操作
    @Test
    public void map() throws InterruptedException {
        Flux.just(1,2,3)
            .log()
            .map(e -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                return e *2;
            }).subscribe(e -> log.info("get : {}", e)) ;
    }

    @Test
    public void test1(){
        List<Integer> list = Arrays.asList(1,2,3) ;
        Flux<Integer> just1 = Flux.just(1, 2, 3);

        Flux<List<Integer>> just = Flux.just(list);
        just.flatMap(Flux::fromIterable)
            .subscribe(System.out::println) ;
    }


    @Test
    public void flatMap2() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(4) ;
        Flux<Player> playerFlux = Flux.just("Michael Jordan", "Scottie Pippen", "Steve Kerr","Hello World")
                .flatMap(n -> Mono.just(n).map(p ->{
                    String[] split = p.split("\\s");
                    return new Player(split[0], split[1]) ;
                }).subscribeOn(Schedulers.parallel()).log()) ;

        playerFlux.subscribe(player -> {
            log.info("====> player : {}", player);
            latch.countDown();
        }) ;
        latch.await();
    }
}
