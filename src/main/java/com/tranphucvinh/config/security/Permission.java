package com.tranphucvinh.config.security;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.tranphucvinh.config.util.Utils;
import com.tranphucvinh.constant.Const;
import com.tranphucvinh.mybatis.model.MenuPermissionVO;
import com.tranphucvinh.mybatis.model.MenuResourceVO;
import com.tranphucvinh.mybatis.model.MenuResourceVO.ActionEnum;
import com.tranphucvinh.mybatis.model.MenuResourceVO.TypeEnum;
import com.tranphucvinh.mybatis.model.MenuVO;
import com.tranphucvinh.service.ConfigService;

@Component("permission")
public class Permission {

    @Autowired
    private ConfigService configService;

    public List<MenuVO> menus;
    public List<MenuResourceVO> resources;
    public List<MenuResourceVO> viewReources;
    public List<MenuResourceVO> dataResources;

    private AntPathMatcher antPath = new AntPathMatcher();

    @PostConstruct
    public void init() {
//        menus = configService.getMenuList();
//        resources = configService.getPermissResourceList();
//        splitResources();
    }

    public void splitResources() {
        viewReources = resources.stream().filter(t -> t.getType() == TypeEnum.VIEW).collect(Collectors.toList());
        dataResources = resources.stream().filter(t -> t.getType() == TypeEnum.DATA).collect(Collectors.toList());
    }

    public Boolean check(String roleName, HttpServletRequest request) {
        String requestURI = StringUtils.removeEnd(Utils.getPath(request), "/");
        if(StringUtils.isEmpty(requestURI)) {
            return true;
        }

        // check menu
        String pageContentHeader = request.getHeader(Const.PAGE_CONTENT_HEADER);
        boolean isFromPageContent = Objects.nonNull(pageContentHeader) && StringUtils.equals(Const.PAGE_CONTENT_HEADER, pageContentHeader);
        if(isFromPageContent) {
            Optional<MenuVO> menu = getMenu(requestURI);
            if(menu.isPresent()) {
                return checkMenu(roleName, menu);
            }
        }
        String method = request.getMethod();
        return checkResource(roleName, method, requestURI, resources);
    }

    public Optional<MenuVO> getMenu(String requestURI) {
        Optional<MenuVO> menu = menus.stream().filter(t -> {
            return StringUtils.equals(t.getPath(), requestURI);
        }).findFirst();
        return menu;
    }

    public Optional<MenuVO> getMenuByPageURI(String requestURI) {
        Optional<MenuVO> menu = menus.stream().filter(t -> {
            if(StringUtils.isEmpty(t.getPath())) {
                return false;
            }
            String menuPattern = t.getPath().concat("/**");
            return antPath.match(menuPattern, requestURI);
        }).findFirst();
        return menu;
    }

    public Boolean checkMenu(String roleName, Optional<MenuVO> menu) {
        if(menu.isPresent()) {
            List<MenuPermissionVO> pers = menu.get().getPermission();
            boolean result = Objects.nonNull(pers)&&pers.stream().anyMatch(t -> (Objects.equals(t.getRead(), 1) && StringUtils.equals(roleName, t.getRoleName())));
            return result;
        }
        return false;
    }

    public Boolean checkResource(String roleName, String method, String requestURI, List<MenuResourceVO> resources) {
        Boolean canExcute = resources.stream().anyMatch(t -> {
            Boolean checkRole = StringUtils.equals(t.getRoleName(), roleName);
            if(!checkRole) {
                return false;
            }
            Boolean checkRequest = antPath.match(t.getUrlPattern(), requestURI);
            if(!checkRequest) {
                return false;
            }
            Boolean checkMethod = StringUtils.equals(t.getMethod(), method);
            if(!checkMethod) {
                return false;
            }
            boolean canAction = false;
            switch (t.getAction()) {
                case CREATE:
                    if(t.isCanRead() && t.isCanCreate()) {
                        canAction = true;
                    }
                    break;
                case READ:
                    if(t.isCanRead()) {
                        canAction = true;
                    }
                    break;
                case UPDATE:
                    if(t.isCanRead() && t.isCanUpdate()) {
                        canAction = true;
                    }
                    break;
                case DELETE:
                    if(t.isCanRead() && t.isCanDelete()) {
                        canAction = true;
                    }
                    break;
                default:
                    break;
            }
            return canAction;
        });
        return canExcute;
    }

    public Boolean checkView(String pageURI) {
        String roleName = AuthUtil.crrAcc().getRole();
        Optional<MenuVO> menu = getMenu(pageURI);
        if(menu.isPresent()) {
            return checkMenu(roleName, menu);
        }
        String method = "GET";
        Boolean canView = checkResource(roleName, method, pageURI, viewReources);
        return canView;
    }

    public Boolean checkItem(ActionEnum itemAction) {
        String pageURI = Utils.getRequestUri();
        return checkItem(itemAction, pageURI);
    }

    public Boolean checkItem(ActionEnum itemAction, String pageURI) {
        Optional<MenuVO> menu = getMenuByPageURI(pageURI);
        if(!menu.isPresent()) {
            return false;
        }
        List<MenuPermissionVO> pers = menu.get().getPermission();
        if(Objects.isNull(pers) || pers.isEmpty()) {
            return false;
        }

        String roleName = AuthUtil.crrAcc().getRole();
        Boolean canShow = pers.stream().anyMatch(t -> {
            if(!StringUtils.equals(roleName, t.getRoleName())) {
                return false;
            }
            switch (itemAction) {
                case CREATE:
                    if(t.getCreate().equals(1)) {
                        return true;
                    }
                    break;
                case READ:
                    if(t.getRead().equals(1)) {
                        return true;
                    }
                    break;
                case UPDATE:
                    if(t.getUpdate().equals(1)) {
                        return true;
                    }
                    break;
                case DELETE:
                    if(t.getDelete().equals(1)) {
                        return true;
                    }
                    break;
            }
            return false;
        });
        return canShow;
    }
}
