package com.yinglongai.chatglm.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ImageCompletionRequest
 * @Author: 南风
 * @Email: 1480207032@qq.com
 * @Description:
 * @Date: 2024/2/5 18:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageCompletionRequest {
    /**
     * 模型；24年1月发布了 cogview-3 生成图片模型
     */
    private Model model = Model.COGVIEW_3;

    /**
     *
     */
    private String prompt;

    public String getModel() {
        return model.getCode();
    }

    public Model getModelEnum() {
        return model;
    }

    @Override
    public String toString() {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("model", model.getCode());
        paramsMap.put("prompt", prompt);
        try {
            return new ObjectMapper().writeValueAsString(paramsMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
