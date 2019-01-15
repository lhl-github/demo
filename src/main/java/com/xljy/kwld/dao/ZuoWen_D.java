package com.xljy.kwld.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.xljy.bean.testlhl;

public interface ZuoWen_D {

	/* 查全部作文 */
	public List<Map<String, Object>> findall(Map<String, String> parameterStringMap);

	public List<Map<String, Object>> find_kewendanyuan(Map map);

	public int add_kewendanyuan(@Param("uuid_kewendanyuan") Object uuid_kewendanyuan, @Param("map") Map map);

	public int add_zuowen(@Param("uuid_zuowen") Object object, @Param("uuid_kewendanyuan") Object object2,
			@Param("map") Map tingli);

	public int add_yaodian(@Param("uuid_zuowen") Object object2, @Param("map") Map tingli_wenti);

	public void add_zhuyidian(@Param("uuid_zuowen") Object object, @Param("map") Map zuowen_zhuyidian);

	public List<Map<String, Object>> get_kewemdanyuan(HashMap<String, Object> hashMap);

	public List<Map<String, Object>> find_allyaodian(Map<String, String> parameterStringMap);

	public List<Map<String, Object>> find_allzhuyidian(Map<String, String> parameterStringMap);

	public void xiugai_zuowen(@Param("map") Map zuowen);

	public void xiugai_yaodian(Map zuowen_yaodian);

	public void del_yaodian(Object yaodian_id);

	public void xiugai_zhuyidian(Map zuowen_zhuyidian);

	public void del_zhuyidian(Object zhuyidian_id);

	@Delete("DELETE FROM k109 WHERE Id = #{zuowen_id}")
	public void del_zuowenid(String zuowen_id);

	@Delete("DELETE FROM k112 WHERE k112_03 = #{zuowen_id}")
	public void del_yaodian_zuowenid(String zuowen_id);

	@Delete("DELETE FROM k113 WHERE k113_02 = #{zuowen_id}")
	public void del_zhuyidian_zuowenid(String zuowen_id);

	@Select(" SELECT TYPE kewen_id, t01 jiaocaileixing_d, t02 xueduan_d, t03 nianji_d, t04 shangxiace_d, t05 danyuan_d, t06 xuexiao_d FROM texttype WHERE 1=1  and texttype.TYPE =(select k109_06 kewen_id  from  k109 where 1=1 and k109.Id = #{zuowen_id} ) ")
	public List<Map<String, Object>> find_kewendanyuan_uuid(Map map);

	@Select(" SELECT  CONCAT_WS('_',t01,t02,t03,t04,t05) save FROM texttype WHERE 1=1  AND texttype.TYPE =( select k109_06 kewen_id  from  k109 where 1=1 and k109.Id = #{zuowen_id} )")
	public Map<String, String> get_savepath(@Param("zuowen_id")String ti_id);

}
