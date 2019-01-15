package com.xljy.student.text.servers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xljy.student.text.dao.Homework_di;
import com.xljy.student.text.dao.StuText_di;
import com.xljy.student.user.dao.StuLogin_di;
import com.xljy.util.Const;

import cn.hutool.core.io.FileUtil;

@Service
public class Homework_sm {

	@Autowired
	private Homework_di hw_di;
	@Autowired
	private StuText_sm text_sm;
	@Autowired
	private StuText_di text_di;
	@Autowired
	private StuLogin_di login_di;
	
	//进入作业
	public List<Map<String,Object>> findByHomework(Map parameterStringMap){
		List<Map<String,Object>> list = null;
		list = hw_di.findByHomework(parameterStringMap);//根据班级代码查询班级作业
		
		if(list !=null) {
			//判断是否存在学生完成的作业
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("stuid", parameterStringMap.get("stuid"));
				Date now = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
				String sysdate = dateFormat.format(now); 
				Date startTime = (Date) list.get(i).get("startTime");
				Date endTime = (Date) list.get(i).get("endTime");
				if(now.before(startTime)) {
					list.get(i).put("flag", "未开始");
				}else if(now.after(endTime)) {
					list.get(i).put("flag", "已结束");
				}else {
					list.get(i).put("flag", "进行中");
				}
				Map<String, Object> map =hw_di.findStuHomework(list.get(i));
				if(map == null || map.isEmpty()) {
					list.get(i).put("scoreAll", "待完成");
				}else if(map !=null && map.containsKey("endTime")== false){
					list.get(i).put("scoreAll", "0");
				}else {
					list.get(i).put("scoreAll", map.get("scoreAll"));
				}
		}	
		}
		return list;
	}
	
	//进入作业子页面
	public Map<String,Object> findByHomeworkTwo(Map<String, String> parameterStringMap){
		Map<String,Object> hashMap = new HashMap<String,Object>();
		
		List<Map<String,Object>> list = hw_di.findByHwType(parameterStringMap);//hwid
		if(parameterStringMap.get("flag").equals("未开始") || parameterStringMap.get("flag").equals("已结束")) {
			hashMap.put("tips", "作业未开始或已结束，不可做作业");
		}else if(parameterStringMap.get("scoreAll").equals("待完成")) {
			hashMap.put("tips", "点击进去完成作业");
		}else if(parameterStringMap.get("scoreAll").equals("0")) {
			hashMap.put("tips", "点击进去继续完成作业");
		}else {
			//判断作业是一次的  还是   n次的
			if(parameterStringMap.get("homework_cishu").equals("1")) {
				hashMap.put("tips", "本作业只能完成一次");
			}else if(parameterStringMap.get("homework_cishu").equals("0")) {
				hashMap.put("tips", "点击重做作业");
			}
		}
		
		
		for (int i = 0; i < list.size(); i++) {
			double avgScore = Double.valueOf(String.format("%.2f", (double)100.00/list.size()));
			list.get(i).put("avgScore", avgScore);
			list.get(i).put("listenType", "");
			list.get(i).put("score", "");
			//
			Map<String, Object> hw1 = hw_di.findStuHomework(parameterStringMap);
			if(hw1 !=null && !hw1.isEmpty()) {
				list.get(i).put("stu_hw_id", (String)hw1.get("stu_hw_id"));
				Map<String, Object> hw2 = hw_di.findStuHomeWorkType(list.get(i));
				if(hw2 !=null && !hw2.isEmpty()) {
					list.get(i).put("score", hw2.get("score"));
				}
			}
			if(list.get(i).get("hw_type").equals("5")) {
				Map<String,Object> map =new HashMap<String,Object>();
				map.put("listen_id", list.get(i).get("hw_content"));
				List<Map<String, Object>> con = text_di.findByListenById1(map);
				list.get(i).put("listenType", con.get(0).get("listen_type"));
			}
		}
		hashMap.put("homeworkType", list);
		return hashMap;
	}
	
	//作业详情
	public Map<String,Object> findByHwId(Map<String, String> parameterStringMap){
		Map<String,Object> all =new HashMap<String,Object>();
		List<Map<String,Object>> list = null;
		List<Map<String,Object>> symbollist = null;
		List<Map<String,Object>> wordlist = null;
		List<Map<String,Object>> textlist = null;
		List<Map<String,Object>> readlist = null;
		List<Map<String,Object>> listenlist = null;
		List<Map<String,Object>> phraselist = null;
		List<Map<String,Object>> talklist = null;
		List<Map<String,Object>> sentencelist = null;
		
		list = hw_di.findByHwType(parameterStringMap);//hwid;hw_type;hw_content
		String hw_type = parameterStringMap.get("hw_type");//类型
	    String hw_content = parameterStringMap.get("hw_content");//题目id
       
		for (int i = 0; i < list.size(); i++) {
		     
			Map<String, String> map =new HashMap<String, String>();
			Map<String, Object> save = hw_di.findByType(list.get(i));
			
			if(hw_type.equals("0")) {//音标  flag  ；
			
				String save_path = "yinbiao" + "/" +hw_content +"/" ;
				all.put("Relative_path", save_path);
				map.put("ybid", hw_content);
				String flag = parameterStringMap.get("flag");
				map.put("flag", flag);
				if(parameterStringMap.containsKey("shiliType") && parameterStringMap.get("shiliType").equals("1")) {
					map.put("shiliType", parameterStringMap.get("shiliType"));
				}
				symbollist = text_sm.findBySymbol(map);
				for (int j = 0; j < symbollist.size(); j++) {
					double everyScore = Double.valueOf(String.format("%.2f", (Double.valueOf( parameterStringMap.get("avgScore")) )/ (symbollist.size())));
					symbollist.get(j).put("everyScore", everyScore);
				}
				all.put("typeList", symbollist);
				
			}else if(hw_type.equals("1")) {//课文
				
				String save_path=save.get("save")+"/" + Const.KEWEN + hw_content + "/";
				all.put("Relative_path", save_path);
				map.put("text_id", hw_content);
				textlist = text_sm.detail(map);
				for (int j = 0; j < textlist.size(); j++) {
					double everyScore = Double.valueOf(String.format("%.2f", (Double.valueOf( parameterStringMap.get("avgScore"))) / (textlist.size())));
					textlist.get(j).put("everyScore", everyScore);
				}
				all.put("typeList", textlist);
				
			}else if(hw_type.equals("2")) {//单词
				
				String save_path=save.get("save")+"/" + Const.DANCI+ hw_content + "/";
				all.put("Relative_path", save_path);
				map.put("flag", "word");
				map.put("type",hw_content);
				wordlist = text_sm.findByDy(map);
				for (int j = 0; j < wordlist.size(); j++) {
					double everyScore = Double.valueOf(String.format("%.2f", (Double.valueOf( parameterStringMap.get("avgScore"))) / (wordlist.size())));
					wordlist.get(j).put("everyScore",everyScore);
				}
				all.put("typeList", wordlist);
				
			}else if(hw_type.equals("3")) {//句子
				
				String save_path=save.get("save")+"/" + Const.JVZI + hw_content + "/";
				all.put("Relative_path", save_path);
				map.put("sentence_id", hw_content);
				sentencelist = text_sm.detail(map);
				for (int j = 0; j < sentencelist.size(); j++) {
					double everyScore = Double.valueOf(String.format("%.2f", (Double.valueOf( parameterStringMap.get("avgScore"))) / (sentencelist.size())));
					sentencelist.get(j).put("everyScore", everyScore);
				}
				all.put("typeList", sentencelist);
				
			}else if(hw_type.equals("4")) {//对话
				
				String save_path=save.get("save")+"/" + Const.DUIHUA + hw_content + "/";
				all.put("Relative_path", save_path);
				map.put("talk_id", hw_content);
				talklist = text_sm.detail(map);
				for (int j = 0; j < talklist.size(); j++) {
					double everyScore = Double.valueOf(String.format("%.2f", (Double.valueOf( parameterStringMap.get("avgScore"))) / (talklist.size())));
					talklist.get(j).put("everyScore",everyScore);
				}
				all.put("typeList", talklist);
				
			}else if(hw_type.equals("5")) {//听力 listen_type
				
				String save_path=save.get("save")+"/" + Const.TINGLI + hw_content + "/";
				all.put("Relative_path", save_path);
				map.put("listen_id", hw_content);
				listenlist = text_sm.detail(map);
				for (int j = 0; j < listenlist.size(); j++) {
					List<Map> listen_questionList = (List<Map>)listenlist.get(j).get("listen_questionList");
					for (int k = 0; k < listen_questionList.size(); k++) {
						double everyScore = Double.valueOf(String.format("%.2f", (Double.valueOf( parameterStringMap.get("avgScore"))) / (listen_questionList.size())));
						listen_questionList.get(k).put("everyScore", everyScore);
					}
					
				}
				all.put("typeList", listenlist);
				
			}else if(hw_type.equals("6")) {//阅读 
				
				String save_path=save.get("save")+"/" + Const.YUEDU + hw_content + "/";
				all.put("Relative_path",save_path);
				map.put("read_id", hw_content);
				readlist = text_sm.detail(map);
				for (int j = 0; j < readlist.size(); j++) {
					List<Map> read_questionList = (List<Map>)readlist.get(j).get("read_questionList");
					for (int k = 0; k < read_questionList.size(); k++) {
						double everyScore = Double.valueOf(String.format("%.2f", (Double.valueOf( parameterStringMap.get("avgScore"))) / (read_questionList.size())));
						read_questionList.get(k).put("everyScore", everyScore);
					}
				}
				all.put("typeList", readlist);
				
			}else if(hw_type.equals("7")) {//作文
				
			}else if(hw_type.equals("8")) {//短语
				
				String save_path=save.get("save")+"/" + Const.DUANYU+ hw_content + "/";
				all.put("Relative_path", save_path);
				map.put("flag", "phrase");
				map.put("type",hw_content);
				phraselist = text_sm.findByDy(map);
				for (int j = 0; j < phraselist.size(); j++) {
					double everyScore = Double.valueOf(String.format("%.2f", (Double.valueOf( parameterStringMap.get("avgScore"))) / (phraselist.size())));
					phraselist.get(j).put("everyScore", everyScore);
				}
				all.put("typeList", phraselist);
				
			}
			
		}
		
		return all;
	}
	
	
	//查看此学生本次作业下的所有答案
	public  Map<String, Object> findByHomeWork(Map<String, String> parameterStringMap){
		
		 Map<String, Object> list = this.findByHwId(parameterStringMap);//stuid;hwid
		 
		
        	 Map<String, Object> contentScore = hw_di.findByContentByScore(list);//hw_content
        	
        	 if(contentScore != null) {
        		 double score = (Double)contentScore.get("score");
        	     list.put("score", score);
        	 }
			List<Map> typeList = (List<Map>) list.get("typeList");
			
			if(typeList !=null && !typeList.isEmpty()) {
				for (int j = 0; j < typeList.size(); j++) {
					HashMap<String, Object> hashMap=new HashMap<>();//传递给后台的
					hashMap.put("hw_type", parameterStringMap.get("hw_type"));
					hashMap.put("hw_content",parameterStringMap.get("hw_content"));//
					hashMap.put("stuid",parameterStringMap.get("stuid"));
					hashMap.put("hwid",parameterStringMap.get("hwid"));
					if(parameterStringMap.get("hw_type").equals("5")) {

						List<Map<String, Object>> rlist = (List<Map<String, Object>>) typeList.get(j).get("listen_questionList");
						for (int i = 0; i < rlist.size(); i++) {
							Map<String, Object> map =new HashMap<String, Object>();
							map.putAll(hashMap);
							map.put("hw_answerId", rlist.get(i).get("hw_answerId"));
						
							String save_path = "homework" + "/" + 
									parameterStringMap.get("hwid") + "/"+ 
									parameterStringMap.get("stuid") + "/"+ 
									parameterStringMap.get("hw_type")+"/"+
									parameterStringMap.get("hw_content") + "/"+ 
									map.get("hw_answerId")+"/";
							rlist.get(i).put("HomeworkPath", save_path);
							Map<String, Object> everyHW =hw_di.findByEveryHomework(map);
							if(everyHW != null) {
								rlist.get(i).put("hw_video", everyHW.get("hw_video"));
								rlist.get(i).put("hw_score", everyHW.get("hw_score"));
							}
						}
					}else if(parameterStringMap.get("hw_type").equals("6")){
						
						List<Map<String, Object>> rlist = (List<Map<String, Object>>) typeList.get(j).get("read_questionList");
						for (int i = 0; i < rlist.size(); i++) {
							Map<String, Object> map =new HashMap<String, Object>();
							map.putAll(hashMap);
							map.put("hw_answerId", rlist.get(i).get("hw_answerId"));
							String save_path = "homework" + "/" + 
									parameterStringMap.get("hwid") + "/"+ 
									parameterStringMap.get("stuid") + "/"+ 
									parameterStringMap.get("hw_type")+"/"+
									parameterStringMap.get("hw_content") + "/"+ 
									rlist.get(i).get("hw_answerId")+"/";
							rlist.get(i).put("HomeworkPath", save_path);
							Map<String, Object> everyHW =hw_di.findByEveryHomework(map);
							if(everyHW != null) {
								rlist.get(i).put("hw_video", everyHW.get("hw_video"));
								rlist.get(i).put("hw_score", everyHW.get("hw_score"));
							}
						}
					}else {
						hashMap.put("hw_answerId",typeList.get(j).get("hw_answerId"));
						String save_path = "homework" + "/" + 
								parameterStringMap.get("hwid") + "/"+ 
								parameterStringMap.get("stuid") + "/"+ 
								parameterStringMap.get("hw_type")+"/"+
								parameterStringMap.get("hw_content") + "/"+ 
								hashMap.get("hw_answerId")+"/";
						typeList.get(j).put("HomeworkPath", save_path);
						Map<String, Object> everyHW =hw_di.findByEveryHomework(hashMap);
						if(everyHW != null) {
							typeList.get(j).put("hw_video", everyHW.get("hw_video"));
							typeList.get(j).put("hw_score", everyHW.get("hw_score"));
						}
					}
					
					
				}
			}
			
		 return list;
	}
	
	
    //写作业
	@Transactional
	public Map<String,String> saveHomeWork(Map<String,String> parameterStringMap){
		
		Map<String,String> map = new HashMap<String,String>();//stuid;hwid;hw_type;hw_content;hw_answerId;hw_video;hw_score
		String save_path = "homework" + "/" + 
		parameterStringMap.get("hwid") + "/"+ 
		parameterStringMap.get("stuid") + "/"+ 
		parameterStringMap.get("hw_type")+"/"+
		parameterStringMap.get("hw_content") + "/"+ 
		parameterStringMap.get("hw_answerId")+"/";
        map.put("save_path", save_path);
        //判断作业类型(不可多次一举)
        //判断是否存在此作业
        Map<String, Object> maps = hw_di.findStuHomework(parameterStringMap);//stuid,hwid
        double hw_score = (Double.valueOf(parameterStringMap.get("hw_score"))/100 ) * Double.valueOf(parameterStringMap.get("everyScore"));
        String hw_scores = String.valueOf(hw_score);
        parameterStringMap.put("hw_score", hw_scores);
        
        if(maps == null || maps.isEmpty()) {
        	//添加学生的人数
        	Map<String, Object> list = hw_di.findByHwid(parameterStringMap);//stuid
        	Integer finishPeople= (Integer) list.get("finishPeople");
            list.put("finishPeople", finishPeople+1);
            hw_di.updateFinshHw(list);
            
            //正式写作业
            parameterStringMap.put("stu_hw_id", UUID.randomUUID().toString());
            
            hw_di.saveStuHomework(parameterStringMap);//保存作业1
            
            parameterStringMap.put("hw_contentid", UUID.randomUUID().toString());
           
            hw_di.saveStuHomeWorkAnswer(parameterStringMap);//保存作业3
            parameterStringMap.put("score", hw_scores);//
            hw_di.saveStuHomeWorkType(parameterStringMap);//保存作业2    
            
        }else if(maps != null){//这是重做的  以及   继续做的
        	
        	
        	//继续做的
        	//正式写作业
        	parameterStringMap.put("stu_hw_id",(String) maps.get("stu_hw_id"));
        	Map<String, Object> hw2 = hw_di.findStuHomeWorkType(parameterStringMap);//hw_type;hw_content;stu_hw_id
        	if(hw2 == null || hw2.isEmpty()) {
        		 parameterStringMap.put("hw_contentid", UUID.randomUUID().toString());
        		 parameterStringMap.put("score", hw_scores);//
        		 
        		 hw_di.saveStuHomeWorkType(parameterStringMap);//保存作业2
        		 hw_di.saveStuHomeWorkAnswer(parameterStringMap);//保存作业3
        	}else {
        		
        		parameterStringMap.put("hw_contentid",(String) hw2.get("hw_contentid"));
        		Map<String, Object> hw3 = hw_di.findStuHomeWorkAnswer(parameterStringMap);//hw_answerId
        		if(hw3 == null || hw3.isEmpty()) {
        			
        			parameterStringMap.put("hw_score", hw_scores);
        			hw_di.saveStuHomeWorkAnswer(parameterStringMap);//保存作业3
        			double score = (double) hw2.get("score");
        			score+=hw_score;
        			hw2.put("score",score);
        			hw_di.updateStuHomeWorkType(hw2);
        		}else {
        			 hw_di.delStuHomeWorkAnswer(hw3);
        			 String filePath = Const.getFilePath_Real(save_path);
                     boolean del = FileUtil.del(filePath);
                     if (!del) {
                         map.put("msg", "文件路径不存在了");
                     } else {
                         map.put("msg", "删除文件成功");
                     }
                     
                    hw_di.saveStuHomeWorkAnswer(parameterStringMap);//保存作业3
         			double score =(double) hw2.get("score") ;
         			score-=Double.valueOf((String) hw3.get("hw_score"));//减去之前的得分
         			score+=hw_score;//加上现在的得分
         			hw2.put("score",score);
         			hw_di.updateStuHomeWorkType(hw2);
        		}
        		
        	}
        }
        
        
		return map;
	}
	
	
	//提交作业(计分数)
	@Transactional
	public int submitHomeWork(Map<String,String> parameterStringMap){
		int num =0;
		Map<String, Object> hw1 = hw_di.findStuHomework(parameterStringMap);
		if(hw1 !=null && !hw1.isEmpty()) {
			Map<String,Object> scoreAll = hw_di.findByScoreAll(hw1);
			Double scoreAlls = Double.valueOf(String.format("%.2f", (Double)scoreAll.get("scoreAll")));
			hw1.put("scoreAll", scoreAlls);
			num = hw_di.updateStuHomework(hw1);
		}
		Map<String,Object> map = new HashMap<String,Object>();
		
		
		  //这里的要求是   如果学生完成作业，则给家长消息表发送 完成作业  消息
        List<Map<String, Object>> pList = hw_di.findStuByPid(parameterStringMap);
        Map<String, Object> student = login_di.findByStuid(parameterStringMap);
        Map<String, Object> maps = hw_di.findStuHomework(parameterStringMap);//stuid,hwid
        Map<String, Object>  hwName = hw_di.findByHwid(parameterStringMap);
        if(pList !=null && !pList.isEmpty()) {
        	for (int i = 0; i < pList.size(); i++) {
                map.put("pid",  pList.get(i).get("pid"));
                map.put("stuid",  parameterStringMap.get("stuid"));
                map.put("hwid",  parameterStringMap.get("hwid"));
                List<Map<String, Object>> pNews = hw_di.findByParent(map);
                if(pNews == null || pNews.isEmpty()) {
                   map.put("newcontent", "您的孩子"+student.get("student")+"已完成作业："+hwName.get("homeworkName")+",本次作业分数为："+maps.get("scoreAll"));
                }else {
                	if(maps != null && maps.containsKey("scoreAll")) {
                       map.put("newcontent", "您的孩子"+student.get("student")+"已重新完成作业："+hwName.get("homeworkName")+",此作业的最高分数为："+maps.get("scoreAll"));
                	}else {
                		 map.put("newcontent", "您的孩子"+student.get("student")+"已完成作业："+hwName.get("homeworkName"));
                	}
                }
                hw_di.saveParentNews(map);
             }
        }
        
        
        //这里是老师  交齐作业通知
        int classPeople = hw_di.countClassByStu(parameterStringMap);//stuid
        Map<String, Object> pid =  hw_di.selectByPid(parameterStringMap);
        int finishPeople = (int) hwName.get("finishPeople");
        if(classPeople == finishPeople )   {
        	parameterStringMap.put("classhomewordId", hwName.get("classhomewordId").toString());
        	parameterStringMap.put("newcontent", "您布置的作业："+ hwName.get("homeworkName") +"已交齐，请及时检查");
        	parameterStringMap.put("pid",  pid.get("pid").toString());
        	List<Map<String, Object>> teacherNewList = hw_di.findByTeaNews(parameterStringMap);
        	if(teacherNewList == null || teacherNewList.isEmpty()) {
        		hw_di.saveTeacherNews(parameterStringMap);
        	}
        }
           return num;
	}
	
	//作业详情
	public Map<String,Object> findDetail(Map<String,String> parameterStringMap){
		Map<String,Object> all =new HashMap<String,Object>();
		List<Map<String,Object>> symbollist = null;
		List<Map<String,Object>> wordlist = null;
		List<Map<String,Object>> textlist = null;
		List<Map<String,Object>> readlist = null;
		List<Map<String,Object>> listenlist = null;
		List<Map<String,Object>> phraselist = null;
		List<Map<String,Object>> talklist = null;
		List<Map<String,Object>> sentencelist = null;
		
		all = hw_di.findStuHomework(parameterStringMap);//stuid;hwid
		
			List< Map<String, Object>> list = hw_di.findByHwType(parameterStringMap);//hwid
			for (int i = 0; i < list.size(); i++) {
				String hw_type = (String) list.get(i).get("hw_type");
				String hw_content = (String) list.get(i).get("hw_content");
				Map<String,String> map = new HashMap<String,String>();
				if(all != null && all.containsKey("stu_hw_id")==true) {
				    list.get(i).put("stu_hw_id", all.get("stu_hw_id"));
				}else {
					all =new HashMap<String,Object>();
				}
				Map<String, Object> stuhw = hw_di.findStuHomeWorkType(list.get(i));//stu_hw_id;hw_type;hw_content
				if(stuhw == null || stuhw.isEmpty()) {
					list.get(i).put("score", "0");//学生得分
				}else {
					list.get(i).put("score", stuhw.get("score"));//学生得分
					
				}
				
				double avgScore = Double.valueOf(String.format("%.2f", (double)100.00/list.size()));
				list.get(i).put("avgScore", avgScore);//每题分值
				
				if(hw_type.equals("0")) {//音标  flag  ；
					
					map.put("ybid", hw_content);
					String flag = parameterStringMap.get("flag");
					map.put("flag", flag);
					
					List<Map<String,Object>> typeList =new ArrayList<Map<String,Object>>();
					symbollist = text_di.findByFayinshili(map);
					for (int j = 0; j < symbollist.size(); j++) {
						Map<String,Object> typeMap = new HashMap<String,Object>();
						if(stuhw !=null &&stuhw.containsKey("hw_contentid")==true) {
							symbollist.get(j).put("hw_contentid", stuhw.get("hw_contentid"));
							Map<String, Object>  answer = hw_di.findStuHomeWorkAnswer(symbollist.get(j));//hw_answerId;	hw_content_id		
							if(answer !=null && !answer.isEmpty()) {
								typeMap.put("hw_video", answer.get("hw_video"));
							}
						}
						
						typeMap.put("yb_Bvideo", symbollist.get(j).get("yb_Bvideo"));
						typeMap.put("yb_Avideo", symbollist.get(j).get("yb_Avideo"));
						typeMap.put("size", j+1);
						typeList.add(typeMap);
					}
					list.get(i).put("typeList", typeList);
					
				}else if(hw_type.equals("1")) {//课文
					map.put("text_id", hw_content);
					textlist = text_di.findByTextById(map);
					List<Map<String,Object>> typeList =new ArrayList<Map<String,Object>>();
					for (int j = 0; j < textlist.size(); j++) {
						Map<String,Object> typeMap =new HashMap<String,Object>();
						if(stuhw !=null && stuhw.containsKey("hw_contentid")) {
							textlist.get(j).put("hw_contentid", stuhw.get("hw_contentid"));
							Map<String, Object>  answer = hw_di.findStuHomeWorkAnswer(textlist.get(j));
	                        if(answer !=null && !answer.isEmpty()) {
	                        	typeMap.put("hw_video", answer.get("hw_video"));
	                        	
							}
						}
                        typeMap.put("requrie_video", textlist.get(j).get("text_video"));
                        typeMap.put("size", j+1);
                        typeList.add(typeMap);
					}
					list.get(i).put("typeList", typeList);
					
				}else if(hw_type.equals("2")) {//单词
					map.put("flag", "word");
					map.put("type",hw_content);
					List<Map<String,Object>> typeList =new ArrayList<Map<String,Object>>();
					wordlist = text_di.findByDyByWord(map);
					for (int j = 0; j < wordlist.size(); j++) {
						Map<String,Object> typeMap =new HashMap<String,Object>();
						if(stuhw !=null && stuhw.containsKey("hw_contentid")) {
							wordlist.get(j).put("hw_contentid", stuhw.get("hw_contentid"));
							Map<String, Object>  answer = hw_di.findStuHomeWorkAnswer(wordlist.get(j));
	                        if(answer !=null && !answer.isEmpty()) {
	                        	typeMap.put("hw_video", answer.get("hw_video"));
							}
						}
                        typeMap.put("requrie_video", wordlist.get(j).get("word_video"));
                        typeMap.put("size", j+1);
                        typeList.add(typeMap);
					}
					list.get(i).put("typeList", typeList);
					
				}else if(hw_type.equals("3")) {//句子
					map.put("sentence_id", hw_content);
					sentencelist =text_di.findBySentenceById(map);
					List<Map<String,Object>> typeList =new ArrayList<Map<String,Object>>();
					for (int j = 0; j < sentencelist.size(); j++) {
						Map<String,Object> typeMap =new HashMap<String,Object>();
						if(stuhw !=null && stuhw.containsKey("hw_contentid")) {
							sentencelist.get(j).put("hw_contentid", stuhw.get("hw_contentid"));
							Map<String, Object>  answer = hw_di.findStuHomeWorkAnswer(sentencelist.get(j));
	                        if(answer !=null && !answer.isEmpty()) {
	                        	typeMap.put("hw_video", answer.get("hw_video"));
							}
						}
                        typeMap.put("requrie_video", sentencelist.get(j).get("sentence_video"));
                        typeMap.put("size", j+1);
                        typeList.add(typeMap);
					}
					list.get(i).put("typeList", typeList);
					
				}else if(hw_type.equals("4")) {//对话
					map.put("talk_id", hw_content);
					talklist = text_di.findByTalkById(map);
					List<Map<String,Object>> typeList =new ArrayList<Map<String,Object>>();
					for (int j = 0; j < talklist.size(); j++) {
						Map<String,Object> typeMap =new HashMap<String,Object>();
						if(stuhw !=null && stuhw.containsKey("hw_contentid")) {
							talklist.get(j).put("hw_contentid", stuhw.get("hw_contentid"));
							Map<String, Object>  answer = hw_di.findStuHomeWorkAnswer(talklist.get(j));
	                        if(answer !=null && !answer.isEmpty()) {
	                        	typeMap.put("hw_video", answer.get("hw_video"));
							}
						}
						
                        typeMap.put("requrie_video", talklist.get(j).get("juese_video"));
                        typeMap.put("size", j+1);
                        typeList.add(typeMap);
					}
					list.get(i).put("typeList", typeList);
					
				}else if(hw_type.equals("5")) {//听力 listen_type填空
					map.put("listen_id", hw_content);
					List<Map<String,Object>> typeList =new ArrayList<Map<String,Object>>();
					listenlist =   text_di.findByListenById2(map);
					for (int j = 0; j < listenlist.size(); j++) {
						Map<String,Object> typeMap =new HashMap<String,Object>();
						if(stuhw !=null && stuhw.containsKey("hw_contentid")) {
							listenlist.get(j).put("hw_contentid", stuhw.get("hw_contentid"));
							Map<String, Object>  answer = hw_di.findStuHomeWorkAnswer(listenlist.get(j));
	                        if(answer !=null && !answer.isEmpty()) {
	                        	typeMap.put("hw_video", answer.get("hw_video"));
							}
						}
							
                        typeMap.put("requrie_video", listenlist.get(j).get("listen_answer"));
                        typeMap.put("size", j+1);
                        typeList.add(typeMap);
					}
					list.get(i).put("typeList", typeList);
					
					
				}else if(hw_type.equals("6")) {//阅读 
					map.put("read_id", hw_content);
					List<Map<String,Object>> typeList =new ArrayList<Map<String,Object>>();
					readlist = text_di.findByReadById2(map);
					for (int j = 0; j < readlist.size(); j++) {
						
						Map<String,Object> typeMap =new HashMap<String,Object>();
						if(stuhw !=null && stuhw.containsKey("hw_contentid")) {
							readlist.get(j).put("hw_contentid", stuhw.get("hw_contentid"));
							Map<String, Object>  answer = hw_di.findStuHomeWorkAnswer(readlist.get(j));
							if(answer !=null && !answer.isEmpty()) {
	                        	typeMap.put("hw_video", answer.get("hw_video"));
							}
						}
                       
                        typeMap.put("requrie_video", readlist.get(j).get("read_answer"));
                        typeMap.put("size", j+1);
                        typeList.add(typeMap);
					}
					list.get(i).put("typeList", typeList);
					
				}else if(hw_type.equals("7")) {//听力选择
					
					
				}else if(hw_type.equals("8")) {//短语
					map.put("flag", "phrase");
					map.put("type",hw_content);
					List<Map<String,Object>> typeList =new ArrayList<Map<String,Object>>();
					phraselist = text_di.findByDyByPhrase(map);
					for (int j = 0; j < phraselist.size(); j++) {
						Map<String,Object> typeMap =new HashMap<String,Object>();
						if(stuhw !=null && stuhw.containsKey("hw_contentid")) {
							phraselist.get(j).put("hw_contentid", stuhw.get("hw_contentid"));
							Map<String, Object>  answer = hw_di.findStuHomeWorkAnswer(phraselist.get(j));
	                        if(answer !=null && !answer.isEmpty()) {
	                        	typeMap.put("hw_video", answer.get("hw_video"));
							}
						}
						
                        typeMap.put("requrie_video", phraselist.get(j).get("phrase_video"));
                        typeMap.put("size", j+1);
                        typeList.add(typeMap);
					}
					list.get(i).put("typeList", typeList);
					
				}
				all.put("type", list);
			}
		
		
		 return all;
	}
}
