package top.chengdongqing.common.sender.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 短信参数实体
 *
 * @author Luyao
 */
@Data
@Builder
public class SmsEntity {

    private String to;
    private String template;
    private String content;
}
