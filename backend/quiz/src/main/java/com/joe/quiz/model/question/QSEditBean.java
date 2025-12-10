package com.joe.quiz.model.question;

import lombok.Data;

@Data
public class QSEditBean {
    private Integer id;          // 新增：编辑时需要问题ID
    private String question;
    private String optiona;
    private String optionb;
    private String optionc;
    private String optiond;
    private String answer; // "a", "b", "c", or "d"
}
