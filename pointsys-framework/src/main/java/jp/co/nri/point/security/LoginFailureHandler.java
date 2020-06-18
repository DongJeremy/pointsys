package jp.co.nri.point.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jp.co.nri.point.beans.ResponseBean;
import jp.co.nri.point.constant.Constants;
import jp.co.nri.point.util.ResponseUtil;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        String msg = null;
        if (exception instanceof BadCredentialsException) {
            msg = Constants.BADCREDENTIALS_MESSAGE;
        } else {
            msg = exception.getMessage();
        }
        ResponseBean info = new ResponseBean(HttpStatus.UNAUTHORIZED.value(), msg);
        ResponseUtil.responseJson(response, HttpStatus.UNAUTHORIZED.value(), info);
    }

}
