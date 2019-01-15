package com.xljy.student.text.servers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xljy.student.text.dao.StuLearn_di;
import com.xljy.util.Const;

@Service
public class StuLearn_sm {

	@Autowired
	private StuLearn_di learn_di;
	
	//保存尝试发音的数据
	@Transactional
	public Map<String, Object> saveLearnBySymbol(Map<String, String> parameterStringMap) {
		HashMap<String, Object> hashMap=new HashMap<>();//传递给后台的
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合//需要传递的值有：stuid；ybid；learn_video=(时间)
		String stuid = parameterStringMap.get("stuid");
		String ybid = parameterStringMap.get("ybid");
		String save_path = "learn" + "/" + stuid + "/" + "ybid" +"/" + ybid + "/";
		hashMap.put("save_path", save_path);
		//判断是否传来录音数据
		if(parameterStringMap.containsKey("learn_video")== true && parameterStringMap.get("learn_video").toString().length()>0) {
			files.add(parameterStringMap.get("learn_video").toString());//将音频加入集合
			//判断是否存在此用户的音标id
			Map<String, Object> map = learn_di.findLearnByYbid(parameterStringMap);
			if(map == null || map.isEmpty()) {//添加音标学习
				learn_di.saveLearnBySymbol(parameterStringMap);
			}else {
				learn_di.updateLearnBySymbol(parameterStringMap);//这里他给的learn_video是变化的
			}
		}
		 /*
		 * 数据库同步完成 处理多余文件
		 */
		  Object[] arr_files1 = files.toArray();
		  String[] arr_files = ArrayUtils.toStringArray(arr_files1);
		  String[] arr_yinpin = Const.getyinpin(arr_files);// 得到音频的文件名集合
		  String yinpinfilePath = Const.getFilePath_Real(hashMap.get("save_path").toString(), "2");// 2音频
		  Const.delfile_lujingxiabubaohan(hashMap, arr_yinpin, yinpinfilePath, "音频");
		  
		return hashMap;
	}
	//播放音标的录音
	public Map<String, Object> findLearnByYbid(Map<String, String> parameterStringMap){
		Map<String, Object> hashMap=new HashMap<>();//传递给后台的  要有stuid和ybid
		String stuid = parameterStringMap.get("stuid");
		String ybid = parameterStringMap.get("ybid");
		String save_path = "learn" + "/" + stuid + "/" + "ybid" +"/"+ ybid + "/";
		hashMap = learn_di.findLearnByYbid(parameterStringMap);
		if(hashMap !=null) {
			hashMap.put("Relative_path", save_path);
		}
		return hashMap;

	}
	//音标单词学习（挑战一下）:挑战一下就是 1 单词实例
	public Map<String, Object> saveLearnBySymbolWord(Map<String, String> parameterStringMap){
		HashMap<String, Object> hashMap=new HashMap<>();//传递给后台的
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合//需要传递的值有：stuid；yb_wordId；learn_video=(时间);learn_score
		String stuid = parameterStringMap.get("stuid");
		String yb_wordId = parameterStringMap.get("yb_wordId");
		String save_path = "learn" + "/" + stuid + "/" + "yb_wordId" + "/" + yb_wordId +"/";
		hashMap.put("save_path", save_path);
		//判断是否传来录音数据
		if(parameterStringMap.containsKey("learn_video")== true && parameterStringMap.get("learn_video").toString().length()>0) {
			files.add(parameterStringMap.get("learn_video").toString());//将音频加入集合
			//判断是否存在此用户的音标单词id
			Map<String, Object> map = learn_di.findLearnBySymbolWord(parameterStringMap);
			if(map == null || map.isEmpty()) {//如果不存在此录音，则添加
				learn_di.saveLearnBySymbolWord(parameterStringMap);
			}else {//文件的删除操作 只有在修改的时候才可被用  所以在这写即可
				double learn_score1 = Double.valueOf(parameterStringMap.get("learn_score"));//新的
				double learn_score2 = (double) map.get("learn_score");
				if(learn_score1 > learn_score2) {//如果新评分 大于 旧评分，则修改
					
					learn_di.updateLearnBySymbolWord(parameterStringMap);
					/*
					 * 数据库同步完成 处理多余文件
					 */
				    Object[] arr_files1 = files.toArray();
					String[] arr_files = ArrayUtils.toStringArray(arr_files1);
					String[] arr_yinpin = Const.getyinpin(arr_files);// 得到音频的文件名集合
					String yinpinfilePath = Const.getFilePath_Real(hashMap.get("save_path").toString(), "2");// 2音频
					Const.delfile_lujingxiabubaohan(hashMap, arr_yinpin, yinpinfilePath, "音频");
				}
			}
		}
		  
		return hashMap;
	}
	//播放录音单词的录音
	public Map<String, Object> findLearnBySymbolWord(Map<String, String> parameterStringMap){
		Map<String, Object> hashMap=new HashMap<>();//传递给后台的  要有stuid和yb_wordId
		String stuid = parameterStringMap.get("stuid");
		String yb_wordId = parameterStringMap.get("yb_wordId");
		String save_path = "learn" + "/" + stuid + "/" + "yb_wordId" + "/" + yb_wordId +"/";
		hashMap = learn_di.findLearnBySymbolWord(parameterStringMap);
		if(hashMap !=null) {
		hashMap.put("Relative_path", save_path);
		}
		return hashMap;
	}
	//保存单词的录音
	@Transactional
	public Map<String, Object> saveLearnWord(Map<String, String> parameterStringMap){
		HashMap<String, Object> hashMap=new HashMap<>();//传递给后台的
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合//需要传递的值有：stuid；word_id；learn_video=(时间);learn_score;type
		String stuid = parameterStringMap.get("stuid");
		String word_id = parameterStringMap.get("word_id");
		String save_path = "learn" + "/" + stuid + "/" + "word" + "/" + word_id + "/";
		hashMap.put("save_path", save_path);
		//判断是否传来录音数据
		if(parameterStringMap.containsKey("learn_video")== true && parameterStringMap.get("learn_video").toString().length()>0) {
			files.add(parameterStringMap.get("learn_video").toString());//将音频加入集合
			//判断是否存在此用户的单词id
			 Map<String, Object> map = learn_di.findLearnByWord(parameterStringMap);
			 if(map == null || map.isEmpty()) {//不存在此单词录音  进行保存
				 learn_di.saveLearnWord(parameterStringMap);
			 }else {
				 double learn_score1 = Double.valueOf(parameterStringMap.get("learn_score"));//新的
				 double learn_score2 = (double) map.get("learn_score");
				 if(learn_score1 > learn_score2) {//如果新评分 大于 旧评分，则修改
					 learn_di.updateLearnByWord(parameterStringMap);
					 
					 Object[] arr_files1 = files.toArray();
					 String[] arr_files = ArrayUtils.toStringArray(arr_files1);
					 String[] arr_yinpin = Const.getyinpin(arr_files);// 得到音频的文件名集合
					 String yinpinfilePath = Const.getFilePath_Real(hashMap.get("save_path").toString(), "2");// 2音频
					 Const.delfile_lujingxiabubaohan(hashMap, arr_yinpin, yinpinfilePath, "音频"); 
				 }
			 }
		
		}
		return hashMap;
	}
	//播放此单词的录音
	public Map<String, Object> findLearnByWord(Map<String, String> parameterStringMap){
		Map<String, Object> hashMap=new HashMap<>();//传递给后台的
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合//需要传递的值有：stuid；word_id；
		String stuid = parameterStringMap.get("stuid");
		String word_id = parameterStringMap.get("word_id");
		String save_path = "learn" + "/" + stuid + "/" + "word" + "/" + word_id + "/";
		hashMap = learn_di.findLearnByWord(parameterStringMap);
		if(hashMap !=null) {
			hashMap.put("Relative_path", save_path);
		}
		return hashMap;
	}
	//保存对话的录音
	@Transactional
	public Map<String, Object> saveLearnJS(Map<String, String> parameterStringMap){
		HashMap<String, Object> hashMap=new HashMap<>();//传递给后台的
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合//需要传递的值有：stuid；juese_id；learn_video=(时间);learn_score；talk_id
		String stuid = parameterStringMap.get("stuid");
		String juese_id = parameterStringMap.get("juese_id");
		String save_path = "learn" + "/" + stuid + "/" + "talk" + "/" + juese_id + "/";
		hashMap.put("save_path", save_path);
		//判断是否传来录音数据
		if(parameterStringMap.containsKey("learn_video")== true && parameterStringMap.get("learn_video").toString().length()>0) {
			files.add(parameterStringMap.get("learn_video").toString());//将音频加入集合
			//判断是否存在角色的录音
			List<Map<String, Object>> list = learn_di.findLearnByTalk(parameterStringMap);//stuid;juese_id
			if(list == null || list.isEmpty()) {//不存在此角色录音，进行保存
				learn_di.saveLearnJS(parameterStringMap);
			}else {//修改
				double learn_score1 = Double.valueOf(parameterStringMap.get("learn_score"));//新的
				 double learn_score2 = (double) list.get(0).get("learn_score");
				 if(learn_score1 > learn_score2) {//如果新评分 大于 旧评分，则修改
					 learn_di.updateLearnByJS(parameterStringMap);
					 
					 Object[] arr_files1 = files.toArray();
					 String[] arr_files = ArrayUtils.toStringArray(arr_files1);
					 String[] arr_yinpin = Const.getyinpin(arr_files);// 得到音频的文件名集合
					 String yinpinfilePath = Const.getFilePath_Real(hashMap.get("save_path").toString(), "2");// 2音频
					 Const.delfile_lujingxiabubaohan(hashMap, arr_yinpin, yinpinfilePath, "音频"); 
				 }
			}
		}
		return hashMap;
	}

