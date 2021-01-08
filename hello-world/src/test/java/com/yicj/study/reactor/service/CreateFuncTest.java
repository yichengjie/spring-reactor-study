package com.yicj.study.reactor.service;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CountDownLatch;

public class CreateFuncTest {

    @Test
    public void create(){
        Flux.create(fluxSink -> {
            fluxSink.next("create1") ;
            fluxSink.next("create2") ;
            fluxSink.next("create3") ;
            fluxSink.complete();
        }).subscribe(value ->{
            System.out.println(value);
        }) ;
    }

    @Test
    public void generate(){
        Flux.generate(sink -> {
            sink.next("generate");
            //注意generate中next只能调用1次
            sink.complete();
        }).subscribe(System.out::println) ;
    }

    @Test
    public void defer(){
        Flux.defer(() -> Flux.just("just", "just1", "just2"))
            .subscribe(System.out::println);
    }

    @Test
    public void interval() throws InterruptedException {
        Flux.interval(Duration.of(500, ChronoUnit.MILLIS))
            .subscribe(System.out::println);
        //防止程序过早退出，放一个CountDownLatch拦住
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }
}
