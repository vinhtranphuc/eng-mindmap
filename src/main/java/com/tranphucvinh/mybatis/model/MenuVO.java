package com.tranphucvinh.mybatis.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class MenuVO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8102087761097826977L;

    private String id;
    private String title;
    private String path;
    private String parentId;
    private Integer groupSeq;
    private List<MenuPermissionVO> permission;
    private String createText;
    private String readText;
    private String updateText;
    private String deleteText;
    List<MenuResourceVO> resources;
}
