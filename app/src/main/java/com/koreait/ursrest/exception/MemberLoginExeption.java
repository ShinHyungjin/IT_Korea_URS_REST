package com.koreait.ursrest.exception;

public class MemberLoginExeption extends RuntimeException {
    public MemberLoginExeption(String msg){
        super(msg);
    }
    public MemberLoginExeption(String msg, Throwable e){
        super(msg, e);
    }
    public MemberLoginExeption(Throwable e){
        super(e);
    }

}
