package com.yicj.study.user.controller;

import com.yicj.study.user.model.vo.UserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public UserInfo findById(@PathVariable Integer id){
        return UserInfo.builder()
                .id(id)
                .username("yicj")
                .addr("bjs").build();
    }
}
