package com.joe.quiz.service.impl;

import com.joe.quiz.mapper.QuestionMapper;
import com.joe.quiz.model.question.*;
import com.joe.quiz.service.QuestionService;
import com.joe.quiz.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    public int insertQuestion(QSBean qsBean){

        String ans = qsBean.getAnswer();
        if (!List.of("a", "b", "c", "d").contains(ans.toLowerCase())) {
            throw new IllegalArgumentException("Answer must be one of: a, b, c, or d");
        }

        Question q = new Question();
        q.setQuestionText(qsBean.getQuestion());

        q.setAnswer1Text(qsBean.getOptiona());
        q.setAnswer1Correct("a".equalsIgnoreCase(ans));

        q.setAnswer2Text(qsBean.getOptionb());
        q.setAnswer2Correct("b".equalsIgnoreCase(ans));

        q.setAnswer3Text(qsBean.getOptionc());
        q.setAnswer3Correct("c".equalsIgnoreCase(ans));

        q.setAnswer4Text(qsBean.getOptiond());
        q.setAnswer4Correct("d".equalsIgnoreCase(ans));

        q.setIsDelete(0);
        q.setCreateTime(new Date());
        q.setUpdateTime(new Date());

        int result = questionMapper.insertQuestion(q);
        return result;
    }

    public boolean delQuestion(Integer id){
        int result = questionMapper.delQuestionById(id);
        return result>0;
    }

    public List<QSBeanOut> getQuestions(){
        //获取question list;
        List<Question> questionList = questionMapper.getQuestions();
        //将question list转化为QSBeanOut;
        List<QSBeanOut> qsBeanOutList = Tools.convertToQSBeanList(questionList);
        return qsBeanOutList;
    }

    public QSBeanPage page(Integer page, Integer pageSize){
        //获取总的记录数；
        Integer total=questionMapper.count();

        //获取分页查询结果列表；
        Integer start = (page-1)*pageSize;
        List<Question> questionList=questionMapper.page(start, pageSize);

        //将questionList转化为QSBeanOutManage
        //to be done;
//        QSBeanOutManage qsBeanOutManage = Tools.convertToQSBeanManageList(questionList);
        List<QSBeanOutManage> qsBeanOutManage = Tools.convertToQSBeanManageList(questionList);

        //封装PageBean对象；
        QSBeanPage qsBeanPage = new QSBeanPage();
        qsBeanPage.setTotal(total);
        qsBeanPage.setQsBeanList(qsBeanOutManage);

        return qsBeanPage;
    }

    public  List<QSBeanOutManage> findByName(String keyword){
        List<Question> questionList= questionMapper.findByName(keyword);

        List<QSBeanOutManage>qsBeanOutManage = Tools.convertToQSBeanManageList(questionList);
        return qsBeanOutManage;
    }

    // 添加新的方法
    public QSBeanPage searchByNameWithPage(String keyword, Integer page, Integer pageSize){
        // 获取总的记录数
        Integer total = questionMapper.countByKeyword(keyword);

        // 获取分页查询结果列表
        Integer start = (page - 1) * pageSize;
        List<Question> questionList = questionMapper.findByNameWithPage(keyword, start, pageSize);

        // 将questionList转化为QSBeanOutManage
        List<QSBeanOutManage> qsBeanOutManage = Tools.convertToQSBeanManageList(questionList);

        // 封装PageBean对象
        QSBeanPage qsBeanPage = new QSBeanPage();
        qsBeanPage.setTotal(total);
        qsBeanPage.setQsBeanList(qsBeanOutManage);

        return qsBeanPage;
    }

    // 新增：编辑问题方法
    public int updateQuestion(QSEditBean qsEditBean){
        String ans = qsEditBean.getAnswer();
        if (!List.of("a", "b", "c", "d").contains(ans.toLowerCase())) {
            throw new IllegalArgumentException("Answer must be one of: a, b, c, or d");
        }

        Question q = new Question();
        q.setId(qsEditBean.getId()); // 设置要更新的问题ID
        q.setQuestionText(qsEditBean.getQuestion());

        q.setAnswer1Text(qsEditBean.getOptiona());
        q.setAnswer1Correct("a".equalsIgnoreCase(ans));

        q.setAnswer2Text(qsEditBean.getOptionb());
        q.setAnswer2Correct("b".equalsIgnoreCase(ans));

        q.setAnswer3Text(qsEditBean.getOptionc());
        q.setAnswer3Correct("c".equalsIgnoreCase(ans));

        q.setAnswer4Text(qsEditBean.getOptiond());
        q.setAnswer4Correct("d".equalsIgnoreCase(ans));

        q.setUpdateTime(new Date()); // 只更新修改时间

        int result = questionMapper.updateQuestion(q); // 调用更新方法
        return result;
    }
}
