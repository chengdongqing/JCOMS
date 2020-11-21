### 渲染器
- 凡是继承了Render的类将自动拥有HttpServletRequest和HttpServletResponse
- 可通过继承Render实现更多风格统一、操作简洁的渲染器

#### 默认实现的渲染器
- 文件渲染器
- 图片渲染器
```
String name = "测试.xlsx";
byte[] bytes = ...
FileRender.of(name, bytes).render();
```