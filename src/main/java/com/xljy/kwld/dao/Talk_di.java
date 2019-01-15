package com.xljy.kwld.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface Talk_di {
    //增
	int addTalkName(Map<String, String> parameterStringMap);//对话名称增加
	int addTalk(Map<String, String> parameterStringMap);//对话的增加
	//删
	int delTalkName(Map<String, String> parameterStringMap);//对话名称的删除
	int delTalk(Map<String, String> parameterStringMap);//根据对话名称ID对话的删除
	int delTalkById(Map<String, String> parameterStringMap);//对话的删除
	//改
	int updateTalkName(Map<String, String> parameterStringMap);//对话名称的修改
	int updateTalk(Map<String, String> parameterStringMap);//对话的修改
	//查
	List< Map<String, Object>> findByTalkByTid(Map parameterStringMap);//根据对话名称ID进行查询
	List< Map<String, Object>> findByTalk(Map parameterStringMap);//查询是否存在相关内容
	List< Map<String, Object>> findByTk(Map parameterStringMap);//根据对话名称id查询对话
	
	Map<String, Object> findByID(Map parameterStringMap);
	
	@Select("SELECT CONCAT_WS('_', t01, t02, t03, t04, t05) save FROM  texttype WHERE 1 = 1 AND texttype.TYPE = (SELECT k107.`type` FROM k107  WHERE 1 = 1  AND k107.id = #{dhid})")
	public Map<String, String> get_savepath(@Param("dhid")String dhid);
}
