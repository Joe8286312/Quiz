package com.joe.quiz.model.question;

import lombok.Data;

import java.util.List;

@Data
public class QSBeanPage {
    private int total;
    private List<QSBeanOutManage> qsBeanList;
}
