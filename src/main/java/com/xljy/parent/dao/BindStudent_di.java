package com.xljy.parent.dao;

import java.util.List;
import java.util.Map;

public interface BindStudent_di {

	//登录后查询是否绑定学生，若无绑定，要对学生进行绑定
	List<Map<String,Object>> findByPid(Map parameterStringMap);
	//如果存在此学生，根据此学生看是否存在班级，存在有，不存在加入
	Map<String,Object> findByStuId(Map parameterStringMap);
	
	//添加学生帐号（1.不存在学生帐号；2.存在学生帐号）
	int saveParStu(Map parameterStringMap);
	
	Map<String,Object> findByStuid2(Map parameterStringMap);
	
	int saveStuByPid(Map parameterStringMap);
	//首页选择一个学生，有最近一次班级排名 和 作业情况  和学习情况报告
	
	//本周作业报告

	List<Map<String,Object>> countOneWeekAll(Map parameterStringMap);
	List<Map<String,Object>> countOneWeekFinish(Map parameterStringMap);
	Map<String,Object> findByScoreAll(Map parameterStringMap);
	
	//班级排名
	List<Map<String,Object>> classByRank(Map parameterStringMap);
	Map<String,Object> findByClassId(Map parameterStringMap);
	
	//作业排名
	List<Map<String,Object>> homeworkByRank(Map parameterStringMap);
	
	//每周报告
	
	//本周班级排名（//本周布置的作业数目）
	List<Map<String,Object>> classWeekByRank(Map parameterStringMap);//学生的本周作业
	
	
	//本周学习情况
	List<Map<String,Object>> findLearnByWeek(Map parameterStringMap);
	//本周作业详情
	List<Map<String,Object>> homeworkByWeekDetail(Map parameterStringMap);
	
	//本周作业统计及展示
	List<Map<String,Object>> findByWeekHwShow(Map parameterStringMap);
	List<Map<String,Object>> findByStuHomework(Map parameterStringMap);//查询本周的作业
	
	
	//作业情况  
	List<Map<String,Object>> findBySymbolAll(Map parameterStringMap);//音标
	List<Map<String,Object>> findByWordType(Map parameterStringMap);//单词
	List<Map<String,Object>> findByPhraseType(Map parameterStringMap);//短语
	List<Map<String,Object>> findByTalkType(Map parameterStringMap);//对话
	List<Map<String,Object>> findByTextType(Map parameterStringMap);//课文
	List<Map<String,Object>> findBySentenceType(Map parameterStringMap);//句子
	List<Map<String,Object>> findByReadType(Map parameterStringMap);//阅读
	List<Map<String,Object>> findByListenType(Map parameterStringMap);//听力
	//作业详情
	List<Map<String,Object>> findBySymbolDetail(Map parameterStringMap);//音标
	List<Map<String,Object>> findByWordDetail(Map parameterStringMap);//单词
	List<Map<String,Object>> findByPhraseDetail(Map parameterStringMap);//短语
	List<Map<String,Object>> findByTalkDetail(Map parameterStringMap);//对话
	List<Map<String,Object>> findByTextDetail(Map parameterStringMap);//课文
	List<Map<String,Object>> findBySentenceDetail(Map parameterStringMap);//句子
	List<Map<String,Object>> findByReadDetail(Map parameterStringMap);//阅读
	List<Map<String,Object>> findByReadOption(Map parameterStringMap);//根据read_qid查询选项
	List<Map<String,Object>> findByListenDetail(Map parameterStringMap);//听力
	List<Map<String,Object>> findByListenOption(Map parameterStringMap);//根据listen_qid查询选项
	
	
	
	//临市表
	int saveLinshiTable(Map parameterStringMap);
	int delLinShiTable(Map parameterStringMap);
	List<Map<String,Object>> findLinAll(Map parameterStringMap);
}
