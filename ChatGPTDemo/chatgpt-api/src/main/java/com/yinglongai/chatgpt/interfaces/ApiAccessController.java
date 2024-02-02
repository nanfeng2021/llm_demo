package com.yinglongai.chatgpt.interfaces;

import com.yinglongai.chatgpt.domain.security.service.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ApiAccessController
 * @Author: 南风
 * @Email: 1480207032@qq.com
 * @Description: API访问准入管理，当用户访问OpenAI时，需要进行准入验证
 * @Date: 2024/2/1 22:42
 */
@RestController
public class ApiAccessController {
    private Logger logger = LoggerFactory.getLogger(ApiAccessController.class);

    @RequestMapping("/authorize")
    public ResponseEntity<Map<String, String>> authorize(String username, String password) {
        Map<String, String> map = new HashMap<>();
        // 模拟账号和密码校验
        if (!"nanfeng".equals(username) || !"123".equals(password)) {
            map.put("msg", "用户名密码错误");
            return ResponseEntity.ok(map);
        }
        // 校验通过生成token
        JwtUtil jwtUtil = new JwtUtil();
        Map<String, Object> claim = new HashMap<>();
        claim.put("username", username);
        String jwtToken = jwtUtil.encode(username, 60 * 60 * 1000, claim);
        map.put("msg", "授权成功");
        map.put("token", jwtToken);
        // 生成返回码
        return ResponseEntity.ok(map);
    }


    /**
     * http://localhost:8080/verify?token=
     * @param token
     * @return
     */
    @GetMapping("/verify")
    public ResponseEntity<String> verify(String token) {
        logger.info("verify token: {}", token);
        return ResponseEntity.status(HttpStatus.OK).body("verify success!");
    }

    @GetMapping("/success")
    public String success() {
        return "test success nanfeng";
    }
}
