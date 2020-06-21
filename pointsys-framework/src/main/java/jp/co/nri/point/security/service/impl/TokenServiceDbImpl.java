package jp.co.nri.point.security.service.impl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jp.co.nri.point.domain.Token;
import jp.co.nri.point.domain.TokenModel;
import jp.co.nri.point.dto.LoginUser;
import jp.co.nri.point.security.mapper.TokenMapper;
import jp.co.nri.point.security.service.TokenService;

@Primary
@Service
public class TokenServiceDbImpl implements TokenService {

    private static final Logger log = LoggerFactory.getLogger("adminLogger");

    @Value("${token.expire.seconds}")
    private Integer expireSeconds;

    @Autowired
    private TokenMapper tokenMapper;

    @Value("${token.jwtSecret}")
    private String jwtSecret;

    private static Key KEY = null;

    private static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";

    private Key getKeyInstance() {
        if (KEY == null) {
            synchronized (TokenServiceDbImpl.class) {
                if (KEY == null) {// 双重锁
                    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecret);
                    KEY = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
                }
            }
        }

        return KEY;
    }

    private LoginUser toLoginUser(TokenModel model) {
        if (model == null) {
            return null;
        }

        // 校验是否已过期
        if (model.getExpireTime().getTime() > System.currentTimeMillis()) {
            return JSONObject.parseObject(model.getVal(), LoginUser.class);
        }

        return null;
    }

    private String createJWTToken(LoginUser loginUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(LOGIN_USER_KEY, loginUser.getToken());// 放入一个随机字符串，通过该串可找到登陆用户

        String jwtToken = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, getKeyInstance())
                .compact();

        return jwtToken;
    }
    
    private String getJWTFromUUID(String uuidToken) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(LOGIN_USER_KEY, uuidToken);

        String jwtToken = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, getKeyInstance())
                .compact();

        return jwtToken;
    }

    private String getUUIDFromJWT(String jwtToken) {
        if ("null".equals(jwtToken) || StringUtils.isBlank(jwtToken)) {
            return null;
        }

        Map<String, Object> jwtClaims = null;
        try {
            jwtClaims = Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(jwtToken).getBody();
            return MapUtils.getString(jwtClaims, LOGIN_USER_KEY);
        } catch (ExpiredJwtException e) {
            log.error("{}已过期", jwtToken);
        } catch (Exception e) {
            log.error("{}", e);
        }

        return null;
    }

    @Override
    public Token saveToken(LoginUser loginUser) {
        loginUser.setToken(UUID.randomUUID().toString());
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireSeconds * 1000);

        TokenModel model = new TokenModel();
        model.setId(loginUser.getToken());
        model.setCreateTime(new Date());
        model.setUpdateTime(new Date());
        model.setExpireTime(new Date(loginUser.getExpireTime()));
        model.setVal(JSONObject.toJSONString(loginUser));

        tokenMapper.save(model);
        // 登陆日志
        // logService.save(loginUser.getId(), "登陆", true, null);

        String jwtToken = createJWTToken(loginUser);

        return new Token(jwtToken, loginUser.getLoginTime());
    }

    @Override
    public boolean deleteToken(String jwtToken) {
        String uuidToken = getUUIDFromJWT(jwtToken);
        return deleteUUIDToken(uuidToken);
    }

    @Override
    public LoginUser getLoginUser(String jwtToken) {
        String uuidToken = getUUIDFromJWT(jwtToken);
        if (uuidToken != null) {
            TokenModel model = tokenMapper.getById(uuidToken);
            return toLoginUser(model);
        }

        return null;
    }

    @Override
    public void refresh(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireSeconds * 1000);

        TokenModel model = tokenMapper.getById(loginUser.getToken());
        model.setUpdateTime(new Date());
        model.setExpireTime(new Date(loginUser.getExpireTime()));
        model.setVal(JSONObject.toJSONString(loginUser));

        tokenMapper.update(model);

    }

    @Override
    public Set<String> getAllTokenList(String pattern) {
        Set<String> tokenModels = tokenMapper.getAll();
        Set<String> jwtSet = tokenModels.stream().map(model-> getJWTFromUUID(model)).collect(Collectors.toSet());
        return jwtSet;
    }

    @Override
    public boolean deleteUUIDToken(String uuidToken) {
        if (uuidToken != null) {
            TokenModel model = tokenMapper.getById(uuidToken);
            LoginUser loginUser = toLoginUser(model);
            if (loginUser != null) {
                tokenMapper.delete(uuidToken);
                // logService.save(loginUser.getId(), "退出", true, null);

                return true;
            }
        }

        return false;
    }

}
