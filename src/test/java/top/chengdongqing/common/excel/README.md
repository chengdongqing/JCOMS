### excel处理器
- 支持读取excel
- 支持写入excel
- 支持自动渲染到客户端

#### Apache POI excel处理器
- 读取excel文件
```
Map<String, String> titles = new HashMap<String, String>() {{
    put("手机号", "phoneNumber");
    put("名称", "name");
}};
List<User> items = POIExcelProcessor.me().read(titles, fileName, excelBytes).toList(User.class);
```
- 写入excel文件
```
List<User> users = ...
LinkedHashMap<String, String> titles = new LinkedHashMap<String, String>() {{
    put("phoneNumber", "手机号");
    put("name", "名称");
}};
JSONArray rows = new JSONArray();
rows.addAll(users);
POIExcelProcessor.me().write(titles, rows).renderWithDate("用户手机号和名称关系");
```