package jp.co.nri.point.security.service.impl;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jp.co.nri.point.constant.Constants;
import jp.co.nri.point.domain.Token;
import jp.co.nri.point.dto.LoginUser;
import jp.co.nri.point.security.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

    /**
     * token过期秒数
     */
    @Value("${token.expire.seconds}")
    private Integer expireSeconds;

    @Autowired
    private RedisTemplate<String, LoginUser> redisTemplate;

    @Override
    public Token saveToken(LoginUser loginUser) {
        String token = UUID.randomUUID().toString();

        loginUser.setToken(token);
        cacheLoginUser(loginUser);

        return new Token(token, loginUser.getLoginTime());
    }

    private void cacheLoginUser(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireSeconds * 1000);
        // 缓存
        redisTemplate.boundValueOps(getTokenKey(loginUser.getToken())).set(loginUser, expireSeconds, TimeUnit.SECONDS);
    }

    /**
     * 更新缓存的用户信息
     */
    @Override
    public void refresh(LoginUser loginUser) {
        cacheLoginUser(loginUser);
    }

    @Override
    public LoginUser getLoginUser(String token) {
        return redisTemplate.boundValueOps(getTokenKey(token)).get();
    }

    @Override
    public boolean deleteToken(String token) {
        String key = getTokenKey(token);
        LoginUser loginUser = redisTemplate.opsForValue().get(key);
        if (loginUser != null) {
            redisTemplate.delete(key);

            return true;
        }

        return false;
    }

    private String getTokenKey(String token) {
        return Constants.REDIS_TOKEN_KEY + token;
    }

    @Override
    public Set<String> getAllTokenList(String pattern) {
        Set<String> keySet = redisTemplate.keys(pattern);
        return keySet;
    }

}