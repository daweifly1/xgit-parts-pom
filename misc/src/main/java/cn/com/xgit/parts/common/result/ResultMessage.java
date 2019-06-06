package cn.com.xgit.parts.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
@ApiModel("返回信息")
public class ResultMessage<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private static String SUCCESS = "操作成功";

    private static String ERROR = "操作失败";

    public static int OK = 0;

    public static int FAIL = 1;

    @ApiModelProperty(value = "提示信息", name = "notification", example = "操作成功")
    protected String message;

    @ApiModelProperty(value = "数据", name = "data")
    protected T data;

    @ApiModelProperty(value = "状态码", name = "status")
    protected int status;

    @ApiModelProperty(value = "时间戳", name = "timestamp")
    private Timestamp timestamp;

    public static <T> ResultMessage<T> success() {
        return success(ResultMessage.OK, SUCCESS, null);
    }


    public static <T> ResultMessage<T> success(T data) {
        return success(ResultMessage.OK, SUCCESS, data);
    }

    public static <T> ResultMessage<T> success(String message, T data) {
        return success(ResultMessage.OK, message, data);
    }

    public static <T> ResultMessage<T> success(int status, String message) {
        return success(status, message, null);
    }

    public static <T> ResultMessage<T> success(int status, String message, T data) {
        return ResultMessage.create(status, message, data, null);
    }

    public ResultMessage<T> message(String message) {
        this.message = message;
        return this;
    }

    public static <T> ResultMessage<T> error() {

        return error(FAIL, ERROR, null);
    }

    public static <T> ResultMessage<T> error(String message) {

        return error(FAIL, message, null);
    }

    public static <T> ResultMessage<T> error(int status, String message) {

        return error(status, message, null);
    }

    public static <T> ResultMessage<T> error(int status, String message, T data) {
        return ResultMessage.error(status, message, data, null);
    }

    public static <T> ResultMessage<T> error(int status, String message, T data, String exception) {
        return ResultMessage.create(status, message, data, exception);
    }

    public static <T> ResultMessage<T> create(int status, String message, T data, String exception) {
        return new ResultMessage<T>()
                .data(data)
                .message(message)
                .status(status)
                .putTimeStamp();
    }

    public ResultMessage<T> data(T data) {
        this.data = data;
        return this;
    }

    public T getData() {
        return this.data;
    }

    private ResultMessage<T> putTimeStamp() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        return this;
    }

    public ResultMessage<T> status(int status) {
        this.status = status;
        return this;
    }

    public class success extends ResultMessage<T> {
        public <T> success(int code, String desc, T value) {
        }
    }
}
