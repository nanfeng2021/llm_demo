package com.yinglongai.chatglm.interceptor;

import com.yinglongai.chatglm.session.Configuration;
import com.yinglongai.chatglm.utils.BearerTokenUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @ClassName: OpenAiHTTPInterceptor
 * @Author: 南风
 * @Email: 1480207032@qq.com
 * @Description:
 * @Date: 2024/2/4 17:30
 */
public class OpenAiHTTPInterceptor implements Interceptor {
    /**
     * 智谱AI Jwt加密Token
     */
    private final Configuration configuration;

    public OpenAiHTTPInterceptor(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public @NotNull Response intercept(Chain chain) throws IOException {
        // 1.获取原始 Request
        Request original = chain.request();
        // 2.构建请求
        Request request = original.newBuilder()
                .url(original.url())
                .header("Authorization", "Bearer " + BearerTokenUtils.getToken(configuration.getApiKey(), configuration.getApiSecret()))
                .header("Content-Type", Configuration.JSON_CONTENT_TYPE)
                .header("User-Agent", Configuration.DEFAULT_USER_AGENT)
                .method(original.method(), original.body())
                .build();

        // 3.返回执行结果
        return chain.proceed(request);
    }
}
