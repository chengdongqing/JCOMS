package top.chengdongqing.common.uploader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 上传文件到本地磁盘
 *
 * @author Luyao
 */
@Component
@RefreshScope
public class LocalUploader extends Uploader {

    @Value("${upload.local.base-upload-path}")
    private String baseUploadPath;

    @Async
    @Override
    void upload(byte[] fileBytes, String path, String fileName) throws Exception {
        // 获取文件夹对象
        Path directory = Path.of(baseUploadPath + path);
        // 判断文件夹是否存在
        if (!Files.isDirectory(directory)) {
            // 不存在则自动创建文件夹
            Files.createDirectories(directory);
        }
        // 获取将要存放在该路径的文件对象
        Path targetFile = directory.resolve(fileName);
        // 如果已存在则删除
        Files.deleteIfExists(targetFile);
        // 创建指定路径的文件
        Path file = Files.createFile(targetFile);
        // 将数据写入到文件中
        Files.write(file, fileBytes);
    }
}
