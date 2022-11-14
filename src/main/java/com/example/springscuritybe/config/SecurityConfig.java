package com.example.springscuritybe.config;

import com.example.springscuritybe.CustomUserDetailService;
import com.example.springscuritybe.config.dto.CustomAuthenticationSuccessHandler;
import com.example.springscuritybe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;


/**
 *  WebSecurityConfigurerAdapter가 deprecated 됨 때문에 SecurityFilterChain을 Bean으로 등록해서 사용해야된다.
 */
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    private final RequestMatcher LOGIN_REQUEST_MATCHER = new AntPathRequestMatcher("/login");
    private final UserRepository userRepository;
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CustomUsernamePasswordTokenFilter customUsernamePasswordToken = new CustomUsernamePasswordTokenFilter(authenticationManager());
        customUsernamePasswordToken.setAuthenticationSuccessHandler(authenticationSuccessHandler());


        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/**").authenticated()
                    .and()
                    .formLogin().disable()
                    .addFilterBefore(customUsernamePasswordToken, UsernamePasswordAuthenticationFilter.class)
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
    @Bean
    public AuthenticationProvider customAuthenticationProvider(){
        return new CustomProvider(userDetailsService());
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailService(userRepository);
    }
}
