package top.chengdongqing.common.uploader;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件路径枚举
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum FilePath {

    /**
     * 用户头像
     */
    AVATAR("/user/avatar/");

    /**
     * 存放路径
     */
    private final String path;
}
