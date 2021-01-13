package com.yicj.study.optional;

import com.yicj.study.future.Car;
import com.yicj.study.future.Insurance;
import com.yicj.study.future.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Optional;

@Slf4j
public class HelloTest {

    @Test
    public void test1(){
        Person person = new Person() ;
        Optional<Person> optPerson = Optional.of(person);
        Optional<Optional<Car>> car = optPerson.map(Person::getCar);
    }

    @Test
    public void test2(){
        Person person = new Person() ;
        Optional<Person> optPerson = Optional.of(person);
        String name = optPerson
                .flatMap(Person::getCar)
                .flatMap(Car::getInsurance)
                .map(Insurance::getName)
                .orElse("unknown");
        log.info("name : {}", name);
    }
}
