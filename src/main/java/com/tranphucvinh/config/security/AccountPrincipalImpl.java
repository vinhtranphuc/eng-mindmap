package com.tranphucvinh.config.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tranphucvinh.mybatis.model.AccountVO;

import lombok.Getter;

public class AccountPrincipalImpl implements OAuth2User, UserDetails {

    private static final long serialVersionUID = 1L;

    @Getter
    private Long id;

    @JsonIgnore
    private String loginId;

    @JsonIgnore
    private String password;

    public boolean enabled;

    private boolean deleted;

    private Collection<? extends GrantedAuthority> authorities;

    private Map<String, Object> attributes;

    @Getter
    private AccountVO account;

    public AccountPrincipalImpl(AccountVO account) {
        this.id = account.getId();
        this.loginId = account.getLoginId();
        this.password = account.getPassword();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(account.getRole()));
        this.authorities = authorities;

        // save account
        this.account = account;
    }

     public static AccountPrincipalImpl create(AccountVO account) {
        return new AccountPrincipalImpl(account);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.deleted;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountPrincipalImpl that = (AccountPrincipalImpl) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getUsername() {
        return this.loginId;
    }

    @Override
    public String getName() {
        return this.loginId;
    }
}