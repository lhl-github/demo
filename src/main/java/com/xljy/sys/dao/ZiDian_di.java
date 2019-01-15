package com.xljy.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

public interface ZiDian_di {
	// @Select ("select * from d111" )
	public List<Map<String, Object>> chazidian(Map<String, String> parameterStringMap);

	public List<Map<String, Object>> getXueXiao(Map<String, String> parameterStringMap);

	@Select("SELECT DISTINCT CONCAT_WS('_',t01,t03,t04) jiaocai_d, CONCAT((SELECT d111_01 FROM d111 WHERE d111_02='教材类型' AND  d111_03=t01 ),(SELECT d111_01 FROM d111 WHERE d111_02='年级' AND  d111_03=t03 ),(SELECT d111_01 FROM d111 WHERE d111_02='上下册' AND  d111_03=t04 )) jiaocai_m ,t03 nianji_d FROM texttype ")
	public List<Map<String, Object>> getjiaocai(Map<String, String> parameterStringMap);
}
