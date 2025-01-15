package com.tranphucvinh.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tranphucvinh.mybatis.model.MenuPermissionVO;
import com.tranphucvinh.mybatis.model.MenuResourceVO;
import com.tranphucvinh.mybatis.model.MenuVO;

@Mapper
public interface ConfigMapper {

    List<String> selectRoleNames();

    List<MenuVO> selectMenuListByRole(@Param("roleName") String roleName);

    List<MenuVO> selectMenuList();

    List<MenuPermissionVO> selectMenuPermissionList(@Param("menuId") String menuId);

    void saveMenus(@Param("menuList") List<MenuVO> menuList);

    List<MenuResourceVO> selectMenuResources();

    List<MenuResourceVO> selectPermissResources();

    void insertMenuResource(MenuResourceVO resource);
}
