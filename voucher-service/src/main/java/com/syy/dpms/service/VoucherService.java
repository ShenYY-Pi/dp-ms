package com.syy.dpms.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.syy.dpms.dto.Result;
import com.syy.dpms.entity.Voucher;

/**
 * <p>
 *  服务类
 * </p>
 */
public interface VoucherService extends IService<Voucher> {

    Result queryVoucherOfShop(Long shopId);

    void addSeckillVoucher(Voucher voucher);
}
