package com.ex01.basic.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginDTO {
    private String username;
    private String password;
}
