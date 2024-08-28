package com.syy.dpms.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.syy.dpms.dto.Result;
import com.syy.dpms.entity.VoucherOrder;

import java.util.concurrent.CompletableFuture;

/**
 * <p>
 *  服务类
 * </p>
 */
public interface VoucherOrderService extends IService<VoucherOrder> {

    Result seckillVoucher(Long voucherId) throws InterruptedException;

    void createVoucherOrder(VoucherOrder voucherOrder);

    void cancelOrder(VoucherOrder voucherOrder);
}
