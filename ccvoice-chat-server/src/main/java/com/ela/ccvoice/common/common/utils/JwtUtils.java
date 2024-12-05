package com.ela.ccvoice.common.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * jwt的token生成和解析
 */
@Slf4j
@Component
public class JwtUtils {
    //jwt密钥
    @Value("${jwt.secret}")
    private String secret;

    private static final String UID_CLAIM = "uid";
    private static final String CREATE_TIME = "createTime";

    /**
     * 生成token
     * JWT 三部件： header payload signature
     *
     * @param uid
     * @return
     */
    public String createToken(Long uid) {
        String token = JWT.create() //header
                .withClaim(UID_CLAIM, uid) //只有一个uid，其余要去redis查  payload
                .withClaim(CREATE_TIME, new Date()) //payload
                .sign(Algorithm.HMAC256(secret)); //签名signature
        return token;
    }

    /**
     * 对token进行解密
     *
     * @param token
     * @return
     */
    public Map<String, Claim> verifyToken(String token) {
        if (token.isEmpty()) {
            return null;
        }
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            return decodedJWT.getClaims();
        } catch (Exception ex) {
            log.error("decode error,token:{}", token, ex);
        }
        return null;
    }

    /**
     * 根据uid来获取token
     *
     * @param token
     * @return
     */
    public Long getUid(String token) {
        //optional 优化判断和避免nullPointerException
        return Optional.ofNullable(verifyToken(token))
                .map(map->map.get(UID_CLAIM))
                .map(Claim::asLong)
                .orElse(null);
        //    Map<String, Claim> claims = verifyToken(token);
        //    if (claims == null) {
        //        return null;
        //    }
        //    Claim uidClaim = claims.get(UID_CLAIM);
        //    if (uidClaim == null) {
        //        return null;
        //    }
        //    return uidClaim.asLong();
    }
}
