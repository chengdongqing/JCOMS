package top.chengdongqing.common.file;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.SimplifiedObjectMeta;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.LinkedList;
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
    private OSSConstants constants;

    private final OSS client;

    public OSSFileManager() {
        client = new OSSClientBuilder().build(
                constants.getEndpoint(),
                constants.getAccessId(),
                constants.getAccessSecret());
    }

    @Override
    protected void upload(byte[] fileBytes, String path, String fileName) throws Exception {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes)) {
            client.putObject(constants.getBucket(), path + fileName, inputStream);
        }
    }

    @Override
    public File getFile(String fileUrl, boolean content) throws Exception {
        SimplifiedObjectMeta objectMeta = client.getSimplifiedObjectMeta(constants.getBucket(), fileUrl);
        File file = File.builder()
                .path(fileUrl)
                .name(FileManager.getName(fileUrl))
                .format(FileManager.getFormat(fileUrl))
                .size(objectMeta.getSize())
                .uploadTime(objectMeta.getLastModified().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .isDirectory(false)
                .build();

        if (content) {
            OSSObject ossObject = client.getObject(constants.getBucket(), fileUrl);
            try (InputStream stream = new BufferedInputStream(ossObject.getObjectContent())) {
                file.setBytes(stream.readAllBytes());
            }
        }
        return file;
    }

    @Override
    public List<File> getFiles(FilePath path, boolean content) throws Exception {
        List<OSSObjectSummary> objectSummaries = client.listObjects(constants.getBucket(), path.getPath()).getObjectSummaries();
        LinkedList<File> files = new LinkedList<>();
        for (OSSObjectSummary objectSummary : objectSummaries) {
            files.add(getFile(objectSummary.getKey(), content));
        }
        return files;
    }

    @Override
    public void deleteFile(String fileUrl) {
        client.deleteObject(constants.getBucket(), fileUrl);
    }

    @Override
    public void deleteFiles(List<String> fileUrls) {
        client.deleteObjects(new DeleteObjectsRequest(constants.getBucket()).withKeys(fileUrls));
    }

    @Override
    public void clearDirectory(FilePath path) {
        // 获取当前目录的所有文件概要
        List<OSSObjectSummary> objectSummaries = client.listObjects(constants.getBucket(), path.getPath()).getObjectSummaries();
        // 获取每个文件的key并批量删除
        deleteFiles(objectSummaries.stream().map(OSSObjectSummary::getKey).collect(Collectors.toList()));
    }

    @Override
    public void renameFile(String fileUrl, String name) throws Exception {
        String newFileUrl = fileUrl.replace(FileManager.getName(fileUrl), name);
        move(fileUrl, newFileUrl);
    }

    private void move(String oldKey, String newKey) {
        client.copyObject(constants.getBucket(), oldKey, constants.getBucket(), newKey);
        client.deleteObject(constants.getBucket(), oldKey);
    }

    @Override
    public void moveFile(String fileUrl, FilePath targetPath) throws Exception {
        String newFileUrl = targetPath.getPath() + FileManager.getName(fileUrl);
        move(fileUrl, newFileUrl);
    }

    @Override
    public void moveFiles(List<String> fileUrls, FilePath targetPath) throws Exception {
        for (String fileUrl : fileUrls) {
            moveFile(fileUrl, targetPath);
        }
    }
}

@Data
@Component
@ConfigurationProperties(prefix = "upload.oss")
class OSSConstants {

    private String bucket,
            endpoint,
            accessId,
            accessSecret;
}