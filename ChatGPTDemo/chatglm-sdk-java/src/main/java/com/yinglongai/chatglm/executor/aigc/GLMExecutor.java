package com.yinglongai.chatglm.executor.aigc;

import com.alibaba.fastjson.JSON;
import com.yinglongai.chatglm.IOpenAiApi;
import com.yinglongai.chatglm.executor.Executor;
import com.yinglongai.chatglm.executor.result.ResultHandler;
import com.yinglongai.chatglm.model.*;
import com.yinglongai.chatglm.session.Configuration;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @ClassName: GLMExecutor
 * @Author: 南风
 * @Email: 1480207032@qq.com
 * @Description: 智谱AI 通用大模型 glm-3-turbo、glm-4 执行器
 * @Date: 2024/2/5 16:42
 */
@Slf4j
public class GLMExecutor implements Executor, ResultHandler {

    private final Configuration configuration;

    private final EventSource.Factory factory;

    private IOpenAiApi openAiApi;

    private OkHttpClient okHttpClient;

    public GLMExecutor(Configuration configuration) {
        this.configuration = configuration;
        this.factory = configuration.createRequestFactory();
    }

    @Override
    public EventSource completions(ChatCompletionRequest chatCompletionRequest,
                                   EventSourceListener eventSourceListener) throws Exception {
        // 构建请求信息
        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(IOpenAiApi.v4))
                .post(RequestBody.create(MediaType.parse(Configuration.JSON_CONTENT_TYPE), chatCompletionRequest.toString()))
                .build();

        // 返回事件结果
        return factory.newEventSource(request, chatCompletionRequest.getIsCompatible() ? eventSourceListener(eventSourceListener) : eventSourceListener);
    }

    @Override
    public CompletableFuture<String> completions(ChatCompletionRequest chatCompletionRequest) throws InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();
        StringBuffer dataBuffer = new StringBuffer();

        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(IOpenAiApi.v4))
                .post(RequestBody.create(MediaType.parse("application/json;charset=utf-8"), chatCompletionRequest.toString()))
                .build();

        factory.newEventSource(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                if ("[DONE]".equals(data)) {
                    log.info("[输出结束] Token {}", JSON.toJSONString(data));
                    return;
                }

                ChatCompletionResponse response = JSON.parseObject(data, ChatCompletionResponse.class);
                log.info("测试结果: {}", JSON.toJSONString(response));
                List<ChatCompletionResponse.Choice> choices = response.getChoices();
                for (ChatCompletionResponse.Choice choice : choices) {
                    if (!"stop".equals(choice.getFinishReason())) {
                        dataBuffer.append(choice.getDelta().getContent());
                    }
                }
            }

            @Override
            public void onClosed(EventSource eventSource) {
                future.complete(dataBuffer.toString());
            }

            @Override
            public void onFailure(EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                future.completeExceptionally(new RuntimeException("Request closed before completion"));
            }
        });

        return future;
    }

    @Override
    public ChatCompletionSyncResponse completionsSync(ChatCompletionRequest chatCompletionRequest) throws Exception {
        // sync 同步请求， stream为false
        chatCompletionRequest.setStream(false);

        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(IOpenAiApi.v4))
                .post(RequestBody.create(MediaType.parse(Configuration.JSON_CONTENT_TYPE),
                        chatCompletionRequest.toString()))
                .build();
        OkHttpClient okHttpClient = configuration.getOkHttpClient();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new RuntimeException("Request failed");
        }

        return JSON.parseObject(response.body().string(), ChatCompletionSyncResponse.class);
    }

    /**
     *
     * 图片生成，注释的方式留作扩展使用
     *
     * Request request = new Request.Builder()
     *         .url(configuration.getApiHost().concat(IOpenAiApi.cogview3))
     *         .post(RequestBody.create(MediaType.parse(Configuration.JSON_CONTENT_TYPE), imageCompletionRequest.toString()))
     *         .build();
     * // 请求结果
     * Call call = okHttpClient.newCall(request);
     * Response execute = call.execute();
     * ResponseBody body = execute.body();
     * if (execute.isSuccessful() && body != null) {
     *     String responseBody = body.string();
     *     ObjectMapper objectMapper = new ObjectMapper();
     *     return objectMapper.readValue(responseBody, ImageCompletionResponse.class);
     * } else {
     *     throw new IOException("Failed to get image response");
     * }
     * @param request 请求信息
     * @return
     * @throws Exception
     */
    @Override
    public ImageCompletionResponse genImages(ImageCompletionRequest request) throws Exception {
        return openAiApi.genImages(request).blockingGet();
    }

    @Override
    public EventSourceListener eventSourceListener(EventSourceListener eventSourceListener) {
        return new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                if ("[DONE]".equals(data)) {
                    return;
                }
                ChatCompletionResponse response = JSON.parseObject(data,
                        ChatCompletionResponse.class);
                if (response.getChoices()!= null && 1 == response.getChoices().size()
                        && "stop".equals(response.getChoices().get(0).getFinishReason())) {
                    eventSourceListener.onEvent(eventSource, id, EventType.finish.getCode(), data);
                    return;
                }
                eventSourceListener.onEvent(eventSource, id, EventType.add.getCode(), data);
            }

            @Override
            public void onClosed(EventSource eventSource) {
                eventSourceListener.onClosed(eventSource);
            }

            @Override
            public void onFailure(EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                eventSourceListener.onFailure(eventSource, t, response);
            }
        };
    }
}
