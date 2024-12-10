package com.ela.ccvoice.common.user.domain.dto;

import lombok.Data;

@Data
public class UserInfoDto {
    private String avatar; //url for head icon
    private String nickName;
    private String email;
    private Integer sex;

    @Override
    public String toString(){
        return "UserInfoDto{" +
                "avatar='" + avatar + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", sex=" + sex +
                '}';
    }
}
