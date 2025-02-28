package com.tranphucvinh.config.security;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tranphucvinh.mybatis.model.AccountVO;
import com.tranphucvinh.service.AuthService;

/**
 * TODO : implement authentication user
 *
 * @author ADMIN
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AuthService authService;

    /**
     * check when login
     */
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        AccountVO account = authService.selectAccount(null, loginId);
        if(Objects.isNull(account)) {
            throw new UsernameNotFoundException("User not found with login ID : " + loginId);
        }
        if(!authService.isAdminRole(account.getRole())) {
            throw new UsernameNotFoundException("Admin user not found with login ID : " + loginId);
        }
        return AccountPrincipalImpl.create(account);
    }

    public UserDetails loadAvailbleUserAccountId(Long id) {
        AccountVO account = authService.selectAccount(String.valueOf(id), null);
        if(Objects.isNull(account)) {
            throw new UsernameNotFoundException("User not found with account id : " + id);
        }
        if(!authService.isAdminRole(account.getRole())) {
            throw new UsernameNotFoundException("Admin user not found with account id : " + id);
        }
        return AccountPrincipalImpl.create(account);
    }
}