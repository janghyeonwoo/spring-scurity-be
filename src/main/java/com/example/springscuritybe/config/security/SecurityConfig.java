package com.example.springscuritybe.config.security;


import com.example.springscuritybe.config.security.handler.CustomAuthenticationSuccessHandler;
import com.example.springscuritybe.config.security.filter.CustomAuthFilter;
import com.example.springscuritybe.config.security.filter.CustomJwtAuthFilter;
import com.example.springscuritybe.config.security.filter.CustomUsernamePasswordTokenFilter;
import com.example.springscuritybe.config.security.handler.CustomOauthSuccessHandler;
import com.example.springscuritybe.config.security.provider.CustomProvider;
import com.example.springscuritybe.config.security.provider.JwtTokenProvider;
import com.example.springscuritybe.config.security.service.CustomOauthUserService;
import com.example.springscuritybe.config.security.service.CustomUserDetailService;
import com.example.springscuritybe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;


/**
 * WebSecurityConfigurerAdapter가 deprecated 됨 때문에 SecurityFilterChain을 Bean으로 등록해서 사용해야된다.
 */
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final RequestMatcher APP_LOGIN_REQUEST_MATCHER = new AntPathRequestMatcher("/app/login");
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOauthUserService customOauthUserService;

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().mvcMatchers("/members/**", "/image/**");    // /image/** 있는 모든 파일들은 시큐리티 적용을 무시한다.
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());    // 정적인 리소스들에 대해서 시큐리티 적용 무시.
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/login", "/user/join").permitAll()
                .antMatchers("/user/**").authenticated()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .logout()
                    .invalidateHttpSession(true)
                .and()
                    .formLogin().disable()
                    .addFilterBefore(new CustomJwtAuthFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling()
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                    .oauth2Login()
                    .userInfoEndpoint().userService(customOauthUserService)
                .and()
                    .successHandler(oAuth2AuthenticationSuccessHandler())

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

//


    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthenticationProvider());
    }

    public AuthenticationProvider customAuthenticationProvider() {
        return new CustomProvider(userDetailsService());
    }

    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    public UserDetailsService userDetailsService() {
        return new CustomUserDetailService(userRepository);
    }

    @Bean
    public CustomOauthSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new CustomOauthSuccessHandler();
    }



}