	//播放对话的录音
	public List<Map<String, Object>> findLearnByTalk(Map<String, String> parameterStringMap){
		List<Map<String, Object>> hashMap=null;//传递给后台的
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合//需要传递的值有：stuid；talk_id；
		String stuid = parameterStringMap.get("stuid");
		String juese_id = parameterStringMap.get("juese_id");
		hashMap = learn_di.findAllByLearnTalk(parameterStringMap);
		for (int i = 0; i < hashMap.size(); i++) {
			String save_path = "learn" + "/" + stuid + "/" + "talk" + "/" + hashMap.get(i).get("juese_id") + "/";
			hashMap.get(i).put("Relative_path", save_path);
		} 
		return hashMap;
	}
	//保存课文的录音
	@Transactional
	public Map<String, Object> saveLearnText(Map<String, String> parameterStringMap){
		HashMap<String, Object> hashMap=new HashMap<>();//传递给后台的
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合//需要传递的值有：stuid；text_id；learn_video=(时间);learn_score；type
		String stuid = parameterStringMap.get("stuid");
		String text_id = parameterStringMap.get("text_id");
		String save_path = "learn" + "/" + stuid + "/" + "text" + "/" + text_id + "/";
		hashMap.put("save_path", save_path);
		//判断是否传来录音数据
		if(parameterStringMap.containsKey("learn_video")== true && parameterStringMap.get("learn_video").toString().length()>0) {
			files.add(parameterStringMap.get("learn_video").toString());//将音频加入集合
			//判断是否存在此课文的录音
			 Map<String, Object> map = learn_di.findLearnByText(parameterStringMap);
			 if(map == null || map.isEmpty()) {//添加
				 learn_di.saveLearnText(parameterStringMap);
			 }else {
				 double learn_score1 = Double.valueOf(parameterStringMap.get("learn_score"));//新的
				 double learn_score2 = (double) map.get("learn_score");
				 if(learn_score1 > learn_score2) {//如果新评分 大于 旧评分，则修改
					 learn_di.updateLearnByText(parameterStringMap);
					 
					 Object[] arr_files1 = files.toArray();
					 String[] arr_files = ArrayUtils.toStringArray(arr_files1);
					 String[] arr_yinpin = Const.getyinpin(arr_files);// 得到音频的文件名集合
					 String yinpinfilePath = Const.getFilePath_Real(hashMap.get("save_path").toString(), "2");// 2音频
					 Const.delfile_lujingxiabubaohan(hashMap, arr_yinpin, yinpinfilePath, "音频"); 
				 }
			 }
		}
		return hashMap;
	}
	
