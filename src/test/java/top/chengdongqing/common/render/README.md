### 渲染器
- 凡是继承了Render的类将自动拥有HttpServletRequest和HttpServletResponse
- 默认实现了一个流渲染器，可通过继承Render实现更多风格统一、操作简洁的渲染器

#### 流渲染器
- 支持渲染字节数组
- 支持指定文件名
- 支持分片渲染
- 暂仅在chrome中测试
```
String name = "测试.xlsx";
byte[] bytes = ...
StreamRender.init(name, bytes).render();
```