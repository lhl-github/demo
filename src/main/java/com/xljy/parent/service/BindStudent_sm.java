package com.xljy.parent.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xljy.parent.dao.BindStudent_di;
import com.xljy.parent.dao.ParentNews_di;
import com.xljy.student.text.dao.Homework_di;
import com.xljy.student.text.dao.StuText_di;
import com.xljy.student.text.servers.Homework_sm;
import com.xljy.student.user.dao.StuLogin_di;
import com.xljy.util.Const;

@Service
public class BindStudent_sm {

	@Autowired
	private BindStudent_di bs_di;
	@Autowired
	private StuLogin_di stu_di;
	@Autowired
	private Homework_di hw_di;
	@Autowired
	private StuText_di text_di;
	@Autowired
	private ParentNews_di pnews_di;
	
	//家长登录成功后，查询此家长下是否存在学生
	public List<Map<String,Object>> findByPid(Map parameterStringMap){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String str = sdf.format(date);//时间存储为字符串
	    Timestamp starttime = Timestamp.valueOf(str);//设置起时间
		List<Map<String, Object>> list = bs_di.findByPid(parameterStringMap);//判断该家长下是否绑定学生
		int unRead = pnews_di.findUnRead(parameterStringMap);
		 if(list != null && !list.isEmpty()) {
			 for (int i = 0; i < list.size(); i++) {
				 Timestamp end = (Timestamp) list.get(i).get("end");
				 list.get(i).put("unRead", unRead);
				 if(list.get(i).containsKey("end") && end.after(starttime)) {
					 list.get(i).put("vipState", "1");
				 }else {
					 list.get(i).put("vipState", "0");
				 }
				 Map<String, Object> map = bs_di.findByStuId(list.get(i));//判断该学生是否存在班级
				 if(map !=null && !map.isEmpty()) {
					 String save_path = "user" + "/" + "5" + "/" +list.get(i).get("stuid") + "/";
					 list.get(i).put("class", map.get("class"));
					 list.get(i).put("grade", map.get("grade"));
					 list.get(i).put("save_path", save_path);
				 }else {
					 list.get(i).put("class", "");
				 }
			}
		 }
		 return list;
	}
	
	//绑定学生帐号
	public int saveParStu(Map parameterStringMap) {
		
		return bs_di.saveParStu(parameterStringMap);
	}
	
	//添加学生帐号
	public int saveStuByPid(Map parameterStringMap) {
		
		return bs_di.saveStuByPid(parameterStringMap);
	}
	//班级排名情况；作业情况
	
	
	//本周学习报告
	public Map<String,Object> findByWeekLearn(Map parameterStringMap){
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		List<Map<String,Object>> zuoyezongshu = bs_di.countOneWeekAll(parameterStringMap);
		List<Map<String,Object>> wanchengshu = bs_di.countOneWeekFinish(parameterStringMap);
		int weiwanchengshu = zuoyezongshu.size() - wanchengshu.size();
		Map<String,Object> scoreAll = bs_di.findByScoreAll(parameterStringMap);
		if(scoreAll !=null && !scoreAll.isEmpty()) {
			hashMap.put("total", zuoyezongshu.size());
			hashMap.put("finish",wanchengshu.size());
			hashMap.put("nofinish",weiwanchengshu);
			double finishpre = Double.valueOf(String.format("%.2f", (double)wanchengshu.size()/zuoyezongshu.size()));
			double avgScore = Double.valueOf(String.format("%.2f", (double) scoreAll.get("scoreAll")/zuoyezongshu.size()));
			if(zuoyezongshu.size() ==0) {
				hashMap.put("finishpre","");
				hashMap.put("avgScore","");
			}else {
				hashMap.put("finishpre",finishpre * 100);
				hashMap.put("avgScore",avgScore );
			}
			
		}else {
			 hashMap.put("flags", "本周未存在作业"); 
		 }
		
		return hashMap;
	}
	
	//班级排名
	public Map<String,Object> findClassRank(Map parameterStringMap){
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		 Map<String, Object> map = bs_di.findByClassId(parameterStringMap);//stuid
		 if(map != null && !map.isEmpty()) {
			 List<Map<String,Object>> list = bs_di.classByRank(map);
			 if(list !=null && ! list.isEmpty()) {
				 for (int i = 0; i < list.size(); i++) {
						if(list.get(i).get("stuid").equals(parameterStringMap.get("stuid"))){
							hashMap.put("rank", list.get(i).get("rank"));
						}
					}
			 }else {
				 hashMap.put("rank", "");
			 }
		 }else {
			 hashMap.put("flag", "该学生未加入班级"); 
		 }
		
		return hashMap;
	}
	
