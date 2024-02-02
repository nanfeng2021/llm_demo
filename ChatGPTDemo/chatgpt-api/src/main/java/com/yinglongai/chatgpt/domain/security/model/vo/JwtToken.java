package com.yinglongai.chatgpt.domain.security.model.vo;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @ClassName: JwtToken
 * @Author: 南风
 * @Email: 1480207032@qq.com
 * @Description: Token 信息
 * @Date: 2024/2/1 22:41
 */
public class JwtToken implements AuthenticationToken {
    private String jwt;

    public JwtToken(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public Object getPrincipal() {
        return jwt;
    }

    @Override
    public Object getCredentials() {
        return jwt;
    }
}
