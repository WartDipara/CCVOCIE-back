package com.ela.ccvoice.common.common.utils;


import com.ela.ccvoice.common.common.domain.dto.RequestInfo;

/**
 * 请求上下文
 */
public class RequestHolder {

    private static final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();

    public static void set(RequestInfo requestInfo) {
        threadLocal.set(requestInfo);
    }

    public static RequestInfo get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
