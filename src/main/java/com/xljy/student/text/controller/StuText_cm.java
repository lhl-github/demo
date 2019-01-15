package com.xljy.student.text.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xljy.bean.Result;
import com.xljy.student.text.servers.StuText_sm;
import com.xljy.util.RequestParamsToMap;

@Controller
@RequestMapping("/student/text")
public class StuText_cm {

	@Autowired
	private StuText_sm text_sm;
	
	@RequestMapping("/findByType")
	@ResponseBody
	public Result findByType(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			List<Map<String, Object>> list = text_sm.findByType(parameterStringMap);
			res = Result.resultSuccess(list, "success");
			res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/findBySortByText")
	@ResponseBody
	public Result findBySortByText(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			List<Map<String, Object>> list = text_sm.findBySortByText(parameterStringMap);
			res=Result.resultSuccess(list, "success");
			res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/findBySymbol")
	@ResponseBody
	public Result findBySymbol(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			List<Map<String, Object>> list = text_sm.findBySymbol(parameterStringMap);
			res=Result.resultSuccess(list, "success");
			res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/findByDy")
	@ResponseBody
	public Result findByDy(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			 List<Map<String, Object>> list = text_sm.findByDy(parameterStringMap);
			 res=Result.resultSuccess(list, "success");
				res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/detail")
	@ResponseBody
	public Result detail(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			List<Map<String, Object>> list = text_sm.detail(parameterStringMap);
			res=Result.resultSuccess(list, "success");
			res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
}
