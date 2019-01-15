package com.xljy.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import com.xljy.bean.User;

public interface User_di {

	/** 查全部用户 */
	public List<Map<String, Object>> findall(Map<String, String> parameterStringMap);

	/**
	 *  根据用户ID进行查询用户
	 * @param parameterStringMap
	 * @return
	 */
	public Map<String, String> findUserById(Map<String, String> parameterStringMap);

	/**
	 *  添加用户
	 * @param parameterStringMap
	 * @return
	 */
	public int addUser(Map<String, String> parameterStringMap);

	/**
	 * 修改用户
	 * @param parameterStringMap
	 * @return
	 */
	public int updateUser(Map<String, String> parameterStringMap);

	/**
	 *  删除用户
	 * @param parameterStringMap
	 * @return
	 */
	public int delUserById(Map<String, String> parameterStringMap);

	/** 修改密码 */
	public int updatePW(Map<String, String> parameterStringMap);

	/**
	 * 家长关联孩子
	 * @param parameterStringMap
	 * @return
	 */
	@Insert("insert into d109 (d109_01,d109_02) values ( #{jiazhangId}, #{haiziId} )")
	// @SelectKey(statement="call next value for TestSequence",
	// keyProperty="id",
	// before=true, resultType=int.class)
	public int jiazhangAddhaizi(Map<String, String> parameterStringMap);

	/**
	 *  添加用户 _学生
	 * @param parameterStringMap
	 * @return
	 */
	public int addUser_stu(Map<String, String> parameterStringMap);

	/**
	 *  用户id 角色代码
	 * @param object
	 * @param string
	 */
	public void addUser_juese(@Param("user_id") Object object, @Param("juese_d") String string);

	public int addUser_xuesheng(Map<String, String> parameterStringMap);

	public int addUser_jiazhang(Map<String, String> parameterStringMap);

	public int addUser_jiaoyanyuan(Map<String, String> parameterStringMap);

	public int addUser_laoshi(Map<String, String> parameterStringMap);

	public int addUser_xiaozhang(Map<String, String> parameterStringMap);

	public int addUser_guanliyuan(Map<String, String> parameterStringMap);

	/**
	 * 验证用户存在
	 * 
	 * @param parameterStringMap
	 * @return
	 */
	public List<Map<String, Object>> findall_yz(Map<String, String> parameterStringMap);

	public List<Map<String, Object>> findall_yz_dianhua(Map<String, String> parameterStringMap);

	public int addbanji(Map<String, String> parameterStringMap);

	public int del_laoshi_banji(Map<String, String> parameterStringMap);

	public int del_jiazhang_xuesheng(Map<String, String> parameterStringMap);

	public int del_xuesheng_banji(Map<String, String> parameterStringMap);

	/**
	 * 通过班级码验证班级
	 * @param banji_d
	 * @return
	 */
	@Select("SELECT id banji_id, d107_01 banji_m, d107_02 cj_laoshi_id, d107_03 banji_d, d107_04 nianji_d, d107_05 sf_zdy FROM d107 where d107_03 =#{banji_d} ")
	public List<Map<String, Object>> findbanji_banji_d(String banji_d);// 通过班级代码查班级

	public int laoshi9banji(@Param("laoshi_id") String laoshi_id, @Param("banji_d") String banji_id);// 老师关联班级

	/**
	 * 学生关联班级
	 * 
	 * @param parameterStringMap
	 * @return
	 */
	@Insert(" INSERT INTO d112 ( d112_01, d112_02) VALUES (#{banji_d}, #{stu_id})")
	public int xuesheng9banji(Map<String, String> parameterStringMap);

	@Select("select * from d1015 where id = #{haiziId} ")
	public List<Map<String, Object>> find_yz_xuesheng(Map<String, String> parameterStringMap);

	@Select("select * from d107 where d107_03 = #{banji_d} ")
	public List<Map<String, Object>> find_yz_banji(Map<String, String> parameterStringMap);

	/**
	 * 通过账号验证用户存在
	 * 
	 * @param parameterStringMap
	 * @return
	 */
	public List<Map<String, Object>> findall_zhanghao_yz(Map<String, String> parameterStringMap);

