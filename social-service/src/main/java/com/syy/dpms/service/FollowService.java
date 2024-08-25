package com.syy.dpms.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.syy.dpms.dto.Result;
import com.syy.dpms.entity.Follow;

/**
 * <p>
 *  服务类
 * </p>
 */
public interface FollowService extends IService<Follow> {

    Result follow(Long followUserId, Boolean isFollow);

    Result isFollow(Long followUserId);

    Result followCommons(Long id);
}
