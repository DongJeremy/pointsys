package jp.co.nri.point.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jp.co.nri.point.domain.Token;
import jp.co.nri.point.dto.LoginUser;
import jp.co.nri.point.security.service.TokenService;
import jp.co.nri.point.util.ResponseUtil;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        Token token = tokenService.saveToken(loginUser);
        ResponseUtil.responseJson(response, HttpStatus.OK.value(), token);
    }

}
