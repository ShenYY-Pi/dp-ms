package com.syy.dpms.listener;

import cn.hutool.json.JSONUtil;
import com.syy.dpms.entity.VoucherOrder;
import com.syy.dpms.service.VoucherOrderService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class VoucherOrderSecKillListener {

    @Resource
    private VoucherOrderService voucherOrderService;

    /**
     * 监听秒杀订单消息
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "secKill.queue", durable = "true"),
            exchange = @Exchange(name = "secKill.direct", type = ExchangeTypes.DIRECT),
            key = "secKill"
    ))
    public void listenSecKill(Message message) {
        Map<String, Object> messageMap = JSONUtil.toBean(new String(message.getBody(), StandardCharsets.UTF_8), Map.class);

        // 从 Map 中提取订单信息
        Long userId = ((Number) messageMap.get("userId")).longValue();
        Long voucherId = ((Number) messageMap.get("voucherId")).longValue();
        Long orderId = ((Number) messageMap.get("id")).longValue();
        VoucherOrder voucherOrder = new VoucherOrder();

        voucherOrder.setId(orderId);
        voucherOrder.setUserId(userId);
        voucherOrder.setVoucherId(voucherId);

        voucherOrderService.createVoucherOrder(voucherOrder);
    }

}