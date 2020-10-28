package top.chengdongqing.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 事件详情
 *
 * @author Luyao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    /**
     * 事件名称
     */
    private String name;
    /**
     * 事件内容
     */
    private Object data;
    /**
     * 发布时间
     */
    private LocalDateTime timestamp;

    /**
     * 静态工厂方法获取实例，默认自动设当前时间
     */
    public static Event of(String name, Object data) {
        return new Event(name, data, LocalDateTime.now());
    }
}
