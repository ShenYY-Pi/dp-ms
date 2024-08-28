package com.syy.dpms.controller;


import com.syy.dpms.utils.UserHolder;
import com.syy.dpms.dto.LoginFormDTO;
import com.syy.dpms.dto.Result;
import com.syy.dpms.dto.UserDTO;
import com.syy.dpms.entity.User;
import com.syy.dpms.entity.UserInfo;
import com.syy.dpms.service.UserInfoService;
import com.syy.dpms.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserInfoService userInfoService;

    /**
     * 发送手机验证码
     */
    @PostMapping("code")
    public Result sendCode(@RequestParam("phone") String phone, HttpSession session) {
        return userService.sendCode(phone, session);
    }

    /**
     * 登录功能
     *
     * @param loginForm 登录参数，包含手机号、验证码；或者手机号、密码
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginForm, HttpSession session) {
        return userService.login(loginForm, session);
    }

    /**
     * 登出功能
     *
     * @return 无
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        // TODO 实现登出功能
//        return Result.fail("功能未完成");
        return Result.ok(userService.logout(request));
    }

    @GetMapping("/{id}")
    public Result getUserById(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        if (user != null) {
            return Result.ok(user);
        } else {
            return Result.fail("error");
        }
    }

    @GetMapping("/me")
    public Result me() {
        // 获取当前登录的用户并返回
        UserDTO user = UserHolder.getUser();
        return Result.ok(user);
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long userId) {
        // 查询详情
        UserInfo info = userInfoService.getById(userId);
        if (info == null) {
            // 没有详情，应该是第一次查看详情
            return Result.ok();
        }
        info.setCreateTime(null);
        info.setUpdateTime(null);
        // 返回
        return Result.ok(info);
    }

    @PostMapping("/sign")
    public Result sign() {
        return userService.sign();
    }

    @GetMapping("/sign/count")
    public Result signCount() {
        return userService.signCount();
    }

    @GetMapping("/feign/{id}")
    public Result queryUserById(@PathVariable("id") Long userId) {
        // 查询详情
        User user = userService.getById(userId);
        if (user == null) {
            return Result.ok();
        }
        // 返回
        return Result.ok(user);
    }

    @PostMapping("/queryByIds")
    public Result queryUsersByIdsWithOrder(@RequestBody List<Long> ids) {
        return userService.queryUsersByIdsWithOrder(ids);
    }

    @PostMapping("/getByIds")
    public Result getUsersByIds(@RequestBody List<Long> ids) {
        return userService.getUsersByIds(ids);
    }

    @GetMapping("/getOneError")
    private Result getOneError(){
        throw new RuntimeException("故意的");
    }
}
