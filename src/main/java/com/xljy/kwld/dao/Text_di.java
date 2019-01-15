package com.xljy.kwld.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface Text_di {

	//增加‘’
    int addText(Map parameterStringMap);
	//删除
	int delText(Map parameterStringMap);
	//修改
	int updateText(Map parameterStringMap);
	//查询、
	List< Map<String, Object>> findAllText(Map parameterStringMap);
	List< Map<String, Object>> findByContent(Map parameterStringMap);
	
	Map<String, Object> findByID(Map parameterStringMap);
	
	@Select("SELECT CONCAT_WS('_', t01, t02, t03, t04, t05) save FROM texttype WHERE 1 = 1 AND texttype.TYPE = (SELECT k102.`type` FROM k102 WHERE 1 = 1 AND k102.id = #{id})")
	public Map<String, String> get_savepath(@Param("id")String id);
	
}
