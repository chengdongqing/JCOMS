package top.chengdongqing.common.file.entity;

import lombok.Builder;
import lombok.Data;

import java.io.InputStream;

/**
 * 文件下载实体
 *
 * @author Luyao
 */
@Data
@Builder
public class DownloadFile {

    /**
     * 文件内容
     */
    private InputStream content;
    /**
     * 文件大小
     */
    private long length;
}
