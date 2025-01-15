package com.tranphucvinh.mybatis.model;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class MenuPermissionVO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2173928476657229260L;

    @JsonIgnore
    private String menuId;
    private String roleName;
    private Integer create;
    private Integer read;
    private Integer update;
    private Integer delete;
}

