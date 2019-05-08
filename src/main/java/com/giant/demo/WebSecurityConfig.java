package com.giant.demo;

import com.giant.demo.filter.CsrfHeaderFilter;
import com.giant.demo.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@ComponentScan(value = "resources")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private ErrorAttributes errorAttributes;

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); /*Dao is Data Access Object*/
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*
        Except the home page and login page. All other html will be protected.
        The access to other page will be redirect to either login page or home page
        by: angular.
        The server side will be sending Unauthorized error,
        which will be captured and processed by client side, and redirect to different pages.
        */
        http
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/public/index.html",
                        "/public/login.html",
                        "/public/**/**",
                        "/**",
                        "/public/index",
                        "/public/login",
                        "/resources/public/login.html",
                        "/resources/public/index.html",
                        "/resources/public/login",
                        "/resources/public/index",
                        "/resources/public/**",
                        "/resources/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .failureForwardUrl("/login")
                .and()
                .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutSuccessUrl("/")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and()
                .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
                .csrf()
                .csrfTokenRepository(csrfTokenRepository())
                .and()
                .exceptionHandling()
                    .accessDeniedPage("/")
        ;
        /*http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login", "/register", "/","/index").permitAll()
                .antMatchers("/batch").authenticated()
                .antMatchers("/admin").hasAuthority("ROLE_"+UserType.ADMIN.toString())
                .and()
                .formLogin()
                .loginPage("/login")
                //.loginProcessingUrl("/login-processing")
                .successForwardUrl("/")
                //.failureForwardUrl("/login")
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/login")
        ;*/
    }
    /*Tell the name of the cookie*/
    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

}
