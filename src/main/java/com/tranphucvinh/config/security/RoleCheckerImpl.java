package com.tranphucvinh.config.security;

import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.tranphucvinh.config.util.Utils;
import com.tranphucvinh.mybatis.model.AccountVO;

@Component("RoleChecker")
public class RoleCheckerImpl implements RoleChecker {

    @Autowired
    private Permission permission;

    @Autowired
    AuthProperties authProperties;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @SuppressWarnings("unused")
    private HttpSession httpSession;

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean check(Authentication authentication, HttpServletRequest request) throws ServletException {

        AccountPrincipalImpl principal = (AccountPrincipalImpl) authentication.getPrincipal();
        AccountVO account = principal.getAccount();
        String roleName = account.getRole();

        // check edit role
        String jwt = tokenProvider.getJwtFromRequest(request);
        String roleNameJwt = tokenProvider.getClaimFromJWT(jwt, "roleName");

        // detect update role
        String requestURI = org.apache.commons.lang3.StringUtils.removeEnd(Utils.getPath(request), "/");
        if(StringUtils.isNotEmpty(requestURI) && !StringUtils.equals(roleNameJwt, roleName)) {
            // update to new jwt
            Date expiration = tokenProvider.getExpryDateFromJwt(jwt);
            String newJwt = tokenProvider.generateTokenDefault(authentication, expiration);
            String tokenHeader = authProperties.getAuth().getTokenHeader();
            httpSession = request.getSession();
            request.getSession().setAttribute(tokenHeader, "Bearer " + newJwt);
            return false;
        }

        return permission.check(roleName, request);
    }
}

