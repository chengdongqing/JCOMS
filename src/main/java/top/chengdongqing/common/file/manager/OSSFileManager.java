package top.chengdongqing.common.file.manager;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.file.FileException;
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
public class OSSFileManager extends AbstractUploader {

    @Autowired
    private OSSProps props;
    @Autowired
    private OSS client;

    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(props.getEndpoint(), props.getAccessKeyId(), props.getSecretAccessKey());
    }

    @Override
    protected void upload(InputStream fileStream, String fileKey) throws FileException {
        try {
            client.putObject(props.getBucket(), fileKey, fileStream);
        } catch (Exception e) {
            log.error("文件上传到OSS异常", e);
            throw new FileException();
        }
    }

    @Override
    public DownloadFile download(String fileKey) throws FileException {
        try {
            OSSObject file = client.getObject(props.getBucket(), fileKey);
            return new DownloadFile(file.getObjectContent(), file.getObjectMetadata().getContentLength());
        } catch (Exception e) {
            log.error("从OSS下载文件异常", e);
            throw new FileException();
        }
    }

    @Override
    public void delete(String fileKey) throws FileException {
        try {
            client.deleteObject(props.getBucket(), fileKey);
        } catch (Exception e) {
            log.error("从OSS删除文件异常", e);
            throw new FileException();
        }
    }
}

@Getter
@Setter
@Component
@ConfigurationProperties("file.oss")
class OSSProps {

    private String bucket;
    private String endpoint;
    private String accessKeyId;
    private String secretAccessKey;
}