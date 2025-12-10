package com.joe.quiz.model.question;

import lombok.Data;

import java.util.Date;

@Data
public class Question {
    private Integer id;
    private String questionText;

    private String answer1Text;
    private Boolean answer1Correct;

    private String answer2Text;
    private Boolean answer2Correct;

    private String answer3Text;
    private Boolean answer3Correct;

    private String answer4Text;
    private Boolean answer4Correct;

    /* 是否删除 */
    private Integer isDelete;

    /* 创建时间 */
    private Date createTime;

    /*更新时间 */
    private Date updateTime;
    // Getters and Setters
}

