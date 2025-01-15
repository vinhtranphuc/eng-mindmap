package com.tranphucvinh.config.security;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tranphucvinh.config.exception.LoginException;
import com.tranphucvinh.config.util.Utils;
import com.tranphucvinh.constant.Const;
import com.tranphucvinh.payload.ERROR;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Autowired
    private AuthProperties authProperties;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            AuthenticationException e) throws IOException, ServletException {

        boolean isFromPageContent = Objects.nonNull(httpServletRequest.getHeader(Const.PAGE_CONTENT_HEADER)) && StringUtils.equals(httpServletRequest.getHeader(Const.PAGE_CONTENT_HEADER), Const.PAGE_CONTENT_HEADER);
        String path = Utils.getPath(httpServletRequest);
        if(isFromPageContent || StringUtils.contains(path, "/api/")) {

            // create error body
            ERROR body = new ERROR(new LoginException(), HttpStatus.UNAUTHORIZED);
            httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(body));
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        httpServletResponse.sendRedirect(authProperties.getAuth().getLoginPageUrl());
    }
}