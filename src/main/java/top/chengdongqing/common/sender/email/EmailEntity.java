package top.chengdongqing.common.sender.email;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 邮件参数实体
 *
 * @author Luyao
 */
@Getter
@Setter
@Builder
public class EmailEntity {

    private String to;
    private String title;
    private String content;
}
