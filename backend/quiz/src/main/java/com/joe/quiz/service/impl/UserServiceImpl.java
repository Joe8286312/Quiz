package com.joe.quiz.service.impl;

import com.joe.quiz.mapper.UserMapper;
import com.joe.quiz.model.user.PageBean;
import com.joe.quiz.model.Result;
import com.joe.quiz.model.user.User;
import com.joe.quiz.model.user.UserUpdateBean;
import com.joe.quiz.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/register")
    public Result addUser(String username, String password, String checkpassword){
        //代码逻辑步骤；
        //1、用户输入的账户和密码不能为空；
        //2、校验用户的账户、密码是否符合要求：
        //	- 账户字符不能少于4个；
        //	- 密码不能小于8位；
        //  - 密码和确认密码要一致；
        //	- 账户不能与已有的重复；
        //	- 账户不能包含特殊字符；
        //	- 密码和校验密码相同；
        //3、对密码进行加密；保证后端工作人员不能看到用户密码；
        //4、向数据库插入数据；

        //代码逻辑步骤；
        //1、用户输入的账户和密码不能为空；
        if(StringUtils.isAnyBlank(username, password, checkpassword)){
            return Result.error("用户名或密码为空");
        }

        //2、校验用户的账户、密码是否符合要求：
        //   - 密码和确认密码要一致；
        if (!password.equals(checkpassword)) {
            return Result.error("两次输入的密码不一致");
        }
        //	- 账户不能包含特殊字符；
        String regex = "^[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            return Result.error("用户名包含特殊字符");
        }
        //	- 账户不能与已有的重复；
        //查询数据库，确定是否已经存在用户名;
        //to add...
        int userExist =  userMapper.existsByName(username);
        if(userExist > 0){
            return Result.error("用户名已存在");
        }


        //3、对密码进行加密；保证后端工作人员不能看到用户密码；密码不要用明文；
        //对密码进行加密;
        final String SALT = "com.quiz";
        String encrptedPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        User user = new User();
        user.setUserName(username);
        user.setUserPassword(encrptedPassword);
        /**
         * 注册默认是普通用户，所以userRole设置为0；
         */
        user.setUserRole(0);
        user.setIsDelete(0);

        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);

        //4、向数据库插入数据；
        int result = userMapper.saveUser(user);

        if (result > 0)
            return Result.success("新增用户成功");
        else
            return Result.error("注册用户失败");
    }

    public boolean deleteUserById(Long id) {
        int result = userMapper.deleteUserById(id);
        return result > 0;
    }

    public boolean deleteUserByName(String username) {
        int result = userMapper.deleteUserByName(username);
        return result > 0;
    }

    public PageBean page(Integer page, Integer pageSize){
        //获取总的记录数；
        Integer total=userMapper.count();

        //获取分页查询结果列表；
        Integer start = (page-1)*pageSize;
        List<User> userList=userMapper.page(start, pageSize);

        //封装PageBean对象；
        PageBean pageBean = new PageBean();
        pageBean.setTotal(total);
        pageBean.setRows(userList);

        return pageBean;
    }

    public List<User> findByName(String keyword){
        List<User> userList=userMapper.findByName(keyword);
        for(User user:userList){
            user.setUserPassword("*******");
        }
        return userList;
    }

    // 添加新的方法
    public PageBean searchByNameWithPage(String keyword, Integer page, Integer pageSize){
        // 获取总的记录数
        Integer total = userMapper.countByKeyword(keyword);

        // 获取分页查询结果列表
        Integer start = (page - 1) * pageSize;
        List<User> userList = userMapper.findByNameWithPage(keyword, start, pageSize);

        // 封装PageBean对象
        PageBean pageBean = new PageBean();
        pageBean.setTotal(total);
        pageBean.setRows(userList);

        return pageBean;
    }

    // 新增：编辑用户方法
    public Result updateUser(UserUpdateBean userUpdateBean) {
        try {
            User user = new User();
            user.setId(userUpdateBean.getId());
            user.setUserName(userUpdateBean.getUserName());
            user.setUserRole(userUpdateBean.getUserRole());
            user.setUpdateTime(new Date());

            // 如果密码不为空，则更新密码
            if (userUpdateBean.getUserPassword() != null && !userUpdateBean.getUserPassword().trim().isEmpty()) {
                final String SALT = "com.quiz";
                String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + userUpdateBean.getUserPassword()).getBytes());
                user.setUserPassword(encryptedPassword);
            }

            int result = userMapper.updateUser(user);
            if (result > 0) {
                return Result.success("用户信息更新成功");
            } else {
                return Result.error("更新失败，用户可能不存在或已被删除");
            }
        } catch (Exception e) {
            return Result.error("系统错误，请稍后重试");
        }
    }

    public User login(String username, String password){
        //对密码进行加密;
        final String SALT = "com.quiz";
        String encrptedPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        return userMapper.getByNameAndPassword(username,encrptedPassword);
    }

}
