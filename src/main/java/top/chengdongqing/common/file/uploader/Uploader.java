package top.chengdongqing.common.file.uploader;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import top.chengdongqing.common.file.FileException;
import top.chengdongqing.common.file.FileManagerFactory;
import top.chengdongqing.common.file.FileType;
import top.chengdongqing.common.file.entity.FileMetadata;

/**
 * 上传器
 * 自动获取上传器实例
 * 自动获取不同文件的上传配置
 *
 * @author Luyao
 */
@Component
public class Uploader {

    @Autowired
    private UploadProps props;
    @Autowired
    private FileManagerFactory managerFactory;

    /**
     * 上传图片
     *
     * @param file 图片文件
     * @param type 图片类型
     * @return 文件元数据
     */
    public FileMetadata uploadImage(MultipartFile file, FileType type) throws FileException {
        return managerFactory.getManager().upload(file, type, props.getImageFormats(), props.getImageMaxSize());
    }

    /**
     * 上传视频
     *
     * @param file 视频文件
     * @param type 图片类型
     * @return 文件元数据
     */
    public FileMetadata uploadVideo(MultipartFile file, FileType type) throws FileException {
        return managerFactory.getManager().upload(file, type, props.getVideoFormats(), props.getVideoMaxSize());
    }
}

@Getter
@Setter
@Component
@RefreshScope
@ConfigurationProperties("file")
class UploadProps {

    private String[] imageFormats, videoFormats;
    private long imageMaxSize, videoMaxSize;
}