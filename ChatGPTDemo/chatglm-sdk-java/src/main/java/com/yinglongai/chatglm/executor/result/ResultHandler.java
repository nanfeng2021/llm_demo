package com.yinglongai.chatglm.executor.result;

import okhttp3.sse.EventSourceListener;

/**
 * @ClassName: ResultHandler
 * @Author: 南风
 * @Email: 1480207032@qq.com
 * @Description: 结果封装器
 * @Date: 2024/2/5 16:43
 */
public interface ResultHandler {

    EventSourceListener eventSourceListener(EventSourceListener eventSourceListener);

}