	//播放课文录音
	public Map<String, Object> findLearnByText(Map<String, String> parameterStringMap){
		Map<String, Object> hashMap=new HashMap<>();//传递给后台的
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合//需要传递的值有：stuid；text_id；
		String stuid = parameterStringMap.get("stuid");
		String text_id = parameterStringMap.get("text_id");
		String save_path = "learn" + "/" + stuid + "/" + "text" + "/" + text_id + "/";
		hashMap = learn_di.findLearnByText(parameterStringMap);
		if(hashMap !=null) {
			hashMap.put("Relative_path", save_path);
		}
		return hashMap;
	}
	
	//保存短语录音
	@Transactional
	public Map<String, Object> saveLearnPhrase(Map<String, String> parameterStringMap){
		HashMap<String, Object> hashMap=new HashMap<>();//传递给后台的
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合//需要传递的值有：stuid；phrase_id；learn_video=(时间);learn_score；type
		String stuid = parameterStringMap.get("stuid");
		String phrase_id = parameterStringMap.get("phrase_id");
		String save_path = "learn" + "/" + stuid + "/" + "phrase" + "/" + phrase_id + "/";
		hashMap.put("save_path", save_path);
		//判断是否传来录音数据
		if(parameterStringMap.containsKey("learn_video")== true && parameterStringMap.get("learn_video").toString().length()>0) {
			files.add(parameterStringMap.get("learn_video").toString());//将音频加入集合
			//判断是否存在短语录音
			Map<String, Object> map = learn_di.findLearnByPhrase(parameterStringMap);
			if(map == null || map.isEmpty()) {//保存
				learn_di.saveLearnPhrase(parameterStringMap);
			}else {
				 double learn_score1 = Double.valueOf(parameterStringMap.get("learn_score"));//新的
				 double learn_score2 = (double) map.get("learn_score");
				 if(learn_score1 > learn_score2) {//如果新评分 大于 旧评分，则修改
					 learn_di.updateLearnByPhrase(parameterStringMap);
					 
					 Object[] arr_files1 = files.toArray();
					 String[] arr_files = ArrayUtils.toStringArray(arr_files1);
					 String[] arr_yinpin = Const.getyinpin(arr_files);// 得到音频的文件名集合
					 String yinpinfilePath = Const.getFilePath_Real(hashMap.get("save_path").toString(), "2");// 2音频
					 Const.delfile_lujingxiabubaohan(hashMap, arr_yinpin, yinpinfilePath, "音频"); 
				 }
			}
		}
		
		return hashMap;
	}
	
