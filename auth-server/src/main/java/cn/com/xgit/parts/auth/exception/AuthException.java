package cn.com.xgit.parts.auth.exception;


import cn.com.xgit.parts.auth.exception.code.ErrorCode;

/**
 * 定义鉴权异常类型，此种异常返回前端错误码是约定的，展示message的同时需要考虑其他操作（如前几次用户不需要输入验证码，用户密码错误到一定程度需要输入验证码）
 */
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
