package com.xljy.student.text.servers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xljy.student.text.dao.StuLearn_di;
import com.xljy.student.text.dao.StuText_di;
import com.xljy.util.Const;

@Service
public class StuText_sm {

	@Autowired
	private StuText_di text_di;
	@Autowired
	private StuLearn_di learn_di;
	//查询所有课文类型
	public List<Map<String, Object>> findByType(Map parameterStringMap){
		
		return text_di.findByType(parameterStringMap);
	}
	//查询所有单元的内容
	//通过给jclx，nj，sxc进行查询课文
	public List<Map<String, Object>> findBySortByText(Map<String, String> parameterStringMap){
		List<Map<String, Object>> list = null;
		String json=parameterStringMap.get("json");
		Map map=null;
		ObjectMapper mapper=new ObjectMapper();
		int learnType = 0;
		int type = 0;
		try {
			map=mapper.readValue(json, Map.class);//json={"flag":"text/symbol/word";"jclx":"";"nj":"","sxc":"";stuid}
			 if(map.get("flag").equals("symbol") || map.containsValue("symbol")){//音标
				list = text_di.findBySortBySymbol(map);
			 }else {
				list = text_di.findBySortByType(map);
				if(list.size()>0) {
					for (int i = 0; i < list.size(); i++) {
						parameterStringMap.put("type", (String)list.get(i).get("type"));
						list.get(i).put("Relative_path", list.get(i).get("save")+"/");
						
						if(map.get("flag").equals("word") || map.containsValue("word")) {//单词
							learnType = learn_di.countByLearnWord(parameterStringMap);//传stuid；type
							type = learn_di.countByWord(parameterStringMap);//type
							list.get(i).put("practice", learnType+"/"+type);
							
					    }else if(map.get("flag").equals("text") || map.containsValue("text")) {//课文
					    	learnType = learn_di.countByLearnByText(parameterStringMap);//stuid,type
					    	type = learn_di.countByText(parameterStringMap);
					    	list.get(i).put("practice", learnType+"/"+type);
					    
					    }else if(map.get("flag").equals("sentence") || map.containsValue("sentence")) {//句子
					        
					    }else if(map.get("flag").equals("talk") || map.containsValue("talk")) {//对话
					    	
					    }else if(map.get("flag").equals("read") || map.containsValue("read")) {//阅读
					    	learnType = learn_di.countLearnRead(parameterStringMap);
					    	type = learn_di.countRead(parameterStringMap);
					    	list.get(i).put("practice", learnType+"/"+type);
					    	
					    }else if(map.get("flag").equals("listen") || map.containsValue("listen")) {//听力
					    	learnType = learn_di.countLearnListen(parameterStringMap);
					    	type = learn_di.countListen(parameterStringMap);
					    	list.get(i).put("practice", learnType+"/"+type);
					    	
					    }else if(map.get("flag").equals("phrase") || map.containsValue("phrase")) {//短语
					    	learnType = learn_di.countByLearnByPhrase(parameterStringMap);
					    	type = learn_di.countByPhrase(parameterStringMap);
					    	list.get(i).put("practice", learnType+"/"+type);
					    }
					}
				}
			 }	 
			return list;
		} catch (IOException e) {
			e.printStackTrace();
	 		throw new RuntimeException(e.getMessage());
		}	
	}
	
