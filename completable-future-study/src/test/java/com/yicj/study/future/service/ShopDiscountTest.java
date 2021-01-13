package com.yicj.study.future.service;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShopDiscountTest {
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
    public void findPriceNew(){
        long start = System.nanoTime();
        List<String> phoneList = shops.stream()
                .map(shop -> shop.getPriceNew("myPhone22S"))
                .map(Quote::parse)
                .map(Discount::applyDiscount)
                .collect(Collectors.toList());
        long duration = (System.nanoTime() - start) / 1_000_000 ;
        System.out.println("Done in " + duration +" msecs");
        System.out.println(phoneList);
    }

    @Test
    public void findPriceNew2(){
        long start = System.nanoTime();
        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceNew("myPhone22S"), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)))
                .collect(Collectors.toList());
        // ----------
        List<String> phoneList = priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        long duration = (System.nanoTime() - start) / 1_000_000 ;
        System.out.println("Done in " + duration +" msecs");
        System.out.println(phoneList);
    }


    @Test
    public void findPriceNew3(){
        long start = System.nanoTime() ;
        Stream<CompletableFuture<String>> priceStream = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceNew("myPhone22S"), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)));
        // ----------
        CompletableFuture[] futures =
                priceStream.map(f -> f.thenAccept(s -> {
                        long duration = (System.nanoTime() - start) / 1_000_000 ;
                        System.out.println(s + "Done in " + duration +" msecs") ;
                    }
                )).toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join() ;
    }
}
