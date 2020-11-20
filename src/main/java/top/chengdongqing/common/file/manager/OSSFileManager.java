package top.chengdongqing.common.file.manager;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.SimplifiedObjectMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.file.File;
import top.chengdongqing.common.file.FileManager;
import top.chengdongqing.common.file.FilePath;
import top.chengdongqing.common.file.upload.AbstractUploader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 阿里云OSS文件管理器
 *
 * @author Luyao
 */
@Component
public class OSSFileManager extends AbstractUploader implements FileManager {

    @Autowired
    private OSS client;

    @Value("${upload.oss.bucket}")
    private String bucket;

    @Override
    protected void upload(byte[] fileBytes, String path, String filename) throws Exception {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes)) {
            client.putObject(bucket, path + filename, inputStream);
        }
    }

    @Override
    public File getFile(String fileUrl, boolean content) throws Exception {
        SimplifiedObjectMeta objectMeta = client.getSimplifiedObjectMeta(bucket, fileUrl);
        File file = File.builder()
                .path(fileUrl)
                .name(FileManager.getName(fileUrl))
                .format(FileManager.getFormat(fileUrl))
                .size(objectMeta.getSize())
                .uploadTime(objectMeta.getLastModified().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .isDirectory(false)
                .build();

        if (content) {
            OSSObject ossObject = client.getObject(bucket, fileUrl);
            try (InputStream stream = ossObject.getObjectContent()) {
                file.setBytes(stream.readAllBytes());
            }
        }
        return file;
    }

    @Override
    public List<File> getFiles(FilePath path, boolean content) throws Exception {
        List<OSSObjectSummary> objectSummaries = client.listObjects(bucket, path.getPath()).getObjectSummaries();
        ArrayList<File> files = new ArrayList<>();
        for (OSSObjectSummary summary : objectSummaries) {
            files.add(getFile(summary.getKey(), content));
        }
        return files;
    }

    @Override
    public void deleteFile(String fileUrl) {
        client.deleteObject(bucket, fileUrl);
    }

    @Override
    public void deleteFiles(List<String> fileUrls) {
        client.deleteObjects(new DeleteObjectsRequest(bucket).withKeys(fileUrls));
    }

    @Override
    public void clearDirectory(FilePath path) {
        // 获取当前目录的所有文件概要
        List<OSSObjectSummary> objectSummaries = client.listObjects(bucket, path.getPath()).getObjectSummaries();
        // 获取每个文件的key并批量删除
        deleteFiles(objectSummaries.stream().map(OSSObjectSummary::getKey).collect(Collectors.toList()));
    }

    @Override
    public void renameFile(String fileUrl, String name) {
        String newFileUrl = fileUrl.replace(FileManager.getName(fileUrl), name);
        move(fileUrl, newFileUrl);
    }

    /**
     * 移动文件
     *
     * @param oldKey 旧文件的键
     * @param newKey 新文件的键
     */
    private void move(String oldKey, String newKey) {
        client.copyObject(bucket, oldKey, bucket, newKey);
        client.deleteObject(bucket, oldKey);
    }

    @Override
    public void moveFile(String fileUrl, FilePath targetPath) {
        String newFileUrl = targetPath.getPath() + FileManager.getName(fileUrl);
        move(fileUrl, newFileUrl);
    }

    @Override
    public void moveFiles(List<String> fileUrls, FilePath targetPath) {
        for (String fileUrl : fileUrls) {
            moveFile(fileUrl, targetPath);
        }
    }
}

@Configuration
@ConfigurationProperties("upload.oss")
class OSSConfiguration {

    private String endpoint;
    private String accessId;
    private String accessSecret;

    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(endpoint, accessId, accessSecret);
    }
}