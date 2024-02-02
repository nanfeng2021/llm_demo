package com.yinglongai.chatgpt.domain.security.service;

import com.yinglongai.chatgpt.domain.security.model.vo.JwtToken;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName: JwtFilter
 * @Author: 南风
 * @Email: 1480207032@qq.com
 * @Description: 自定义Filter，用户拦截Token的请求
 * @Date: 2024/2/1 22:40
 */
public class JwtFilter extends AccessControlFilter {

    private Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    /**
     * 判断是否携带有效的JwtToken
     * false 就走onAccessDenied
     * @param servletRequest
     * @param servletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    /**
     * 返回结果为true表明登录通过
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        JwtToken jwtToken = new JwtToken(request.getParameter("token"));
        try {
            // 鉴权认证
            getSubject(servletRequest, servletResponse).login(jwtToken);
            return true;
        } catch (Exception e) {
            logger.error("鉴权认证失败", e);
            onLoginFail(servletResponse);
            return false;
        }
    }

    /**
     * 鉴权认证失败时默认返回 401 状态码
     * @param response
     */
    private void onLoginFail(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("Auth Err!");
    }
}
