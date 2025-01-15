package com.tranphucvinh.config.security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;

import com.tranphucvinh.config.exception.BusinessException;

public interface RoleChecker extends InitializingBean {

    /**
     * Check boolean.
     *
     * @param authentication the authentication
     * @param request        the request
     * @return the boolean
     * @throws ServletException
     * @throws BusinessException
     */
    boolean check(Authentication authentication, HttpServletRequest request) throws ServletException;
}

