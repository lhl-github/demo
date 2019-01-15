package com.xljy.student.user.dao;

import java.util.List;
import java.util.Map;

public interface StuLogin_di {

	//登录
	List<Map<String, Object>> login(Map parameterStringMap);
	//判断是否存在用户名
	List<Map<String, Object>> findByUsername(Map parameterStringMap);
	//判断此用户是否为会员
	int updateStuVip(Map parameterStringMap);
	//修改学生帐号
	Map<String, Object> findByStuid(Map parameterStringMap);
	int updateStuInfo(Map parameterStringMap);
	
	//加入班级（如果学生班级表为空，则学生可以通过班级代码或者老师手机号码）
	int saveByClassId(Map parameterStringMap);
	int updateByClassId(Map parameterStringMap);
	//根据班级代码查询班级
	List<Map<String, Object>> findByClassId(Map parameterStringMap);
	//根据老师手机号查询班级
	List<Map<String, Object>> findByTeacherPhone(Map parameterStringMap);
	
	List<Map<String, Object>> findByClass(Map parameterStringMap);//判断该学生是否存在班级
	
	//修改密码
	int updatePassword(Map parameterStringMap);
	
	//查询学校
	List<Map<String, Object>> findBySheng(Map parameterStringMap);//省
	List<Map<String, Object>> findByShi(Map parameterStringMap);//市
	List<Map<String, Object>> findByQu(Map parameterStringMap);//区
	List<Map<String, Object>> findBySchool(Map parameterStringMap);//学校
	int updateSchool(Map parameterStringMap);
	
	//退出班级
	int delClass(Map parameterStringMap);
	
	//判断是否存在会员卡号及其密码
	String findByCard(Map parameterStringMap);
	int delByCard(Map parameterStringMap);
}