	//音标的学习
	public List<Map<String, Object>> findBySymbol(Map<String, String> parameterStringMap){
		List<Map<String, Object>> list = null;
		String ybid = parameterStringMap.get("ybid");
		String flag = parameterStringMap.get("flag"); 
		if(flag.equals("1")) {//发音实例
		
			list = text_di.findByFayinshili(parameterStringMap);
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("Relative_path", "yinbiao"+ "/" +ybid +"/");
				double avgScore =Double.valueOf(String.format("%.2f", (double)(100 /list.size())));
				list.get(i).put("avgScore", avgScore);
			}
		}else if(flag.equals("2")) {//发音详解
			
			list = text_di.findByFayinxiangjie(parameterStringMap);
			for (int i = 0; i < list.size(); i++) {
				
				list.get(i).put("Relative_path", "yinbiao"+ "/" +ybid +"/");
			}
		}else if(flag.equals("3")) {//尝试发音
			
			list = text_di.findBySymbolByYbid(parameterStringMap);
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("Relative_path", "yinbiao"+ "/" +ybid +"/");
			}
		}else if(flag.equals("4")) {//字母及其组合
			
			list = text_di.findByZimu(parameterStringMap);
			for (int i = 0; i < list.size(); i++) {
				List<Map<String, Object>> lists = text_di.findByZuhe(list.get(i));
				list.get(i).put("word", lists);
				list.get(i).put("Relative_path", "yinbiao"+ "/" +ybid +"/");
			}	
		}
	
		
		return list;
	}
	//查询每个单元下的所有内容
	//根据type进入所有的课文、word等等
	public List<Map<String, Object>> findByDy(Map<String, String> parameterStringMap){
		List<Map<String, Object>> list = null;//{flag=;type=；stuid=}
		String flag = parameterStringMap.get("flag");
		if(flag.equals("text")) {//课文
			list = text_di.findByDyByText(parameterStringMap);
			for (int i = 0; i < list.size(); i++) {
				String save_path=list.get(i).get("save")+"/" + Const.KEWEN + list.get(i).get("text_id") + "/";
				list.get(i).put("Relative_path", save_path);
				parameterStringMap.put("text_id", (String) list.get(i).get("text_id"));
				int num = learn_di.countByLearnByText(parameterStringMap);
				list.get(i).put("practice", num);//0未完成；1以完成
			}
		}else if(flag.equals("word")) {//单词
			list = text_di.findByDyByWord(parameterStringMap);
			
			for (int i = 0; i < list.size(); i++) {
				String save_path=list.get(i).get("save")+"/" + Const.DANCI + list.get(i).get("type") + "/";
				list.get(i).put("Relative_path", save_path);
				
				double avgScore =Double.valueOf(String.format("%.2f", (double)(100 /list.size())));
				list.get(i).put("avgScore", avgScore);
				parameterStringMap.put("word_id",(String) list.get(i).get("word_id"));
				int num = learn_di.countByLearnWord(parameterStringMap);//stuid;word_id
				list.get(i).put("practice", num);//0未完成；1以完成
				
			}
		}else if(flag.equals("phrase")) {//短语
			list = text_di.findByDyByPhrase(parameterStringMap);
			for (int i = 0; i < list.size(); i++) {
				String save_path=list.get(i).get("save")+"/" + Const.DUANYU + list.get(i).get("type") + "/";
				list.get(i).put("Relative_path", save_path);
				double avgScore =Double.valueOf(String.format("%.2f", (double)(100 /list.size())));
				list.get(i).put("avgScore", avgScore);
				parameterStringMap.put("phrase_id", (String) list.get(i).get("phrase_id"));
				int num = learn_di.countByLearnByPhrase(parameterStringMap);
				list.get(i).put("practice", num);//0未完成；1以完成
			}
		}else if(flag.equals("sentence")) {//句子 sentence_id;stuid
			list = text_di.findByDyBySentence(parameterStringMap);
			for (int i = 0; i < list.size(); i++) {
				String save_path=list.get(i).get("save")+"/" + Const.JVZI + list.get(i).get("sentence_id") + "/";
				list.get(i).put("Relative_path", save_path);
				parameterStringMap.put("sentence_id", (String) list.get(i).get("sentence_id"));
				int used = learn_di.countBySentence(parameterStringMap);
				int news = learn_di.countByLearnSentence(parameterStringMap);
				list.get(i).put("practice", news + "/" +used);
			}
		}else if(flag.equals("talk")) {//对话
			list = text_di.findByDyByTalk(parameterStringMap);
			for (int i = 0; i < list.size(); i++) {
				String save_path=list.get(i).get("save")+"/" + Const.DUIHUA + list.get(i).get("talk_id") + "/";
				list.get(i).put("Relative_path", save_path);
				parameterStringMap.put("talk_id", (String)list.get(i).get("talk_id"));
				int js = learn_di.countByTalkJs(parameterStringMap);
				int learn = learn_di.countByLearnTalkJs(parameterStringMap);
				list.get(i).put("practice", learn+"/"+js);
			}
		}else if(flag.equals("listen")) {//听力 listen_type  1为选择2为填空
			list = text_di.findByDyByListen(parameterStringMap);
			for (int i = 0; i < list.size(); i++) {
				String save_path=list.get(i).get("save")+"/" + Const.TINGLI + list.get(i).get("listen_id") + "/";
				list.get(i).put("Relative_path", save_path);
				parameterStringMap.put("listen_id", (String)list.get(i).get("listen_id"));
				int learn = learn_di.countLearnListenAnswer(parameterStringMap);
				int listen = learn_di.countListenAnswer(parameterStringMap);
				list.get(i).put("practice", learn+"/"+listen);
			}
		}else if(flag.equals("read")) {//阅读
			list = text_di.findByDyByRead(parameterStringMap);
			for (int i = 0; i < list.size(); i++) {
				String save_path=list.get(i).get("save")+"/" + Const.YUEDU + list.get(i).get("read_id") + "/";
				list.get(i).put("Relative_path",save_path);
				parameterStringMap.put("read_id", (String)list.get(i).get("read_id"));
				int learn = learn_di.countLearnAnswer(parameterStringMap);
				int read = learn_di.countAnswer(parameterStringMap);
				list.get(i).put("practice", learn+"/"+read);
			}
		}
		
		return list;
	}
	
	//详情页
	public List<Map<String, Object>> detail(Map<String, String> parameterStringMap){
		List<Map<String, Object>> list = null;//flag=sentence_id;talk_id;listen_id;read_id
		if(parameterStringMap.containsKey("sentence_id")) {//句子
			list = text_di.findBySentenceById(parameterStringMap);
			for (int i = 0; i < list.size(); i++) {
				parameterStringMap.put("sentence_sid", (String) list.get(i).get("sentence_sid"));
				int num = learn_di.countByLearnSentence(parameterStringMap);
				double avgScore =Double.valueOf(String.format("%.2f", (double)(100 /list.size())));
				list.get(i).put("avgScore", avgScore);
				list.get(i).put("practice", num);
			}
			
		}else if(parameterStringMap.containsKey("talk_id")) {//对话
			list = text_di.findByTalkById(parameterStringMap);
			for (int i = 0; i < list.size(); i++) {
				double avgScore =Double.valueOf(String.format("%.2f", (double)(100 /list.size())));
				list.get(i).put("avgScore", avgScore);
			}
			
		}else if(parameterStringMap.containsKey("listen_id")) {//听力
			list = text_di.findByListenById1(parameterStringMap);
			double score =Double.valueOf(String.format("%.2f", (double)(100 /list.size())));
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("score", score);
			}
            List<Map<String, Object>> questionList = text_di.findByListenById2(parameterStringMap);
			list.get(0).put("listen_questionList", questionList);
			for (int j = 0; j < questionList.size(); j++) {
				List<Map<String, Object>> optionList = text_di.findByListenById3(questionList.get(j));
				questionList.get(j).put("listen_optionList", optionList);
				double avgScore =Double.valueOf(String.format("%.2f", (double)(100 /questionList.size())));
				questionList.get(j).put("avgScore", avgScore);
			}
		}else if(parameterStringMap.containsKey("read_id")) {//阅读
			list = text_di.findByReadById1(parameterStringMap);
			
		    List<Map<String, Object>> questionList = text_di.findByReadById2(parameterStringMap);
			list.get(0).put("read_questionList", questionList);
			for (int j = 0; j < questionList.size(); j++) {
				List<Map<String, Object>> optionList = text_di.findByReadById3(questionList.get(j));
				questionList.get(j).put("read_optionList", optionList);
				double avgScore =Double.valueOf(String.format("%.2f", (double)(100 /questionList.size())));
				questionList.get(j).put("avgScore", avgScore);
			}
		}else if(parameterStringMap.containsKey("text_id")) {//课文
			list = text_di.findByTextById(parameterStringMap);
			double avgScore =Double.valueOf(String.format("%.2f", (double)(100 /list.size())));
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("avgScore", avgScore);
			}
		}
		return list;
	}
	
}