	/**
	 * 学生的班级list
	 * 
	 * @param xuesheng_id
	 * @return
	 */
	@Select("select a.d107_03 banji_d ,a.d107_01 banji_m ,a.d107_04 nianji_d ,a.d107_05 sf_zdy from d107 a left join d112 b on b.d112_01= a.d107_03 where b.d112_02 =#{xuesheng_id}")
	public List<Map<String, Object>> findbanji4xuesheng(String xuesheng_id);

	/**
	 * list 班级 参数laoshi_id ,xuesheng_id , nianji_d, banji_m
	 * 
	 * @param parameterStringMap
	 * @return
	 */
	public List<Map<String, Object>> findall_banji(Map<String, String> parameterStringMap);

	@Delete("DELETE FROM d107 WHERE d107_03 = #{banji_d} ")
	public int delbanji(String banji_d);

	@Delete("DELETE FROM d113 WHERE d113_02 = #{banji_d} ")
	public int del_banji_laoshi(String banji_d);

	@Delete("DELETE FROM d112 WHERE d112_01 = #{banji_d} ")
	public int del_banji_xuesheng(String banji_d);

	public int update_banji(Map<String, String> parameterStringMap);
	// @Delete("DELETE FROM d112 WHERE d112_01 = #{banji_d} and d112_02 =
	// #{stu_id}
	// ")

	/**
	 * 删除班级下的多个学生 关联
	 * 
	 * @param banji_d
	 * @param list_stu_id
	 * @return
	 */
	public int delxuesheng4banji(@Param("banji_d") String banji_d, @Param("list_stu_id") List<Map> list_stu_id);

	public List<Map<String, Object>> findall_stu(Map<String, String> parameterStringMap);

	@Select(" SELECT COUNT(1) rc_wc,  SELECT ((SELECT COUNT(1)  FROM t102 WHERE  t102_02 ='1' )*(SELECT COUNT(1) FROM  d112 WHERE d112_01='班级id')) zy_zrc " + " FROM hw101 WHERE hw101_03 =#{zuoye_id} AND hw101_02 = ")
	public Map<String, Object> zy_wcd_dfl_5banji(Map<String, String> parameterStringMap);

	/**
	 * 正式卡 试用卡数
	 * @param parameterStringMap
	 * @return
	 */
	@Select(" select  COUNT(IF((d101_14='1'),TRUE,NULL)) rs_zsk,COUNT(IF((d101_14='2'),TRUE,NULL)) rs_syk  FROM d1015 where d1015.Id in(select d112_02 from d112 where d112_01=#{banji_d} ) ")
	public Map<String, Object> zsk_syk_5banji(Map<String, String> parameterStringMap);

	@Delete("DELETE FROM t102 WHERE t102_02 = #{banji_d} ")
	public int del_banji_zuoye(String banji_d);

	public Map<String, Object> laoshi_huizong(Map<String, String> parameterStringMap);

	@Select("SELECT id , wb10_01 wb_biaoti, wb10_02 wb_neirong FROM wb10 ")
	public List<Map<String, Object>> list_zx_bz();

	@Delete("DELETE FROM d112 WHERE d112_02 = #{stu_id} ")
	public int delbanji4xuesheng(@Param("stu_id") String string);

	public List<Map<String, Object>> list_danyuan(Map<String, String> parameterStringMap);

	// ------------------------------------------------------------------------------------
	public List<Map<String, Object>> list_ti_yinbiao(Map<String, String> parameterStringMap);

	public List<Map<String, Object>> list_ti_kewen(Map<String, String> parameterStringMap);

	public List<Map<String, Object>> list_ti_danci(Map<String, String> parameterStringMap);

	public List<Map<String, Object>> list_ti_duanyu(Map<String, String> parameterStringMap);//

	public List<Map<String, Object>> list_ti_jvzi(Map<String, String> parameterStringMap);

	public List<Map<String, Object>> list_ti_duihua(Map<String, String> parameterStringMap);

	public List<Map<String, Object>> list_ti_tingli(Map<String, String> parameterStringMap);

	public List<Map<String, Object>> list_ti_yuedu(Map<String, String> parameterStringMap);

	public List<Map<String, Object>> list_ti_zuowen(Map<String, String> parameterStringMap);

	/**
	 * 题详情
	 * 
	 * @param parameterStringMap
	 * @return
	 */
	public List<Map<String, Object>> xiangqing_ti(Map<String, String> parameterStringMap);

