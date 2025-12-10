package com.joe.quiz.model.question;

import lombok.Data;

@Data
public class AnsBean {
    private String text;
    private boolean correct;
    //类构造方法；
    public AnsBean(String text, boolean correct) {
        this.text = text;
        this.correct = correct;
    }
    //setter.getter.
}
