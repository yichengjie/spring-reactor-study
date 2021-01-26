package com.yicj.study.mq.aspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyRunner implements ApplicationRunner {
    @Autowired
    private UserService userService ;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String result = userService.register("yicj", "123", "xx@qq.com");
        String result2 = userService.register2("yicj", "123", "xx@qq.com");
        System.out.println("=====> " + result);
        System.out.println("=====> " + result2);
    }
}
