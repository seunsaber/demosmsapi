package com.seun.demosms.interceptors;

import com.seun.demosms.exceptions.AuthenticationException;
import com.seun.demosms.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Autowired
    private AccountService accountService;

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String basicAuth = request.getHeader(AUTHORIZATION_HEADER);

        String base64Credentials = basicAuth.substring("Basic".length()).trim();
        byte[] decodedCredentials = Base64.getDecoder().decode(base64Credentials);
        String clearCredentials = new String(decodedCredentials, StandardCharsets.UTF_8);

        String [] credentials = clearCredentials.split(":", 2);
        String username = credentials[0];
        String password = credentials[1];

        boolean iaAuthenticated = accountService.authenticateUser(username, password);

        if(!iaAuthenticated)
            throw new AuthenticationException("Authentication failed");

        return true;
    }
}
