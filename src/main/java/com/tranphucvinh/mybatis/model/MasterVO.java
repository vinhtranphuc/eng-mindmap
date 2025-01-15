package com.tranphucvinh.mybatis.model;

import lombok.Data;

@Data
public class MasterVO {

    private String type;
    private String code;
    private String value;
    private String valueMy;
    private String valueEn;
    private String parentType;
    private String parentCode;
    private Integer sortOrder;
    private String note;
    private Boolean disabled;
}
