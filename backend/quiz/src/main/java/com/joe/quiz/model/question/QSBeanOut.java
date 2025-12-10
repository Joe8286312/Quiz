package com.joe.quiz.model.question;

import lombok.Data;

import java.util.List;

@Data
public class QSBeanOut {
    private String question;
    private List<AnsBean> answers;
    //setter.getter.
}
