package com.syy.dpms.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.syy.dpms.dto.LoginFormDTO;
import com.syy.dpms.dto.Result;
import com.syy.dpms.dto.UserDTO;
import com.syy.dpms.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpRequest;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 */
public interface UserService extends IService<User> {

    Result sendCode(String phone, HttpSession session);

    Result login(LoginFormDTO loginForm, HttpSession session);

    Result sign();

    Result signCount();

    Result queryUsersByIdsWithOrder(List<Long> ids);

    Result getUsersByIds(List<Long> ids);

    Result logout(HttpServletRequest request);
}
