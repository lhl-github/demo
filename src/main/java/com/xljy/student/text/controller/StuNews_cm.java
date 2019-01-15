package com.xljy.student.text.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xljy.bean.Result;
import com.xljy.student.text.servers.StuNews_sm;
import com.xljy.student.text.servers.StuText_sm;
import com.xljy.util.RequestParamsToMap;

@Controller
@RequestMapping("/student/news")
public class StuNews_cm {

	@Autowired
	private StuNews_sm new_sm;
	
	@RequestMapping("/findByUnReadCount")
	@ResponseBody
	public Result findByUnReadCount(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			int count = new_sm.findByUnReadCount(parameterStringMap);
			res=Result.resultSuccess(count, "success");
			res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/findAllByNews")
	@ResponseBody
	public Result findAllByNews(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			int pageNumber = Integer.parseInt(parameterStringMap.get("pageNumber"));
		    int pageSize = Integer.parseInt(parameterStringMap.get("pageSize"));
		    Page<Object> startPage = PageHelper.startPage(pageNumber, pageSize);
			List<Map<String, Object>> list = new_sm.findAllByNews(parameterStringMap);
			res=Result.resultSuccess(list, "success");
			res.setPageInfo(startPage.getTotal());
			res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	@RequestMapping("/findByOne")
	@ResponseBody
	public Result findByOne(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> map = new_sm.findByOne(parameterStringMap);
			res=Result.resultSuccess(map, "success");
			res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	@RequestMapping("/tagAll")
	@ResponseBody
	public Result tagAll(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			 Map<String, Object> map = new_sm.tagAll(parameterStringMap);
			 res = Result.resultSuccess(map, "success");
			 res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
}
