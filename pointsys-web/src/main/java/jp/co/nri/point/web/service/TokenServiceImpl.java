package jp.co.nri.point.web.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import jp.co.nri.point.domain.Token;
import jp.co.nri.point.dto.LoginUser;
import jp.co.nri.point.security.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public Token saveToken(LoginUser loginUser) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean deleteToken(String token) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public LoginUser getLoginUser(String token) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void refresh(LoginUser loginUser) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Set<String> getAllTokenList(String pattern) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean deleteUUIDToken(String sessionId) {
        // TODO Auto-generated method stub
        return false;
    }

}