	//支付详情页面
	public Map<String,Object> payDetail(Map parameterStringMap){
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		 Map<String, Object> student = stu_di.findByStuid(parameterStringMap);//stuid
		 String save_path = "student" + "/" + parameterStringMap.get("stuid") + "/";
		 List<Map<String, Object>> stuList = stu_di.findByClass(parameterStringMap);	
		 hashMap.put("save_path", save_path);
		 hashMap.put("student", student.get("student"));
		 hashMap.put("stuTupian", student.get("stuTupian"));
		 if(stuList !=null && !stuList.isEmpty()) {
			 hashMap.put("classId", stuList.get(0).get("classId"));
			 hashMap.put("teacher", stuList.get(0).get("teacher"));
		 }
		
		return hashMap;
	}
	
	//班级作业排名
	public Map<String,Object> homeworkByRank(Map parameterStringMap){
		Map<String,Object> hashMap = new HashMap<String, Object>();
		 Map<String, Object> map = bs_di.findByClassId(parameterStringMap);//stuid;hwid
		 if(map != null && !map.isEmpty()) {
			 parameterStringMap.put("classId", map.get("classId"));
			 Map<String, Object> student = bs_di.findByStuid2(parameterStringMap);
			 Map<String, Object> student2 = bs_di.findByStuId(parameterStringMap);
			 List<Map<String, Object>> list = bs_di.homeworkByRank(parameterStringMap);
			 if(list == null || list.isEmpty()) {
				 hashMap.put("list", "暂无排名");
				 Map<String,Object> presonRank = new HashMap<String,Object>();
				 
				 presonRank.put("school", student.get("school"));
				    presonRank.put("class", student2.get("class"));
				    presonRank.put("grade", student2.get("grade"));
				    presonRank.put("rank", "");
				    presonRank.put("student", student.get("stuName"));
				    presonRank.put("scoreAll", "");
				    presonRank.put("homeworkName", hw_di.findByHwid(parameterStringMap).get("homeworkName"));
				    hashMap.put("presonRank", presonRank);
			 }else {
				 hashMap.put("list", list);
				 for (int i = 0; i < list.size(); i++) {
					 Map<String,Object> presonRank = new HashMap<String,Object>();
					 if(list.get(i).get("stuid").equals(parameterStringMap.get("stuid"))){
						    
						    presonRank.put("school", list.get(i).get("school"));
						    presonRank.put("class", list.get(i).get("class"));
						    presonRank.put("grade", list.get(i).get("grade"));
						    presonRank.put("rank", list.get(i).get("rank"));
						    presonRank.put("student", list.get(i).get("student"));
						    presonRank.put("scoreAll", list.get(i).get("scoreAll"));
						    presonRank.put("homeworkName", hw_di.findByHwid(parameterStringMap).get("homeworkName"));
						    hashMap.put("presonRank", presonRank);
					 }else {
						 presonRank.put("school", student.get("school"));
						    presonRank.put("class", student2.get("class"));
						    presonRank.put("grade", student2.get("grade"));
						    presonRank.put("rank", "");
						    presonRank.put("student", student.get("stuName"));
						    presonRank.put("scoreAll", "");
						    presonRank.put("homeworkName", hw_di.findByHwid(parameterStringMap).get("homeworkName"));
						    hashMap.put("presonRank", presonRank);
					 }
					 
				}
			 }
		 }else {
			 hashMap.put("list", "该学生未加入班级");
		 }
		 
		return hashMap;
	}
	
