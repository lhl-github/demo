package com.xljy.kwld.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.xljy.bean.Sentence;

//句子
public interface Sentence_di {
	//增
	int addSentence(Map parameterStringMap);
	int addSentName(Map parameterStringMap);
	//删
	int delSentence(Map parameterStringMap);
	int delSentName(Map parameterStringMap);
	int del(Map parameterStringMap);
	//改
	int updateSentence(Map parameterStringMap);
	int updateSentName(Map parameterStringMap);
	
	//查
	//所有(教材、学段、年级、上下册、单元)
	List< Map<String, Object>> findAllSentence(Map parameterStringMap);
	//根据id查询一个
	List< Map<String, Object>> findByIdSentence(Map parameterStringMap);
	//查询是否存在此句子内容
	 Map<String, Object> findByContent(Map parameterStringMap);
	 //根据jzid查询句子
	 List< Map<String, Object>> findByJz(Map parameterStringMap);
	 
	 Map<String, Object> findByID(Map parameterStringMap);
	 
	 @Select("SELECT CONCAT_WS('_', t01, t02, t03, t04, t05) save FROM texttype WHERE 1 = 1  AND texttype.TYPE = (SELECT  k105.`type` FROM k105 WHERE 1 = 1 AND k105.id = #{jzid})")
	 public Map<String, String> get_savepath(@Param("jzid")String jzid);
		
	 
	
}
