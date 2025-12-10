package com.joe.quiz.controller;

import com.joe.quiz.model.user.PageBean;
import com.joe.quiz.model.Result;
import com.joe.quiz.model.user.User;
import com.joe.quiz.model.user.UserUpdateBean;
import com.joe.quiz.service.UserService;
import com.joe.quiz.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/register")
    public Result addUser(String username, String password, String checkpassword){
        Result result = userService.addUser(username, password, checkpassword);
        return result;
    }

    @GetMapping("/deleteById")
    public Result deleteUserById(Long id) {
        boolean success = userService.deleteUserById(id);
        if (success) {
            return Result.success("用户已删除");
        }
        return Result.error("用户不存在或已被删除");
    }

    @GetMapping("/deleteByName")
    public Result deleteUser(String username) {
        boolean success = userService.deleteUserByName(username);
        if (success) {
            return Result.success("用户已删除");
        }
        return Result.error("用户不存在或已被删除");
    }

    @GetMapping("/users")
    public Result getPage(@RequestParam(defaultValue="1")Integer page, @RequestParam(defaultValue="5")Integer pageSize){
        PageBean pageBean=userService.page(page, pageSize);
        return Result.success(pageBean);
    }

//    @GetMapping("/findUser")
//    public Result getUser(String keyword){
//        List<User> users=userService.findByName(keyword);
//        return Result.success(users);
//    }

    // 修改原有的搜索方法，添加分页参数
    @GetMapping("/findUser")
    public Result searchUser(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer pageSize) {

        PageBean pageBean = userService.searchByNameWithPage(keyword, page, pageSize);
        return Result.success(pageBean);
    }

    // 新增：编辑用户接口
    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody UserUpdateBean userUpdateBean) {
        Result result = userService.updateUser(userUpdateBean);
        return result;
    }

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> loginData){
        String username = loginData.get("username");
        String password = loginData.get("password");

        if (StringUtils.isAnyBlank(username, password)) {
            return Result.error("用户名或密码为空");
        }
        User userResult = userService.login(username, password);
        if(userResult!=null){
//            return Result.success("用户登录成功");
            Claims claims = Jwts.claims();
            claims.put("id", userResult.getId());
            claims.put("username", userResult.getUserName());

            String token = JwtUtil.generateTokenWithClaims(claims);
            Result result = Result.success("用户登录成功");
            result.setData(token);
            return result;
        }else{
            return Result.error("用户登录失败");
        }
    }

//    @PostMapping("/login")
//    public Result login(@RequestBody Map<String, String> loginData){
//
//        String username = loginData.get("username");
//        String password = loginData.get("password");
//
//        if (StringUtils.isAnyBlank(username, password)) {
//            return Result.error("用户名或密码为空");
//        }
//        User userResult = userService.login(username, password);
//        if(userResult!=null){
//            Claims claims = Jwts.claims();
//            claims.put("id", userResult.getId());
//            claims.put("username", userResult.getUserName());
//
//            String token = JwtUtil.generateTokenWithClaims(claims);
//            Result result = Result.success("用户登录成功");
//            result.setData(token);
//            return result;
//        }else{
//            return Result.error("用户登录失败");
//        }
//    }






//    @Autowired
//    private UserMapper userMapper;
//
//    @RequestMapping("/register")
//    public Result addUser(String username, String password, String checkpassword){
//        //代码逻辑步骤；
//        //1、用户输入的账户和密码不能为空；
//        //2、校验用户的账户、密码是否符合要求：
//        //	- 账户字符不能少于4个；
//        //	- 密码不能小于8位；
//        //  - 密码和确认密码要一致；
//        //	- 账户不能与已有的重复；
//        //	- 账户不能包含特殊字符；
//        //	- 密码和校验密码相同；
//        //3、对密码进行加密；保证后端工作人员不能看到用户密码；
//        //4、向数据库插入数据；
//
//        //代码逻辑步骤；
//        //1、用户输入的账户和密码不能为空；
//        if(StringUtils.isAnyBlank(username, password, checkpassword)){
//            return Result.error("用户名或密码为空");
//        }
//
//        //2、校验用户的账户、密码是否符合要求：
//        //   - 密码和确认密码要一致；
//        if (!password.equals(checkpassword)) {
//            return Result.error("两次输入的密码不一致");
//        }
//        //	- 账户不能包含特殊字符；
//        String regex = "^[a-zA-Z0-9]+$";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(username);
//        if (!matcher.matches()) {
//            return Result.error("用户名包含特殊字符");
//        }
//        //	- 账户不能与已有的重复；
//        //查询数据库，确定是否已经存在用户名;
//        //to add...
//
//        //3、对密码进行加密；保证后端工作人员不能看到用户密码；密码不要用明文；
//        //对密码进行加密;
//        final String SALT = "com.quiz";
//        String encrptedPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
//
//        User user = new User();
//        user.setUserName(username);
//        user.setUserPassword(encrptedPassword);
//        /**
//         * 注册默认是普通用户，所以userRole设置为0；
//         */
//        user.setUserRole(0);
//        user.setIsDelete(0);
//
//        Date now = new Date();
//        user.setCreateTime(now);
//        user.setUpdateTime(now);
//
//        //4、向数据库插入数据；
//        int result = userMapper.saveUser(user);
//
//        if (result > 0)
//            return Result.success("新增用户成功");
//        else
//            return Result.error("注册用户失败");
//    }
}
