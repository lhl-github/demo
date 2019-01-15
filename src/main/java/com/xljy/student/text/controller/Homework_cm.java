package com.xljy.student.text.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xljy.bean.Result;
import com.xljy.student.text.dao.Homework_di;
import com.xljy.student.text.servers.Homework_sm;
import com.xljy.student.user.dao.StuLogin_di;
import com.xljy.student.user.servers.StuLogin_sm;
import com.xljy.util.RequestParamsToMap;

@Controller
@RequestMapping("/student/homework")
public class Homework_cm {

	@Autowired
	private Homework_sm hw_sm;
	@Autowired
	private Homework_di hw_di;
	@Autowired
	private StuLogin_sm stu_sm;
	@Autowired
	private StuLogin_di login_di;
	
	@RequestMapping("/findByHomework")
	@ResponseBody
	public Result findByHomework(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			//判断此用户是否为会员  不是会员购买会员
			 Map<String, Object> map = stu_sm.findByStuid(parameterStringMap);
			 if((map.get("memberState").equals("1") || map.get("memberState").equals("2") ) && new Date().before((Date)map.get("endtime"))) {
				
				 //判断是否添加班级
				 List<Map<String, Object>> classList = login_di.findByClass(parameterStringMap);
				 if(classList == null || classList.isEmpty()) {
					 throw new RuntimeException("请前去添加班级");
				 }
				 List<Map<String, Object>> num = hw_sm.findByHomework(parameterStringMap);
					res=Result.resultSuccess(num, "success");
				    res.setCode(200);
			 }else {
				 throw new RuntimeException("请前去购买会员");
			 }
			
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/findByHomeworkTwo")
	@ResponseBody
	public Result findByHomeworkTwo(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num = hw_sm.findByHomeworkTwo(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/findByHomeWorkDetail")
	@ResponseBody
	public Result findByHomeWorkDetail(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> homework = hw_di.findStuHomework(parameterStringMap);//stuid;hwid
			//学生是否完成作业，完成有数据，没完成没数据
			if(homework == null){	
				Map<String, Object> num = hw_sm.findByHwId(parameterStringMap);
	            res=Result.resultSuccess(num, "success");
			    res.setCode(200);  
			}else {
				
				Map<String, Object> list = hw_sm.findByHomeWork(parameterStringMap);
	            res=Result.resultSuccess(list, "success");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/saveHomeWork")
	@ResponseBody
	public Result saveHomeWork(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, String> num = hw_sm.saveHomeWork(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/submitHomeWork")
	@ResponseBody
	public Result submitHomeWork(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			int num = hw_sm.submitHomeWork(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/findDetail")
	@ResponseBody
	public Result findDetail(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num = hw_sm.findDetail(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
}
