package com.xljy.sys.servers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xljy.sys.dao.ZiDian_di;
@Service
public class ZiDian_sm {

	@Autowired
	private ZiDian_di zidian_di;
	
	
	public List<Map<String, Object>> chazidian(Map<String, String> parameterStringMap) {
		return  zidian_di.chazidian(parameterStringMap);
		
		
	}


	public List<Map<String, Object>> getXueXiao(Map<String, String> parameterStringMap) {
		return  zidian_di.getXueXiao(parameterStringMap);
	}


	public List<Map<String, Object>> getjiaocai(Map<String, String> parameterStringMap) {
		return zidian_di.getjiaocai(parameterStringMap);
	}

}
