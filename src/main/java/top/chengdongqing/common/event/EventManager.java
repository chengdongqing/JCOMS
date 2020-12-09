package top.chengdongqing.common.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.StrKit;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 事件管理器
 *
 * @author Luyao
 */
@Slf4j
@Component
public class EventManager implements ApplicationContextAware {

    // 异步执行线程池
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();
    // 监听器集合
    private static final Map<String, Set<EventListener>> syncListeners = new ConcurrentHashMap<>();
    private static final Map<String, Set<EventListener>> asyncListeners = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取所有的监听器
        Collection<EventListener> listeners = applicationContext.getBeansOfType(EventListener.class).values();
        // 将监听器按是否异步存放在map里
        listeners.forEach(listener -> {
            Class<? extends EventListener> listenerClass = listener.getClass();
            // 是否有配置注解
            if (!listenerClass.isAnnotationPresent(Events.class)) {
                throw new RuntimeException("监听器" + listenerClass.getName() + "必须配置注解@Events");
            }

            Events events = listenerClass.getAnnotation(Events.class);
            // 添加监听器
            for (String name : events.value()) {
                addListener(name, listener, events.async());
            }
        });
    }

    /**
     * 添加监听器
     * 异步执行
     *
     * @param name     事件名称
     * @param listener 监听器
     */
    public static void addListener(String name, EventListener listener) {
        addListener(name, listener, true);
    }

    /**
     * 添加监听器
     *
     * @param name     事件名称
     * @param listener 监听器
     * @param async    是否异步
     */
    public static void addListener(String name, EventListener listener, boolean async) {
        if (StrKit.isBlank(name) || listener == null) {
            throw new IllegalArgumentException("The args are wrong!");
        }

        Set<EventListener> eventListeners = new HashSet<>() {{
            add(listener);
        }};
        // 根据是否异步来加到指定的map
        if (async) {
            addListener(asyncListeners, name, eventListeners);
        } else {
            addListener(syncListeners, name, eventListeners);
        }
    }

    /**
     * 添加监听器
     */
    private static void addListener(Map<String, Set<EventListener>> listeners, String name, Set<EventListener> eventListeners) {
        if (listeners.containsKey(name)) {
            eventListeners.addAll(listeners.get(name));
        }
        listeners.put(name, eventListeners);
    }

    /**
     * 发送事件
     *
     * @param name 事件名称
     * @param data 事件内容
     */
    public static void sendEvent(String name, Object data) {
        sendEvent(asyncListeners, name, data, true);
        sendEvent(syncListeners, name, data, false);
    }

    /**
     * 通知监听者
     */
    private static void sendEvent(Map<String, Set<EventListener>> listeners, String name, Object data, boolean async) {
        listeners.forEach((key, value) -> {
            if (Objects.equals(key, name)) {
                value.forEach(listener -> {
                    if (async) {
                        threadPool.execute(() -> publish(listener, name, data));
                    } else {
                        publish(listener, name, data);
                    }
                });
            }
        });
    }

    /**
     * 发布事件
     */
    private static void publish(EventListener listener, String name, Object data) {
        log.info("发布事件, name: {}, data: {}", name, data);
        listener.onEvent(Event.of(name, data));
    }
}
