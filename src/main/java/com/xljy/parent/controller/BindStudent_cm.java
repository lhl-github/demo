package com.xljy.parent.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xljy.bean.Result;
import com.xljy.parent.dao.BindStudent_di;
import com.xljy.parent.service.BindStudent_sm;
import com.xljy.student.text.dao.Homework_di;
import com.xljy.student.user.servers.StuLogin_sm;
import com.xljy.sys.servers.Login_sm;
import com.xljy.util.RequestParamsToMap;

@Controller
@RequestMapping("/parent")
public class BindStudent_cm {

	@Autowired
	private BindStudent_sm bs_sm;
	@Autowired
	private StuLogin_sm login_sm;
	@Autowired
	private Homework_di hw_di;
	@RequestMapping("/findByPid")
	@ResponseBody
	public Result findByPid(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			List<Map<String, Object>> num = bs_sm.findByPid(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	//绑定学生帐号
	@RequestMapping("/bindStudent")
	@ResponseBody
	public Result bindStudent(HttpServletRequest request) {
	Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
	Result res=null;
	try {
		if(StringUtils.isEmpty(parameterStringMap.get("username"))) {
			res=Result.resultError("用户名不可为空");
			res.setCode(500);
			return res;
		}
		if(StringUtils.isEmpty(parameterStringMap.get("password"))) {
			res=Result.resultError("密码不可为空");
			res.setCode(500);
			return res;
		}
		List<Map<String, Object>> username = login_sm.findByUsername(parameterStringMap);
			if(username==null || username.isEmpty()) {
				res=Result.resultError("该学生不存在");
				res.setCode(500);
				return res;
			}
			List<Map<String, Object>> map = login_sm.login(parameterStringMap);
			if(map == null || map.isEmpty()) {
				res=Result.resultError("密码输入错误");
				res.setCode(500);
				return res;
			}else if(map.size()>1) {
				res=Result.resultError("存在多条数据");
				res.setCode(500);
				return res;
			}
			parameterStringMap.put("stuid", String.valueOf(map.get(0).get("stuid")));
			int num = bs_sm.saveParStu(parameterStringMap);
			res=Result.resultSuccess(num, "绑定成功");
			return res;
		 
	} catch (Exception e) {
		e.printStackTrace();
		res = Result.resultError(e.getMessage());
		res.setCode(500);
		return res;
	}
	}
	
	//添加学生帐号
	@RequestMapping("/addStu")
	@ResponseBody
	public Result addStu(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			int num = bs_sm.saveStuByPid(parameterStringMap);
			if(num==1) {
				List<Map<String, Object>> list = login_sm.login(parameterStringMap);
				parameterStringMap.put("stuid", String.valueOf(list.get(0).get("stuid")));
				int add = bs_sm.saveParStu(parameterStringMap);
				res = Result.resultSuccess(add, "success");
				res.setData2(String.valueOf(list.get(0).get("stuid")));
			}
			res.setCode(200);
			
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			if (StringUtils.contains(e.getMessage(), "Duplicate entry") && StringUtils.contains(e.getMessage(), "d101_03")) {// 联合主键重复
				res = Result.resultError("用户帐号已存在，请更换用户帐号");
			} else {
				res = Result.resultError(e.getMessage());
			}
			res.setCode(500);
			return res;
		}
	}
	
	@RequestMapping("/findByWeekLearn")
	@ResponseBody
	public Result findByWeekLearn(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			 Map<String, Object> map = bs_sm.findByWeekLearn(parameterStringMap);
			 res = Result.resultSuccess(map, "success");
			res.setCode(200);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
	
	@RequestMapping("/findClassRank")
	@ResponseBody
	public Result findClassRank(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			 Map<String, Object> map = bs_sm.findClassRank(parameterStringMap);
			 res = Result.resultSuccess(map, "success");
			res.setCode(200);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
	
	@RequestMapping("/payDetail")
	@ResponseBody
	public Result payDetail(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			 Map<String, Object> map = bs_sm.payDetail(parameterStringMap);
			 res = Result.resultSuccess(map, "success");
			res.setCode(200);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
	
	@RequestMapping("/homeworkByRank")
	@ResponseBody
	public Result homeworkByRank(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			 Map<String, Object> map = bs_sm.homeworkByRank(parameterStringMap);
			 res = Result.resultSuccess(map, "success");
			res.setCode(200);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
	
	@RequestMapping("/classWeekByRank")
	@ResponseBody
	public Result classWeekByRank(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		
		try {
			 Map<String, Object> map = bs_sm.classWeekByRank(parameterStringMap);
			 res = Result.resultSuccess(map, "success");
			res.setCode(200);
			Map<String, Object> learn = bs_sm.findByWeekHwShow(parameterStringMap);
			res.setData2(learn);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
	
	@RequestMapping("/findByHwCondition")
	@ResponseBody
	public Result findByHwCondition(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			bs_sm.findByHwCondition1(parameterStringMap);
			if(parameterStringMap.containsKey("pageNumber")==true&& parameterStringMap.containsKey("pageSize")==true) {
				int pageNumber = Integer.parseInt(parameterStringMap.get("pageNumber"));
				int pageSize = Integer.parseInt(parameterStringMap.get("pageSize"));
				Page<Object> startPage = PageHelper.startPage(pageNumber, pageSize);
				 List<Map<String, Object>> map = bs_sm.findByHwCondition(parameterStringMap);
				 res = Result.resultSuccess(map, "success");
				 res.setPageInfo(startPage.getTotal());
				
			}else {
				 List<Map<String, Object>> map = bs_sm.findByHwCondition(parameterStringMap);
				 res = Result.resultSuccess(map, "success");
			}
			res.setCode(200);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
	
	@RequestMapping("/findByHomeworkDetail")
	@ResponseBody
	public Result findByHomeworkDetail(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			 List<Map<String, Object>> map = bs_sm.findByHomeworkDetail(parameterStringMap);
			 res = Result.resultSuccess(map, "success");
			res.setCode(200);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
	
	@RequestMapping("/findDetail")
	@ResponseBody
	public Result findDetail(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			 List<Map<String, Object>> map = bs_sm.findDetail(parameterStringMap);
			 res = Result.resultSuccess(map, "success");
			res.setCode(200);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
	
	@RequestMapping("/findByWeekHwShow")
	@ResponseBody
	public Result findByWeekHwShow(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			 Map<String, Object> map = bs_sm.findByWeekHwShow(parameterStringMap);
			 res = Result.resultSuccess(map, "success");
			res.setCode(200);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
	
	@RequestMapping("/firstPage")
	@ResponseBody
	public Result firstPage(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			bs_sm.findByHwCondition1(parameterStringMap);
			Page<Object> startPage = PageHelper.startPage(1, 3);
			List<Map<String, Object>> conditionList = bs_sm.findByHwCondition(parameterStringMap);
			Map<String, Object> rank = bs_sm.findClassRank(parameterStringMap);
			Map<String, Object> learnReport = bs_sm.findByWeekLearn(parameterStringMap);
			res = Result.resultSuccess(conditionList, "success");
			res.setCode(200);
			res.setPageInfo(startPage.getTotal());
			res.setData2(rank);
			res.setData3(learnReport);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
}
