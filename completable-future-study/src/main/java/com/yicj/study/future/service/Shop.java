package com.yicj.study.future.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shop {

    private String name ;

    public double getPrice(String product){
        return calculatePrice(product) ;
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
        delay();
        return new Random().nextDouble() * product.charAt(0) + product.charAt(1) ;
    }

    private void delay(){
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e) ;
        }
    }
}
