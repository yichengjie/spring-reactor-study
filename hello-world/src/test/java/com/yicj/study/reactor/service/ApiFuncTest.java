package com.yicj.study.reactor.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

@Slf4j
public class ApiFuncTest {

    @Test
    public void create(){
        Flux.create(new Consumer<FluxSink<String>>() {
            @Override
            public void accept(FluxSink<String> fluxSink) {
                fluxSink.next("create1") ;
                fluxSink.next("create2") ;
                fluxSink.next("create3") ;
                fluxSink.complete();
            }
        }).subscribe(value ->{
            System.out.println(value);
        }) ;
    }

    @Test
    public void filter(){
        Flux.create(fluxSink -> {
            fluxSink.next("create1") ;
            fluxSink.next("create2") ;
            fluxSink.next("create3") ;
            fluxSink.complete();
        }).filter(item -> item != null).subscribe(value ->{
            System.out.println(value);
        }) ;
    }

    @Test
    public void fromRunnable() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1) ;
        Mono.fromRunnable(()->{
            log.info("runnable info hello");
        }).publishOn(Schedulers.newSingle("test"))
        .subscribe(new Subscriber<Object>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.info("onSubscribe !");
            }
            @Override
            public void onNext(Object o) {
                log.info("onNext value : {}", o);
            }
            @Override
            public void onError(Throwable t) {
                log.error("error : {}", t.getMessage());
            }
            @Override
            public void onComplete() {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("onComplete !");
                latch.countDown();
            }
        }) ;
        log.info("===== end ...");
        latch.await();
    }


    private void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fluxCreate(){
        Flux.create(emitter -> {
            for (int i =0 ; i < 1000; i++){
                if (emitter.isCancelled()){
                    return;
                }
                log.info("Source create : {}", i);
                emitter.next(i) ;
            }
        }).doOnNext(s -> log.info("Source push {}", s))
        .publishOn(Schedulers.single())
        .subscribe(id -> {
            sleep(10);
            log.info("所获取到的Customer的id为: {}", id);
        }) ;
        sleep(12000);
    }

    @Test
    public void array(){
        String [] arr = {"create1", "create2", "create3"} ;
        Flux.fromArray(arr).subscribe(System.out::println) ;
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

    //ono.just创建的数据源时间没变，但是Mono.defer创建的数据源时间相应的延迟了5秒钟，
    // 原因在于Mono.just会在声明阶段构造Date对象，只创建一次，
    // 但是Mono.defer却是在subscribe阶段才会创建对应的Date对象，每次调用subscribe方法都会创建Date对象
    @Test
    public void defer2(){
        //声明阶段创建DeferClass对象
        Mono<Date> m1 = Mono.just(new Date());
        Mono<Date> m2 = Mono.defer(()->Mono.just(new Date()));
        m1.subscribe(System.out::println);
        m2.subscribe(System.out::println);
        //延迟5秒钟
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        m1.subscribe(System.out::println);
        m2.subscribe(System.out::println);
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
