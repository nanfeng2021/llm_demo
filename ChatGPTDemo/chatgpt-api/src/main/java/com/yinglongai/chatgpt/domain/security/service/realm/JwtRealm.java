package com.yinglongai.chatgpt.domain.security.service.realm;

import com.yinglongai.chatgpt.domain.security.model.vo.JwtToken;
import com.yinglongai.chatgpt.domain.security.service.JwtUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @ClassName: JwtRealm
 * @Author: 南风
 * @Email: 1480207032@qq.com
 * @Description:
 * @Date: 2024/2/1 22:41
 */
public class JwtRealm extends AuthorizingRealm {
    private Logger logger = LoggerFactory.getLogger(JwtRealm.class);

    private static JwtUtil jwtUtil = new JwtUtil();


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 暂时不需要实现
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String jwt = (String) authenticationToken.getPrincipal();
        if (jwt == null) {
            throw new NullPointerException("jwtToken不允许为空");
        }
        // 判断
        if (!jwtUtil.isVerify(jwt)) {
            throw new UnknownAccountException();
        }
        // 获取username信息
        String username = (String) jwtUtil.decode(jwt).get("username");
        logger.info("鉴权用户 username: {}", username);
        return new SimpleAuthenticationInfo(jwt, jwt, "JwtRealm");
    }
}
