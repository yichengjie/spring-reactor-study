package com.yicj.study.reactor.model.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {
    private Integer id ;
    private String username ;
    private String addr ;
}