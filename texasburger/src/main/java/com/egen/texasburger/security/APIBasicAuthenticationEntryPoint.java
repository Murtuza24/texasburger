//package com.egen.texasburger.security;
//
//import lombok.extern.log4j.Log4j2;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//
///**
// * @author Murtuza
// */
//
//@Log4j2
//@Component
//public class APIBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response,
//                         AuthenticationException authException) throws IOException {
//
//        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        PrintWriter writer = response.getWriter();
//        writer.println("HTTP Status 401 - " + authException.getMessage());
//
//        log.info("Initializing BasicAuthentication EntryPoint");
//    }
//
//    @Override
//    public void afterPropertiesSet() {
//        log.info("afterPropertiesSet called!!");
//        setRealmName("texashamburger");
//        super.afterPropertiesSet();
//    }
//}
