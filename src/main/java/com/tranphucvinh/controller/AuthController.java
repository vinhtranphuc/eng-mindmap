package com.tranphucvinh.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tranphucvinh.config.security.AuthProperties;
import com.tranphucvinh.config.security.AuthUtil;
import com.tranphucvinh.config.security.JwtTokenProvider;
import com.tranphucvinh.payload.BODY;
import com.tranphucvinh.payload.ReqLogin;
import com.tranphucvinh.service.AuthService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AuthService authService;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    AuthProperties authProperties;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private HttpSession httpSession;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("login")
    public String login(Model model, HttpServletRequest request, HttpServletResponse response) {
        if (AuthUtil.isAuthenticated()) {
            return "redirect:/";
        }
        return "/auth/login";
    }

    @PostMapping(path = "api/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody ReqLogin reqLogin, HttpServletResponse response,
            HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(reqLogin.getUsername(), reqLogin.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateTokenDefault(authentication);
            httpSession.setAttribute(authProperties.getAuth().getTokenHeader(), "Bearer " + jwt);
            return ResponseEntity.ok(new BODY(jwt, "Login OK"));
        }catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호를 잘못 입력했습니다. 입력하신 내용을 다시 확인해 주세요.");
            }
            logger.error("Exception : {}", ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
