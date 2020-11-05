package top.chengdongqing.common.payment.wxpay.v2;

import lombok.extern.slf4j.Slf4j;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.kit.StrKit;
import top.chengdongqing.common.payment.PaymentRequestEntity;
import top.chengdongqing.common.payment.wxpay.TradeType;
import top.chengdongqing.common.signature.Bytes;
import top.chengdongqing.common.signature.HMacSigner;
import top.chengdongqing.common.signature.SignatureAlgorithm;

import java.util.Map;

/**
 * 微信扫码支付
 *
 * @author James Lu
 */
@Slf4j
public class PCRequestPayment extends V2RequestPayment {

    @Override
    protected void fillSpecialParams(Map<String, String> params, PaymentRequestEntity entity) {
        params.put("trade_type", TradeType.NATIVE.name());
        Bytes sign = HMacSigner.signatureForHex(StrKit.buildQueryStr(params), constants.getSecretKey(), SignatureAlgorithm.HMAC_SHA256);
        params.put("sign", sign.toHex());
        params.remove("key");
    }

    @Override
    protected Ret packageData(Map<String, String> resultMap) {
        // 返回二维码链接
        return Ret.ok(resultMap.get("code_url"));
    }
}
