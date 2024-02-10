package com.yinglongai.chatglm.executor;

import com.yinglongai.chatglm.model.ChatCompletionRequest;
import com.yinglongai.chatglm.model.ChatCompletionSyncResponse;
import com.yinglongai.chatglm.model.ImageCompletionRequest;
import com.yinglongai.chatglm.model.ImageCompletionResponse;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.util.concurrent.CompletableFuture;

/**
 * @ClassName: Executor
 * @Author: 南风
 * @Email: 1480207032@qq.com
 * @Description:
 * @Date: 2024/2/5 15:36
 */
public interface Executor {
    /**
     * 问答模式，流式反馈
     *
     * @param chatCompletionRequest 请求信
     * @param eventSourceListener 实现监听；通过监听的 onEvent 方法接收数据
     * @return 应答结果
     * @throws Exception 异常
     */
    EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws Exception;

    /**
     * 问答模式， 同步反馈 ---- 用流式转化Future
     *
     * @param chatCompletionRequest 请求信息
     * @return 应答结果
     */
    CompletableFuture<String> completions(ChatCompletionRequest chatCompletionRequest) throws InterruptedException;

    /**
     * 同步应答接口
     *
     * @param chatCompletionRequest 请求信息
     * @return ChatCompletionSyncResponse
     * @throws java.io.IOException 异常
     */
    ChatCompletionSyncResponse completionsSync(ChatCompletionRequest chatCompletionRequest) throws Exception;

    /**
     * 图片生成接口
     *
     * @param request 请求信息
     * @return 应答结果
     * @throws Exception
     */
    ImageCompletionResponse genImages(ImageCompletionRequest request) throws Exception;
}
