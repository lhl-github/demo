package com.xljy.kwld.dao;

import java.util.List;
import java.util.Map;

public interface SubText_di {

	    //增
		int addSubText1(Map parameterStringMap);//跟读课文的大类的添加
		int addSubText2(Map parameterStringMap);//跟读课文的添加
		//删
		int delSubText1(Map parameterStringMap);//删除跟读课文大类
		int delSubText2(Map parameterStringMap);//根据ftid 删除
		//改
		int updateSubText1(Map parameterStringMap);//跟读课文的修改
		//查
		List<Map<String, Object>> findBySubTextAll(Map parameterStringMap);//查询所有的跟读课文
		List<Map<String, Object>> findBySid(Map parameterStringMap);//根据ftid 查询跟读课文
		Map<String, Object> findBySidBySub(Map parameterStringMap);
}
