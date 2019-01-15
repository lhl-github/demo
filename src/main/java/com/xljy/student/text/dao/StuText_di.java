package com.xljy.student.text.dao;

import java.util.List;
import java.util.Map;

public interface StuText_di {

	//查询课文单元表进行对jclx，nj，sxc的拼接查询
	List<Map<String, Object>> findByType(Map parameterStringMap);
	//根据jclx，nj，sxc进行课文分类查询单元
	List<Map<String, Object>> findBySortBySymbol(Map parameterStringMap);//音标
	List<Map<String, Object>> findBySortByType(Map parameterStringMap);//课文等所有
	
	//根据各个单元进去选择每个单元下的所有
	List<Map<String, Object>> findByDyByText(Map parameterStringMap);//课文
	List<Map<String, Object>> findByDyByWord(Map parameterStringMap);//单词
	List<Map<String, Object>> findByDyBySentence(Map parameterStringMap);//句子
	List<Map<String, Object>> findByDyByTalk(Map parameterStringMap);//对话
	List<Map<String, Object>> findByDyByListen(Map parameterStringMap);//听力
	List<Map<String, Object>> findByDyByRead(Map parameterStringMap);//阅读
	List<Map<String, Object>> findByDyByPhrase(Map parameterStringMap);//短语
	//每个学习中都会存在音标
	List<Map<String, Object>> findByFayinshili(Map parameterStringMap);//根据音标id查询发音实例
	List<Map<String, Object>> findByFayinxiangjie(Map parameterStringMap);//根据音标id查询详解
	List<Map<String, Object>> findByChangshifayin(Map parameterStringMap);//根据音标id查询尝试发音
	List<Map<String, Object>> findByZimu(Map parameterStringMap);//根据音标id查询字母及其单词
	List<Map<String, Object>> findByZuhe(Map parameterStringMap);//根据字母id查询下面的字母
	List<Map<String, Object>> findBySymbolByYbid(Map parameterStringMap);//根据ybid查询音标给尝试发音
	//详情页
	List<Map<String, Object>> findBySentenceById(Map parameterStringMap);//句子详情
	List<Map<String, Object>> findByTalkById(Map parameterStringMap);//对话详情
	List<Map<String, Object>> findByTextById(Map parameterStringMap);//课文详情
	List<Map<String, Object>> findByListenById1(Map parameterStringMap);//听力详情
	List<Map<String, Object>> findByListenById2(Map parameterStringMap);
	List<Map<String, Object>> findByListenById3(Map parameterStringMap);
	List<Map<String, Object>> findByReadById1(Map parameterStringMap);//阅读详情
	List<Map<String, Object>> findByReadById2(Map parameterStringMap);
	List<Map<String, Object>> findByReadById3(Map parameterStringMap);
}
