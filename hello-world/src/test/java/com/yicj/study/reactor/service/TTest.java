package com.yicj.study.reactor.service;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TTest {

    class Fruit{}

    class Apple extends Fruit{}

    class Pear extends Fruit{}

    class FruitTest <T>{
        private List<? super Fruit> list = new ArrayList<>() ;
        private List<? extends Fruit> list2 = new ArrayList<>() ;

        public void add(){
            list.add(new Apple());
            list.get(0) ;
        }
    }



    @Test
    public void test(){


    }
}
