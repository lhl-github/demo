package com.xljy.sys.servers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xljy.sys.dao.DiQu_di;

@Service
public class DiQu_s {

	@Autowired
	private DiQu_di diqu_di;

	public List<Map<String, Object>> ChaDiQuListByJiBie(Map<String, String> parameterStringMap) {
		return diqu_di.ChaDiQuListByJiBie(parameterStringMap);

	}

}
