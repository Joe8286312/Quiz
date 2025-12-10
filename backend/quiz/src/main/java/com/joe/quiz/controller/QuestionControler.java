package com.joe.quiz.controller;

import com.joe.quiz.model.*;
import com.joe.quiz.model.question.*;
import com.joe.quiz.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuestionControler  {
    @Autowired
    private QuestionService questionService;

//    @PostMapping("/addQuestion")
//    public Result addQuestion(@RequestBody QSBean qsBean) {
//        int result = questionService.insertQuestion(qsBean);
//        if (result > 0){
//            return Result.success("插入新问题成功");
//        }else{
//            return Result.error("插入失败");
//        }
//    }

    @GetMapping("/delQuestion")
    public Result deleteQuestion(Integer id) {
        boolean success = questionService.delQuestion(id);
        if (success) {
            return Result.success("题目成功删除");
        }
        return Result.error("题目不存在或已被删除");
    }

    @GetMapping("/getQuestion")
    public Result getQuestion() {
        List<QSBeanOut> qsBeanOut = questionService.getQuestions();
        return Result.success(qsBeanOut);
    }

    @GetMapping("/questions")
    public Result getPage(@RequestParam(defaultValue="1")Integer page, @RequestParam(defaultValue="5")Integer pageSize){
        QSBeanPage qsBeanPage=questionService.page(page, pageSize);
        return Result.success(qsBeanPage);
    }

//    @GetMapping("/findQuestion")
//    public Result getUser(String keyword){
//        List<QSBeanOutManage> qsBeanOut=questionService.findByName(keyword);
//        return Result.success(qsBeanOut);
//    }

    // 修改原有的搜索方法，添加分页参数
    @GetMapping("/findQuestion")
    public Result searchQuestion(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer pageSize) {

        QSBeanPage qsBeanPage = questionService.searchByNameWithPage(keyword, page, pageSize);
        return Result.success(qsBeanPage);
    }

    // 修改添加问题的方法，返回更详细的响应
    @PostMapping("/addQuestion")
    public Result addQuestion(@RequestBody QSBean qsBean) {
        try {
            int result = questionService.insertQuestion(qsBean);
            if (result > 0) {
                return Result.success("插入新问题成功");
            } else {
                return Result.error("插入失败");
            }
        } catch (IllegalArgumentException e) {
            return Result.error("答案必须是 a, b, c, 或 d");
        } catch (Exception e) {
            return Result.error("系统错误，请稍后重试");
        }
    }

    // 新增：编辑问题接口
    @PostMapping("/updateQuestion")
    public Result updateQuestion(@RequestBody QSEditBean qsEditBean) {
        try {
            int result = questionService.updateQuestion(qsEditBean);
            if (result > 0) {
                return Result.success("更新问题成功");
            } else {
                return Result.error("更新失败，问题可能不存在或已被删除");
            }
        } catch (IllegalArgumentException e) {
            return Result.error("答案必须是 a, b, c, 或 d");
        } catch (Exception e) {
            return Result.error("系统错误，请稍后重试");
        }
    }
}
