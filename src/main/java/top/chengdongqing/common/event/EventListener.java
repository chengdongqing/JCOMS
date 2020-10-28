package top.chengdongqing.common.event;

/**
 * 事件监听器
 *
 * @author Luyao
 */
@FunctionalInterface
public interface EventListener {

    /**
     * 当事件发生时
     */
    void onEvent(Event event);
}
