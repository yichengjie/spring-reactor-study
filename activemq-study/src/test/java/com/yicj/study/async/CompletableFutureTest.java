package com.yicj.study.async;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * 1. 创建一个CompletableFuture是通过CompletableFuture.supplyAsync()实现的
 * 2. CompletableFuture可以指定异步处理流程：
 *   2.1 thenAccept()处理正常结果；
 *   2.2 exceptional()处理异常结果；
 *   2.3 thenApplyAsync()用于串行化另一个CompletableFuture；
 * 3. anyOf()和allOf()用于并行化多个CompletableFuture。
 */
//参考：https://www.liaoxuefeng.com/wiki/1252599548343744/1306581182447650
public class CompletableFutureTest {

    @Test
    public void basicApi() throws InterruptedException {
        // 创建异步执行任务
        CompletableFuture<Double> cf =
                CompletableFuture.supplyAsync(CompletableFutureTest::fetchPrice);
        // 如果执行成功
        cf.thenAccept(result ->{
            System.out.println("price : " + result);
        }) ;
        // 如果执行异常
        cf.exceptionally(e ->{
            System.out.println("发生异常：" + e.getMessage());
            return null ;
        }) ;
        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        Thread.sleep(200);
    }

    // 使用thenApplyAsync串行化执行两个异步任务
    @Test
    public void serialExecute() throws InterruptedException {
        // 第一个任务
        CompletableFuture<String> cfQuery = CompletableFuture.supplyAsync(() -> {
            return queryCode("中国石油", null);
        });
        // cfQuery成功后继续执行下一个任务
        CompletableFuture<Double> cfFetch = cfQuery.thenApplyAsync(code -> {
            return fetchPrice(code, null);
        });
        // cfFetch成功后打印结果
        cfFetch.thenAccept(result ->{
            System.out.println("price : " + result);
        }) ;
        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭
        Thread.sleep(2000);
    }
    // 并行执行
    @Test
    public void parallelExecute() throws InterruptedException {
        // 两个CompletableFuture执行异步查询
        CompletableFuture<String> cfQueryFromSina  =
                CompletableFuture.supplyAsync(() -> queryCode("中国石油", "https://finance.sina.com.cn/code/"));

        CompletableFuture<String> cfQueryFrom163  =
                CompletableFuture.supplyAsync(() -> queryCode("中国石油", "https://money.163.com/code/"));
        //使用anyOf合并为一个新的CompletableFuture
        CompletableFuture<Object> cfQuery  =
                CompletableFuture.anyOf(cfQueryFromSina, cfQueryFrom163);
        // 两个CompletableFuture执行异步查询:
        CompletableFuture<Double> cfFetchFromSina  =
                cfQuery .thenApplyAsync(code -> fetchPrice(((String) code), "https://finance.sina.com.cn/price/"));
        CompletableFuture<Double> cfFetchFrom163  =
                cfQuery .thenApplyAsync(code -> fetchPrice((String) code, "https://finance.sina.com.cn/price/"));
        // 用anyOf合并为一个新的CompletableFuture
        CompletableFuture<Object> cfFetch  =
                CompletableFuture.anyOf(cfFetchFromSina, cfFetchFrom163);
        //最终结果
        cfFetch.thenAccept(result ->{
            System.out.println("price : " + result);
        }) ;
        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        Thread.sleep(200);
    }
    private String queryCode(String name, String url){
        try {
            long time = (long)(Math.random() * 100);
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "601857" ;
    }
    private Double fetchPrice(String code, String url){
        try {
            long time = (long)(Math.random() * 100);
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 5 + Math.random() * 20 ;
    }
    static Double fetchPrice() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        if (Math.random() < 0.3) {
            throw new RuntimeException("fetch price failed!");
        }
        return 5 + Math.random() * 20;
    }
}
