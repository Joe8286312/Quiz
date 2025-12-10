package com.joe.quiz.model.user;

import lombok.Data;

@Data
public class UserUpdateBean {
    private Long id;
    private String userName;
    private String userPassword;
    private Integer userRole;
    // 不需要包含敏感信息如密码确认等
}