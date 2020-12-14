package top.chengdongqing.common.kit;

import lombok.Getter;
import top.chengdongqing.common.constant.Status;

import java.io.Serializable;

/**
 * Generic response entity
 *
 * @author Luyao
 */
@Getter
public record Ret<T>(int code, T data, String msg) implements Serializable {

    public static <T> Ret<T> ok() {
        return ok(null);
    }

    public static <T> Ret<T> ok(T data) {
        return ok(data, null);
    }

    public static <T> Ret<T> ok(T data, String msg) {
        return new Ret<>(Status.ENABLED, data, msg);
    }

    public static <T> Ret<T> fail() {
        return fail(null);
    }

    public static <T> Ret<T> fail(String msg) {
        return new Ret<>(Status.DISABLED, null, msg);
    }

    public boolean isOk() {
        return code == Status.ENABLED;
    }

    public boolean isFail() {
        return !isOk();
    }
}
