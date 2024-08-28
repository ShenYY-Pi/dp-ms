package com.syy.dpms.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syy.dpms.utils.RedisIdWorker;
import com.syy.dpms.utils.UserHolder;
import com.syy.dpms.constants.MQConstants;
import com.syy.dpms.dto.Result;
import com.syy.dpms.entity.SeckillVoucher;
import com.syy.dpms.entity.VoucherOrder;
import com.syy.dpms.mapper.VoucherOrderMapper;
import com.syy.dpms.service.SeckillVoucherService;
import com.syy.dpms.service.VoucherOrderService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 */
@Service
@Slf4j
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements VoucherOrderService {

    @Resource
    private SeckillVoucherService seckillVoucherService;

    @Resource
    private RedisIdWorker redisIdWorker;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;


    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;
    private static final DefaultRedisScript<Long> RECOVER_SCRIPT;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);

        RECOVER_SCRIPT = new DefaultRedisScript<>();
        RECOVER_SCRIPT.setLocation(new ClassPathResource("recover.lua"));
        RECOVER_SCRIPT.setResultType(Long.class);
    }

    @Override
    public Result seckillVoucher(Long voucherId) {
        Long userId = UserHolder.getUser().getId();
        long orderId = redisIdWorker.nextId("order");
        // 1.执行lua脚本
        Long result = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(),
                userId.toString()
        );
        int r = result.intValue();
        // 2.判断结果是否为0
        if (r != 0) {
            // 2.1.不为0 ，代表没有购买资格
            return Result.fail(r == 1 ? "库存不足" : "不能重复下单");
        }
        // 发送消息到 RabbitMQ
        Map<String, Object> message = new HashMap<>();
        message.put("userId", userId);
        message.put("voucherId", voucherId);
        message.put("id", orderId);
        rabbitTemplate.convertAndSend("secKill.direct", "secKill", message);

        // 3.返回订单id
        return Result.ok(orderId);
    }


    @Override
    @Async("taskExecutor")
    public void createVoucherOrder(VoucherOrder voucherOrder) {
        // 一人一单
        Long userId = voucherOrder.getUserId();
        Long voucherId = voucherOrder.getVoucherId();
        Integer count = Math.toIntExact(query().eq("user_id", userId).eq("voucher_id", voucherId).count());
        if (count > 0) {
            // 不允许重复下单
            return;
        }
        // 扣减库存
        boolean success = seckillVoucherService.update()
                .setSql("stock = stock - 1")
                .eq("voucher_id", voucherId)
                .gt("stock", 0)
                .update();

        if (!success) {
            // 库存不足
            return;
        }
        // 发生订单消息到延迟交换机
        rabbitTemplate.convertAndSend(
                MQConstants.DELAY_EXCHANGE_NAME,
                MQConstants.DELAY_VOUCHER_ORDER_KEY,
                voucherOrder.getId(),
                message -> {
                    message.getMessageProperties().setDelay(30000);
                    return message;
                });

        save(voucherOrder);
    }

    @Override
    @Transactional
    public void cancelOrder(VoucherOrder voucherOrder) {
        Long voucherId = voucherOrder.getVoucherId();
        Long voucherOrderId = voucherOrder.getId();
        Long userId = voucherOrder.getUserId();
        // 1：未支付；2：已支付；3：已核销；4：已取消；5：退款中；6：已退款
        // 设置订单状态为 4：已取消
        lambdaUpdate()
                .set(VoucherOrder::getStatus, 4)
                .eq(VoucherOrder::getId, voucherOrderId)
                .update();
        // 恢复数据库库存
        seckillVoucherService.lambdaUpdate()
                .setSql("stock = stock + 1") // 使用 setSql 直接修改库存
                .eq(SeckillVoucher::getVoucherId, voucherId) // 设置条件，voucherId 等于传入的值
                .update(); // 执行更新操作
        // 修改redis缓存里的数据
        stringRedisTemplate.execute(
                RECOVER_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(),
                userId.toString()
        );
    }

}
