package com.tranphucvinh.mybatis.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class MenuResourceVO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5620088496273979353L;

    private String menuId;
    private ActionEnum action;
    private TypeEnum type;
    private String urlPattern;
    private String method;
    private String roleName;
    private boolean canCreate;
    private boolean canRead;
    private boolean canUpdate;
    private boolean canDelete;

    public enum ActionEnum {
        CREATE,
        READ,
        UPDATE,
        DELETE
    }

    public enum TypeEnum {
        DATA,
        VIEW
    }
}
