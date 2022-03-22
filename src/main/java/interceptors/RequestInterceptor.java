package interceptors;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        boolean iaAuthenticated = false;
        String basicAuth = request.getHeader(AUTHORIZATION_HEADER);

        String base64Credentials = basicAuth.substring("Basic".length()).trim();
        byte[] decodedCredentials = Base64.getDecoder().decode(base64Credentials);
        String clearCredentials = new String(decodedCredentials, StandardCharsets.UTF_8);

        String [] credentials = clearCredentials.split(":", 2);

        //TODO Authenticate user

        return iaAuthenticated;
    }
}
