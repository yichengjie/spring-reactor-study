package com.yicj.study.reactor.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Mono;

@Slf4j
public class MonoTest {

    @Test
    public void fromRunnable() throws InterruptedException {
        Mono.fromCallable(()-> "hello world1")
                .subscribe(new MyLoggerSubscriber()) ;
        Thread.sleep(10);
    }

    @Test
    public void fromSupplier() throws InterruptedException {
        Mono.fromSupplier(()-> "hello world2")
                .subscribe(new MyLoggerSubscriber()) ;
        Thread.sleep(10);
    }

    @Test
    public void fromRunnable2() throws InterruptedException {
        Mono.fromCallable(()-> "hello world1")
            .subscribe(value -> log.info("value : {}", value)) ;
        Thread.sleep(10);
    }

    @Test
    public void fromSupplier2() throws InterruptedException {
        Mono.fromSupplier(()-> "hello world2")
            .subscribe(value -> log.info("=====>  {}", value)) ;
        Thread.sleep(10);
    }


    @Test
    public void then() throws InterruptedException {
        Mono.fromCallable(()->"hello world1")
                .then(Mono.fromSupplier(()-> "hello world2"))
                .subscribe(new MyLoggerSubscriber()) ;
        Thread.sleep(10);
    }

    class MyLoggerSubscriber implements Subscriber<String>{
        @Override
        public void onSubscribe(Subscription s) {
            s.request(77);
            log.info("===> onSubscribe : {}", s);
        }
        @Override
        public void onNext(String s) {
            log.info("===> onNext : {}", s);
        }
        @Override
        public void onError(Throwable t) {
            log.info("===> onError : {}", t.getMessage());
        }
        @Override
        public void onComplete() {
            log.info("===> onComplete..");
        }
    }

}
