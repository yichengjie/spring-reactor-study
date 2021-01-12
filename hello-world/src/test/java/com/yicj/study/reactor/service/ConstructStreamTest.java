package com.yicj.study.reactor.service;

import org.junit.Test;

import java.util.function.IntSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ConstructStreamTest {

    @Test
    public void iterator(){
        Stream.iterate(0, n -> n +2)
            .limit(10)
            .forEach(System.out::println);

    }

    @Test
    public void iterator2(){

        Stream.iterate(new int[]{0,1}, n -> new int[]{n[1], n[0] + n[1]})
            .limit(10)
            .forEach(ints -> System.out.println("("+ints[0]+", "+ints[1]+")"));

    }

    @Test
    public void generator(){
        Stream.generate(()-> Math.random())
                .limit(5)
                .forEach(System.out::println);

    }


    @Test
    public void generator2(){
        //这种方式也能生产与iterator一样的斐波那契额数列，但是尽量避免，因为这里是有状态的，并行时会有问题

        IntSupplier fib = new IntSupplier() {

            private int previous = 0 ;
            private int current =1 ;


            @Override
            public int getAsInt() {
                int oldPrevious = this.previous ;
                int nextValue = this.previous + this.current ;
                this.previous = this.current ;
                this.current = nextValue ;

                return oldPrevious;
            }
        } ;

        IntStream.generate(fib).limit(10).forEach(System.out::println) ;

    }
}
