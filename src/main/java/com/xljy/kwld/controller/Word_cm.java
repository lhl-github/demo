package com.xljy.kwld.controller;


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
import com.xljy.kwld.servers.Word_sm;
import com.xljy.util.RequestParamsToMap;

@Controller
@RequestMapping("/word")
public class Word_cm {

	@Autowired
	private Word_sm word_sm;
	
	@RequestMapping("/findAllWord")
	@ResponseBody
	public Result findAllWord(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
     	Result res = null;
     	try {
     		if(parameterStringMap.containsKey("pageNumber")==true&& parameterStringMap.containsKey("pageSize")==true) {
     			int pageNumber = Integer.parseInt(parameterStringMap.get("pageNumber"));
			    int pageSize = Integer.parseInt(parameterStringMap.get("pageSize"));
			    Page<Object> startPage = PageHelper.startPage(pageNumber, pageSize,"nj_d*1,sxc_d*1,dy_d*1");
     	        List<Map<String, Object>> list=word_sm.findByType(parameterStringMap);
     	        res = Result.resultSuccess(list, "");
			    res.setPageInfo(startPage.getTotal());
			    res.setCode(200);
     		}else {
     			Map<String, Object> list=word_sm.findAllWord(parameterStringMap);
     	     	res = Result.resultSuccess(list, "");
     		    res.setCode(200);
     		   res.setPageInfo(list.size());
     		}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
	@RequestMapping("/delWordByType")
	@ResponseBody
	public Result delWordByType(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
     	Result res = null;
     	try {
     		Map<String, Object> list=word_sm.delWordByType(parameterStringMap);
     		res=Result.result(list, true, 200, "success");
     		return res;
     	} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
	@RequestMapping("/addWord")
	@ResponseBody
	public Result addWord(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
     	Result res = null;
     	try {
     		Map<String, Object> list=word_sm.addWord(parameterStringMap);
     		res=Result.result(list, true, 200, "success");
     		return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
}
