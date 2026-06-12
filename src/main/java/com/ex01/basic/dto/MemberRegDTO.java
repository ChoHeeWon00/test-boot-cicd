package com.ex01.basic.dto;

import com.ex01.basic.entity.MemberEntity;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberRegDTO {
    private String username;
    private String password;
    private String role;
    private String fileName;
    public MemberRegDTO(MemberEntity memberEntity ){

        BeanUtils.copyProperties(memberEntity, this);
    }
}
