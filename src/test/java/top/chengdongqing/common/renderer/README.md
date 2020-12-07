### 渲染器

- 凡是继承了Renderer的类将自动拥有HttpServletRequest和HttpServletResponse
- 可通过继承Renderer实现更多风格统一、操作简洁的渲染器

#### 默认实现的渲染器

- 流渲染器
- 字节渲染器
- 图片渲染器

示例：
```
String name = "测试.xlsx";
byte[] bytes = ...
BytesRenderer.of(bytes, name).render();
```