package com.ex01.basic.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MemberNotFoundException extends RuntimeException{
    public MemberNotFoundException(String msg){
        super(msg);
    }
}
