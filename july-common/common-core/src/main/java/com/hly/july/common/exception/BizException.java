package com.hly.july.common.exception;

import com.hly.july.common.result.ResultCode;
import lombok.Data;

/**
 * @author Linyuan Hou
 * @date 2021/5/13 10:49
 */
@Data
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected ResultCode resultCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public BizException() {
        super();
    }

    public BizException(Throwable cause) {
        super(cause);
        this.resultCode = ResultCode.API_FAIL_400;
        this.errorMsg = cause.getMessage();
    }

    public BizException(String errorMsg) {
        super(errorMsg);
        this.resultCode = ResultCode.API_FAIL_400;
        this.errorMsg = errorMsg;
    }

    public BizException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
        this.errorMsg = resultCode.getMsg();
    }

    public BizException(ResultCode resultCode, Throwable cause) {
        super(cause);
        this.resultCode = resultCode;
        this.errorMsg = cause.getMessage();
    }

    public BizException(String errorMsg, Throwable cause) {
        super(cause);
        this.resultCode = ResultCode.API_FAIL_400;
        this.errorMsg = errorMsg;
    }

    public BizException(ResultCode resultCode, String errorMsg) {
        super(errorMsg);
        this.resultCode = resultCode;
        this.errorMsg = errorMsg;
    }

    public BizException(ResultCode resultCode, String errorMsg, Throwable cause) {
        super(cause);
        this.resultCode = resultCode;
        this.errorMsg = errorMsg;
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
