package top.chengdongqing.common.file.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.file.FileException;
import top.chengdongqing.common.file.entity.DownloadFile;
import top.chengdongqing.common.file.uploader.AbstractUploader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 本地文件管理器
 *
 * @author Luyao
 */
@Slf4j
@Component
public class LocalFileManager extends AbstractUploader {

    @Value("${file.local.base-path}")
    private String basePath;

    @Override
    protected void upload(InputStream fileStream, String fileKey) throws FileException {
        try {
            // 文件应该存放的位置
            Path filePath = Path.of(basePath + fileKey);
            // 如果该位置已有文件则删除
            Files.deleteIfExists(filePath);
            // 获取文件所属的文件夹
            Path directory = Path.of(filePath.getParent().toString());
            // 判断该文件夹是否存在
            if (!Files.isDirectory(directory)) {
                // 如果该文件夹不存在则创建，无论多少层级
                Files.createDirectories(directory);
            }
            // 创建文件并将文件数据流向该文件
            Files.copy(fileStream, filePath);
        } catch (IOException e) {
            log.error("文件上传到本地错误", e);
            throw new FileException();
        }
    }

    @Override
    public DownloadFile download(String fileKey) throws FileException {
        try {
            Path file = Path.of(basePath + fileKey);
            return new DownloadFile(Files.newInputStream(file), Files.size(file));
        } catch (Exception e) {
            log.error("从本地下载文件错误", e);
            throw new FileException();
        }
    }

    @Override
    public void delete(String fileKey) throws FileException {
        try {
            Files.delete(Path.of(basePath + fileKey));
        } catch (Exception e) {
            log.error("从本地删除文件错误", e);
            throw new FileException();
        }
    }
}
