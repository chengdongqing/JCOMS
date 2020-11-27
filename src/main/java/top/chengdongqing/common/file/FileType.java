package top.chengdongqing.common.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件类型枚举
 *
 * @author Luyao
 */
@Getter
@AllArgsConstructor
public enum FileType {

    /**
     * 用户头像
     */
    AVATAR("user/avatar"),
    /**
     * 商品评论
     */
    COMMENT("goods/comment");

    /**
     * 路径名称
     */
    private final String pathname;
}
