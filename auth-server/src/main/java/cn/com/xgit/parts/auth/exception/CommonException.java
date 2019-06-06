package cn.com.xgit.parts.auth.exception;

import cn.com.xgit.parts.auth.account.infra.ErrorCode;

/**
 * 定义普通异常类型，此种异常返回前端往往是默认的错误码仅需展示message即可
 */
public class CommonException extends RuntimeException {
    private static final long serialVersionUID = 2368925481129834020L;
    private int code;
    private Object value;

    public CommonException(String message) {
        super(message);
    }

    public CommonException(int code, String message) {
        super(message);
        this.code = code;
    }

    public CommonException(ErrorCode errorCode) {
        super(errorCode.getDesc());
        this.code = errorCode.getCode();
    }

    public CommonException(ErrorCode errorCode, Object value) {
        this(errorCode);
        this.value = value;
    }

    public CommonException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getDesc(), cause);
        this.code = errorCode.getCode();
    }

    public CommonException(ErrorCode errorCode, Object value, Throwable cause) {
        super(errorCode.getDesc(), cause);
        this.code = errorCode.getCode();
        this.value = value;
    }

    public int getCode() {
        return this.code;
    }

    public Object getValue() {
        return this.value;
    }
}
