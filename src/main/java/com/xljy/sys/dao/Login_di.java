package com.xljy.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface Login_di {
	/*@Select("SELECT * FROM table1")
	public  List< Map<String, Object>>  getLogUser(String key);*/

	public List<Map<String, Object>> getLogUser(Map<String, String> parameterStringMap);

	public List<Map<String, Object>> findjuese(String userid);

	public List<Map<String, Object>> findcaidan(@Param("juese") List<Map<String, Object>> juese);

	public int find_dianhuahao_cz(String shoujihao);//验证手机号在库中码   --未做实现

	public List<Map<String, Object>> getLogUser_weixin(Map<String, String> parameterStringMap);

	public List<Map<String, Object>> getLogUser_shouji(Map<String, String> parameterStringMap);

	public int up_bd_weixin_id(@Param("user_id")Object Id, @Param("map")Map<String, String> parameterStringMap);

	public int zc_user(Map<String, String> parameterStringMap);

}
