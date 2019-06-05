package cn.com.xgit.parts.auth.account.web;

import cn.com.xgit.parts.auth.account.infra.ErrorCode;
import com.xgit.bj.core.rsp.ResultMessage;

import javax.servlet.http.HttpServletRequest;

public class BasicController {
    public <T> ResultMessage<T> ResultMessage(ErrorCode code, T value) {
        return new ResultMessage(code.getCode(), code
                .getDesc(), value);
    }

    public <T> ResultMessage<T> ResultMessage(T value) {
        ErrorCode code = ErrorCode.Success;
        return ResultMessage(code, value);
    }

    public <T> ResultMessage<T> actionErrorResult(String errorMsg) {
        ErrorCode code = ErrorCode.Failure;
        return new ResultMessage(code.getCode(), errorMsg, null);
    }

    public <T> ResultMessage<T> actionErrorResult(int code, String errorMsg) {
        return new ResultMessage(code, errorMsg, null);
    }

    public ResultMessage ResultMessage(ErrorCode code) {
        return ResultMessage(code, null);
    }

    public String getUserId(HttpServletRequest request) {
        String userId = request.getHeader("x-user-id");
        return userId;
    }

    public String getRemoteIp(HttpServletRequest request) {
        String userIp = request.getHeader("x-remote-ip");
        return userIp;
    }
}
