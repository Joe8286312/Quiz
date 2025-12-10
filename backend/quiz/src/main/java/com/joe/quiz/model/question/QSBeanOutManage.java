package com.joe.quiz.model.question;

import lombok.Data;

import java.util.List;

@Data
public class QSBeanOutManage {
    private Integer id;
    private String questionText;
    private List<String> options;
    private String answer;
}
