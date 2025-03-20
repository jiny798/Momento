package jiny.futurevia.service.infra.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jiny.futurevia.service.infra.security.token.RestAuthenticationToken;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class RestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public RestAuthenticationFilter(HttpSecurity httpSecurity, String loginUrl, ObjectMapper objectMapper) {
        // 해당 URL 이 아니면, 인증을 진행하지 않는다
        super(new AntPathRequestMatcher(loginUrl, "POST"));
        this.objectMapper = objectMapper;

        setSecurityContextRepository(getSecurityContextRepository(httpSecurity));
    }

    private SecurityContextRepository getSecurityContextRepository(HttpSecurity httpSecurity) {
        SecurityContextRepository sharedRepository = httpSecurity.getSharedObject(SecurityContextRepository.class);
        if (sharedRepository == null) {
            sharedRepository = new DelegatingSecurityContextRepository(
                    new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository()
            );
        }
        return sharedRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {

        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            throw new IllegalArgumentException("HTTP method not supported: " + request.getMethod());
        }

        LoginInfo loginInfo = objectMapper.readValue(request.getInputStream(), LoginInfo.class);

        if (!StringUtils.hasText(loginInfo.getEmail()) || !StringUtils.hasText(loginInfo.getPassword())) {
            throw new IllegalArgumentException("Email or password not provided");
        }

        RestAuthenticationToken authToken = new RestAuthenticationToken(loginInfo.getEmail(), loginInfo.getPassword());

        return this.getAuthenticationManager().authenticate(authToken);
    }

    @Getter
    private static class LoginInfo {
        private String email;
        private String password;
    }
}