	//本周班级排名
	public Map<String,Object> classWeekByRank(Map parameterStringMap){
		 Map<String,Object> hashMap = new HashMap<String, Object>();
		 Map<String, Object> map = bs_di.findByClassId(parameterStringMap);//stuid
		 if(map != null && !map.isEmpty()) {
				List<Map<String,Object>> list = bs_di.classWeekByRank(map);
				List<Map<String,Object>> zuoyezongshu = bs_di.countOneWeekAll(parameterStringMap);
				List<Map<String,Object>> wanchengshu = bs_di.countOneWeekFinish(parameterStringMap);
				if(list !=null && ! list.isEmpty()) {
					hashMap.put("rankList", list);
					for (int i = 0; i < list.size(); i++) {
						this.findByHwCondition1(list.get(i));
						double avgScore = Double.valueOf(String.format("%.2f", (Double)list.get(i).get("scoreAll")/zuoyezongshu.size()));
						 list.get(i).put("savePath", "user" + "/"+ "5" + "/" + list.get(i).get("stuid") + "/");
						 list.get(i).put("avgScore", avgScore);
						 if(zuoyezongshu.size() == 0) {
							 list.get(i).put("avgScore", "");
						 }
						if(list.get(i).get("stuid").equals(parameterStringMap.get("stuid"))) {
							hashMap.put("studentName", list.get(i).get("sutdent"));
							hashMap.put("avgScore", avgScore);
							hashMap.put("class", map.get("class"));
							hashMap.put("hwSpeed", wanchengshu.size()+"/"+zuoyezongshu.size());
							hashMap.put("rank",list.get(i).get("rank"));
						}
						
					}
				}	
		 }else {
			 hashMap.put("flag", "该学生未加入班级"); 
		 }
		
		return hashMap;
	}
	public List<Map<String,Object>> findByHwForThree(Map parameterStringMap){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String str = sdf.format(date);//时间存储为字符串
	    Timestamp starttime = Timestamp.valueOf(str);//设置起时间
		List<Map<String,Object>> hwList = hw_di.findByHomework(parameterStringMap);//stuid
		List<Map<String,Object>> conditionList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < hwList.size(); i++) {
			List<Map<String,Object>> hwTypeList = hw_di.findByHwType(hwList.get(i));
			Map<String,Object> homework = new HashMap<String,Object>();
			 homework.put("title", "作业");
			 homework.put("finishTime", hwList.get(i).get("end"));
			 homework.put("content_title", hwList.get(i).get("homewordName"));
			 homework.put("hwid", hwList.get(i).get("hwid"));
			 hwList.get(i).put("stuid", parameterStringMap.get("stuid"));
			 Map<String, Object> hwStu = hw_di.findStuHomework(hwList.get(i));
			 List<Map<String, Object>> hwStuTypeList = hw_di.findStuHomeWorkType2(hwStu);
			 Timestamp end =  (Timestamp) hwList.get(i).get("endTime");
			 if( hwList.get(i).containsKey("endTime") && end.after(starttime)) {//未批阅
				 homework.put("finishSpeed",hwStuTypeList.size() + "/" + hwTypeList.size() );
				 if( hwStu == null || hwStu.isEmpty()) {
					 homework.put("finishState", "0");//未完成
				 }else{
					 if(hwStu.containsKey("endTime")==false) {
						 homework.put("finishState", "0");//未完成
					 }else {
						 homework.put("finishState", "1");//未批阅
					 }
				 }
			 }else {//已批阅
				 homework.put("finishSpeed",hwStuTypeList.size() + "/" + hwTypeList.size() );
				 homework.put("finishState", "2");//已批阅
			 }
			 
			 conditionList.add(homework);
		}
		return conditionList;
			 
			
	}
	//作业情况
	public List<Map<String,Object>> findByHwCondition(Map parameterStringMap){
		
		List<Map<String, Object>> linshiList = bs_di.findLinAll(parameterStringMap);
		return linshiList;
	}
	
	public List<Map<String,Object>> findByHwCondition1(Map parameterStringMap){//stuid
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String str = sdf.format(date);//时间存储为字符串
	    Timestamp starttime = Timestamp.valueOf(str);//设置起时间
		List<Map<String,Object>> conditionList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> hwList = hw_di.findByHomework(parameterStringMap);//stuid
		for (int i = 0; i < hwList.size(); i++) {
			List<Map<String,Object>> hwTypeList = hw_di.findByHwType(hwList.get(i));
			Map<String,Object> homework = new HashMap<String,Object>();
			hwList.get(i).put("stuid", parameterStringMap.get("stuid"));
			 Map<String, Object> stuhw = hw_di.findStuHomework(hwList.get(i));
			 homework.put("scoreAll", "0");
			 if(stuhw != null && stuhw.containsKey("scoreAll")) {
				 homework.put("scoreAll", stuhw.get("scoreAll"));
			 }
			 homework.put("startTime", hwList.get(i).get("start"));
			 homework.put("title", "作业");
			 homework.put("finishTime", hwList.get(i).get("end"));
			 homework.put("content_title", hwList.get(i).get("homewordName"));
			 homework.put("hwid", hwList.get(i).get("hwid"));
			 hwList.get(i).put("stuid", parameterStringMap.get("stuid"));
			 Map<String, Object> hwStu = hw_di.findStuHomework(hwList.get(i));
			 List<Map<String, Object>> hwStuTypeList = hw_di.findStuHomeWorkType2(hwStu);
			 Timestamp end =  (Timestamp) hwList.get(i).get("endTime");
			 
			 if(hwStuTypeList == null || hwStuTypeList.isEmpty()) {
				 homework.put("finishCount","0"); 
			 }else {
				 homework.put("finishCount",hwStuTypeList.size());
			 }
			 homework.put("allCount", hwTypeList.size() );
			 homework.put("finishSpeed",hwStuTypeList.size() + "/" + hwTypeList.size() );
			 if( hwList.get(i).containsKey("endTime") && end.after(starttime)) {//未批阅
				
				 if( hwStu == null || hwStu.isEmpty()) {
					 homework.put("finishState", "0");//待完成
				 }else{
					 if(hwStu.containsKey("endTime")==false) {
						 homework.put("finishState", "0");//待完成
					 }else {
						 homework.put("finishState", "1");//待批阅
					 }
				 }
			 }else {//已批阅
				 homework.put("finishSpeed",hwStuTypeList.size() + "/" + hwTypeList.size() );
				 homework.put("finishState", "2");//已批阅
			 }
			 
			 conditionList.add(homework);
		}
		List<Map<String, Object>>  symbolList = bs_di.findBySymbolAll(parameterStringMap);//音标
		for (int i = 0; i < symbolList.size(); i++) {
			symbolList.get(i).put("title", "音标");
			symbolList.get(i).put("finishState", "3");//已完成
			symbolList.get(i).put("hw_type", "0");
			conditionList.add(symbolList.get(i));
		}
		List<Map<String, Object>> wordList = bs_di.findByWordType(parameterStringMap);//单词
		for (int i = 0; i < wordList.size(); i++) {
			wordList.get(i).put("hw_type", "2");
			wordList.get(i).put("title", "单词");
			wordList.get(i).put("finishState", "3");
			
			conditionList.add(wordList.get(i));
		}
		List<Map<String, Object>> phraseList = bs_di.findByPhraseType(parameterStringMap);
		for (int i = 0; i < phraseList.size(); i++) {
			phraseList.get(i).put("title", "短语");
			phraseList.get(i).put("hw_type", "8");
			phraseList.get(i).put("finishState", "3");
			
			conditionList.add(phraseList.get(i));
		}
		List<Map<String, Object>> talkList = bs_di.findByTalkType(parameterStringMap);//stuid
		for (int i = 0; i < talkList.size(); i++) {
			talkList.get(i).put("title", "对话");
			talkList.get(i).put("hw_type", "4");
			talkList.get(i).put("finishState", "3");
			
			conditionList.add(talkList.get(i));
		}
		List<Map<String, Object>> textList = bs_di.findByTextType(parameterStringMap);//课文
		
		for (int i = 0; i < textList.size(); i++) {
			textList.get(i).put("title", "课文");
			textList.get(i).put("hw_type", "1");
			textList.get(i).put("finishState", "3");
			
			conditionList.add(textList.get(i));
		}
		List<Map<String, Object>> sentenceList = bs_di.findBySentenceType(parameterStringMap);
		for (int i = 0; i < sentenceList.size(); i++) {
			sentenceList.get(i).put("title", "句子");
			sentenceList.get(i).put("hw_type", "3");
			sentenceList.get(i).put("finishState", "3");
			
			conditionList.add(sentenceList.get(i));
		}
		List<Map<String, Object>> readList = bs_di.findByReadType(parameterStringMap);
		
		for (int i = 0; i < readList.size(); i++) {
			readList.get(i).put("title", "阅读");
			readList.get(i).put("hw_type", "6");
			readList.get(i).put("finishState", "3");
			
			conditionList.add(readList.get(i));
		}
		List<Map<String, Object>> ListenList = bs_di.findByListenType(parameterStringMap);
		for (int i = 0; i < ListenList.size(); i++) {
			ListenList.get(i).put("title", "听力");
			ListenList.get(i).put("hw_type", "5");
			ListenList.get(i).put("finishState", "3");
			ListenList.get(i).put("listen_type", ListenList.get(i).get("listen_type"));
			ListenList.get(i).put("Relative_path", ListenList.get(i).get("save")+"/" + Const.TINGLI + ListenList.get(i).get("hwid") + "/");
			conditionList.add(ListenList.get(i));
		}
		
		bs_di.delLinShiTable(parameterStringMap);//删除临时表的所有数据
		for (int i = 0; i < conditionList.size(); i++) {
			conditionList.get(i).put("stuid", parameterStringMap.get("stuid"));
			bs_di.saveLinshiTable(conditionList.get(i));//添加临时表数据
		}
		 
		return conditionList;
	}
	
	
	
	// 作业情况  详情页面  子页面
	public List<Map<String,Object>> findByHomeworkDetail(Map parameterStringMap){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> flag = new HashMap<String,Object>();
		if(parameterStringMap.get("finishState").equals("0")) {
			flag.put("flag", "您的孩子尚未完成作业，请尽快完成作业");
			list.add(flag);
			return list;
		}else if(parameterStringMap.get("finishState").equals("1")) {
			flag.put("flag","老师未批阅，请老师批阅后查看");
			list.add(flag);
			return list;
		}else {
			if(parameterStringMap.get("title").equals("作业")) {
				//总题数
				//进入该作业的子页面
			    list = hw_di.findByHwType(parameterStringMap);//hwid

			    for (int i = 0; i < list.size(); i++) {
					double avgScore = Double.valueOf(String.format("%.2f", (double)100.00/list.size()));
					list.get(i).put("avgScore", avgScore);
					if(list.get(i).get("hw_type").equals("5")) {
						list.get(i).put("listen_id", list.get(i).get("hw_content"));
						List<Map<String, Object>> listenListHw = text_di.findByListenById1(list.get(i));
						list.get(i).put("listen_type", listenListHw.get(0).get("listen_type"));
					}
					//
					Map<String, Object> hw1 = hw_di.findStuHomework(parameterStringMap);
					
					if(hw1 !=null && !hw1.isEmpty()) {
						list.get(i).put("stu_hw_id", (String)hw1.get("stu_hw_id"));
						Map<String, Object> hw2 = hw_di.findStuHomeWorkType(list.get(i));
						if(hw2 !=null && !hw2.isEmpty()) {
							list.get(i).put("score", hw2.get("score"));
						}else {
							list.get(i).put("score", "0");
						}	
					}else {
						list.get(i).put("score", "0");
					}
			    }
			 }else if(parameterStringMap.get("title").equals("音标")) {
				list = bs_di.findBySymbolDetail(parameterStringMap);//stuid;ybid
				for (int i = 0; i < list.size(); i++) {
					list.get(i).put("Relative_path", "yinbiao"+ "/" + parameterStringMap.get("hwid") + "/");
					list.get(i).put("stuLearn_path", "learn" + "/" + parameterStringMap.get("stuid") + "/" + "yb_wordId" + "/" + list.get(i).get("yb_wordId") +"/");
				}
			}else if(parameterStringMap.get("title").equals("单词")) {
				list = bs_di.findByWordDetail(parameterStringMap);//type;stuid
				for (int i = 0; i < list.size(); i++) {
					list.get(i).put("Relative_path", list.get(i).get("save")+"/" + Const.DANCI + parameterStringMap.get("hwid") + "/");
					list.get(i).put("stuLearn_path", "learn" + "/" + parameterStringMap.get("stuid") + "/" + "word" + "/" + list.get(i).get("word_id") + "/");

				}
			}else if(parameterStringMap.get("title").equals("短语")) {
				list = bs_di.findByPhraseDetail(parameterStringMap);
				for (int i = 0; i < list.size(); i++) {
					list.get(i).put("Relative_path", list.get(i).get("save")+"/" + Const.DUANYU + parameterStringMap.get("hwid") + "/");
					list.get(i).put("stuLearn_path", "learn" + "/" + parameterStringMap.get("stuid") + "/" + "phrase" + "/" + list.get(i).get("phrase_id") + "/");

				}
			}else if(parameterStringMap.get("title").equals("对话")) {
				list = bs_di.findByTalkDetail(parameterStringMap);
				for (int i = 0; i < list.size(); i++) {
					list.get(i).put("Relative_path", list.get(i).get("save")+"/" + Const.DUIHUA + parameterStringMap.get("hwid") + "/");
					list.get(i).put("stuLearn_path", "learn" + "/" + parameterStringMap.get("stuid") + "/" + "talk" + "/" + list.get(i).get("juese_id") + "/");

				}
			}else if(parameterStringMap.get("title").equals("课文")) {
				list = bs_di.findByTextDetail(parameterStringMap);
				for (int i = 0; i < list.size(); i++) {
					list.get(i).put("Relative_path", list.get(i).get("save")+"/" + Const.KEWEN + parameterStringMap.get("hwid") + "/");
					list.get(i).put("stuLearn_path", "learn" + "/" + parameterStringMap.get("stuid") + "/" + "text" + "/" + list.get(i).get("text_id") + "/");

				}
			}else if(parameterStringMap.get("title").equals("句子")) {
				list = bs_di.findBySentenceDetail(parameterStringMap);
				for (int i = 0; i < list.size(); i++) {
					list.get(i).put("Relative_path", list.get(i).get("save")+"/" + Const.JVZI + parameterStringMap.get("hwid") + "/");
					list.get(i).put("stuLearn_path", "learn" + "/" + parameterStringMap.get("stuid") + "/" + "sentence" + "/" + list.get(i).get("sentence_sid") + "/");

				}
			}else if(parameterStringMap.get("title").equals("阅读")) {
				list = bs_di.findByReadDetail(parameterStringMap);
				for (int i = 0; i < list.size(); i++) {
					List<Map<String,Object>> optionList = bs_di.findByReadOption(list.get(i));
					if(!optionList.isEmpty() && optionList !=null) {
						list.get(i).put("optionList", optionList);
					}
				}
			}else if(parameterStringMap.get("title").equals("听力")) {
				list = bs_di.findByListenDetail(parameterStringMap);
				for (int i = 0; i < list.size(); i++) {
					List<Map<String,Object>> optionList = bs_di.findByListenOption(list.get(i));
					if(!optionList.isEmpty() && optionList !=null) {
						list.get(i).put("optionList", optionList);
					}
				}
			}
			
			return list;
			
		}
			
	}
	
	//查看作业
	//作业情况  详情页面 
	public 	List<Map<String,Object>> findDetail(Map<String,String> parameterStringMap){
		
		List<Map<String,Object>> symbollist = null;
		List<Map<String,Object>> wordlist = null;
		List<Map<String,Object>> textlist = null;
		List<Map<String,Object>> readlist = null;
		List<Map<String,Object>> listenlist = null;
		List<Map<String,Object>> phraselist = null;
		List<Map<String,Object>> talklist = null;
		List<Map<String,Object>> sentencelist = null;
		List<Map<String,Object>> typeList = new ArrayList<Map<String,Object>>();
		Map<String, Object> all = hw_di.findStuHomework(parameterStringMap);//stuid;hwid
	    List< Map<String, Object>> list = hw_di.findByHwType(parameterStringMap);//hwid
		for (int i = 0; i < list.size(); i++) {
				Map<String, Object> save = hw_di.findByType(list.get(i));
				String hw_type = (String) list.get(i).get("hw_type");
				String hw_content = (String) list.get(i).get("hw_content");
				Map<String,String> map = new HashMap<String,String>();
				if(all != null && all.containsKey("stu_hw_id")) {
				list.get(i).put("stu_hw_id", all.get("stu_hw_id"));
				}
				
				Map<String, Object> stuhw = hw_di.findStuHomeWorkType(list.get(i));//stu_hw_id;hw_type;hw_content
				
				if(hw_type.equals("0")) {//音标  flag  ；
					
					map.put("ybid", hw_content);
					String flag = parameterStringMap.get("flag");
					map.put("flag", flag);
					symbollist = text_di.findByFayinshili(map);
					for (int j = 0; j < symbollist.size(); j++) {
						Map<String,Object> symbol = new HashMap<String,Object>();
						String homework_path = "homework" + "/" + 
					            parameterStringMap.get("hwid") + "/"+ 
								parameterStringMap.get("stuid") + "/"+ 
								hw_type+"/"+hw_content+"/"+symbollist.get(j).get("hw_answerId")+"/";
						symbol.put("stuLearn_path", homework_path);
						symbol.put("standard_example", symbollist.get(j).get("yb_word"));
						symbol.put("standard_explain", symbollist.get(j).get("yb_translate"));
						symbol.put("standard_video", symbollist.get(j).get("yb_Avideo"));
						String save_path = "yinbiao" + "/" +hw_content +"/" ;
						symbol.put("Relative_path", save_path);
						if(stuhw !=null && stuhw.containsKey("hw_contentid")) {
							symbollist.get(j).put("hw_contentid", stuhw.get("hw_contentid"));
							Map<String, Object>  answer = hw_di.findStuHomeWorkAnswer(symbollist.get(j));			
							if(answer !=null && !answer.isEmpty()) {
								symbol.put("learn_video", answer.get("hw_video"));
								symbol.put("learn_score", answer.get("hw_score"));
								
							}
						}
						
						typeList.add(symbol);
					}
					
					list.get(i).put("typeList", typeList);
					list.get(i).put("size", i+1);
					
				}else if(hw_type.equals("1")) {//课文
					map.put("text_id", hw_content);
					textlist = text_di.findByTextById(map);
					for (int j = 0; j < textlist.size(); j++) {
						Map<String,Object> text = new HashMap<String,Object>();
						String homework_path = "homework" + "/" + 
					            parameterStringMap.get("hwid") + "/"+ 
								parameterStringMap.get("stuid") + "/"+ 
								hw_type+"/"+hw_content+"/"+textlist.get(j).get("hw_answerId")+"/";
						
						text.put("stuLearn_path", homework_path);
						text.put("standard_example", textlist.get(j).get("text_yw"));
						text.put("standard_explain", textlist.get(j).get("text_zw"));
						text.put("standard_video", textlist.get(j).get("text_video"));
						String save_path=save.get("save")+"/" + Const.KEWEN + hw_content + "/";
						text.put("Relative_path", save_path);
						if(stuhw !=null && stuhw.containsKey("hw_contentid")) {
							textlist.get(j).put("hw_contentid", stuhw.get("hw_contentid"));
							Map<String, Object>  answer = hw_di.findStuHomeWorkAnswer(textlist.get(j));
	                        if(answer !=null && !answer.isEmpty()) {
	                        	text.put("learn_video", answer.get("hw_video"));
	                        	text.put("learn_score", answer.get("hw_score"));
							}
						}
						typeList.add(text);
					}
					
					
					list.get(i).put("typeList", typeList);
					
				}else if(hw_type.equals("2")) {//单词
					map.put("flag", "word");
					map.put("type",hw_content);
					wordlist = text_di.findByDyByWord(map);
					for (int j = 0; j < wordlist.size(); j++) {
						Map<String,Object> word = new HashMap<String,Object>();
						String homework_path = "homework" + "/" + 
					            parameterStringMap.get("hwid") + "/"+ 
								parameterStringMap.get("stuid") + "/"+ 
								hw_type+"/"+hw_content+"/"+wordlist.get(j).get("hw_answerId")+"/";
						
						word.put("stuLearn_path", homework_path);
						word.put("standard_example", wordlist.get(j).get("word"));
						word.put("standard_explain", wordlist.get(j).get("word_tran"));
						word.put("standard_video", wordlist.get(j).get("word_video"));
						String save_path=save.get("save")+"/" + Const.DANCI+ hw_content + "/";
						word.put("Relative_path", save_path);
						if(stuhw !=null && stuhw.containsKey("hw_contentid")) {
							wordlist.get(j).put("hw_contentid", stuhw.get("hw_contentid"));
							Map<String, Object>  answer = hw_di.findStuHomeWorkAnswer(wordlist.get(j));
	                        if(answer !=null && !answer.isEmpty()) {
	                        	word.put("learn_video", answer.get("hw_video"));
	                        	word.put("learn_score", answer.get("hw_score"));
							}
						}
						
						typeList.add(word);
					}
					
					list.get(i).put("typeList", typeList);
					
				}else if(hw_type.equals("3")) {//句子
					map.put("sentence_id", hw_content);
					sentencelist =text_di.findBySentenceById(map);
				    for (int j = 0; j < sentencelist.size(); j++) {
				    	Map<String,Object> sentence = new HashMap<String,Object>();
				    	String homework_path = "homework" + "/" + 
					            parameterStringMap.get("hwid") + "/"+ 
								parameterStringMap.get("stuid") + "/"+ 
								hw_type+"/"+hw_content+"/"+sentencelist.get(j).get("hw_answerId")+"/";
				    	
				    	sentence.put("stuLearn_path", homework_path);
				    	sentence.put("standard_example", sentencelist.get(j).get("sentence_yw"));
				    	sentence.put("standard_explain", sentencelist.get(j).get("sentence_zw"));
				    	sentence.put("standard_video", sentencelist.get(j).get("sentence_video"));
				    	String save_path=save.get("save")+"/" + Const.JVZI + hw_content + "/";
				    	sentence.put("Relative_path", save_path);
						
				    	if(stuhw !=null && stuhw.containsKey("hw_contentid")) {
				    		sentencelist.get(j).put("hw_contentid", stuhw.get("hw_contentid"));
				    		Map<String, Object>  answer = hw_di.findStuHomeWorkAnswer(sentencelist.get(j));
	                        if(answer !=null && !answer.isEmpty()) {
	                        	sentence.put("learn_video", answer.get("hw_video"));
	                        	sentence.put("learn_score", answer.get("hw_score"));
							}
				    	}
				    	typeList.add(sentence);
					}
				  
					list.get(i).put("typeList", typeList);
					
				}else if(hw_type.equals("4")) {//对话
					map.put("talk_id", hw_content);
					talklist = text_di.findByTalkById(map);
					for (int j = 0; j < talklist.size(); j++) {
						Map<String,Object> talk = new HashMap<String,Object>();
						String homework_path = "homework" + "/" + 
					            parameterStringMap.get("hwid") + "/"+ 
								parameterStringMap.get("stuid") + "/"+ 
								hw_type+"/"+hw_content+"/"+talklist.get(j).get("hw_answerId")+"/";
						
						talk.put("stuLearn_path", homework_path);
						talk.put("standard_example", talklist.get(j).get("juese_yw"));
						talk.put("standard_explain", talklist.get(j).get("juese_zw"));
						talk.put("standard_video", talklist.get(j).get("juese_video"));
						String save_path=save.get("save")+"/" + Const.DUIHUA + hw_content + "/";
						talk.put("Relative_path", save_path);
						
						if(stuhw !=null && stuhw.containsKey("hw_contentid")) {
							talklist.get(j).put("hw_contentid", stuhw.get("hw_contentid"));
							Map<String, Object>  answer = hw_di.findStuHomeWorkAnswer(talklist.get(j));
							if(answer !=null && !answer.isEmpty()) {
								talk.put("learn_video", answer.get("hw_video"));
								talk.put("learn_score", answer.get("hw_score"));
							}
						}
						typeList.add(talk);
					}
					
					list.get(i).put("typeList", typeList);
					
				}else if(hw_type.equals("5")) {//听力 listen_type填空
					map.put("listen_id", hw_content);
					List<Map<String, Object>> standard_contentList =   text_di.findByListenById1(map);
					listenlist =   text_di.findByListenById2(map);
					for (int j = 0; j < listenlist.size(); j++) {
						Map<String,Object> listen = new HashMap<String,Object>();
						
						listen.put("standard_example", listenlist.get(j).get("listen_answer"));
						listen.put("standard_explain", listenlist.get(j).get("listen_resolve"));
						listen.put("standard_video", listenlist.get(j).get("listen_questVideo"));
						listenlist.get(j).put("listen_qid", listenlist.get(j).get("listen_questId"));
						String save_path=save.get("save")+"/" + Const.TINGLI + hw_content + "/";
						listen.put("Relative_path", save_path);
						List<Map<String,Object>> optionList = bs_di.findByListenOption(listenlist.get(j));
						if(!optionList.isEmpty() && optionList !=null) {
							listen.put("optionList", optionList);
						}
						if(stuhw !=null && stuhw.containsKey("hw_contentid")) {
							listenlist.get(j).put("hw_contentid", stuhw.get("hw_contentid"));
							Map<String, Object>  answer = hw_di.findStuHomeWorkAnswer(listenlist.get(j));
						    if(answer !=null && !answer.isEmpty()) {
						    	listen.put("learn_video", answer.get("hw_video"));
						    	listen.put("learn_score", answer.get("hw_score"));
							}
						}
						typeList.add(listen);
					}
				     list.get(i).put("standard_content", standard_contentList.get(0).get("listen_content"));
				     list.get(i).put("standard_video", standard_contentList.get(0).get("listen_video"));
				     
					list.get(i).put("typeList", typeList);
					
					
				}else if(hw_type.equals("6")) {//阅读 
					map.put("read_id", hw_content);
					List<Map<String, Object>> read_contentList = text_di.findByReadById1(map);
					readlist = text_di.findByReadById2(map);
					for (int j = 0; j < readlist.size(); j++) {
						Map<String,Object> read = new HashMap<String,Object>();
						
						read.put("standard_example", readlist.get(j).get("read_answer"));
						read.put("standard_explain", readlist.get(j).get("read_resolve"));
						read.put("standard_video", readlist.get(j).get("read_question"));
						List<Map<String,Object>> optionList = bs_di.findByReadOption(readlist.get(i));
						if(!optionList.isEmpty() && optionList !=null) {
							read.put("optionList", optionList);
						}
						if(stuhw !=null && stuhw.containsKey("hw_contentid")) {
							readlist.get(j).put("hw_contentid", stuhw.get("hw_contentid"));
							Map<String, Object>  answer = hw_di.findStuHomeWorkAnswer(readlist.get(j));
	                        if(answer !=null && !answer.isEmpty()) {
	                        	read.put("learn_video", answer.get("hw_video"));
	                        	read.put("learn_score", answer.get("hw_score"));
							}
						}
						
						typeList.add(read);
					}
					list.get(i).put("typeList", typeList);
					if(read_contentList !=null ) {
						list.get(i).put("standard_content", read_contentList.get(0).get("read_content"));
					}
					
					
				}else if(hw_type.equals("7")) {//听力选择
					
					
				}else if(hw_type.equals("8")) {//短语
					map.put("flag", "phrase");
					map.put("type",hw_content);
					wordlist = text_di.findByDyByPhrase(map);
					for (int j = 0; j < wordlist.size(); j++) {
						Map<String,Object> phrase = new HashMap<String,Object>();
						String homework_path = "homework" + "/" + 
					            parameterStringMap.get("hwid") + "/"+ 
								parameterStringMap.get("stuid") + "/"+ 
								hw_type+"/"+hw_content+"/"+wordlist.get(j).get("hw_answerId")+"/";
						
						phrase.put("stuLearn_path", homework_path);
						phrase.put("standard_example", wordlist.get(j).get("phrase"));
						phrase.put("standard_explain", wordlist.get(j).get("phrase_tran"));
						phrase.put("standard_video", wordlist.get(j).get("phrase_video"));
						String save_path=save.get("save")+"/" + Const.DUANYU+ hw_content + "/";
						phrase.put("Relative_path", save_path);
						if(stuhw !=null && stuhw.containsKey("hw_contentid")) {
							wordlist.get(j).put("hw_contentid", stuhw.get("hw_contentid"));
							Map<String, Object>  answer = hw_di.findStuHomeWorkAnswer(wordlist.get(j));
							if(answer !=null && !answer.isEmpty()) {
								phrase.put("learn_video", answer.get("hw_video"));
								phrase.put("learn_score", answer.get("hw_score"));
							}
						}
						typeList.add(phrase);
					}
					
					list.get(i).put("typeList", typeList);
					
				}
			
			}
		
		
		 return list;
	}
	
	//本周作业统计及展示
	public Map<String,Object> findByWeekHwShow(Map<String,String> parameterStringMap){
		Map<String,Object> hashMap = new HashMap<String,Object>();
		Map<String, Object> stuMap = stu_di.findByStuid(parameterStringMap);
		List<Map<String, Object>> stuHomeworkList = bs_di.countOneWeekFinish(parameterStringMap);//stuid
		List<Map<String, Object>> homeworkList = bs_di.countOneWeekAll(parameterStringMap);
		int a = homeworkList.size();
		int b = stuHomeworkList.size();
		double score = 0;
		if(stuHomeworkList !=null && !stuHomeworkList.isEmpty()) {
			for (int i = 0; i < stuHomeworkList.size(); i++) {
				
				double scoreAll = (double) stuHomeworkList.get(i).get("scoreAll");
				score += scoreAll;
			}
		}
		
		if(a == 0 ) {
			hashMap.put("pingjia",stuMap.get("student")+ 
					"本周共完成"+a+"项作业，"+a+"项作业未完成，平均成绩"+0
					+"分。希望能够继续努力，取得更优秀的成绩");
		}else if(b>a && a !=0) {
			hashMap.put("pingjia",stuMap.get("student")+ 
					"本周共完成"+a+"项作业，"+a+"项作业未完成，平均成绩"+Double.valueOf(String.format("%.2f", (Double)(score/a)))
					+"分。希望能够继续努力，取得更优秀的成绩");
		}
		else {
			hashMap.put("pingjia",stuMap.get("student")+ 
					"本周共完成"+b+"项作业，"+(a-b)+"项作业未完成，平均成绩"+Double.valueOf(String.format("%.2f", (Double)(score/a)))
					+"分。希望能够继续努力，取得更优秀的成绩");
		}
		
		
		if(homeworkList != null && !homeworkList.isEmpty()) {
			for (int i = 0; i < homeworkList.size(); i++) {
				parameterStringMap.put("hwid", (String) homeworkList.get(i).get("hwid"));
				List<Map<String, Object>> studentOne = bs_di.findByStuHomework(parameterStringMap);
				homeworkList.get(i).put("flag", "已完成");
					
			}
			hashMap.put("detail", homeworkList);
		}
	
		return hashMap;
	}
	
	
}
