package jp.co.nri.point.security.service;

import java.util.Set;

import jp.co.nri.point.domain.Token;
import jp.co.nri.point.dto.LoginUser;

public interface TokenService {

    Token saveToken(LoginUser loginUser);

    boolean deleteToken(String token);

    LoginUser getLoginUser(String token);

    void refresh(LoginUser loginUser);

    Set<String> getAllTokenList(String pattern);

    boolean deleteUUIDToken(String sessionId);

}
