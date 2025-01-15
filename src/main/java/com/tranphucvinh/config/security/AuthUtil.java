package com.tranphucvinh.config.security;

import java.util.Objects;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tranphucvinh.mybatis.model.AccountVO;

public class AuthUtil {

    static Authentication authentication;

//    static {
//        authentication = SecurityContextHolder.getContext().getAuthentication();
//    }
    public static boolean isAuthenticated() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.isNull(authentication)) {
            return false;
        }
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public static AccountVO crrAcc() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!isAuthenticated()) {
            throw new BadCredentialsException("BadCredentialsException");
        }
        AccountPrincipalImpl principal = (AccountPrincipalImpl) authentication.getPrincipal();
        return principal.getAccount();
    }
}
