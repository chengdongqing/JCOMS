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

    public static Ret ok() {
        return new Ret<>(RetCode.ok, null, null);
    }

    public static <T> Ret ok(T data) {
        return new Ret<>(RetCode.ok, data, null);
    }

    public static Ret fail() {
        return new Ret<>(RetCode.fail, null, null);
    }

    public static Ret fail(String msg) {
        return new Ret<>(RetCode.fail, null, msg);
    }

    @JsonIgnore
    public boolean isOk() {
        return code == RetCode.ok;
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
        int ok = Status.ENABLED;
        /**
         * 失败
         */
        int fail = Status.DISABLED;
    }
}
