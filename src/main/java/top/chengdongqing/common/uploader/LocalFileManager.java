package top.chengdongqing.common.uploader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    @Override
    protected void upload(byte[] fileBytes, String path, String fileName) throws Exception {
        // 获取文件夹对象
        Path directory = Path.of(basePath + path);
        // 文件夹不存在则自动创建
        if (!Files.isDirectory(directory)) Files.createDirectories(directory);
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
                .name(filename)
                .size(Files.size(path))
                .path(path.toUri().toString())
                .isDirectory(Files.isDirectory(path))
                .format(FileManager.getFormat(filename))
                .bytes(content ? Files.readAllBytes(path) : null)
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
        recursiveDelete(directory);
    }

    /**
     * 递归删除文件
     *
     * @param directory 文件夹
     * @throws Exception
     */
    private void recursiveDelete(Path directory) throws Exception {
        List<Path> files = Files.list(directory).collect(Collectors.toList());
        for (Path file : files) {
            if (Files.isDirectory(file) && Files.list(file).count() > 0) {
                recursiveDelete(file);
            }
            Files.delete(file);
        }
    }

    @Override
    public void renameFile(String fileUrl, String name) throws Exception {
        Path file = Paths.get(basePath + fileUrl);
        String fileName = file.getFileName().toString();
        Path newFile = Path.of(basePath + fileUrl.replace(fileName, name));
        Files.move(file, newFile, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void moveFile(String fileUrl, FilePath targetPath) throws Exception {
        Path file = Paths.get(basePath + fileUrl);
        Path directory = Paths.get(basePath + targetPath.getPath());
        if (!Files.isDirectory(directory)) Files.createDirectories(directory);
        Files.move(file, directory.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void moveFiles(List<String> fileUrls, FilePath targetPath) throws Exception {
        for (String fileUrl : fileUrls) {
            moveFile(fileUrl, targetPath);
        }
    }
}
