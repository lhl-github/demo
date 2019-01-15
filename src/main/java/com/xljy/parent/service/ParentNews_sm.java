package com.xljy.parent.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xljy.parent.dao.ParentNews_di;

@Service
public class ParentNews_sm {
	
	@Autowired
	private ParentNews_di news_di;
	
		//查看全部消息（本用户下的所有消息）全部需要传stuid；
		public List<Map<String, Object>> findAllByNews(Map parameterStringMap){
			List<Map<String, Object>> newList = news_di.findAllByNews(parameterStringMap);
			for (int i = 0; i < newList.size(); i++) {
				 Map<String, Object> hw = news_di.findByHomework(newList.get(i));
				 if(hw !=null && ! hw.isEmpty()) {
					 newList.get(i).put("homewordName", hw.get("homewordName"));
					 newList.get(i).put("endTime", hw.get("endTime"));
				 }
			}
			return newList;
		}
}
