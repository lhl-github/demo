package com.xljy.kwld.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.xljy.bean.testlhl;

public interface TingLi_di {

	/* 查全部听力 */
	public List<Map<String, Object>> findall(Map<String, String> parameterStringMap);

	public List<Map<String, Object>> find_kewendanyuan(Map map);

	public int add_kewendanyuan(@Param("uuid_kewendanyuan") Object uuid_kewendanyuan, @Param("map") Map map);

	public int add_tingli(@Param("uuid_tingli") Object object, @Param("uuid_kewendanyuan") Object object2,
			@Param("map") Map tingli);

	public int add_wenti(@Param("uuid_wenti") Object object, @Param("uuid_tingli") Object object2,
			@Param("timudalei") Object object3, @Param("map") Map tingli_wenti);

	/**
	 * 
	 * @param object
	 *            问题id
	 * @param object2
	 *            选项
	 */
	public void add_wenti_xuanxiang(@Param("uuid_wenti") Object object, @Param("map") Object object2);

	public List<Map<String, Object>> get_kewemdanyuan(HashMap<String, Object> hashMap);

	public List<Map<String, Object>> find_tingli(Object object, Object object2);

	public void xiugai_wenti(Map tingli_wenti);

	public void del_wenti(Map tingli_wenti);

	public void del_wenti_xuanxiang(Map tingli_wenti);

	public void xiugai_tingli(Map tingli);

	public void xiugai_wenti_xuanxiang(Map tingli_wenti);

	public void del_wenti_xuanxiang_all(Map tingli_wenti);

	/**
	 * 通过题id删除所有选项
	 * 
	 * @param ti_id
	 */
	public void del_xuanxuang_all(String ti_id);

	public void del_wenti_all(String ti_id);

	public void del_ti_tingli(String ti_id);

	public List<Map<String, Object>> Find_allwenti(Map<String, String> parameterStringMap);

	/**
	 * 通过问题id找选项
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> Find_allwentixuanxiang(Map<String, Object> map);

	@Select(" SELECT TYPE kewen_id, t01 jiaocaileixing_d, t02 xueduan_d, t03 nianji_d, t04 shangxiace_d, t05 danyuan_d, t06 xuexiao_d FROM texttype WHERE 1=1  and texttype.TYPE =( select type kewen_id  from  k101 where 1=1 and  k101.Id = #{tingli_id})")
	public List<Map<String, Object>> find_kewendanyuan_uuid(Map map);

	@Select(" SELECT  CONCAT_WS('_',t01,t02,t03,t04,t05) save FROM texttype WHERE 1=1  AND texttype.TYPE =( SELECT TYPE kewen_id  FROM  k101 WHERE 1=1 AND  k101.Id = #{tingli_id})")
	public Map<String, String> get_savepath(@Param("tingli_id")String ti_id);

}
