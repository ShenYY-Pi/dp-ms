package com.syy.dpms.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syy.dpms.entity.SeckillVoucher;
import com.syy.dpms.mapper.SeckillVoucherMapper;
import com.syy.dpms.service.SeckillVoucherService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 秒杀优惠券表，与优惠券是一对一关系 服务实现类
 * </p>
 */
@Service
public class SeckillVoucherServiceImpl extends ServiceImpl<SeckillVoucherMapper, SeckillVoucher> implements SeckillVoucherService {

}
