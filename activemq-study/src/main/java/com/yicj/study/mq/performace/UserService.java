package com.yicj.study.mq.performace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    @PfLog
    public void hello(){
        try {
            Thread.sleep(345);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("user service hello world");
    }
}
