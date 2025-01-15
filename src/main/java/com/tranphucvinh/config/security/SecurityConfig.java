package com.tranphucvinh.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;
    @Autowired
    private LogoutHandlerImpl logoutHandlerImpl;
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandlerImpl;
    @Autowired
    private AuthProperties authProperties;

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter();
    }

    @Bean
    public AccessDeniedHanlderImpl accessDeniedHanlderImpl() {
        return new AccessDeniedHanlderImpl();
    }


    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        // cors session
        httpSecurity.cors().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .maximumSessions(1);

        // disable csrf
        httpSecurity.csrf().disable();

        httpSecurity
//                .authorizeRequests()
//                .antMatchers("/assets/**", "/store/**", "/favicon**")
//                    .permitAll()
//                .antMatchers(authProperties.getAuth().getLoginApiUrl())
//                    .permitAll()
//                .antMatchers("/**")
//                    .access("not(hasAnyRole('ROLE_ANONYMOUS')) and @RoleChecker.check(authentication, request)")
//                    .anyRequest().authenticated();
                  .authorizeRequests()
                      .antMatchers("/**")
                      .permitAll();


        httpSecurity.formLogin().loginPage(authProperties.getAuth().getLoginPageUrl()).permitAll();

        httpSecurity.logout(
                logout -> logout.logoutUrl(authProperties.getAuth().getLogoutUrl()).addLogoutHandler(logoutHandlerImpl)
                        .invalidateHttpSession(true).logoutSuccessHandler(logoutSuccessHandlerImpl));

        // Add our custom JWT security filter
        httpSecurity.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // httpBasic
        httpSecurity
            .httpBasic().disable()
            .exceptionHandling()
            .authenticationEntryPoint(unauthorizedHandler)
            .accessDeniedHandler(accessDeniedHanlderImpl());
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        super.configure(webSecurity);
        webSecurity.ignoring().antMatchers(
                    "/v3/api-docs/**",
                    "configuration/**",
                    "/swagger-ui/**",
                    "/swagger*/**",
                    "/webjars/**");
        webSecurity.httpFirewall(allowUrlEncodedSlashHttpFirewall());
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowSemicolon(true);
        return firewall;
    }

}