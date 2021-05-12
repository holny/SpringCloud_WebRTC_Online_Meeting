package com.hly.july.common.validation.handler;

import com.hly.july.common.result.Result;
import com.hly.july.common.result.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;;
import java.util.Set;

/**
 * @author Linyuan Hou
 * @date 2021/5/11 15:15
 */
@RestControllerAdvice
public class GlobalControllerAdvice {
    private static final String BAD_REQUEST_MSG = "客户端请求参数错误";
    // <1> 处理 form data方式调用接口校验失败抛出的异常
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result<String> bindExceptionHandler(BindException e) {
//        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
//        List<String> collect = fieldErrors.stream()
//                .map(o -> o.getDefaultMessage())
//                .collect(Collectors.toList());
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder("校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
        }
        String msg = sb.toString();
        return Result.failure(ResultCode.API_VALIDATION_ERROR,ResultCode.API_VALIDATION_ERROR.getMsg(), msg);
    }

    // <2> 处理 json 请求体调用接口校验失败抛出的异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
//        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
//        List<String> collect = fieldErrors.stream()
//                .map(o -> o.getDefaultMessage())
//                .collect(Collectors.toList());
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder("校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
        }
        String msg = sb.toString();
        return Result.failure(ResultCode.API_VALIDATION_ERROR,ResultCode.API_VALIDATION_ERROR.getMsg(), msg);
    }

    // <3> 处理单个参数校验失败抛出的异常
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result<String> constraintViolationExceptionHandler(ConstraintViolationException e) {
//        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
//        List<String> collect = constraintViolations.stream()
//                .map(o -> o.getMessage())
//                .collect(Collectors.toList());
        Set<ConstraintViolation<?>>  constraintViolations = e.getConstraintViolations();
        StringBuilder sb = new StringBuilder("校验失败:");
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            sb.append(constraintViolation.getInvalidValue()).append("：").append(constraintViolation.getMessage()).append(", ");
        }
        String msg = sb.toString();
        return Result.failure(ResultCode.API_VALIDATION_ERROR,ResultCode.API_VALIDATION_ERROR.getMsg(), msg);
    }
}