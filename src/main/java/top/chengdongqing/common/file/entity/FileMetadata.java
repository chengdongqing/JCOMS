package top.chengdongqing.common.file.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文件元数据
 *
 * @author Luyao
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadata {

    /**
     * 文件唯一键名
     */
    private String key;
    /**
     * 原始文件名
     */
    private String originalName;
    /**
     * 文件大小（字节）
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
