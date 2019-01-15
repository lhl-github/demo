package com.xljy.kwld.dao;

import java.util.List;
import java.util.Map;

public interface TextType_di {
    //添加类型
	int  addTextType(Map parameterStringMap);
	//查询，根据五个字段进行查询
	List< Map<String, Object>> findByFive(Map parameterStringMap);
	//修改
	int updatePhoto(Map parameterStringMap);
}
