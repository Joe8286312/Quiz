package com.joe.quiz.service;

import com.joe.quiz.model.user.PageBean;
import com.joe.quiz.model.Result;
import com.joe.quiz.model.user.User;
import com.joe.quiz.model.user.UserUpdateBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
//    //保存新用户；
//    public Result addUser(String username, String password, String checkpassword);

    /**
     * 新增用户
     * @param username 用户名
     * @param password 密码
     * @param checkpassword 确认密码
     * @param userRole 角色（0-普通用户，1-管理员）；register 时强制 0
     */
    Result addUser(String username, String password, String checkpassword, Integer userRole);


    public boolean deleteUserById(Long id);
    public boolean deleteUserByName(String username);

    PageBean page(Integer page, Integer pageSize);

    public List<User> findByName(String keyword);

    // 添加新的分页查询方法
    public PageBean searchByNameWithPage(String keyword, Integer page, Integer pageSize);

    // 新增：编辑用户方法
    public Result updateUser(UserUpdateBean userUpdateBean);

    public User login(String username, String password);

}
