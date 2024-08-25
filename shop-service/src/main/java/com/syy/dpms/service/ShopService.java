package com.syy.dpms.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.syy.dpms.dto.Result;
import com.syy.dpms.entity.Shop;

/**
 * <p>
 *  服务类
 * </p>
 */
public interface ShopService extends IService<Shop> {
    Result queryById(Long id);

    Result update(Shop shop);

    Result queryShopByType(Integer typeId, Integer current, Double x, Double y);
}
