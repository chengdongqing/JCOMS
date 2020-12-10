package top.chengdongqing.common.file.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 文件元数据
 *
 * @author Luyao
 */
@Getter
@Setter
@Builder
public class FileMetadata {

    /**
     * 文件键名
     */
    private String fileKey;
    /**
     * 原始文件名
     */
    private String filename;
    /**
     * 文件哈希值
     */
    private String md5;
    /**
     * 文件大小
     */
    private Long length;
    /**
     * 文件格式
     */
    private String format;
    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;
}
