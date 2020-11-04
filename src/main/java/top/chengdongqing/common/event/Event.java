package top.chengdongqing.common.event;

import java.time.LocalDateTime;

/**
 * 事件详情
 *
 * @author Luyao
 */
public record Event(String name, Object data, LocalDateTime timestamp) {

    /**
     * 链式调用获取实例，默认自动设当前时间
     */
    public static Event of(String name, Object data) {
        return new Event(name, data, LocalDateTime.now());
    }
}
