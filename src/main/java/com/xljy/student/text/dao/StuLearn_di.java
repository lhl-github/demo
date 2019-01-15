package com.xljy.student.text.dao;

import java.util.List;
import java.util.Map;

public interface StuLearn_di {

	//音标（尝试发音）
	int saveLearnBySymbol(Map parameterStringMap);//保存尝试发音
	Map<String, Object> findLearnByYbid(Map parameterStringMap);//根据ybid和stuid判断用户是否存在学习
	int updateLearnBySymbol(Map parameterStringMap);//如果用户存在新的录音，则修改为新录音
	
	//音标单词学习（挑战一下）:挑战一下就是 1 单词实例
	int saveLearnBySymbolWord(Map parameterStringMap);//保存音标单词实例
	Map<String, Object> findLearnBySymbolWord(Map parameterStringMap);//根据yb_wordId和stuid判断用户是否存在
	int updateLearnBySymbolWord(Map parameterStringMap);//若存在替换为新录音;判断评分是否为最高
	//课文
	int saveLearnText(Map parameterStringMap);//保存课文学习录音
	Map<String,Object> findLearnByText(Map parameterStringMap);//根据text_id,stuid判断是否存在此学习课文
	int updateLearnByText(Map parameterStringMap);//若存在替换为新录音;判断评分是否为最高
	int countByLearnByText(Map parameterStringMap);//统计本单元下的用户的学习课文情况
	int countByText(Map parameterStringMap);//统计本单元下的所有课文
	//短语
	int saveLearnPhrase(Map parameterStringMap);
	Map<String,Object> findLearnByPhrase(Map parameterStringMap);
	int updateLearnByPhrase(Map parameterStringMap);
	int countByLearnByPhrase(Map parameterStringMap);
	int countByPhrase(Map parameterStringMap);//统计本单元下的所有课文
	//单词
	int saveLearnWord(Map parameterStringMap);//保存单词学习录音
	Map<String, Object> findLearnByWord(Map parameterStringMap);//根据word_id,stuid判断是否存在此学习单词
	int updateLearnByWord(Map parameterStringMap);//若存在替换为新录音;判断评分是否为最高
	int countByLearnWord(Map parameterStringMap);//统计本单元下的用户的学习单词的个数
	int countByWord(Map parameterStringMap);//统计本单元下的所有单词
	
	//对话
	int countByLearnTalkName(Map parameterStringMap);//统计此用户下的本单元的学习对话
	int countByTalkName(Map parameterStringMap);//统计本单元下的对话
	int countByLearnTalkJs(Map parameterStringMap);//此对话下的学习角色
	int countByTalkJs(Map parameterStringMap);//统计此对话下的所有对话
//	int saveLearnTalk(Map parameterStringMap);//保存对话学习
	int saveLearnJS(Map parameterStringMap);//保存角色学习
	List<Map<String, Object>> findLearnByTalk(Map parameterStringMap);//根据juese_id;stuid判断是否存在
//	int updateLearnByTalk(Map parameterStringMap);//若存在替换为新录音;判断评分是否为最高
	int updateLearnByJS(Map parameterStringMap);//若存在替换为新录音;判断评分是否为最高
	List<Map<String, Object>> findAllByLearnTalk(Map parameterStringMap);//查询一个对话下的录音
	
	//句子
	int countByLearnSentence(Map parameterStringMap);//统计一个句子名称下的句子、每个句子
	int countBySentence(Map parameterStringMap);//统计对话名称下的句子
	int saveLearnSentence(Map parameterStringMap);//保存学习句子
	Map<String, Object> findLearnSentence(Map parameterStringMap);//根据句子id和stuid
	int updateLearnSentence(Map parameterStringMap);//若存在替换为新录音;判断评分是否为最高
	
	//阅读   如果一个阅读完成  则保存阅读学习表
	int saveLearnRead(Map parameterStringMap);//保存阅读学习
	int saveLearnAnswer(Map parameterStringMap);//保存阅读答案
	int updateLearnRead(Map parameterStringMap);//修改阅读学习
	int updateLearnAnswer(Map parameterStringMap);//修改阅读答案
	Map<String, Object> findLearnRead(Map parameterStringMap);//根据阅读id和stuid和type
	Map<String, Object> findLearnAnswer(Map parameterStringMap);//根据答案id和stuid
	int countLearnRead(Map parameterStringMap);
	int countLearnAnswer(Map parameterStringMap);
	int countRead(Map parameterStringMap);
	int countAnswer(Map parameterStringMap);
	List<Map<String, Object>> findLearnReadID(Map parameterStringMap);
	//听力
	int saveLearnListen(Map parameterStringMap);//保存学习
	int saveLearnListenAnswer(Map parameterStringMap);//保存阅读答案
	int updateLearnListen(Map parameterStringMap);//修改阅读学习
	int updateLearnListenAnswer(Map parameterStringMap);//修改阅读答案
	Map<String, Object> findLearnListen(Map parameterStringMap);//根据阅读id和stuid和type
	Map<String, Object> findLearnListenAnswer(Map parameterStringMap);//根据答案id和stuid
	int countLearnListen(Map parameterStringMap);
	int countLearnListenAnswer(Map parameterStringMap);
	int countListen(Map parameterStringMap);
	int countListenAnswer(Map parameterStringMap);
	List<Map<String, Object>> findLearnListenID(Map parameterStringMap);
}
