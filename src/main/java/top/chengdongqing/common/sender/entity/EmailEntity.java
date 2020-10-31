package top.chengdongqing.common.sender.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 邮件参数实体
 *
 * @author Luyao
 */
@Data
@Builder
public class EmailEntity {

    private String to;
    private String title;
    private String content;
}
