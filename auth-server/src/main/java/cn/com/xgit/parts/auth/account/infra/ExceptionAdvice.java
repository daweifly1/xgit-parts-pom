package cn.com.xgit.parts.auth.account.infra;

import cn.com.xgit.parts.auth.exception.AuthException;
import cn.com.xgit.parts.auth.exception.code.ErrorCode;
import cn.com.xgit.parts.common.result.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler({AuthException.class})
    @ResponseBody
    public ResultMessage processExcption(NativeWebRequest request, AuthException e) {
        log.error(e.getMessage(), e.getCode());
        return  ResultMessage.error(e.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResultMessage processExcption(NativeWebRequest request, Exception e) {
        this.log.error("Exception ", e);
        ErrorCode code = ErrorCode.UnExceptedError;

        return ResultMessage.error(code.getCode(), code.getDesc(), e.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseBody
    public ResultMessage processIllegalArumentExcption(NativeWebRequest request, IllegalArgumentException e) {
        this.log.error("IllegalArgumentException ", e);
        ErrorCode code = ErrorCode.IllegalArument;

        return  ResultMessage.error(code.getCode(), code.getDesc(), null);
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    @ResponseBody
    public ResultMessage processSQLIntegrityConstraintViolationException(NativeWebRequest request, IllegalArgumentException e) {
        this.log.error("SQLIntegrityConstraintViolationException 异常", e);
        ErrorCode code = ErrorCode.SQLIntegrityConstraintViolation;
        return  ResultMessage.error(code.getCode(), code.getDesc(), null);
    }
}
