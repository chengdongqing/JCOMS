### 上传器
- 支持文件格式、大小检查，且支持动态修改检查规则
- 支持动态切换上传位置
- 支持上传到本地
- 支持上传到mongodb
- 支持上传到阿里云oss

#### 使用
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