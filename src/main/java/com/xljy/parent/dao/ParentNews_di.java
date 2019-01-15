package com.xljy.parent.dao;

import java.util.List;
import java.util.Map;

public interface ParentNews_di {

	  
		//查看全部消息（本用户下的所有消息）
		List<Map<String, Object>> findAllByNews(Map parameterStringMap);
		Map<String, Object> findByHomework (Map parameterStringMap);
		//查询单个消息，进行详细查看
		Map<String, Object> findByOneNews(Map parameterStringMap);
		
		//删除消息
		int delByNews(Map parameterStringMap);
		
		int findUnRead(Map parameterStringMap);
}
