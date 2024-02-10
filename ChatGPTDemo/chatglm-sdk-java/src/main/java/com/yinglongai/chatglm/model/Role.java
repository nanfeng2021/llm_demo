package com.yinglongai.chatglm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: Role
 * @Author: 南风
 * @Email: 1480207032@qq.com
 * @Description:
 * @Date: 2024/2/5 15:28
 */
@Getter
@AllArgsConstructor
public enum  Role {
    /**
     * user 用户输入的内容，role位user
     */
    user("user"),
    /**
     * 模型生成的内容，role位assistant
     */
    assistant("assistant"),

    /**
     * 系统
     */
    system("system"),

    ;
    private final String code;
}
