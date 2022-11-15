package com.example.springscuritybe.config;

import com.example.springscuritybe.CustomUserDetailService;
import com.example.springscuritybe.config.dto.CustomAuthenticationSuccessHandler;
import com.example.springscuritybe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.logging.Filter;


/**
 *  WebSecurityConfigurerAdapter가 deprecated 됨 때문에 SecurityFilterChain을 Bean으로 등록해서 사용해야된다.
 */
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    private final RequestMatcher APP_LOGIN_REQUEST_MATCHER = new AntPathRequestMatcher("/app/login");
    private final UserRepository userRepository;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/**").authenticated()
                    .and()
                    .formLogin().disable()
                    .addFilterBefore(customAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                    .logout()
                    .invalidateHttpSession(true)



//                .exceptionHandling()
//                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider());
        super.configure(auth);
    }


    public AbstractAuthenticationProcessingFilter UsernamePasswordAuthenticationFilter() throws Exception {
        CustomUsernamePasswordTokenFilter customUsernamePasswordTokenFilter = new CustomUsernamePasswordTokenFilter(authenticationManager());
        customUsernamePasswordTokenFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        return customUsernamePasswordTokenFilter;
    }


    public AbstractAuthenticationProcessingFilter customAuthFilter() throws Exception {
        CustomAuthFilter customAuthFilter = new CustomAuthFilter(APP_LOGIN_REQUEST_MATCHER);
        customAuthFilter.setAuthenticationManager(authenticationManager());
        customAuthFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        return customAuthFilter;
    }

    public AuthenticationManager authenticationManager(){
        return new ProviderManager(customAuthenticationProvider());
    }



    public AuthenticationProvider customAuthenticationProvider(){
        return new CustomProvider(userDetailsService());
    }


    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new CustomAuthenticationSuccessHandler();
    }

    public UserDetailsService userDetailsService(){
        return new CustomUserDetailService(userRepository);
    }
}