	//播放短语录音
	public Map<String, Object> findLearnByPhrase(Map<String, String> parameterStringMap){
		Map<String, Object> hashMap=new HashMap<>();//传递给后台的
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合//需要传递的值有：stuid；phrase_id；
		String stuid = parameterStringMap.get("stuid");
		String phrase_id = parameterStringMap.get("phrase_id");
		String save_path = "learn" + "/" + stuid + "/" + "phrase" + "/" + phrase_id + "/";
		hashMap = learn_di.findLearnByPhrase(parameterStringMap);
		if(hashMap !=null) {
			hashMap.put("Relative_path", save_path);
		}
		return hashMap;
	}
	
	//保存句子录音
	@Transactional
	public Map<String, Object> saveLearnSentence(Map<String, String> parameterStringMap){
		HashMap<String, Object> hashMap=new HashMap<>();//传递给后台的
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合//需要传递的值有：stuid；sentence_id;sentence_sid；learn_video=(时间);learn_score
		String stuid = parameterStringMap.get("stuid");
		String sentence_sid = parameterStringMap.get("sentence_sid");
		String save_path = "learn" + "/" + stuid + "/" + "sentence" + "/" + sentence_sid + "/";
		hashMap.put("save_path", save_path);
		//判断是否传来录音数据
	    if(parameterStringMap.containsKey("learn_video")== true && parameterStringMap.get("learn_video").toString().length()>0) {
	    	files.add(parameterStringMap.get("learn_video").toString());//将音频加入集合
	    	Map<String, Object> map = learn_di.findLearnSentence(parameterStringMap);//stuid;sentence_sid
	    	if(map == null || map.isEmpty()) {//添加
	    		learn_di.saveLearnSentence(parameterStringMap);
	    	}else {
	    		double learn_score1 = Double.valueOf(parameterStringMap.get("learn_score"));//新的
				 double learn_score2 = (double) map.get("learn_score");
				 if(learn_score1 > learn_score2) {//如果新评分 大于 旧评分，则修改
					 learn_di.updateLearnSentence(parameterStringMap);
					 
					 Object[] arr_files1 = files.toArray();
					 String[] arr_files = ArrayUtils.toStringArray(arr_files1);
					 String[] arr_yinpin = Const.getyinpin(arr_files);// 得到音频的文件名集合
					 String yinpinfilePath = Const.getFilePath_Real(hashMap.get("save_path").toString(), "2");// 2音频
					 Const.delfile_lujingxiabubaohan(hashMap, arr_yinpin, yinpinfilePath, "音频"); 
				 }
	    	}
		}
		return hashMap;
	}
	
	//播放句子录音
	public Map<String, Object> findLearnSentence(Map<String, String> parameterStringMap){
		Map<String, Object> hashMap=new HashMap<>();//传递给后台的
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合//需要传递的值有：stuid；sentence_sid；
		String stuid = parameterStringMap.get("stuid");
		String sentence_sid = parameterStringMap.get("sentence_sid");
		String save_path = "learn" + "/" + stuid + "/" + "sentence" + "/" + sentence_sid + "/";
		hashMap = learn_di.findLearnSentence(parameterStringMap);
		if(hashMap !=null) {
			hashMap.put("Relative_path", save_path);
		}
		return hashMap;
	}
	