	@Select(" SELECT Id xuanxiang_id, k111_01 xuanxiangbiaoqian, k111_02 xuanxiangneirong, k111_04 xuanxiang_tupian, k111_05 xuanxiang_yinpin FROM k111 where 1=1 and k111_03 = #{wenti_id} order by xuanxiangbiaoqian ")
	public List<Map<String, Object>> Find_allwentixuanxiang(Map<String, Object> map);

	@Select(" SELECT Id xuanxiang_id, k126_02 xuanxiangbiaoqian, k126_04 xuanxiangneirong, k126_03 xuanxiang_tupian FROM k126 where 1=1 and k126_01 = #{wenti_id} order by xuanxiangbiaoqian ")
	public List<Map<String, Object>> Find_allwentixuanxiang_yuedu(Map<String, Object> map);

	@Select("SELECT Id yaodian_id, k112_01 yaodian_biaoti, k112_02 yaodian_neirong FROM k112 where 1=1 and k112_03 = #{wenti_id}")
	public List<Map<String, Object>> find_allyaodian(Map<String, String> parameterStringMap);

	@Select(" SELECT Id zhuyidian_id, k113_01 zhuyidian_neirong FROM k113 where 1=1 and k113_02 = #{wenti_id} ")
	public List<Map<String, Object>> find_allzhuyidian(Map<String, String> parameterStringMap);

	@Select(" SELECT id zuoye_id, t101_13 zuoye_buzhi_status  FROM t101 where 1=1 and t101_09 = #{laoshi_id} and t101_13='0'")
	public List<Map<String, Object>> find_buzhi_zuoye_wwc(Map<String, String> parameterStringMap);

	// @Insert("insert into t101 (id,t101_03,t101_09,t101_13) values
	// (#{zuoye_id},#{nianji_d},#{laoshi_id},'0')")
	public int add_zuoye(@Param("zuoye_id") Object zuoye_id, @Param("laoshi_id") Object laoshi_id, @Param("jiaocai_d") String nianji_d);

	/**
	 * 点布置作业 查询作业的详情列表
	 * @param map
	 * @return
	 */
	@Select(" SELECT t104_02 neirong_type_d, (SELECT d111_01 FROM d111 WHERE d111_02='题目大类' AND d111_03=t104_02) neirong_type_m,"
			+ " t104_03 ti_id, t104_05 danyuan_id, "
			+ "( SELECT d111_01 FROM d111 WHERE d111_02 ='单元' AND d111_03 = (SELECT t05 FROM  texttype WHERE texttype.type =t104_05)) danyuan_m, "
			+ "t104_06 jiaocai_d , ( SELECT DISTINCT CONCAT((SELECT d111_01 FROM d111 WHERE d111_02='教材类型' AND  d111_03=t01 ),(SELECT d111_01 FROM d111 WHERE d111_02='年级' AND  d111_03=t03 ),(SELECT d111_01 FROM d111 WHERE d111_02='上下册' AND  d111_03=t04 )) jiaocai_m FROM texttype WHERE texttype.type= t104_05) jiaocai_m, "
			+ "t104_07 zuoye_neirong_m FROM t104 WHERE 1=1 AND t104_04 = #{zuoye_id} ")
	public List<Map<String, String>> find_xiangqing_zuoye(Map<String, Object> map);

	@Insert("insert into t104 (t104_02,t104_03,t104_04,t104_05,t104_06,t104_07) values (#{neirong_type_d},#{ti_id},#{zuoye_id},#{danyuan_id},#{jiaocai_d},#{zuoye_neirong_m})")
	public int buzhi_zuoye_add(Map<String, String> parameterStringMap);

	@Delete(" DELETE FROM t104 where  t104_02=#{neirong_type_d} and t104_03=#{ti_id} and t104_04=#{zuoye_id} and t104_05=#{danyuan_id} and t104_06=#{jiaocai_d} ")
	public int buzhi_zuoye_del(Map<String, String> parameterStringMap);

	@Update(" UPDATE t101  SET t101_13 = '1',t101_14 =NOW() WHERE id = #{zuoye_id} and t101_09 = #{laoshi_id}")
	public int update_zuoye_over(Map<String, String> parameterStringMap);

	public int banji_zuoye_over(@Param("arr") String[] banji_d_arr, @Param("map") Map<String, String> parameterStringMap);

	/**
	 * 班级作业消息(给学生端)
	 * @param banji_d_arr
	 * @param xiaoxi_neirong
	 * @param zuoye_id
	 * @return
	 */
	public int banji_zuoye_xiaoxi_over(@Param("arr") String[] banji_d_arr, @Param("xiaoxi_neirong") String xiaoxi_neirong, @Param("zuoye_id") String zuoye_id);

