package top.chengdongqing.common.uploader;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import top.chengdongqing.common.kit.Ret;

/**
 * 上传器
 *
 * @author Luyao
 */
@Component
public class Uploader {

    @Autowired
    private UploadConstants constants;
    @Autowired
    private UploaderFactory uploaderFactory;

    /**
     * 上传图片
     *
     * @param file 图片文件
     * @param path 存放路径
     * @return 上传结果
     */
    public Ret<String> uploadImage(MultipartFile file, FilePath path) {
        return uploaderFactory.getUploader().upload(file, path,
                constants.getImageFormats(),
                constants.getImageMaxSize());
    }

    /**
     * 上传视频
     *
     * @param file 视频文件
     * @param path 存放路径
     * @return 上传结果
     */
    public Ret<String> uploadVideo(MultipartFile file, FilePath path) {
        return uploaderFactory.getUploader().upload(file, path,
                constants.getVideoFormats(),
                constants.getVideoMaxSize());
    }
}

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "upload")
class UploadConstants {

    private String[] imageFormats, videoFormats;
    private Integer imageMaxSize, videoMaxSize;
}