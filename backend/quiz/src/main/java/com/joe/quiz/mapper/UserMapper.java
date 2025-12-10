package com.joe.quiz.mapper;

import com.joe.quiz.model.user.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from user")
    public List<User> list();

    @Insert("insert into user(userName, userPassword,isDelete,userRole,createTime, updateTime)"+
            "values(#{userName}, #{userPassword}, #{isDelete}, #{userRole}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public int saveUser(User user);

    @Select("SELECT COUNT(*) FROM user WHERE userName = #{username} AND isDelete = 0")
    public int existsByName(@Param("username") String username);

    //根据id删除用户；
    @Update("UPDATE user SET isDelete = 1, updateTime = NOW() WHERE id = #{id} AND isDelete = 0")
    public int deleteUserById(@Param("id") Long id);

    //根据username删除用户；
    @Update("UPDATE user SET isDelete = 1, updateTime = NOW() WHERE userName = #{username} AND isDelete = 0")
    public int deleteUserByName(@Param("username") String username);

    @Select("SELECT COUNT(*) FROM user WHERE isDelete=0")
    public int count();

    @Select("SELECT * FROM user WHERE isDelete=0 limit #{start},#{pageSize}")
    public List<User> page(Integer start, Integer pageSize);

    @Select("SELECT * FROM user WHERE username LIKE CONCAT('%', #{keyword}, '%') AND isDelete=0")
    public List<User> findByName(String keyword);

    // 添加分页查询方法（按关键词）
    @Select("SELECT * FROM user WHERE username LIKE CONCAT('%', #{keyword}, '%') AND isDelete=0 LIMIT #{start}, #{pageSize}")
    public List<User> findByNameWithPage(@Param("keyword") String keyword, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    // 添加按关键词计数的查询
    @Select("SELECT COUNT(*) FROM user WHERE username LIKE CONCAT('%', #{keyword}, '%') AND isDelete=0")
    public int countByKeyword(String keyword);

    @Select("select * from user where userName=#{username} AND userPassword=#{password}")
    public User getByNameAndPassword(String username, String password);

    // 新增：根据id更新用户信息
//    @Update("UPDATE user SET " +
//            "userName = #{userName}, " +
//            "userPassword = #{userPassword}, " +
//            "userRole = #{userRole}, " +
//            "updateTime = #{updateTime} " +
//            "WHERE id = #{id} AND isDelete = 0")
//    int updateUser(User user);

    // 修改：使用动态SQL更新用户信息
    @Update({"<script>",
            "UPDATE user SET",
            "  userName = #{userName},",
            "  userRole = #{userRole},",
            "  updateTime = #{updateTime}",
            "  <if test='userPassword != null'>, userPassword = #{userPassword}</if>",
            " WHERE id = #{id} AND isDelete = 0",
            "</script>"})
    int updateUser(User user);

}