	/**
	 * 班级作业消息(给家长端)
	 * @param banji_d_arr
	 * @param xiaoxi_neirong
	 * @param zuoye_id
	 * @return
	 */
	public int banji_zuoye_xiaoxi_over_jz(@Param("arr") String[] banji_d_arr, @Param("xiaoxi_neirong") String xiaoxi_neirong, @Param("zuoye_id") String zuoye_id);

	@Select("select id zuoye_id, t102_08 zuoye_m ,t102_10 zuoye_lx_d," 
			+ "(select d111_01 from d111 where d111_02='作业类型' and d111_03=t102_10) zuoye_lx_m," 
			+ "t102_07 zuoye_beizhu," 
			+ "t102_09 zuoye_yjwcsj, "// 预计完成时间
			+ "t102_02 banji_d," 
			+ "(select d107_01 from d107 where d107_03=t102_02) banji_m," 
			+ " (select d111_01 from d111 where d111_02='年级' and d111_03=(select d107_04 from d107 where d107_03=t102_02))nianji_m,"
			+ " date_format(t102_04, '%Y-%m-%d %T') zuoye_time_s," 
			+ " date_format(t102_05, '%Y-%m-%d %T') zuoye_time_e, " 
			+ " (CASE  WHEN  (NOW()- t102_04 )>0 AND (NOW()- t102_05 )>0 THEN '1' "
			+ " WHEN  (NOW()- t102_04 )>0 AND (NOW()- t102_05 )<0 THEN '0' " 
			+ " WHEN  (NOW()- t102_04 )<0 AND (NOW()- t102_05 )<0 THEN '-1' END) zuoye_status,"
			+ " ( select IFNULL(ROUND(AVG(hw101_04),2) ,0) pjf FROM hw101 WHERE hw101_03 =t101.id AND hw101_02 IN (select d112_02 from d112 where d112_01=t102_02 ))zy_bj_defen,"// 作业班级得分
			+ " (select count(1) from d112 where d112_01 =t102_02)banji_rs, " // 班级人数
			+ " if(t102_06 is not null,t102_06,0) zuoye_wcrs "// 完成人数
			+ " from t101 left JOIN t102 on t102_03=t101.id where 1=1 and t101_13='1' and t101_09=#{laoshi_id} ") // t101_13='1'
																													// 完成布置的
	public List<Map<String, Object>> list_zuoye(Map<String, String> parameterStringMap);

	@Delete("DELETE FROM t102 where t102_02 =#{banji_d} and t102_03=#{zuoye_id}")
	public int del_zuoye_banji(Map<String, String> parameterStringMap);

	@Select(" select count(1) from t102 where t102_03 =#{zuoye_id} ")
	public int shu_zuoye_banji(Map<String, String> parameterStringMap);

	@Delete(" DELETE FROM t101 where id =#{zuoye_id} ")
	public int del_zuoye(Map<String, String> parameterStringMap);

	public int upd_zuoye_banji(Map<String, String> parameterStringMap);

	@Delete(" DELETE FROM t104 where t104_04 =#{zuoye_id} ")
	public int clean_zuoye(Map<String, String> parameterStringMap);

	/*
	 * @Update(" UPDATE t101  SET t101_03 = #{jiaocai_d} WHERE id = #{zuoye_id} and t101_09 = #{laoshi_id} and t101_13='0' "
	 * ) public int upd_zuoye(Map<String, String> parameterStringMap);
	 */

	// @Select("SELECT DATE_FORMAT(t102_11, '%Y-%m-%d') zuoye_day, t102_03
	// zuoye_id
	// FROM t102 GROUP BY DATE_FORMAT(t102_11, '%Y-%m-%d') ORDER BY t102_11")
	public List<Map<String, Object>> jc_zuoye_shijian_list(@Param("arr") String[] arr_neirong_type_d, @Param("map") Map<String, String> parameterStringMap);

	public List<Map<String, Object>> jc_zuoye_banji(@Param("zuoye_status") String string, @Param("arr") String[] arr_neirong_type_d, @Param("map") Map<String, String> map ,@Param("zy_bz_o_time") String zy_bz_o_time);

