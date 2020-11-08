package top.chengdongqing.common.uploader;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

/**
 * 上传文件到阿里云OSS
 *
 * @author Luyao
 */
@Component
public class OSSUploader extends AbstractUploader {

    @Autowired
    private OSSConstants constants;

    private final OSS client;

    public OSSUploader() {
        client = new OSSClientBuilder()
                .build(constants.getEndpoint(),
                        constants.getAccessId(),
                        constants.getAccessSecret());
    }

    @Async
    @Override
    protected void upload(byte[] fileBytes, String path, String fileName) throws Exception {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes)) {
            client.putObject(constants.getBucket(), path + fileName, inputStream);
        }
    }
}

@Data
@Component
@ConfigurationProperties(prefix = "upload.oss")
class OSSConstants {

    private String bucket;
    private String endpoint;
    private String accessId;
    private String accessSecret;
}