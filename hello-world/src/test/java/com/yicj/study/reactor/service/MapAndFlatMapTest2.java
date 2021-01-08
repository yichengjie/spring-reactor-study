package com.yicj.study.reactor.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

@Slf4j
public class MapAndFlatMapTest2 {

    /**
     * 异步回调中创建mono
     * @throws InterruptedException
     */
    @Test
    public void createMonoFromCallback() throws InterruptedException {
        Mono<Object> helloWorld = Mono.create(monoSink -> {
            // 在业务回调函数中将monoSink赋值
            printlnThread("mono 业务");
            asyncTask(value ->{
                monoSink.success(value);
            });
        });
        //System.out.println(helloWorld.block());
        CountDownLatch latch = new CountDownLatch(1) ;
        helloWorld.subscribe(value ->{
            printlnThread("subscribe");
            System.out.println("===> value : " + value);
            latch.countDown();
        }) ;
        latch.await();
    }

    @Test
    public void flatMap(){
        Flux<String> flux = Flux.just("A,B,C", "D,E,F", "G,H");
        flux.flatMap(s -> {
            //System.out.println(s);
            String[] split = s.split(",");
            return Flux.fromArray(split) ;
        }).subscribe(str->{
            System.out.println(str);
        }) ;
    }

    @Test
    public void map(){
        Flux<String> flux = Flux.just("A,B,C", "D,E,F", "G,H");
        flux.map(s -> {
            //System.out.println(s);
            String[] split = s.split(",");
            return split ;
        }).subscribe(str->{
            System.out.println("====> " + Arrays.asList(str));
        }) ;
    }


    @Test
    public void flatMap2(){
        Map<String, List<String>> map1 = new HashMap<>() ;
        map1.put("key", Arrays.asList("A","B","C"));

        Map<String,List<String>> map2 = new HashMap<>() ;
        map2.put("key", Arrays.asList("D","E","F"));

        Map<String,List<String>> map3 = new HashMap<>() ;
        map3.put("key", Arrays.asList("G","H"));
        List<Map<String,List<String>>> list = Arrays.asList(map1, map2, map3) ;

        Flux<Map<String, List<String>>> mapFlux = Flux.fromIterable(list);
        mapFlux.flatMap( map -> {
            List<String> tmpList = map.get("key");
            return Flux.fromIterable(tmpList) ;
        })
        .subscribe(strings -> System.out.println(strings)) ;
    }


    private void asyncTask(Consumer<String> consumer){
        Thread thread = new Thread(()->{
            try {
                printlnThread("异步任务");
                Thread.sleep(20);
                consumer.accept("HelloWorld");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }) ;
        thread.start();
    }

    private void printlnThread(String tag){
        System.out.println("tag ["+tag+"] : " + Thread.currentThread().getName());
    }
}
