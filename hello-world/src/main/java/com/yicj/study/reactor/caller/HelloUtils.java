package com.yicj.study.reactor.caller;

import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

public class HelloUtils {

    @CallerSensitive
    public void hello(){
        Class<?> callerClass = Reflection.getCallerClass();
        System.out.println("----> " + callerClass.getName());
    }
}
