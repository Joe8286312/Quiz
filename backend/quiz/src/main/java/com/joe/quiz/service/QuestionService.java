package com.joe.quiz.service;

import com.joe.quiz.model.question.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionService {
    //接口代码；
    public int insertQuestion(QSBean qsBean);

    public boolean delQuestion(Integer id);

    public List<QSBeanOut> getQuestions();

    public QSBeanPage page(Integer page, Integer pageSize);

    public  List<QSBeanOutManage> findByName(String keyword);

    // 添加新的分页查询方法
    public QSBeanPage searchByNameWithPage(String keyword, Integer page, Integer pageSize);

    // 新增：编辑问题方法
    public int updateQuestion(QSEditBean qsEditBean);
}
