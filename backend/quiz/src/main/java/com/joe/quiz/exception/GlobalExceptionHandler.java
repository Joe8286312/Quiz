package com.joe.quiz.exception;

import com.joe.quiz.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException; // 新增：用于捕获数据库相关异常
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * - 捕获未被业务层捕获的异常，统一返回友好提示
 * - 记录错误日志，便于排查
 */
@RestControllerAdvice // 修改：标记为全局异常处理器
public class GlobalExceptionHandler {

    // 修改：全局日志记录器
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理数据库访问相关异常（Mapper 层抛出的典型异常会包裹为 DataAccessException）
     */
    @ExceptionHandler(DataAccessException.class) // 新增
    public Result handleDataAccessException(DataAccessException ex) {
        log.error("数据库访问异常", ex); // 记录异常堆栈
        return Result.error("系统繁忙，请稍后重试"); // 统一友好提示
    }

    /**
     * 处理参数校验异常（如果使用了 @Valid 等）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class) // 新增
    public Result handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("参数校验异常: {}", ex.getMessage());
        String msg = ex.getBindingResult().getFieldError() != null
                ? ex.getBindingResult().getFieldError().getDefaultMessage()
                : "参数校验失败";
        return Result.error(msg);
    }

    /**
     * 兜底异常处理：捕获所有未处理的异常
     */
    @ExceptionHandler(Exception.class) // 修改：兜底
    public Result handleException(Exception ex) {
        log.error("未处理异常", ex); // 记录异常堆栈
        return Result.error("系统开小差了，请稍后再试");
    }
}