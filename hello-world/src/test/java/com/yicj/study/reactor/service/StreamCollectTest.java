package com.yicj.study.reactor.service;


import com.yicj.study.reactor.model.vo.Dish;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class StreamCollectTest {

    private List<Dish> menu = new DishService().getMenu() ;

    @Test
    public void testCount(){
        Long count = menu.stream().collect(Collectors.counting());
        log.info("count1 : {}", count);
        long count1 = menu.stream().count();
        log.info("count2 : {}", count1);
    }

    @Test
    public void testMaxBy(){
        Comparator<Dish> dishComparator =
                Comparator.comparingInt(Dish::getCalories);
        Optional<Dish> dish = menu.stream()
                .collect(Collectors.maxBy(dishComparator));
        log.info("max dish : {}", dish.get());
    }


    @Test
    public void testSummingInt(){

        Integer totalCalories = menu.stream().collect(Collectors.summingInt(Dish::getCalories));
        log.info("total calories : {}", totalCalories);
    }


    @Test
    public void testJoin(){
        String content1 =
                menu.stream()
                    .map(Dish::getName)
                    .collect(Collectors.joining());
        log.info("content1 : {}", content1);
        ////
        String content2 = menu.stream().map(Dish::getName)
                .collect(Collectors.joining(", "));

        log.info("content2 : {}", content2);
    }


    //与前面的SummingInt效果一样
    @Test
    public void testReducing(){
        Integer totalCalories = menu.stream().collect(Collectors.reducing(
                0, Dish::getCalories, (i, j) -> i + j
        ));
        log.info("totalCalories : {}", totalCalories);
    }

    @Test
    public void testReducing2(){
        Optional<Dish> mostCalorieDish = menu.stream().collect(Collectors.reducing(
                (d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2
        ));
        log.info("most calorie dish : {}", mostCalorieDish.get());
    }

    @Test
    public void testReduce(){
        List<Integer> reduce = Stream.iterate(0, n -> n + 1)
                .limit(6)
                .reduce(new ArrayList<>(),
                        (List<Integer> l, Integer e) -> {
                            l.add(e);
                            return l;
                        }, (List<Integer> l1, List<Integer> l2) -> {
                            l1.addAll(l2);
                            return l1;
                        });

        log.info("reduce : {}", reduce);
    }

    @Test
    public void collect(){
        Integer totalCalories = menu.stream()
                .collect(Collectors.reducing(0, Dish::getCalories, Integer::sum));
        log.info("totalCalories : {}", totalCalories);
    }

    @Test
    public void testJoin2(){
        String shortMenu = menu.stream().collect(Collectors.reducing(
                "", Dish::getName, (s1, s2) -> s1 + s2
        ));
        log.info("short menu : {}", shortMenu);
    }

    @Test
    public void testGroupingBy(){
        Map<Dish.Type, List<Dish>> map = menu.stream()
                .collect(Collectors.groupingBy(Dish::getType));
        map.keySet().stream().forEach(key ->{
            List<Dish> dishes = map.get(key);
            log.info("--------------key:{}-----------------", key);
            dishes.stream().map(Dish::toString).forEach(log::info);
        });
    }

    @Test
    public void testGroupingBy2(){
        Map<Dish.CaloricLevel, List<Dish>> map = menu.stream()
                .collect(Collectors.groupingBy(dish -> {
                    if (dish.getCalories() <= 400) {
                        return Dish.CaloricLevel.DIET;
                    } else if (dish.getCalories() <= 700) {
                        return Dish.CaloricLevel.NORMAL;
                    } else {
                        return Dish.CaloricLevel.FAT;
                    }
                }));
    }


    @Test
    public void testGroupingBy3(){
        Map<Dish.Type, Map<Dish.CaloricLevel, List<Dish>>> map = menu.stream().collect(
                Collectors.groupingBy(Dish::getType, Collectors.groupingBy(dish -> {
                    if (dish.getCalories() <= 400) {
                        return Dish.CaloricLevel.DIET;
                    } else if (dish.getCalories() <= 700) {
                        return Dish.CaloricLevel.NORMAL;
                    } else {
                        return Dish.CaloricLevel.FAT;
                    }
                }))
        );
        log.info("map : {}", map);
    }

    @Test
    public void testGroupingBy4(){
        Map<Dish.Type, Long> collect = menu.stream().collect(
                Collectors.groupingBy(Dish::getType, Collectors.counting())
        );
        log.info("map : {}", collect);
    }
}
