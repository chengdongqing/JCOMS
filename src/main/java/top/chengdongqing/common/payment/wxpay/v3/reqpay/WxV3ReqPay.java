package top.chengdongqing.common.payment.wxpay.v3.reqpay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.IReqPay;
import top.chengdongqing.common.payment.PayReqEntity;
import top.chengdongqing.common.payment.wxpay.WxConstants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Luyao
 */
@Slf4j
@Component
public class WxV3ReqPay implements IReqPay {

    @Autowired
    private WxConstants constants;

    @Override
    public Ret requestPayment(PayReqEntity entity) {
        // 封装请求参数
        Map<String, String> params = new HashMap<>();
        params.put("mchid", constants.getMchId());
        params.put("description", constants.getWebTitle());
        params.put("out_trade_no", entity.getOrderNo());
        params.put("time_expire", LocalDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));

        // TODO
        return null;
    }
}
