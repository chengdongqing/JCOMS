package top.chengdongqing.common.uploader;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 文件
 *
 * @author Luyao
 */
@Getter
@Builder
public class File {

    /**
     * 文件名
     */
    private String name;
    /**
     * 是否是文件夹
     */
    private boolean isDirectory;
    /**
     * 文件大小
     */
    private Long size;
    /**
     * 文件路径
     */
    private String path;
    /**
     * 文件格式
     */
    private String format;
    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;
    /**
     * 文件二进制数据
     */
    private byte[] bytes;
}
