package com.xljy.kwld.controller;

import java.util.ArrayList;
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
import com.xljy.kwld.servers.Sentence_sm;
import com.xljy.util.RequestParamsToMap;

@Controller
@RequestMapping("/sentence")
public class Sentence_cm {

	@Autowired
	private Sentence_sm sm;

	
	//所有(教材、学段、年级、上下册、单元)	
	@RequestMapping("findAllSentence")
	@ResponseBody
	public Result findAllSentence(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
     	Result res = null;
     	try {
     		if(parameterStringMap.containsKey("pageNumber")==true&& parameterStringMap.containsKey("pageSize")==true) {
     		    int pageNumber = Integer.parseInt(parameterStringMap.get("pageNumber"));
			    int pageSize = Integer.parseInt(parameterStringMap.get("pageSize"));
			    Page<Object> startPage = PageHelper.startPage(pageNumber, pageSize,"nj_d*1,sxc_d*1,dy_d*1");
			    List<Map<String, Object>> find =sm.findAllSentence(parameterStringMap);
     		    res = Result.resultSuccess(find, "success");
			    res.setPageInfo(startPage.getTotal());
			    res.setCode(200);
     		}else {
     			Map<String, Object> find =sm.findByIdSentence(parameterStringMap);
         		res = Result.resultSuccess(find, "success");
         		res.setCode(200);
         		res.setPageInfo(find.size());
     		}
			
     		
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
	
	//删
	@RequestMapping("/delSentence")
	@ResponseBody
	public Result delSentence(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num=sm.delSentence(parameterStringMap);
		
			res=Result.resultSuccess(num, "success");
			res.setCode(200);
			return res;	
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
	//更新
	@RequestMapping("/updateSentence")
	@ResponseBody
	public Result updateSentence(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
     	Result res = null;
     	try {
     		Map<String, Object> num=sm.updateSentence(parameterStringMap);
			res=Result.resultSuccess(num, "success");
			res.setCode(200);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
	//保存句子
	@RequestMapping("/addSentence")
	@ResponseBody
	public Result addSentence(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
     	Result res = null;
     	try {
     		Map<String, Object> map=sm.add(parameterStringMap);
    		res=Result.resultSuccess(map, "success");
     		res.setCode(200);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
}
