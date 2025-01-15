package com.tranphucvinh.mybatis.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class AccountVO {

    private Long id;
    private String role;

    private String accountProvider;

    private String loginId;
    private String name;

    @JsonIgnore
    private String password;

    private LocalDateTime createdDt;
    private Long createdId;

    private LocalDateTime updatedDt;
    private String updatedId;

    private LocalDateTime deletedDt;
    private Long deletedId;
}
