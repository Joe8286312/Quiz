package com.joe.quiz.mapper;

import com.joe.quiz.model.question.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    //添加题目
    @Insert("INSERT INTO questions (questionText,answer1Text, answer1Correct,answer2Text, answer2Correct, answer3Text, answer3Correct,answer4Text, answer4Correct, isDelete, createTime, updateTime)" +
            "VALUES (#{questionText},#{answer1Text}, #{answer1Correct}, #{answer2Text}, #{answer2Correct},#{answer3Text}, #{answer3Correct},#{answer4Text}, #{answer4Correct}, #{isDelete},#{createTime},#{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertQuestion(Question question);

    //根据id删除题目；
    @Update("UPDATE questions SET isDelete = 1 WHERE id = #{id} AND isDelete = 0")
    public int delQuestionById(Integer id);

    //查询题目
    @Select("SELECT * FROM questions where isDelete=0 ORDER BY RAND() LIMIT 5")
    public List<Question> getQuestions();

    @Select("SELECT COUNT(*) FROM questions WHERE isDelete=0")
    public int count();

    @Select("SELECT * FROM questions WHERE isDelete=0 limit #{start},#{pageSize}")
    public List<Question> page(Integer start, Integer pageSize);

    @Select("SELECT * FROM questions WHERE questionText LIKE CONCAT('%', #{keyword}, '%') AND isDelete=0")
    public List<Question> findByName(String keyword);

    // 添加分页查询方法
    @Select("SELECT * FROM questions WHERE questionText LIKE CONCAT('%', #{keyword}, '%') AND isDelete=0 LIMIT #{start}, #{pageSize}")
    public List<Question> findByNameWithPage(@Param("keyword") String keyword, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    // 添加按关键词计数的查询
    @Select("SELECT COUNT(*) FROM questions WHERE questionText LIKE CONCAT('%', #{keyword}, '%') AND isDelete=0")
    public int countByKeyword(String keyword);

    // 新增：根据id更新题目
    @Update("UPDATE questions SET " +
            "questionText = #{questionText}, " +
            "answer1Text = #{answer1Text}, answer1Correct = #{answer1Correct}, " +
            "answer2Text = #{answer2Text}, answer2Correct = #{answer2Correct}, " +
            "answer3Text = #{answer3Text}, answer3Correct = #{answer3Correct}, " +
            "answer4Text = #{answer4Text}, answer4Correct = #{answer4Correct}, " +
            "updateTime = #{updateTime} " +
            "WHERE id = #{id} AND isDelete = 0")
    int updateQuestion(Question question);
}