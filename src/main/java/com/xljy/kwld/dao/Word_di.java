package com.xljy.kwld.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface Word_di {
    //查询
	List< Map<String, Object>> findAllWord(Map parameterStringMap);
	//删除
	int delWordById(Map parameterStringMap);
	//添加
	int addWord(Map parameterStringMap );
	//修改
	int updateWord(Map parameterStringMap);
    //查询是否存在此单词
	List< Map<String, Object>> findByContent(Map parameterStringMap);
	//根据type进行删除
	int delWordByType(Map parameterStringMap);
	
	List< Map<String, Object>> findByID(Map parameterStringMap);
	//根据单元进行查询单元单词
	List< Map<String, Object>> findByType(Map parameterStringMap);
	
	@Select("SELECT DISTINCT CONCAT_WS('_', t.t01, t.t02, t.t03, t.t04, t.t05) save FROM texttype t,k103 k WHERE t.`type` = k.`type` AND k.`type`=#{type}")
	public Map<String, String> get_savepath(@Param("type")String type);
}
