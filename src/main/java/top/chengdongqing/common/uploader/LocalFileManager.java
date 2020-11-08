package top.chengdongqing.common.uploader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 本地文件管理器
 *
 * @author Luyao
 */
@Component
@RefreshScope
public class LocalFileManager extends AbstractUploader implements FileManager {

    @Value("${upload.local.base-path}")
    private String basePath;

    @Async
    @Override
    protected void upload(byte[] fileBytes, String path, String fileName) throws Exception {
        // 获取文件夹对象
        Path directory = Path.of(basePath + path);
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

    @Override
    public File getFile(String fileUrl, boolean content) throws Exception {
        Path path = Paths.get(basePath + fileUrl);
        String filename = path.getFileName().toString();
        return File.builder()
                .bytes(content ? Files.readAllBytes(path) : null)
                .name(filename)
                .format(FileManager.getFormat(filename))
                .size(Files.size(path))
                .path(path.toUri().toString())
                .uploadTime(Files.getLastModifiedTime(path).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
    }

    @Override
    public List<File> getFiles(FilePath path, boolean content) throws Exception {
        // 获取文件夹
        Path directory = Paths.get(basePath + path.getPath());
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException(path.getPath() + " does not a directory.");
        }

        // 获取该文件夹下的每个文件信息
        LinkedList<File> files = new LinkedList<>();
        List<Path> pathFiles = Files.list(directory).collect(Collectors.toList());
        for (Path file : pathFiles) {
            files.add(getFile(path.getPath() + file.getFileName(), content));
        }
        return files;
    }

    @Override
    public void deleteFile(String fileUrl) throws Exception {
        Files.deleteIfExists(Paths.get(basePath + fileUrl));
    }

    @Override
    public void deleteFiles(List<String> fileUrls) throws Exception {
        for (String fileUrl : fileUrls) {
            deleteFile(fileUrl);
        }
    }

    @Override
    public void clearDirectory(FilePath path) throws Exception {
        // 获取文件夹
        Path directory = Paths.get(basePath + path.getPath());
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException(path.getPath() + " does not a directory.");
        }

        // 获取指定文件夹下的所有文件并删除
        List<Path> pathFiles = Files.list(directory).collect(Collectors.toList());
        for (Path file : pathFiles) {
            deleteFile(path.getPath() + file.getFileName());
        }
    }
}
