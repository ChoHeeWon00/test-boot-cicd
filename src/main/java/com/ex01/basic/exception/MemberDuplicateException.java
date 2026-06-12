package com.ex01.basic.exception;

public class MemberDuplicateException extends RuntimeException{
    public MemberDuplicateException(String username){
        super(username + " 존재하는 회원입니다");
    }
}
