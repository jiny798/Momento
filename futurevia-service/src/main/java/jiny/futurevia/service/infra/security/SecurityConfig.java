package jiny.futurevia.service.infra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jiny.futurevia.service.infra.security.csrf.SpaCsrfTokenRequestHandler;
import jiny.futurevia.service.infra.security.filter.RestAuthenticationFilter;
import jiny.futurevia.service.infra.security.handler.Http401Handler;
import jiny.futurevia.service.infra.security.handler.Http403Handler;
import jiny.futurevia.service.infra.security.handler.LoginFailHandler;
import jiny.futurevia.service.infra.security.handler.LoginSuccessHandler;
import jiny.futurevia.service.modules.product.infra.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final DataSource dataSource;
    private final ObjectMapper objectMapper;
    private final AuthenticationProvider restAuthenticationProvider;
    private final ProductRepository productRepository;

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(restAuthenticationProvider);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        SpaCsrfTokenRequestHandler csrfTokenRequestHandler = new SpaCsrfTokenRequestHandler();

        http
                // request
                .securityMatcher("/**")
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/uploads/**","/css/**", "/images/**", "/js/**", "/favicon.ico", "/error").permitAll()
                                .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/api/user/**").hasAuthority("ROLE_USER")
                                .anyRequest().permitAll()
                )
                        .csrf(csrf -> csrf.disable());
                // csrf
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .csrfTokenRequestHandler(csrfTokenRequestHandler))
//                .addFilterBefore(new CsrfCookieFilter(), BasicAuthenticationFilter.class);

        http
                // 인증필터
                .addFilterBefore(restAuthenticationFilter(http, authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(authenticationManager)
                .exceptionHandling(e -> {
                    e.accessDeniedHandler(new Http403Handler(objectMapper)); // 인증O -> 접근 거부
                    e.authenticationEntryPoint(new Http401Handler(objectMapper)); // 인증X -> 접근 거부
                })
                .rememberMe(rm -> rm.rememberMeParameter("remember")
                        .alwaysRemember(false)
                        .tokenValiditySeconds(2592000)
                );


        return http.build();
    }

    public RestAuthenticationFilter restAuthenticationFilter(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) {
        RestAuthenticationFilter filter = new RestAuthenticationFilter(httpSecurity, "/api/login", objectMapper);
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(new LoginSuccessHandler(objectMapper));
        filter.setAuthenticationFailureHandler(new LoginFailHandler(objectMapper));

        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setValiditySeconds(3600 * 24 * 30);
        filter.setRememberMeServices(rememberMeServices);
        return filter;
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        var handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(new PostPermissionEvaluator(productRepository));
        return handler;
    }

}
