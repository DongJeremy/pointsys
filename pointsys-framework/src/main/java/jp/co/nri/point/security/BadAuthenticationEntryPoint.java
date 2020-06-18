package jp.co.nri.point.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jp.co.nri.point.beans.ResponseBean;
import jp.co.nri.point.constant.Constants;
import jp.co.nri.point.util.ResponseUtil;

@Component
public class BadAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        ResponseBean info = new ResponseBean(HttpStatus.UNAUTHORIZED.value(), Constants.UNAUTHORIZED_MESSAGE);
        ResponseUtil.responseJson(response, HttpStatus.UNAUTHORIZED.value(), info);
    }

}
