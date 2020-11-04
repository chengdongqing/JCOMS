package top.chengdongqing.common.kit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.chengdongqing.common.constant.Status;

import java.io.Serializable;

/**
 * 通用响应对象
 *
 * @author Luyao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ret implements Serializable {

    private int code;
    private String msg;
    private Object data;

    public Ret(int code) {
        this.code = code;
    }

    public Ret(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public Ret(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Ret ok() {
        return new Ret(RetCode.ok);
    }

    public static Ret ok(Object data) {
        return new Ret(RetCode.ok, data);
    }

    public static Ret fail() {
        return new Ret(RetCode.fail);
    }

    public static Ret fail(String msg) {
        return new Ret(RetCode.fail, msg);
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
