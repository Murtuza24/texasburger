package com.egen.texasburger.interceptor;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

/**
 * @author Murtuza
 */

@Component
@Log4j2
public class RequestValidatorInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Before Handler execution");

        try {
            String user = request.getHeaders("USER").nextElement();
            log.info("Header user Param: {}", user);
        } catch (NoSuchElementException | NullPointerException e) {
            log.info("User doesn't exist in the header");
            response.sendRedirect("/auth-failed");
            return false;
        }

        return true;
    }
}
