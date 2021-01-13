package com.yicj.study.future.service;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ShopTest {
    private List<Shop> shops = Arrays.asList(
            new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoritesShop"),
            new Shop("BuyItAll"),
            new Shop("BuyItAll2")
            );

    private Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(), 100), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            // 使用守护线程：这一种方式不会阻止程序的关停
            t.setDaemon(true);
            return t;
        }
    });

    @Test
    public void getPriceAsync(){
        Shop shopService = new Shop("Hello");
        long start = System.nanoTime();
        Future<Double> futurePrice = shopService.getPriceAsync("my favorite product");
        long invocationTime = ((System.nanoTime() - start) / 1_000_000) ;
        System.out.println("Invocation return after " + invocationTime +" msecs");
        // 执行更多任务，比u查询其他商店
        doSomethingElse() ;
        // 在计算商品价格的同时
        try {
            Double price = futurePrice.get(3, TimeUnit.SECONDS);
            System.out.printf("Price is %.2f%n", price);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long retrievalTime = ((System.nanoTime() - start) /1_000_000) ;
        System.out.println("Price returned after " + retrievalTime +" msecs");
    }

    private void doSomethingElse() {
    }



    @Test
    public void findPrices(){
        long start = System.nanoTime();
        List<String> phoneList = shops.stream()
                .map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice("my phone27S")))
                .collect(Collectors.toList());
        long duration = (System.nanoTime() - start) / 1_000_000 ;
        System.out.println("Done in " + duration +" msecs");
        System.out.println(phoneList);
    }


    @Test
    public void findPrices2(){
        long start = System.nanoTime();
        List<String> phoneList = shops.parallelStream()
                .map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice("my phone27S")))
                .collect(Collectors.toList());
        long duration = (System.nanoTime() - start) / 1_000_000 ;
        System.out.println("Done in " + duration +" msecs");
        System.out.println(phoneList);
    }

    //Done in 1046 msecs
    @Test
    public void findPrices3(){
        long start = System.nanoTime();
        List<CompletableFuture<String>> priceFuture = shops.stream()
                .map(shop ->
                        CompletableFuture.supplyAsync(
                                () -> shop.getName() + " price is " + shop.getPrice("my phone27S")
                        )
                ).collect(Collectors.toList());
        // 等待所有异步操作结束
        List<String> phoneList = priceFuture.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        long duration = (System.nanoTime() - start) / 1_000_000 ;
        System.out.println("Done in " + duration +" msecs");
        System.out.println(phoneList);
    }

    @Test
    public void findPrices4(){
        // 注意这种操作与findPrices3的差异,这种需要更多的时间
        long start = System.nanoTime();
        List<String> phoneList = shops.stream()
                .map(shop ->
                        CompletableFuture.supplyAsync(
                                () -> shop.getName() + " price is " + shop.getPrice("my phone27S")
                        )
                ).map(CompletableFuture::join).collect(Collectors.toList());

        long duration = (System.nanoTime() - start) / 1_000_000 ;
        System.out.println("Done in " + duration +" msecs");
        System.out.println("==> phone list : " + phoneList);
    }


    @Test
    public void findPrice5(){

        long start = System.nanoTime();
        List<CompletableFuture<String>> priceFuture = shops.stream()
                .map(shop ->
                        CompletableFuture.supplyAsync(
                                () -> shop.getName() + " price is " + shop.getPrice("my phone27S")
                        , executor)
                ).collect(Collectors.toList());

        // 等待所有异步操作结束
        List<String> phoneList = priceFuture.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        long duration = (System.nanoTime() - start) / 1_000_000 ;
        System.out.println("Done in " + duration +" msecs");
        System.out.println(phoneList);
    }
}
