package com.yinglongai.chatglm.model;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: ImageCompletionResponse
 * @Author: 南风
 * @Email: 1480207032@qq.com
 * @Description:
 * @Date: 2024/2/5 18:07
 */
@Data
public class ImageCompletionResponse {
    /**
     * 请求创建时间，是以秒为单位的Unix时间戳。
     */
    private Long created;

    private List<Image> data;

    @Data
    public static class Image {
        private String url;
    }
}
