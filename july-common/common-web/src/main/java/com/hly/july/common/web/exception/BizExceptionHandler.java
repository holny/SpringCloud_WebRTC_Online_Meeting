package com.hly.july.common.web.exception;

import com.hly.july.common.core.exception.BizException;
import com.hly.july.common.core.result.Result;
import com.hly.july.common.core.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Set;
/**
 * @author Linyuan Hou
 * @date 2021/5/13 15:15
 */
@RestControllerAdvice
@Slf4j
public class BizExceptionHandler {

    /**
     * 自定义的ExceptionHandler，注意这里的exception处理顺序从上至下，只到匹配到合适的handler。
     * 本项目是处理exception，获取错误信息，跳转错误页并显示。
     * @param request
     * @param response
     * @param e
     * @return
     * @throws IOException
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = BizException.class)
    public Object myExceptionHandler(HttpServletRequest request,
                                     HttpServletResponse response, BizException e) throws IOException {

        // 获取到异常对象
        BizException ex = null;
        if (e instanceof BizException) {
            ex = (BizException) e;
        } else {
            ex = new BizException("系统正在维护...");
        }
        log.warn("url:{}",request.getRequestURL());
        log.warn("exception.resultCode:{}",e.getResultCode().toString());
        log.warn("exception.msg:{}",e.getErrorMsg());
        log.warn("exception.string:{}",e.getMessage());
        e.printStackTrace();
        return Result.failure(e.getResultCode(),e.getErrorMsg());

    }

    // <1> 处理 form data方式调用接口校验失败抛出的异常
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
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
        log.error("bindExceptionHandler e:"+msg);
        return Result.failure(ResultCode.API_VALIDATION_ERROR, msg);
    }

    // <2> 处理 json 请求体调用接口校验失败抛出的异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
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
        log.error("methodArgumentNotValidExceptionHandler e:"+msg);
        return Result.failure(ResultCode.API_VALIDATION_ERROR, msg);
    }

    // <3> 处理单个参数校验失败抛出的异常
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<String> constraintViolationExceptionHandler(ConstraintViolationException e) {
//        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
//        List<String> collect = constraintViolations.stream()
//                .map(o -> o.getMessage())
//                .collect(Collectors.toList());
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        StringBuilder sb = new StringBuilder("校验失败:");
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            sb.append(constraintViolation.getInvalidValue()).append("：").append(constraintViolation.getMessage()).append(", ");
        }
        String msg = sb.toString();
        log.error("ConstraintViolationException e:"+msg);
        return Result.failure(ResultCode.API_VALIDATION_ERROR, msg);
    }


    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = Exception.class)
    public Object exceptionHandler(HttpServletRequest request,
                                   HttpServletResponse response, Exception e) throws IOException {
        log.warn("url:{}",request.getRequestURL());
        log.warn("exception.message:{}",e.getMessage());
        e.printStackTrace();
        // 获取到异常对象
        Exception ex = null;
        if (e instanceof Exception) {
            ex = (Exception) e;
        } else {
            ex = new Exception("系统正在维护...");
        }
        return Result.failure(ResultCode.API_FAIL_400,e.getMessage());
//        if (isAjax(request)) {
//            response.setCharacterEncoding("UTF-8");
//            response.setContentType("application/json; charset=utf-8");
//            PrintWriter writer = response.getWriter();
//            //具体操作
//            ObjectMapper mapper = new ObjectMapper();
//
//            writer.write(mapper.writeValueAsString(Msg.fail().setMsg( ex.getMessage()).add("url", request.getRequestURL())));
//            //
//            writer.flush();
//            writer.close();
//            return null;
//        } else {
//            ModelAndView mv = new ModelAndView();
//            mv.addObject("msg", ex.getMessage());
//            mv.addObject("url", request.getRequestURL());
//            mv.setViewName(ERROR_VIEW);
//            return mv;
//        }
    }

//    /**
//     * 判断是否是ajax请求
//     */
//    public static boolean isAjax(HttpServletRequest httpRequest) {
//        return (httpRequest.getHeader("X-Requested-With") != null
//                && "XMLHttpRequest"
//                .equals(httpRequest.getHeader("X-Requested-With").toString()));
//    }
//
//    /**
//     * 判断请求是否为ajax请求
//     *
//     * @param request
//     * @return
//     */
//    private boolean isAjaxRequest(HttpServletRequest request) {
//        if ((request.getHeader("accept") != null && request.getHeader("accept").contains("application/json")) || (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").contains("XMLHttpRequest"))) {
//            return true;
//        } else {
//            return false;
//        }
//    }
}
