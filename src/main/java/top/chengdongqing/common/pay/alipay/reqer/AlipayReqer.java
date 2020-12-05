package top.chengdongqing.common.pay.alipay.reqer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.chengdongqing.common.kit.Kv;
import top.chengdongqing.common.kit.Ret;
import top.chengdongqing.common.pay.IRequestPay;
import top.chengdongqing.common.pay.PayProps;
import top.chengdongqing.common.pay.alipay.AlipayHelper;
import top.chengdongqing.common.pay.alipay.AlipayProps;
import top.chengdongqing.common.pay.entity.PayReqEntity;
import top.chengdongqing.common.pay.enums.TradeType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 请求支付宝付款
 *
 * @author Luyao
 */
@Slf4j
public abstract class AlipayReqer implements IRequestPay {

    @Autowired
    protected PayProps payProps;
    @Autowired
    protected AlipayProps alipayProps;
    @Autowired
    protected AlipayHelper helper;

    @Override
    public Ret<Object> requestPayment(PayReqEntity entity, TradeType tradeType) {
        // 封装请求参数
        Kv<String, String> params = Kv.of("notify_url", alipayProps.getNotifyUrl())
                .add("return_url", payProps.getWebDomain());
        helper.buildRequestParams(params, buildBizContent(entity), getMethodName());
        return buildResponse(params);
    }

    /**
     * 构建业务内容
     *
     * @param entity 参数实体
     * @return 业务内容
     */
    private String buildBizContent(PayReqEntity entity) {
        // 构建商品详情
        List<Kv<String, Object>> goodsDetail = entity.getItems().stream().map(item -> Kv.ofAny("goods_id", item.getId())
                .add("goods_name", item.getName())
                .add("quantity", item.getQuantity())
                .add("price", item.getPrice())
                .add("show_url", item.getPictureUrl()))
                .collect(Collectors.toList());
        // 构建业务参数
        Kv<String, Object> bizContent = Kv.ofAny("out_trade_no", entity.getOrderNo())
                .add("total_amount", entity.getAmount())
                .add("subject", entity.getDescription())
                .add("goods_detail", goodsDetail)
                .add("timeout_express", payProps.getTimeout() + "m");
        // 添加额外的业务参数
        addBizContent(bizContent, entity);
        return bizContent.toJson();
    }

    /**
     * 获取请求方法名称
     *
     * @return 请求方法名
     */
    protected abstract String getMethodName();

    /**
     * 添加业务参数
     *
     * @param params 业务参数容器
     * @param entity 参数实体
     */
    protected void addBizContent(Kv<String, Object> params, PayReqEntity entity) {
    }

    /**
     * 构建给客户端的请求付款响应
     *
     * @param params 请求的参数
     * @return 响应数据
     */
    protected abstract Ret<Object> buildResponse(Kv<String, String> params);
}
