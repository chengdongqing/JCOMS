package top.chengdongqing.common.sender.sms;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 短信参数实体
 *
 * @author Luyao
 */
@Getter
@Setter
@Builder
public class SmsEntity {

    private String to;
    private String template;
    private String content;
}
