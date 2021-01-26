package com.yicj.study.mq.aspect;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    @MetricTime("register")
    public String register(String name, String password, String email){
        try {
            Thread.sleep(345);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "success" ;
    }
}
