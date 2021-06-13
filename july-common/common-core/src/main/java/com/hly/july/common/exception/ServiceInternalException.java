package com.hly.july.common.exception;

import com.hly.july.common.result.ResultCode;
import lombok.Data;

/**
 * @ClassName ServerInternalException
 * @Description
 * @Author Linyuan Hou
 * @Date 2021/6/13 17:39
 * @Version 1.0.0
 **/
@Data
public class ServiceInternalException extends Exception{
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected ResultCode resultCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public ServiceInternalException(String errorMsg) {
        super(errorMsg);
        this.resultCode = ResultCode.API_FAIL_400;
        this.errorMsg = errorMsg;
    }

    public ServiceInternalException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
        this.errorMsg = resultCode.getMsg();
    }

    /**
     * fillInStackTrace() 消耗性能最大，如无必要，自定义异常建议重写fillInStackTrace()直接返回this
     * @return
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