	/**
	 * 完成人数 平均分
	 * 
	 * @param parameterStringMap
	 * @return
	 */
	@Select(" SELECT COUNT(1) zrs,IFNULL(ROUND(AVG(hw101_04),2) ,0)   pjf " + "FROM hw101 WHERE hw101_03 =#{zuoye_id} AND hw101_02 IN (select d112_02 from d112 where d112_01=#{banji_d} ) ")
	public Map<String, Object> find_wcrs_pjfs(Map<String, String> parameterStringMap);

	/**
	 * 0-20,20-40,40-60,60-80,80-100 的人数分布   横坐标
	 * 
	 * @param parameterStringMap
	 * @return
	 */
	@Select(" SELECT COUNT(IF((hw101_04 >=0 AND hw101_04 <20),TRUE,NULL)) '0~20'," + " COUNT(IF((hw101_04 >=20 AND hw101_04 <40),TRUE,NULL)) '20~40'," + " COUNT(IF((hw101_04 >=40 AND hw101_04 <60),TRUE,NULL)) '40~60',"
			+ " COUNT(IF((hw101_04 >=60 AND hw101_04 <80),TRUE,NULL)) '60~80'," + " COUNT(IF((hw101_04 >=80 AND hw101_04 <=100),TRUE,NULL)) '80~100'"
			+ " FROM hw101 WHERE hw101_03 =#{zuoye_id} AND hw101_02 IN (select d112_02 from d112 where d112_01=#{banji_d} ) ")
	public Map<String, Object> find_fs_rs_fb(Map<String, String> parameterStringMap);

	/**
	 * 未完成学生名单
	 * 
	 * @param parameterStringMap
	 * @return
	 */
	@Select(" SELECT  d101_01 stu_m,  d101_03 stu_zh FROM  d1015 WHERE  Id IN (SELECT d112_02 FROM   d112 WHERE d112_01 =#{banji_d} AND NOT EXISTS (SELECT 1 FROM hw101 WHERE hw101_02 = d112_02 and hw101_03 =#{zuoye_id})) ")
	public List<Map<String, Object>> find_wwc_stu_list(Map<String, String> parameterStringMap);

	/**
	 * 教师的未读消息
	 * 
	 * @param parameterStringMap
	 * @return
	 */
	@Select(" select count(1) xx_wds from t103 where 1=1 and t103_05=#{laoshi_id} and t103_04 ='0' ")
	public Map<String, Object> xx_count(Map<String, String> parameterStringMap);

	public List<Map<String, Object>> xx_list(Map<String, String> parameterStringMap);

	public int del_laoshi_xx(@Param("arr") String[] arr_xx_id);

	@Select(" select yb100_00 yinbiao_id,yb100_01 yinbiao_m,yb100_02 yinbiao_type_d,yb100_03 yinbiao_yinpin,"
			+ "(case when EXISTS(select 3 from t104 where t104_02 = #{neirong_type_d} and t104_03 =yb100_00 and t104_04 in(select id from t101 where t101_09=#{laoshi_id} AND t101_13='1')  ) >0 then '1' else '0' end) sf_old,"// 是否是已经布置的内容(布置过吗?1是0否)
			+ "(case when EXISTS(select 3 from t104 where  t104_04= #{zuoye_id}  and t104_02 = #{neirong_type_d} and t104_03 =yb100_00 ) >0 then '1' else '0' end) sf_xz,"// 是否选中(1是0否)
			+ "CONCAT('yinbiao/',yb100_00,'/') yb_savepath" + " from yb100 ")
	public List<Map<String, Object>> list_yinbiao(Map<String, String> parameterStringMap);

	/**
	 * 统计学生加入班级数
	 * 
	 * @param parameterStringMap
	 * @return
	 */
	@Select(" select count(1)  from d112 where d112_02=#{stu_id} ")
	public int xuesheng9banji_count(Map<String, String> parameterStringMap);

	/**
	 * 音标的发音示例
	 * 
	 * @param map
	 * @return
	 */
	@Select(" select yb101_01 yb_danci_shili,yb101_02 yb_danci_jieshi,yb101_03 yb_fayin_tu,yb101_04 yb_fayin_ms, yb101_05 yb_fayin_ys ,yb101_07 yb_leixing_ci  from yb101 where yb101_06=#{yinbiao_id} ")
	public List<Map<String, Object>> list_fayinshili(Map<String, Object> map);

