package com.xljy.kwld.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xljy.bean.Result;
import com.xljy.kwld.servers.Text_sm;
import com.xljy.util.RequestParamsToMap;

@Controller
@RequestMapping("/text")
public class Text_cm {

	@Autowired
	private Text_sm text_sm;
	
	//
	@RequestMapping("/findAllText")
	@ResponseBody
	public Result findAllText(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
     	Result res = null;
     	try {
     		if(parameterStringMap.containsKey("pageNumber")==true&& parameterStringMap.containsKey("pageSize")==true) {
     		int pageNumber = Integer.parseInt(parameterStringMap.get("pageNumber"));
			int pageSize = Integer.parseInt(parameterStringMap.get("pageSize"));
			Page<Object> startPage = PageHelper.startPage(pageNumber, pageSize,"nj_d*1,sxc_d*1,dy_d*1");
     		List<Map<String, Object>> find=text_sm.findAllText(parameterStringMap);
     		res = Result.resultSuccess(find, "");
			res.setPageInfo(startPage.getTotal());
			res.setCode(200);
     		}else {
     			List<Map<String, Object>> find=text_sm.findAllText(parameterStringMap);
         		res = Result.resultSuccess(find, "");
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
	//
	@RequestMapping("/addText")
	@ResponseBody
	public Result addText(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
     	Result res = null;
     	try {
     		Map<String, Object> find=text_sm.addText(parameterStringMap);
     		res=Result.resultSuccess(find, "success");
			res.setCode(200);
			return res;	
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
	//
	@RequestMapping("delText")
	@ResponseBody
	public Result delText(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
     	Result res = null;
     	try {
     		Map<String,Object> num=text_sm.delText(parameterStringMap);
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
}
