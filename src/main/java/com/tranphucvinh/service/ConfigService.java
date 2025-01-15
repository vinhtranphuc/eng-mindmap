package com.tranphucvinh.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tranphucvinh.config.security.Permission;
import com.tranphucvinh.mybatis.mapper.ConfigMapper;
import com.tranphucvinh.mybatis.model.MenuPermissionVO;
import com.tranphucvinh.mybatis.model.MenuResourceVO;
import com.tranphucvinh.mybatis.model.MenuResourceVO.ActionEnum;
import com.tranphucvinh.mybatis.model.MenuResourceVO.TypeEnum;
import com.tranphucvinh.mybatis.model.MenuTreeVO;
import com.tranphucvinh.mybatis.model.MenuVO;

@Service
public class ConfigService {

    @Resource
    private ConfigMapper configMapper;

    @Autowired
    private Permission permission;

    public List<String> getRoleNames() {
        return configMapper.selectRoleNames();
    }

    private String convertMenuResourceToText(List<MenuResourceVO> rs) {
        if(Objects.isNull(rs) || rs.size() < 1) {
            return "";
        }
        String text = "";
        for(MenuResourceVO e:rs) {
            text += e.getType()+"|"+e.getMethod()+"|"+e.getUrlPattern();
            text += "\n";
        }
        return text;
    }

    public List<MenuResourceVO> convertTextToMenuResource(String menuId, ActionEnum action, String text) {
        List<MenuResourceVO> resources = new ArrayList<>();
        if (StringUtils.isEmpty(text)) {
            return resources;
        }
        String[] lines = text.split("\n");
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length == 3) {
                MenuResourceVO resource = new MenuResourceVO();
                resource.setMenuId(menuId);
                resource.setAction(action);
                resource.setType(TypeEnum.valueOf(parts[0]));
                resource.setMethod(parts[1]);
                resource.setUrlPattern(parts[2]);
                resources.add(resource);
            }
        }
        return resources;
    }

    public List<MenuVO> getMenuList() {
        List<MenuVO> menuList = configMapper.selectMenuList();
        List<MenuPermissionVO> permissionList = configMapper.selectMenuPermissionList(null);
        List<MenuResourceVO> resources = configMapper.selectMenuResources();
        for(MenuVO menu:menuList) {
            List<MenuPermissionVO> menuPermissList = permissionList.stream().filter(t -> menu.getId().equals(t.getMenuId())).collect(Collectors.toList());
            menu.setPermission(menuPermissList);
            List<MenuResourceVO> menuResources = resources.stream().filter(t -> menu.getId().equals(t.getMenuId())).collect(Collectors.toList());
            if(resources.size() < 1) {
                continue;
            }

            List<MenuResourceVO> createRs = menuResources.stream().filter(t -> ActionEnum.CREATE == t.getAction()).collect(Collectors.toList());
            String createText = convertMenuResourceToText(createRs);
            menu.setCreateText(createText);

            List<MenuResourceVO> readRs = menuResources.stream().filter(t -> ActionEnum.READ == t.getAction()).collect(Collectors.toList());
            String readText = convertMenuResourceToText(readRs);
            menu.setReadText(readText);

            List<MenuResourceVO> updateRs = menuResources.stream().filter(t -> ActionEnum.UPDATE == t.getAction()).collect(Collectors.toList());
            String updateText = convertMenuResourceToText(updateRs);
            menu.setUpdateText(updateText);

            List<MenuResourceVO> deleteRs = menuResources.stream().filter(t -> ActionEnum.DELETE == t.getAction()).collect(Collectors.toList());
            String deleteText = convertMenuResourceToText(deleteRs);
            menu.setDeleteText(deleteText);
        }
        return menuList;
    }

    public List<MenuResourceVO> getPermissResourceList() {
        return configMapper.selectPermissResources();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<MenuVO> saveMenus(List<MenuVO> menuList) {
        try {
            // convert resource
            for(MenuVO menu:menuList) {
                List<MenuResourceVO> resources = new ArrayList<>();
                resources.addAll(convertTextToMenuResource(menu.getId(), ActionEnum.CREATE, menu.getCreateText()));
                resources.addAll(convertTextToMenuResource(menu.getId(), ActionEnum.READ, menu.getReadText()));
                resources.addAll(convertTextToMenuResource(menu.getId(), ActionEnum.UPDATE, menu.getUpdateText()));
                resources.addAll(convertTextToMenuResource(menu.getId(), ActionEnum.DELETE, menu.getDeleteText()));
                menu.setResources(resources);
            }
            configMapper.saveMenus(menuList);
            return getMenuList();
        } finally {
            permission.menus = getMenuList();
            permission.resources = getPermissResourceList();
            permission.splitResources();
        }
    }

    public List<MenuTreeVO> getMenuTree(String roleName) {
        List<MenuVO> menuList = permission.menus.stream().filter(t -> {
            if(StringUtils.isEmpty(t.getPath())) {
                return true;
            }
            if(Objects.nonNull(t.getPermission())) {
                return t.getPermission().stream().anyMatch(t1 -> {
                    return (roleName.equals(t1.getRoleName()) && Objects.equals(t1.getRead(), 1));
                });
            }
            return false;
        }).collect(Collectors.toList());
        return getSubMenuTreeByParentId(menuList,"0");
    }

    private List<MenuTreeVO> getSubMenuTreeByParentId(List<MenuVO> menuList, String parentId) {
        List<MenuTreeVO> tree = menuList.stream().filter(t -> parentId.equals(t.getParentId()))
                .map(t -> new MenuTreeVO(t)).collect(Collectors.toList());
        if(Objects.isNull(tree) || tree.size() < 1) {
            return null;
        }
        for(MenuTreeVO menu: tree) {
            List<MenuTreeVO> subTree = getSubMenuTreeByParentId(menuList, menu.getId());
            if(Objects.nonNull(subTree)) {
                menu.setHasChild(true);
                menu.setSubMenus(subTree);
            }
        }
        return tree;
    }
}