	//保存阅读的学习
	@Transactional
	public Map<String, Object> saveLearnRead(Map<String, String> parameterStringMap){
		HashMap<String, Object> hashMap=new HashMap<>();//传递给后台的
		String json = parameterStringMap.get("json");//json={read_id;type;stuid;answerList:[read_qid;learn_video;learn_score]}
		ObjectMapper mapper =new ObjectMapper();
		Map maps =null;
		try {
			maps = mapper.readValue(json, Map.class);
			List<Map> answerList = (List<Map>) maps.get("answerList");
			double score =0;
			for (int i = 0; i < answerList.size(); i++) {
				Map answer = answerList.get(i);
				answer.put("stuid", maps.get("stuid"));
				answer.put("read_id", maps.get("read_id"));
				//判断是否存在此阅读的答案
				Map<String, Object> map = learn_di.findLearnAnswer(answer);//read_qid;stuid;read_id
				if(map == null || map.isEmpty()) {
					learn_di.saveLearnAnswer(answer);//保存学习阅读答案表
				}else {
					learn_di.updateLearnAnswer(answer);
				}
				
				hashMap.put("score", score+=Double.valueOf((String) answer.get("learn_score")));
			} 
			maps.put("score", hashMap.get("score"));
			Map<String, Object> list = learn_di.findLearnRead(maps);
			if(list == null || list.isEmpty()) {
				learn_di.saveLearnRead(maps);
			}else {
				double score1 = (double) list.get("score");
				double score2 = (double) maps.get("score");
				if(score2 > score1) {
					learn_di.updateLearnRead(maps);
				}
			}	
		} catch (IOException e) {
			e.printStackTrace();
	 		throw new RuntimeException(e.getMessage());
		}
		return hashMap;
	}
	
	//显示阅读的学习
	public List<Map<String, Object>> findLearnRead(Map<String, String> parameterStringMap){
		List<Map<String, Object>> hashMap=null;//传递给后台的   stuid;read_id
		hashMap = learn_di.findLearnReadID(parameterStringMap);
		return hashMap;
	}
	
	//保存听力的学习
	@Transactional
	public Map<String, Object> saveLearnListen(Map<String, String> parameterStringMap){
		//json={listen_id;type;stuid;listen_type;listen_questId;learn_video;learn_score}
		double score =0;
		Map<String, Object> hashMap =new HashMap<String, Object>();
		hashMap.put("stuid", parameterStringMap.get("stuid"));
		hashMap.put("listen_id", parameterStringMap.get("listen_id"));
		hashMap.put("listen_type", parameterStringMap.get("listen_type"));
		hashMap.put("type", parameterStringMap.get("type"));
		//判断是否存在此听力
		Map<String, Object> map = learn_di.findLearnListen(parameterStringMap);//read_qid;stuid;read_id
		if(map == null || map.isEmpty()) {
			learn_di.saveLearnListenAnswer(parameterStringMap);
			hashMap.put("score", score+=Double.valueOf((String) parameterStringMap.get("learn_score")));
			
			learn_di.saveLearnListen(hashMap);//保存学习阅读答案表
		}else {
			score =(double) map.get("score");
			Map<String, Object> request = learn_di.findLearnListenAnswer(parameterStringMap);
			if(request == null || request.isEmpty()) {
				learn_di.saveLearnListenAnswer(parameterStringMap);
				hashMap.put("score", score+=Double.valueOf((String) parameterStringMap.get("learn_score")));
			}else {
				learn_di.updateLearnListenAnswer(parameterStringMap);
				score-=(Double)request.get("learn_score");
				score+=Double.valueOf((String) parameterStringMap.get("learn_score"));
				hashMap.put("score", score);
			}
			
			learn_di.updateLearnListen(hashMap);//保存学习阅读答案表
		}
		
		
		
		hashMap.put("flag", "添加成功");
		return hashMap;
			
	}
	
	//显示听力的学习
	public List<Map<String, Object>> findLearnListen(Map<String, String> parameterStringMap){
		List<Map<String, Object>> hashMap=null;//传递给后台的   stuid;listen_id
		hashMap = learn_di.findLearnListenID(parameterStringMap);
		return hashMap;
	}
}
