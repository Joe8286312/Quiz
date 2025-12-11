package com.joe.quiz.controller;

import com.joe.quiz.model.Result;
import com.joe.quiz.model.user.PageBean;
import com.joe.quiz.model.user.User;
import com.joe.quiz.model.user.UserUpdateBean;
import com.joe.quiz.service.UserService;
import com.joe.quiz.utils.JwtUtil; // 修改：新增导入，封装 claims
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户端注册：强制 userRole=0（普通用户），无需 JWT
     */
    @PostMapping("/register") // 修改：用户注册专用
    public Result register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String checkpassword = body.get("checkpassword");
        Integer userRole = 0; // 修改：注册强制普通用户
        return userService.addUser(username, password, checkpassword, userRole);
    }

    /**
     * 管理端添加用户：需要 JWT；userRole 由前端传入（0/1）
     */
    @PostMapping("/adduser") // 修改：管理端添加用户接口
    public Result addUser(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String checkpassword = body.get("checkpassword");
        String roleStr = body.get("userRole");
        Integer userRole = null;
        if (StringUtils.isNotBlank(roleStr)) {
            try {
                userRole = Integer.parseInt(roleStr);
            } catch (NumberFormatException e) {
                return Result.error("userRole 必须是数字 0 或 1");
            }
        }
        return userService.addUser(username, password, checkpassword, userRole);
    }

    @GetMapping("/deleteById")
    public Result deleteUserById(Long id) {
        boolean success = userService.deleteUserById(id);
        if (success) return Result.success("用户已删除");
        return Result.error("用户不存在或已被删除");
    }

    @GetMapping("/deleteByName")
    public Result deleteUser(String username) {
        boolean success = userService.deleteUserByName(username);
        if (success) return Result.success("用户已删除");
        return Result.error("用户不存在或已被删除");
    }

    @GetMapping("/users")
    public Result getPage(@RequestParam(defaultValue = "1") Integer page,
                          @RequestParam(defaultValue = "5") Integer pageSize) {
        PageBean pageBean = userService.page(page, pageSize);
        return Result.success(pageBean);
    }

    @GetMapping("/findUser")
    public Result searchUser(@RequestParam(defaultValue = "") String keyword,
                             @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "5") Integer pageSize) {
        PageBean pageBean = userService.searchByNameWithPage(keyword, page, pageSize);
        return Result.success(pageBean);
    }

    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody UserUpdateBean userUpdateBean) {
        return userService.updateUser(userUpdateBean);
    }

    /**
     * 用户端登录（普通/管理员均可用于普通站点）
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        if (StringUtils.isAnyBlank(username, password)) {
            return Result.error("用户名或密码为空");
        }
        User userResult = userService.login(username, password);
        if (userResult != null) {
            Claims claims = JwtUtil.buildClaims(userResult); // 修改：统一封装 claims
            String token = JwtUtil.generateTokenWithClaims(claims);
            Result result = Result.success("用户登录成功");
            result.setData(token);
            return result;
        } else {
            return Result.error("用户登录失败");
        }
    }

    /**
     * 管理端登录：仅允许 userRole=1；普通用户返回 msg: invalid account.
     */
    @PostMapping("/admin/login") // 修改：新增管理员登录接口
    public Result adminLogin(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        if (StringUtils.isAnyBlank(username, password)) {
            return Result.error("用户名或密码为空");
        }
        User userResult = userService.login(username, password);
        if (userResult == null) {
            return Result.error("用户登录失败");
        }
        if (userResult.getUserRole() == null || userResult.getUserRole() != 1) {
            // 修改：普通用户登录管理端，返回 invalid account
            return Result.error("invalid account");
        }
        Claims claims = JwtUtil.buildClaims(userResult);
        String token = JwtUtil.generateTokenWithClaims(claims);
        Result result = Result.success("管理员登录成功");
        result.setData(token);
        return result;
    }
}