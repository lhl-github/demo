package com.xljy.student.text.servers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xljy.student.text.dao.Homework_di;
import com.xljy.student.text.dao.StuNews_di;

@Service
public class StuNews_sm {

	@Autowired
	private StuNews_di news_di;
	@Autowired
	private Homework_di hw_di;
	
	//当进入主页时，有个铃铛会显示有未读的消息多少条（本用户下的未读消息个数）
	public int findByUnReadCount(Map parameterStringMap) {
		
		return news_di.findByUnReadCount(parameterStringMap);
	}
	
	//查看全部消息（本用户下的所有消息）全部需要传stuid；
	public List<Map<String, Object>> findAllByNews(Map parameterStringMap){
		
		return news_di.findAllByNews(parameterStringMap);
	}
	
	//查询单个消息，进行详细查看后，将未读变已读
	@Transactional
	public  Map<String, Object> findByOne(Map<String, String> parameterStringMap){
		 Map<String, Object> map = news_di.findByOneNews(parameterStringMap);
		int num = news_di.updateByUnRead(parameterStringMap);//
		Map<String, Object> xiaoxi = null;
	
		if(map.get("newtype").equals("作业消息")) {
			 xiaoxi = hw_di.findByHwid(map);
			 if(xiaoxi == null || xiaoxi.isEmpty()) {
				 map.put("flag","此作业被删除");
			 }else {
				 map.put("homeworkName", xiaoxi.get("homeworkName"));
				 map.put("end", xiaoxi.get("end"));
			 }
			 
		}
		return map;
	}
	//表示为已读  或者   删除
	@Transactional
	public Map<String, Object> tagAll(Map<String, String> parameterStringMap){
		Map<String, Object> hashMap=new HashMap<>();
		String json = parameterStringMap.get("json");
		Map map=null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			map=mapper.readValue(json, Map.class);//json={"flag":"read/delete"，"news":[{"id":""},{},]}
			List<Map> newsList = (List<Map>) map.get("news");
			if(map.get("flag").equals("read") || map.containsValue("read")) {
				for (int i = 0; i < newsList.size(); i++) {
					Map news = newsList.get(i);
					news_di.updateByUnRead(news);
				}
				hashMap.put("flag", "标记已读");
			}else if(map.get("flag").equals("delete") || map.containsValue("delete")) {
				for (int i = 0; i < newsList.size(); i++) {
					Map news = newsList.get(i);
					news_di.delByNews(news);
				}
				hashMap.put("flag", "删除");
			}else {
			hashMap.put("flag", "未选择任何数据！");
			}
		} catch (IOException e) {
			e.printStackTrace();
	 		throw new RuntimeException(e.getMessage());
		}
		return hashMap;
	}
}
