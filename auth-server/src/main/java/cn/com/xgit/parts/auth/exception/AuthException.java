package cn.com.xgit.parts.auth.exception;

import cn.com.xgit.parts.auth.account.infra.ErrorCode;

public class AuthException extends RuntimeException {
    private static final long serialVersionUID = 2368925481129834020L;
    private int code;
    private Object value;

    public AuthException(String message) {
        super(message);
    }

    public AuthException(int code, String message) {
        super(message);
        this.code = code;
    }

    public AuthException(ErrorCode errorCode) {
        super(errorCode.getDesc());
        this.code = errorCode.getCode();
    }

    public AuthException(ErrorCode errorCode, Object value) {
        this(errorCode);
        this.value = value;
    }

    public AuthException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getDesc(), cause);
        this.code = errorCode.getCode();
    }

    public AuthException(ErrorCode errorCode, Object value, Throwable cause) {
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
