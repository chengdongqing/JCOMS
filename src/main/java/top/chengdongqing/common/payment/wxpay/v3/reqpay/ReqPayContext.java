package top.chengdongqing.common.payment.wxpay.v3.reqpay;

import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.payment.IReqPay;
import top.chengdongqing.common.payment.PayClient;
import top.chengdongqing.common.payment.PayReqEntity;

/**
 * @author Luyao
 */
public class ReqPayContext {

    private IReqPay strategy;

    /**
     * 根据请求客户端获取请求实例
     *
     * @param client 请求支付客户端
     */
    public ReqPayContext(PayClient client) {
        if (client == PayClient.PC) {
            strategy = new PCReqPay();
        } else if (client == PayClient.APP) {
            strategy = new APPReqPay();
        } else if (client == PayClient.MP) {
            strategy = new MPReqPay();
        } else if (client == PayClient.MB) {
            strategy = new MBReqPay();
        }
    }

    /**
     * 请求付款
     *
     * @param entity 参数实体
     * @return 请求响应
     */
    public Ret request(PayReqEntity entity) {
        return strategy.requestPayment(entity);
    }
}
