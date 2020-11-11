package top.chengdongqing.common.payment.wxpay.v2.reqpay;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.TradeType;
import top.chengdongqing.common.payment.entity.PayReqEntity;

import java.util.Map;

/**
 * 微信外手机浏览器调起微信客户端支付
 *
 * @author Luyao
 */
public class MBReqPay extends WxV2ReqPay {

    @Override
    protected void addSpecialParams(Map<String, String> params, PayReqEntity entity) {
        params.put("appid", constants.getAppId().getMb());
        params.put("trade_type", TradeType.MWEB.name());
        // 场景信息
        params.put("scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"" + constants.getWebDomain() + "\",\"wap_name\": \"" + constants.getWebTitle() + "\"}}");
    }

    @Override
    protected Ret packageData(Map<String, String> resultMap) {
        // 调起微信支付的跳转链接
        return Ret.ok(resultMap.get("mweb_url"));
    }
}
