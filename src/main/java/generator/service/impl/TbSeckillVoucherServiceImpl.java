package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.TbSeckillVoucher;
import generator.service.TbSeckillVoucherService;
import generator.mapper.TbSeckillVoucherMapper;
import org.springframework.stereotype.Service;

/**
* @author PYY
* @description 针对表【tb_seckill_voucher(秒杀优惠券表，与优惠券是一对一关系)】的数据库操作Service实现
* @createDate 2024-08-23 18:59:55
*/
@Service
public class TbSeckillVoucherServiceImpl extends ServiceImpl<TbSeckillVoucherMapper, TbSeckillVoucher>
    implements TbSeckillVoucherService{

}




