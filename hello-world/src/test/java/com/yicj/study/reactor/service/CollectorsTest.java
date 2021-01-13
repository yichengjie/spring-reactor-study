package com.yicj.study.reactor.service;

import com.yicj.study.reactor.common.ToListCollector;
import com.yicj.study.reactor.model.vo.Dish;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.IntStream;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

@Slf4j
public class CollectorsTest {
    private List<Dish> menu = new DishService().getMenu() ;

    @Test
    public void testToList(){
        List<Dish> collect = menu.stream().collect(toList());
        log.info("collect : {}", collect);
    }

    @Test
    public void testToSet(){
        Set<Dish> collect = menu.stream().collect(toSet());
        log.info("collect : {}", collect);
    }

    @Test
    public void testMaxBy(){
        Optional<Dish> collect = menu.stream().collect(maxBy(comparingInt(Dish::getCalories)));
        log.info("info : {}", collect.get());
    }

    @Test
    public void testCounting(){
        Long collect = menu.stream().collect(counting());
        log.info("count : {}", collect);
    }

    @Test
    public void testSummingInt(){
        Integer collect = menu.stream().collect(summingInt(Dish::getCalories));
        log.info("summing : {}", collect);
    }

    @Test
    public void testCollectingAndThen(){
        Integer collect = menu.stream().collect(collectingAndThen(toList(), li -> li.size()));
        log.info("size : {}", collect);
    }

    @Test
    public void testCustomCollector(){
        List<Dish> dishes = menu.stream().collect(new ToListCollector<>());
        dishes.forEach(System.out::println);
    }

    @Test
    public void testCustomCollector2(){
        List<Dish> dishes = menu.stream().collect(ArrayList::new, List::add, List::addAll);
        dishes.forEach(System.out::println);
    }

    @Test
    public void testPartition(){
        Map<Boolean, List<Integer>> map = IntStream.range(2, 100).boxed()
                .collect(partitioningBy(candidate -> isPrime(candidate)));
        log.info("map : {}", map);
    }

    private boolean isPrime(int candidate){
        int candidateRoot = (int) Math.sqrt((double) candidate) ;
        return IntStream.rangeClosed(2, candidateRoot).noneMatch(i -> candidate %i == 0) ;
    }

    @Test
    public void testSqrt(){
        int candidateRoot = (int) Math.sqrt((double) 13) ;
        log.info("candidateRoot :{}", candidateRoot);
    }

}
