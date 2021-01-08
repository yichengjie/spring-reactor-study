package com.yicj.study.reactor.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInfo {
    private Integer id ;
    private String username ;
    private String addr ;
}