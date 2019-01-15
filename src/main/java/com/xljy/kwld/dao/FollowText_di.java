package com.xljy.kwld.dao;

import java.util.List;
import java.util.Map;

public interface FollowText_di {

	//增
	int addFollowText1(Map parameterStringMap);//跟读课文的大类的添加
	int addFollowText2(Map parameterStringMap);//跟读课文的添加
	//删
	int delFollowText1(Map parameterStringMap);//删除跟读课文大类
	int delFollowText2(Map parameterStringMap);//根据ftid 删除
	//改
	int updateFollowText1(Map parameterStringMap);//跟读课文的修改
	//查
	List<Map<String, Object>> findByFollowTextAll(Map parameterStringMap);//查询所有的跟读课文
	List<Map<String, Object>> findByFtid(Map parameterStringMap);//根据ftid 查询跟读课文
	Map<String, Object> findByFtidByFt(Map parameterStringMap);
}
