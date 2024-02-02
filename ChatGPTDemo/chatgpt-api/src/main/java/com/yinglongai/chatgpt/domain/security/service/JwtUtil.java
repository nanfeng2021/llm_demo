package com.yinglongai.chatgpt.domain.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ser.Serializers;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;


import java.util.*;

/**
 * @ClassName: JwtUtil
 * @Author: 南风
 * @Email: 1480207032@qq.com
 * @Description: 生成JwtToken，获取JwtToken中的封装信息，判断JwtToken是否有效
 * @Date: 2024/2/1 22:40
 */
public class JwtUtil {
    // 创建默认的秘钥和算法，供无参的构造方法使用
    private static final String defaultBase64EncodedSecretKey = "B*B^";
    private static final SignatureAlgorithm defaultSignatureAlgorithm = SignatureAlgorithm.HS256;

    public JwtUtil() {
        this(defaultBase64EncodedSecretKey, defaultSignatureAlgorithm);
    }

    private final String base64EncodedSecretKey;
    private final SignatureAlgorithm signatureAlgorithm;

    public JwtUtil(String secretKey, SignatureAlgorithm signatureAlgorithm) {
        this.base64EncodedSecretKey = Base64.encodeBase64String(secretKey.getBytes());
        this.signatureAlgorithm = signatureAlgorithm;
    }

    /**
     * 这里就是产生jwt字符串的地方
     * jwt字符串包括三个部分
     * 1. header
     *    当前字符串的类型，一般都是"JWT"
     *    哪种算法加密，"HS256"或者其他的加密算法
     *    所以一般都是固定的，没有什么变化
     * 2.payload
     *    一般有四个最常见的标准字段
     *    iat：签发时间，也就是这个jwt什么时候生成的
     *    jti：JWT的唯一标识
     *    iss：签发人，一般都是username或者userId
     *    exp：过期时间
     * @param issuser 签发人
     * @param ttlMillis 生存时间
     * @param claims 指还想要在jwt中存储的一些非隐私信息
     * @return
     */
    public String encode(String issuser, long ttlMillis, Map<String, Object> claims) {
        if (claims == null) {
            claims = new HashMap<>();
        }
        long nowMillis = System.currentTimeMillis();

        JwtBuilder builder = Jwts.builder()
                // 荷载部分
                .setClaims(claims)
                // JWT唯一标识，一般设置成唯一的
                .setId(UUID.randomUUID().toString())
                // 签发时间
                .setIssuedAt(new Date(nowMillis))
                // 签发人，也就是JWT是给谁的（逻辑上一般都是username或者userId）
                .setSubject(issuser)
                // 生成jwt使用的算法和秘钥
                .signWith(signatureAlgorithm, base64EncodedSecretKey);
        if (ttlMillis >= 0) {
            long expMills = nowMillis + ttlMillis;
            // 过期时间，使用当前时间+前面传入的持续时间生成
            Date exp = new Date(expMills);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 相当于encode的方向，传入jwtToken生成对应的username和password等字段。
     * Claim就是一个map
     * 拿到荷载部分所有的键值对
     * @param jwtToken
     * @return
     */
    public Claims decode(String jwtToken) {
        // 得到DefaultJwtParser
        return Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(base64EncodedSecretKey)
                // 设置需要解析的 jwt
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    /**
     *
     * @param jwtToken
     * @return
     */
    public boolean isVerify(String jwtToken) {
        // 官方自己的校验规则，只写一个校验算法
        Algorithm algorithm = null;
        switch (signatureAlgorithm) {
            case HS256:
                algorithm = Algorithm.HMAC256(Base64.decodeBase64(base64EncodedSecretKey));
                break;
            default:
                throw new RuntimeException("不支持该算法");
        }

        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(jwtToken);
        // 校验不通过会抛出异常
        // 判断合法的标准： 1、头部和荷载部分没有篡改过 2、没有过期
        return true;
    }


}
