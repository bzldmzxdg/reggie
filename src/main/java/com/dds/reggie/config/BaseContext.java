package com.dds.reggie.config;

public class BaseContext {

    private static ThreadLocal<Long> threadLocal_userId = new ThreadLocal<>();

    /**
     * 存值
     */

    public static void set(Long id){
        threadLocal_userId.set(id);
    }

    /**
     * 取值
     */

    public static Long get(){
        return threadLocal_userId.get();
    }
}
