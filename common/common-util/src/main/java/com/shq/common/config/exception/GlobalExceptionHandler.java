package com.shq.common.config.exception;

import com.shq.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常处理
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail().message("执行全局处理异常...");
    }

    /**
     * 特定异常状态处理
     * @param e
     * @return
     */
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e){
        e.printStackTrace();
        return Result.fail().message("执行特定处理异常...");
    }

    /**
     * 数据库查询异常处理
     * @param ex
     * @return
     */

    @ExceptionHandler(SQLIntegrityConstraintViolationException .class)
    @ResponseBody
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){

        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存着";
            return Result.fail(msg);
        }

        return Result.fail("未知错误");
    }


    /**
     * 自定义异常状态处理
     * @param e
     * @return
     */
    @ExceptionHandler(MyException.class)
    @ResponseBody
    public Result error(MyException e){
        e.printStackTrace();
        return Result.fail().message("执行自定义处理异常...");
    }


}
