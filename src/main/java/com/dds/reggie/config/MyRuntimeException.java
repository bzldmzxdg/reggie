package com.dds.reggie.config;

//自定义运行时异常
public class MyRuntimeException extends RuntimeException{
    public MyRuntimeException(String msg){
        super(msg);
    }
}
