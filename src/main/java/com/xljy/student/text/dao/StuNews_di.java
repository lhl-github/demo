package com.xljy.student.text.dao;

import java.util.List;
import java.util.Map;

public interface StuNews_di {

	//当进入主页时，有个铃铛会显示有未读的消息多少条（本用户下的未读消息个数）
	int findByUnReadCount(Map parameterStringMap);
	
	//查看全部消息（本用户下的所有消息）
	List<Map<String, Object>> findAllByNews(Map parameterStringMap);
	
	//查询本用户下的所有未读消息
	List<Map<String, Object>> findAllByUnRead(Map parameterStringMap);
	
	//将未读消息标记为已读（更新消息状态）
	int updateByUnRead(Map parameterStringMap);
	
	//查询单个消息，进行详细查看
	Map<String, Object> findByOneNews(Map parameterStringMap);
	
	//删除消息
	int delByNews(Map parameterStringMap);
	
}
