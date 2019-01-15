package com.xljy.student.user.servers;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xljy.student.user.dao.StuLogin_di;
import com.xljy.util.Const;



@Service
public class StuLogin_sm {

	@Autowired
	private StuLogin_di login_di;
	
	//登录
	public List<Map<String, Object>> login(Map parameterStringMap){
		
		return login_di.login(parameterStringMap);
	}
	//判断是否存在此用户名
	public List<Map<String, Object>> findByUsername(Map parameterStringMap){
		
		return login_di.findByUsername(parameterStringMap);
	}
	//根据stuid查询学生
	public Map<String, Object> findByStuid(Map parameterStringMap){
		 Map<String, Object> map = login_di.findByStuid(parameterStringMap);
		 List<Map<String, Object>> list = login_di.findByClass(parameterStringMap);
		
		 if(list !=null && ! list.isEmpty()) {
			 map.put("classId",list.get(0).get("classId") );
		 }
		return map;
	}
	//学生信息的修改
	@Transactional
	public Map<String,Object> updateStuInfo(Map parameterStringMap) {
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合
		HashMap<String, Object> hashMap =new HashMap<>();//传递给后台的
		String save_path = "user" + "/" + "5" + "/" + parameterStringMap.get("stuid") + "/";
		
		hashMap.put("save_path", save_path);
		if(parameterStringMap.containsKey("stuTupian") == true && parameterStringMap.get("stuTupian").toString().length()>0) {
			files.add(parameterStringMap.get("stuTupian").toString());//将图片加入集合
			/*
			 * 数据库同步完成 处理多余文件
			 */
		    Object[] arr_files1 = files.toArray();
			String[] arr_files = ArrayUtils.toStringArray(arr_files1);
			String[] arr_tupian = Const.getyinpin(arr_files);// 得到图片的文件名集合
			String tupianfilePath = Const.getFilePath_Real(hashMap.get("save_path").toString(), "1");// 2音频
			Const.delfile_lujingxiabubaohan(hashMap, arr_tupian, tupianfilePath, "图片");
		}
		login_di.updateStuInfo(parameterStringMap);
		
		
		return hashMap;
	}
	//查询该用户是否为会员
	
	//购买会员
	@Transactional
	public int updateStuVip(Map parameterStringMap){
	
		//stuid;memberState=1;time=1,2,3
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String str = sdf.format(date);//时间存储为字符串
	    Timestamp starttime = Timestamp.valueOf(str);//设置起时间
	  //判断会员是否到期
	  	Map<String, Object> member = login_di.findByStuid(parameterStringMap);
	  	Timestamp end = (Timestamp) member.get("endtime");
	  	 Calendar curr = Calendar.getInstance();
	  	if(member.containsKey("endtime") && end.after(starttime) ) {
	  		   
	  		    SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy");
	  		    SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
	  		    SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
	  		    Integer endYear = Integer.valueOf(sdf0.format(end));
	  		    Integer endMonth = Integer.valueOf(sdf1.format(end));
	  		    Integer endDay = Integer.valueOf(sdf2.format(end));
	  		    curr.set(endYear+ Integer.valueOf((String) parameterStringMap.get("time")), endMonth-1,endDay);
			    Date date2=curr.getTime(); 
			    String str2 = sdf.format(date2);
			    Timestamp endtime = Timestamp.valueOf(str2);//设置起时间
			    parameterStringMap.put("starttime", member.get("starttime"));
			    parameterStringMap.put("endtime", endtime);
	  	}else {
	  	    
		    curr.set(Calendar.YEAR,curr.get(Calendar.YEAR)+ Integer.valueOf((String) parameterStringMap.get("time")));
		    Date date2=curr.getTime(); 
		    String str2 = sdf.format(date2);
		    Timestamp endtime = Timestamp.valueOf(str2);//设置起时间
		    
		    parameterStringMap.put("starttime", starttime);
		    parameterStringMap.put("endtime", endtime);
	  	}	
	    
	    int num = login_di.updateStuVip(parameterStringMap);
		return num;
	}
	
	//判断是否存在会员卡号及其密码
	public Map<String, Object> addCard(Map parameterStringMap){
		Map<String, Object> map = new HashMap<String, Object>();
		String time = login_di.findByCard(parameterStringMap);
		if(time == null || time.isEmpty()) {
			map.put("flag", "不存在此会员卡");
		}else {
			parameterStringMap.put("memberState", 1);
			parameterStringMap.put("time", time);
			this.updateStuVip(parameterStringMap);
			map.put("flag", "添加会员成功");
			login_di.delByCard(parameterStringMap);
		}
		return map;
	}
	
	//加入班级
	public List<Map<String, Object>> addClass(Map parameterStringMap){
		List<Map<String, Object>> list=null;//传递给后台的:infor
		if(parameterStringMap.get("infor").toString().length() == 11) {//手机号
			parameterStringMap.put("teacherPhone", parameterStringMap.get("infor"));
			list = login_di.findByTeacherPhone(parameterStringMap);
			if(list == null || list.isEmpty()) {
				throw new RuntimeException("该老师未创建班级");
			}
		}else {//班级代码
			parameterStringMap.put("classId", parameterStringMap.get("infor"));
			 list = login_di.findByClassId(parameterStringMap);
			 if(list == null || list.isEmpty()) {
				 throw new RuntimeException("此班级id不存在");
			 }
		}
		return list;
	} 
	//填老师手机号后 要进行对班级的选择
	@Transactional
	public int addTeaClass(Map parameterStringMap) {
		List<Map<String, Object>> list = login_di.findByClass(parameterStringMap);
		if(list == null || list.isEmpty()) {
			return login_di.saveByClassId(parameterStringMap);
		}else if(list.size() >1){
			throw new RuntimeException("发现该学生存在多个班级!");
		}else {
			return login_di.updateByClassId(parameterStringMap);
		}
	}
	//确认订单页面
	public List<Map<String, Object>> orders(Map parameterStringMap){
		List<Map<String, Object>> list =null;
		list = login_di.findByClass(parameterStringMap);
	
		return list;
	}
	
	//修改密码
	public int updatePassword(Map parameterStringMap){
		Map<String, Object> map =new HashMap<String, Object>();//afterpassword;password;username;stuid
		//判断是否存在此帐号 及  密码
		map.put("password", parameterStringMap.get("afterpassword"));
		map.put("username", parameterStringMap.get("username"));
		List<Map<String, Object>> list = login_di.login(map);
		if(list.size() == 0) {
			throw new RuntimeException("帐号或原来密码输入错误");
		}
		int num = login_di.updatePassword(parameterStringMap);
		return num;
		
	}
	//查询省
	public List<Map<String, Object>> findBySheng(Map parameterStringMap){
		return login_di.findBySheng(parameterStringMap);
	}
	//查询市
	public List<Map<String, Object>> findByShi(Map parameterStringMap){
		return login_di.findByShi(parameterStringMap);
	}
	//查询区
	public List<Map<String, Object>> findByQu(Map parameterStringMap){
		return login_di.findByQu(parameterStringMap);
	}
	//查询学校
	public List<Map<String, Object>> findBySchool(Map parameterStringMap){
		return login_di.findBySchool(parameterStringMap);
	}
	//修改学校
	public int updateSchool(Map parameterStringMap) {
		
		return login_di.updateSchool(parameterStringMap);
	}
	//退出班级
	public int delClass(Map parameterStringMap) {
		
		return login_di.delClass(parameterStringMap);
	}
}
