package com.syy.dpms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syy.dpms.entity.UserInfo;
import com.syy.dpms.mapper.UserInfoMapper;
import com.syy.dpms.service.UserInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
