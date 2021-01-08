package com.yicj.study.user.model.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {
    private Integer id ;
    private String username ;
    private String addr ;
}
