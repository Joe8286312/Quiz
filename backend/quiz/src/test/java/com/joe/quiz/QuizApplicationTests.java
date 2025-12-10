package com.joe.quiz;

import com.joe.quiz.mapper.QuestionMapper;
import com.joe.quiz.mapper.UserMapper;
import com.joe.quiz.model.question.Question;
import com.joe.quiz.model.user.User;
import com.joe.quiz.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class QuizApplicationTests {

    @Autowired
    private UserMapper userMapper;

	@Test
    public void testListUser(){
        List<User> userList = userMapper.list();
        userList.forEach(System.out::println);
    }

    @Test
    public void testSaveUser(){
        User user = new User();
        user.setUserName("testuser");
        user.setUserPassword("password123");

        user.setUserRole(0);
        user.setIsDelete(0);

        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);

        // 执行插入操作,插入后返回插入成功的行数，这里默认就应该是1；
        int result = -1;
        result = userMapper.saveUser(user);
        System.out.println(result);
        //生成的id会默认回填到user对象中；
        System.out.println(user.getId());
//        return result;
    }

    @Test
    public void testExistsByName(){
        String name = "testuser";
        int result = userMapper.existsByName(name);
        System.out.println(result);
    }

    @Test
    public void testDeleteById(){
        long id = 1L;
        int result = userMapper.deleteUserById(id);
        System.out.println(result);
    }

    @Test
    public void testDeleteByUsername(){
        String name = "fromapifox";
        int result = userMapper.deleteUserByName(name);
        System.out.println(result);
    }

    @Autowired
    private QuestionMapper questionMapper;

    @Test
    public void testInsertQuestion(){
        Question question = new Question();
        question.setQuestionText("What is the capital of France?");

        question.setAnswer1Text("Paris");
        question.setAnswer1Correct(true);

        question.setAnswer2Text("London");
        question.setAnswer2Correct(false);

        question.setAnswer3Text("Berlin");
        question.setAnswer3Correct(false);

        question.setAnswer4Text("Madrid");
        question.setAnswer4Correct(false);

        question.setIsDelete(0);
        question.setCreateTime(new Date());
        question.setUpdateTime(new Date());

        // 执行插入操作
        int result = questionMapper.insertQuestion(question);
        System.out.println(result);
        System.out.println(question.getId());
    }

    @Test
    public void DelQuestionById() {
        Integer id = 1;

        int result = questionMapper.delQuestionById(id);
        System.out.println(result);
    }

    @Test
    public void testQuestionList(){
        List<Question> qlist = questionMapper.getQuestions();
        for (Question q : qlist) {
            System.out.println(q);
        }
    }

    @Test
    public void testJWT(){
        //生成Toekn
        String token = JwtUtil.generateToken("toms");
        System.out.println("generated token:"+token);

        //解析Toekn
        String username = JwtUtil.parseToken(token);
        System.out.println("解析出的用户名：" + username);

    }

    @Test
    public  void JwtTest() {
        //生成JWT令牌；
        Claims userClaims = Jwts.claims(); // 新建一个 Claims 对象
        userClaims.put("id", "123");
        userClaims.put("username", "toms");
        String token = JwtUtil.generateTokenWithClaims(userClaims);
        System.out.println("token:"+token);
        //解析JWT令牌；
        Claims claim = JwtUtil.parseTokenReturnClaims(token);
        System.out.println("=== 解析后的 JWT Claims ===");
        System.out.println("ID: " + claim.get("id", String.class));
        System.out.println("Username: " + claim.get("username", String.class));
    }


}
