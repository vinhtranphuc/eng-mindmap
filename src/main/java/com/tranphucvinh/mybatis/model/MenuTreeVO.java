package com.tranphucvinh.mybatis.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuTreeVO extends MenuVO {

    /**
     *
     */
    private static final long serialVersionUID = 5238245040495104707L;

    public MenuTreeVO(MenuVO menu) {
        this.setId(menu.getId());
        this.setTitle(menu.getTitle());
        this.setPath(menu.getPath());
        this.setParentId(menu.getParentId());
        this.setGroupSeq(menu.getGroupSeq());
        this.setPermission(menu.getPermission());
    }

    private Boolean hasChild = false;
    private List<MenuTreeVO> subMenus;

}

