package com.yicj.study.reactor.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ApiFuncTest {

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
        Flux<Long> intervalFlux = Flux.interval(Duration.of(500, ChronoUnit.MILLIS))
                .take(5);
        StepVerifier.create(intervalFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .expectNext(4L)
                .verifyComplete() ;
        //防止程序过早退出，放一个CountDownLatch拦住
//        CountDownLatch latch = new CountDownLatch(1);
//        latch.await();
    }

    @Test
    public void error(){
        Flux.error(new RuntimeException())
            .subscribe(System.out::println);
    }

    @Test
    public void range(){
        Flux.range(1, 10)
            .subscribe(System.out::println) ;
    }

    @Test
    public void buffer(){
        Flux<String> fruitFlux = Flux.just("apple", "orange", "banana", "kiwi", "strawberry") ;
        Flux<List<String>> bufferFlux = fruitFlux.buffer(3);
        StepVerifier.create(bufferFlux)
                .expectNext(Arrays.asList("apple", "orange", "banana"))
                .expectNext(Arrays.asList("kiwi", "strawberry"))
                .verifyComplete() ;
    }

    @Test
    public void buffer2() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5) ;
        Flux<String> fruitFlux = Flux.just("apple", "orange", "banana", "kiwi", "strawberry") ;
        fruitFlux.buffer(3)
            .flatMap(x -> {
                return Flux.fromIterable(x)
                    .map(y -> y.toUpperCase())
                    .subscribeOn(Schedulers.parallel())
                    .log() ;
            })
            .subscribe(ele -> {
                log.info("====> element : {}", ele) ;
                latch.countDown();
            }) ;
        latch.await();
    }

    @Test
    public void mergeFlux(){
        Flux<String> characterFlux =
                Flux.just("Garfiedld", "Kojak", "Barbossa")
                .delayElements(Duration.ofMillis(500)) ;
        Flux<String> foodFlux =
                Flux.just("Lasagna", "Lollipops", "Apples")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500));

        Flux<String> mergeWith = characterFlux.mergeWith(foodFlux);

        StepVerifier.create(mergeWith)
                .expectNext("Garfiedld")
                .expectNext("Lasagna")
                .expectNext("Kojak")
                .expectNext("Lollipops")
                .expectNext("Barbossa")
                .expectNext("Apples")
                .verifyComplete();
    }

    @Test
    public void zipFluxes(){
        Flux<String> characterFlux =
                Flux.just("Garfiedld", "Kojak", "Barbossa") ;
        Flux<String> foodFlux =
                Flux.just("Lasagna", "Lollipops", "Apples") ;

        Flux<Tuple2<String, String>> zippedFlux = Flux.zip(characterFlux, foodFlux);

        StepVerifier.create(zippedFlux)
            .expectNextMatches(p -> p.getT1().equals("Garfiedld") && p.getT2().equals("Lasagna"))
            .expectNextMatches(p -> p.getT1().equals("Kojak") && p.getT2().equals("Lollipops"))
            .expectNextMatches(p -> p.getT1().equals("Barbossa") && p.getT2().equals("Apples"))
            .verifyComplete() ;

    }

}
