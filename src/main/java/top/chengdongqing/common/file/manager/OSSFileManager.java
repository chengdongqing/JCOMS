package top.chengdongqing.common.file.manager;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.file.FileException;
import top.chengdongqing.common.file.FileManager;
import top.chengdongqing.common.file.entity.DownloadFile;
import top.chengdongqing.common.file.uploader.AbstractUploader;

import java.io.InputStream;

/**
 * 阿里云OSS文件管理器
 *
 * @author Luyao
 */
@Slf4j
@Component
@Configuration
public class OSSFileManager extends AbstractUploader implements FileManager {

    @Autowired
    private OSSConfigs configs;
    @Autowired
    private OSS client;

    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(configs.getEndpoint(), configs.getAccessKeyId(), configs.getSecretAccessKey());
    }

    @Override
    protected void upload(InputStream fileStream, String fileKey) throws FileException {
        try {
            client.putObject(configs.getBucket(), fileKey, fileStream);
        } catch (Exception e) {
            log.error("文件上传到OSS异常", e);
            throw new FileException();
        }
    }

    @Override
    public DownloadFile download(String fileKey) throws FileException {
        try {
            OSSObject file = client.getObject(configs.getBucket(), fileKey);
            return DownloadFile.builder()
                    .length(file.getObjectMetadata().getContentLength())
                    .content(file.getObjectContent())
                    .build();
        } catch (Exception e) {
            log.error("从OSS下载文件异常", e);
            throw new FileException();
        }
    }

    @Override
    public void delete(String fileKey) throws FileException {
        try {
            client.deleteObject(configs.getBucket(), fileKey);
        } catch (Exception e) {
            log.error("从OSS删除文件异常", e);
            throw new FileException();
        }
    }
}

@Data
@Component
@ConfigurationProperties("file.oss")
class OSSConfigs {

    private String bucket;
    private String endpoint;
    private String accessKeyId;
    private String secretAccessKey;
}