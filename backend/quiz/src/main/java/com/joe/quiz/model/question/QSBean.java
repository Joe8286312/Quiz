package com.joe.quiz.model.question;

import lombok.Data;

@Data
public class QSBean {
    private String question;
    private String optiona;
    private String optionb;
    private String optionc;
    private String optiond;
    private String answer; // "a", "b", "c", or "d"
}
