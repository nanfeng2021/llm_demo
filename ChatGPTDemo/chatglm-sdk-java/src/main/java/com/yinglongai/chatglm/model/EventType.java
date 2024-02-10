package com.yinglongai.chatglm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: EventType
 * @Author: 南风
 * @Email: 1480207032@qq.com
 * @Description:
 * @Date: 2024/2/5 15:28
 */
@Getter
@AllArgsConstructor
public enum  EventType {
    add("add", "增量"),
    finish("finish", "结束"),
    error("error", "错误"),
    interrupted("interrupted", "中断"),

            ;
    private final String code;
    private final String info;
}
