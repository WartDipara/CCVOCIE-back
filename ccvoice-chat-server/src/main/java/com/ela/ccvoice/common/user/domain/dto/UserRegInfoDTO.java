package com.ela.ccvoice.common.user.domain.dto;

import lombok.Data;

@Data
public class UserRegInfoDTO {
    private String name;
    private String email;
    private String password;

    @Override
    public String toString() {
        return "UserRegInfoDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
