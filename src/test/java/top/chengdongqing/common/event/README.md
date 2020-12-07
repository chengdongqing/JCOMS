### 事件机制

- 支持在单个进程中发布和监听事件
- 支持同步和异步

#### 监听事件

- 通过实现EventListener接口 如：

```
@Component
@Events("register")
// 可以监听多个事件：@Events({"event1", "event2"})
// 默认异步，指定同步：@Events(value = "register", async = false)
public class RegisterListener implements EventListener {
    
    @override
    public void onEvent(Event event) {
        System.out.println(JSON.toJsonString(event));
    }
}
```

- 通过lambda表达式注册并监听 如：

```
EventManager.addListener("login", event -> {
    System.out.println(JSON.toJsonString(event));
});
// 默认异步，指定同步：
EventManager.addListener("login", event -> {
    System.out.println(JSON.toJsonString(event));
}, false);
```

#### 发布事件

```
User user = ...
EventManager.sendEvent("register", user);
```