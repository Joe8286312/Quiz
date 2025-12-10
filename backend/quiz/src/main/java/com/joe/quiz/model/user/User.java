package com.joe.quiz.model.user;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    /** 用户ID */
    private Long id;

    /** 用户名 */
    private String userName;

    /* 密码*/
    private String userPassword;

    /* 是否删除 */
    private Integer isDelete;

    /* 用户角色：0-普通用户，1-管理员 */
    private Integer userRole;

    /* 创建时间 */
    private Date createTime;

    /*更新时间 */
    private Date updateTime;
}

//@Data
//public class User {
//    private Long id;
//    private String name;
//    private Integer age;
//    private String email;
//}
