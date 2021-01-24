package com.yicj.study.reactor.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

@Slf4j
public class FluxTest {

    @Test
    public void fluxGenerate(){
        Flux.generate((Consumer<SynchronousSink<Integer>>) synchronousSink -> {
            synchronousSink.next(1);
            synchronousSink.complete();
        }).subscribe(value -> log.info("value : {}", value)) ;
    }

    @Test
    public void fluxGenerate2() throws InterruptedException {
        final Random random = new Random() ;
        Flux.generate(ArrayList::new, (list, sink) ->{
            int value = random.nextInt(100) ;
            list.add(value) ;
            sink.next(value);
            if (list.size() ==257){
                sink.complete();
            }
            return list ;
        }).doOnNext(item -> System.out.println("emitted on thread " + Thread.currentThread().getName() +" " + item))
        .publishOn(Schedulers.parallel())
        .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
        .publishOn(Schedulers.elastic())
        .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
        .subscribeOn(Schedulers.parallel())
        .subscribe(value -> log.info("value : {}", value)) ;
        Thread.sleep(1000);
    }
}
