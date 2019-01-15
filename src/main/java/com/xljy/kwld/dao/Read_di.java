package com.xljy.kwld.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface Read_di {

	//查询
	List< Map<String, Object>> findAll(Map parameterStringMap);//根据阅读表查询
	List< Map<String, Object>> findAllQuest(Map parameterStringMap);//根据rid进行查询
	List< Map<String, Object>> findAllXuan(Map parameterStringMap);//根据qid进行查询
	List< Map<String, Object>> findQuestByContent(Map parameterStringMap);
	List< Map<String, Object>> findXuanByContent(Map parameterStringMap);
	//修改
	int updateRead(Map parameterStringMap);
	int updateQuest(Map parameterStringMap);
	int updateXuan(Map parameterStringMap);
	//删除
	int delRead(Map parameterStringMap);//删除阅读表
	int delQuest(Map parameterStringMap);//根据问题表删除问题
	int delQuestByRid(Map parameterStringMap);//根据阅读ID删除问题表
	int delXuan(Map parameterStringMap);//根据选项id删除选项
	int delXuanByQid(Map parameterStringMap);//根据问题id删除选项
	//保存
	int saveRead(Map parameterStringMap);//阅读表
	int saveQuest(Map parameterStringMap);//
	int saveXuan(Map parameterStringMap);
	
	Map<String, Object> findByID(Map parameterStringMap);
	
	@Select("SELECT CONCAT_WS('_', t01, t02, t03, t04, t05) save FROM texttype WHERE 1 = 1 AND texttype.TYPE = (SELECT   k106.`type`  FROM k106  WHERE 1 = 1   AND k106.id = #{rid})")
	public Map<String, String> get_savepath(@Param("rid")String rid);
}
