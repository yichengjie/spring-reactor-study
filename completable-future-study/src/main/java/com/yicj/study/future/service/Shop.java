package com.yicj.study.future.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Data
@RequiredArgsConstructor
public class Shop {

    private final String name ;
    private Random random = new Random() ;

    public double getPrice(String product){
        double price = calculatePrice(product);
        return price ;
    }

    public String getPriceNew(String product){
        double price = calculatePrice(product);
        Discount.Code code = Discount.Code.values()[new Random().nextInt(Discount.Code.values().length)] ;
        return String.format("%s:%.2f:%s", name, price, code);
    }

    public Future<Double> getPriceAsync(String product){
        CompletableFuture<Double> futurePrice = new CompletableFuture<>() ;
        new Thread(()->{
            try {
                double price = calculatePrice(product)  ;
                futurePrice.complete(price) ;
            }catch (Exception e){
                futurePrice.completeExceptionally(e) ;
            }
        }).start();
        return futurePrice ;
    }


    public Future<Double> getPriceAsync2(String product){
       return CompletableFuture.supplyAsync(() -> calculatePrice(product)) ;
    }

    private double calculatePrice(String product){
        randomDelay();
        return new Random().nextDouble() * product.charAt(0) + product.charAt(1) ;
    }

    private void delay(){
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e) ;
        }
    }

    private void randomDelay(){

        int delay = 500 + random.nextInt(2000) ;
        try {
            Thread.sleep(delay);
        }catch (InterruptedException e){
            throw new RuntimeException(e) ;
        }
    }
}
