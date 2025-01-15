package com.tranphucvinh.constant;

import lombok.Getter;

@Getter
public enum RoleEnum {

    ROLE_SYSTEM_ADMIN("ROLE_SYSTEM_ADMIN"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_MEMBER("ROLE_ADMIN");

    RoleEnum(String title) {
        this.title = title;
    }

    @Getter
    private String title;
}
