package com.leapfrog.chattest.filter;

import com.leapfrog.chattest.commons.context.ContextHolderService;
import com.leapfrog.chattest.constants.ErrorMessage;
import com.leapfrog.chattest.exception.RestException;
import com.leapfrog.chattest.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class AuthFilter implements Filter {
    private final JwtUtil jwtUtil;
    private final ContextHolderService contextHolderService;
    public AuthFilter(JwtUtil jwtUtil, ContextHolderService contextHolderService) {
        this.jwtUtil = jwtUtil;
        this.contextHolderService = contextHolderService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("Inside JWT Filter");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        final String requestTokenHeader = request.getHeader("Authorization");
        String url = request.getRequestURI();
        if (!(isByPassUrl(url))) {
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer")) {
                String jwtToken = requestTokenHeader.substring(7);
                System.out.println(jwtToken);
                String username = jwtUtil.getUsernameFromToken(jwtToken);
                contextHolderService.setContext(username);
                log.info("Username from token:: " + username);
            } else {
                log.info(url);
                log.error("JWT Token does not begin with Bearer String");
                throw new RestException(ErrorMessage.INVALID_ACCESS_TOKEN);
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
    private boolean isByPassUrl(String url) {
        final String BASE_URL = "/chatapp/v1/users";
        final String CREATE_USER = BASE_URL + "/register-new";
        final String LOGIN_USER = BASE_URL + "/authenticate";
        final String WS_URL = BASE_URL ;
        List<String> byPassUrl = Arrays.asList(CREATE_USER, LOGIN_USER,WS_URL);
        return byPassUrl.stream().anyMatch(url::equalsIgnoreCase);
    }
}
