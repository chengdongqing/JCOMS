package top.chengdongqing.common.payment.wxpay.v2;

import top.chengdongqing.common.constant.ErrorMsg;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.wxpay.WxStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 微信支付V2工具类
 *
 * @author Luyao
 */
public class WxV2Helper {

    /**
     * 转换时间
     *
     * @param time 时间字符串
     * @return LocalDateTime对象
     */
    public static LocalDateTime convertTime(String time) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * 验证请求结果
     *
     * @param resultMap 响应信息
     * @return 验证结果
     */
    public static <T> Ret<T> getResult(Map<String, String> resultMap) {
        boolean isOk = WxStatus.isOk(resultMap.get("return_code")) && WxStatus.isOk(resultMap.get("result_code"));
        return isOk ? Ret.ok() : Ret.fail(ErrorMsg.REQUEST_FAILED);
    }
}