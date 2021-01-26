package com.yicj.study.async;

import org.junit.Test;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.core.publisher.Mono;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncTest {
    private ExecutorService dbThreadPool = Executors.newFixedThreadPool(5);

    @Test
    public void originalTest(){
        DeferredResult<String> deferredResult = new DeferredResult<>() ;
        CompletableFuture<Object> objectCompletableFuture = CompletableFuture.supplyAsync(() -> {
            sleep(2);
            // 返回阻塞数据库IO
            return "Hello world";
            // dbThreadPool用来处理阻塞的数据库IO
        }, dbThreadPool).thenComposeAsync(result -> {
            // spring的DeferredResult来实现异步回调写入结果返回
            deferredResult.setResult(result);
            return null;
        });
    }


    @Test
    public void webfluxTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1) ;
        Mono.fromFuture(CompletableFuture.supplyAsync(()->{
            sleep(3);
            return "阻塞数据库IO" ;
            // dbThreadPool用来处理阻塞的数据库IO
        },dbThreadPool)).subscribe(value ->{
            System.out.println("====> " + value);
            latch.countDown();
        }) ;
        latch.await();
    }



    private void sleep(int seconds){
        try {
            Thread.sleep(seconds  * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
