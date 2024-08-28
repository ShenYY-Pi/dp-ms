package com.syy.dpms.listener;

import com.syy.dpms.constants.MQConstants;
import com.syy.dpms.entity.VoucherOrder;
import com.syy.dpms.service.VoucherOrderService;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class VoucherOrderDelayMessageListener {

    @Resource
    private VoucherOrderService voucherOrderService;

    /**
     * 监听订单支付状态
     *
     * @param voucherId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MQConstants.DELAY_VOUCHER_ORDER_QUEUE_NAME),
            exchange = @Exchange(name = MQConstants.DELAY_EXCHANGE_NAME, delayed = "true"),
            key = MQConstants.DELAY_VOUCHER_ORDER_KEY
    ))
    public void listenVoucherOrderDelayMessage(Long voucherId) {
        // 查询订单
        VoucherOrder voucherOrder = voucherOrderService.getById(voucherId);
        // 判断订单状态 1：未支付；2：已支付；3：已核销；4：已取消；5：退款中；6：已退款
        if (voucherOrder == null || voucherOrder.getStatus() != 1) {
            // 订单不存在或者已经支付
            return;
        }
        // 未支付并已超时 取消订单 并且恢复库存
        voucherOrderService.cancelOrder(voucherOrder);
    }

}
