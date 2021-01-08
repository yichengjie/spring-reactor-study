package com.yicj.study.reactor.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
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
}
