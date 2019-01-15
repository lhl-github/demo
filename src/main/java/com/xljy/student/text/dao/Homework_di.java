package com.xljy.student.text.dao;

import java.util.List;
import java.util.Map;

public interface Homework_di {
	
	Map<String,Object> findByType(Map parameterStringMap);//查询save

	int saveStuHomework(Map parameterStringMap);
	int saveStuHomeWorkType(Map parameterStringMap);
	int saveStuHomeWorkAnswer(Map parameterStringMap);
	
	int updateStuHomework(Map parameterStringMap);
	int updateStuHomeWorkType(Map parameterStringMap);
	int updateStuHomeWorkAnswer(Map parameterStringMap);
	
	//判断
	Map<String,Object> findStuHomework(Map parameterStringMap);
	Map<String,Object> findStuHomeWorkType(Map parameterStringMap);
	List<Map<String,Object>> findStuHomeWorkType2(Map parameterStringMap);
	Map<String,Object> findStuHomeWorkAnswer(Map parameterStringMap);
	Map<String,Object> findByScoreAll(Map parameterStringMap);//
	
	
	List<Map<String,Object>> findByHomework(Map parameterStringMap);//根据班级代码查询学生作业
	Map<String,Object> findByHwid(Map parameterStringMap);//根据hwid查询作业类型
	List<Map<String,Object>> findByHwType(Map parameterStringMap);
	
	int updateFinshHw(Map parameterStringMap);//在完成的人数上+1
	//判断该题目是否完成
	
	int delStuHomework(Map parameterStringMap);
	int delStuHomeWorkType(Map parameterStringMap);
	int delStuHomeWorkAnswer(Map parameterStringMap);
	List<Map<String,Object>> findByContentId(Map parameterStringMap);//根据hw_contentid判断
	List<Map<String,Object>> findByAnswerId(Map parameterStringMap);
	Map<String,Object> findByEveryHomework(Map parameterStringMap);
	
	Map<String,Object> findByContentByScore(Map parameterStringMap);//连接hw101，hw102 根据stuid和hwid 和contrent——id查分数
	
	//添加家长消息表
	int saveParentNews(Map parameterStringMap);
	
	//添加作业交齐 老师消息表
	int countClassByStu(Map parameterStringMap);//统计一个班级下的所有学生
	int countHomeWorkByStu(Map parameterStringMap);//统计完成作业的学生
	int saveTeacherNews(Map parameterStringMap);
	List<Map<String,Object>> findByTeaNews(Map parameterStringMap);
	//查询家长学生表   根据学生查询所有的家长
	List<Map<String,Object>> findStuByPid(Map parameterStringMap);
	//查询学生是否重新做作业
	List<Map<String,Object>> findByParent(Map parameterStringMap);
	
	Map<String,Object> selectByPid(Map parameterStringMap);
}
