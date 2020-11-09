### 上传器&文件管理器
- 支持文件上传与管理，包含各个实现的完整的增删改查
- 支持文件格式、大小检查，且支持动态修改检查规则
- 支持动态切换上传位置
- 支持本地磁盘
- 支持mongodb gridFs
- 支持阿里云 oss

#### 使用
- 上传器
```
@Autowired
private Uploader uploader;

public void test() {
    // 上传图片
    uploader.uploadImage(file, FilePath.AVATAR);
    // 上传视频
    uploader.uploadVideo(file, ...);
}
```
- 文件管理器
```
@Autowried
private LocalFileManager localFileManager;

public void test() {
    List<File> files = localFileManager.getFiles(FilePath.AVATAR, false);
    .....
}
```