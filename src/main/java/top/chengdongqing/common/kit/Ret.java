package top.chengdongqing.common.kit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import top.chengdongqing.common.constant.Status;

import java.io.Serializable;

/**
 * 通用响应对象
 *
 * @author Luyao
 */
public record Ret<T>(int code, T data, String msg) implements Serializable {

    public static <T> Ret<T> ok() {
        return ok(null);
    }

    public static <T> Ret<T> ok(T data) {
        return ok(data, null);
    }

    public static <T> Ret<T> ok(T data, String msg) {
        return new Ret<>(RetCode.OK, data, msg);
    }

    public static <T> Ret<T> fail() {
        return fail(null);
    }

    public static <T> Ret<T> fail(String msg) {
        return new Ret<>(RetCode.FAIL, null, msg);
    }

    @JsonIgnore
    public boolean isOk() {
        return code == RetCode.OK;
    }

    @JsonIgnore
    public boolean isFail() {
        return !isOk();
    }

    /**
     * 返回代码定义
     */
    private interface RetCode {
        /**
         * 成功
         */
        int OK = Status.ENABLED;
        /**
         * 失败
         */
        int FAIL = Status.DISABLED;
    }
}
