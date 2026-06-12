package com.ex01.basic.exception;

public class InvalidLoginException  extends RuntimeException {
    public InvalidLoginException(){
        super("아이디 또는 비밀번호 틀림");
    }
}