	/**
	 * 音标的发音详情表
	 * 
	 * @param map
	 * @return
	 */
	@Select(" select yb102_02 yb_jieshao,yb102_03 yb_fayin_donghua,yb102_04 yb_fayin_zhenren  from yb102 where yb102_01 =#{yinbiao_id} ")
	public List<Map<String, Object>> list_fayinxiangqing(Map<String, Object> map);

	/**
	 * 音标的发音相关的表
	 * 
	 * @param map
	 * @return
	 */
	@Select(" select yb103_00, yb103_02 yb_fayin_xiangguan,yb103_03 yb_fayin from yb103 where yb103_01 =#{yinbiao_id} ")
	public List<Map<String, Object>> list_fayinxiangguan(Map<String, Object> map);

	/**
	 * 音标的发音相关的单词
	 * 
	 * @param map
	 * @return
	 */
	@Select(" select yb104_02 danci,yb104_03 danci_yb ,yb104_04 danci_fayin from yb104 where yb104_01 =#{yb103_00} ")
	public List<Map<String, Object>> list_yb_danci(Map<String, Object> map);

	/**
	 * 家长手机号找学生id
	 * 
	 * @param user_dianhua
	 * @return
	 */
	@Select(" select d109_01 jiazhang_id, d109_02 stu_id from d109 where d109_01 in( select Id  jiazhang_id from d1014 where d101_12 =#{user_dianhua}) ")
	public List<Map<String, Object>> find_stu_by_jiazhang_shoujihao(String user_dianhua);

	/**
	 * 找作业结束的
	 * 
	 * @param user_dianhua
	 * @return
	 */
	@Select("  select t102_01 zy_bj_id , (select d107_01 from d107 where d107_03=a.t102_02) banji_m , t102_08 zuoye_m ,t102_06 zuoye_wcrs from t102 a where 1=1 and t102_12!='9' and (t102_05-NOW())<0 and t102_03 in(select id zuoye_id from t101 where t101_09=#{laoshi_id}) ")
	public List<Map<String, Object>> find_zy_jieshu(Map<String, String> parameterStringMap);

	/**
	 * 修改作业结束的状态为 结束
	 * @param find_zy_jieshu
	 * @return
	 */
	public int upd_zy_jieshu(List<Map<String, Object>> find_zy_jieshu);

	/**
	 * 创建作业结束消息
	 * @param find_zy_jieshu
	 * @param laoshi_id
	 * @return
	 */
	public int ins_xx_zy_jieshu(@Param("list") List<Map<String, Object>> find_zy_jieshu, @Param("laoshi_id") String laoshi_id);

	/**
	 * 解绑微信
	 * @param parameterStringMap
	 * @return
	 */
	public int out_weixin_id(Map<String, String> parameterStringMap);

	/**
	 * 绑定微信
	 * @param parameterStringMap
	 * @return
	 */

	public int bd_weixin_id(Map<String, String> parameterStringMap);

	/**
	 * 改消息 为已读消息
	 * @param parameterStringMap
	 * @return
	 */
	public int upd_xx_yidu(@Param("arr") String[] arr_xx_id);

	/**
	 * 作业学生成绩(全部学生)
	 * @param parameterStringMap
	 * @return
	 */

	@Select(" SELECT hw101_04  fs, " + "( SELECT t102_08 FROM  t102 WHERE t102_03=#{zuoye_id} AND t102_02=#{banji_d} ) zuoye_m," + " ( SELECT d101_01 FROM  d1015 WHERE hw101_02=d1015.Id ) stu_m,"
			+ " ( (SELECT COUNT(1) FROM  hw102 WHERE hw101_03=hw102_01 )/(SELECT  COUNT(1) FROM t104 WHERE t104_04=#{zuoye_id}) ) zuoye_wcd"
			+ " FROM hw101 WHERE hw101_03 =#{zuoye_id} AND hw101_02 IN (SELECT d112_02 FROM d112 WHERE d112_01=#{banji_d})")
	public List<Map<String, Object>> zuoye_stu_cj(Map<String, String> parameterStringMap);

	@Insert("insert into dd101 () values ()")
	public int add_dd(Map<String, String> parameterStringMap);

	@Delete("delete from d109 where d109_01=#{pid} and d109_02=#{stu_id} ")
	public int p_ubd_stu(Map<String, String> parameterStringMap);

	public int xiugaiUser_sj(Map<String, String> parameterStringMap);

}
