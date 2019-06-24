package cn.com.xgit.gw.http.exceptions;

/**
 * 网关异常信息
 */
public class GwException extends RuntimeException {
    private static final long serialVersionUID = 2368925481129834020L;
    private int code;
    private Object value;

    public GwException(String message) {
        super(message);
    }

    public GwException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public Object getValue() {
        return this.value;
    }
}
