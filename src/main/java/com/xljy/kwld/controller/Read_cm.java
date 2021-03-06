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
import com.xljy.kwld.servers.Read_sm;
import com.xljy.util.RequestParamsToMap;

@Controller
@RequestMapping("/read")
public class Read_cm {
	@Autowired
	private Read_sm sm;
	
	//查询
	@RequestMapping("/findAll")
	@ResponseBody
	public Result findAll(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
     	Result res = null;
     	try {
     		if(parameterStringMap.containsKey("pageNumber")==true&& parameterStringMap.containsKey("pageSize")==true) {
     		
     			int pageNumber = Integer.parseInt(parameterStringMap.get("pageNumber"));
 			    int pageSize = Integer.parseInt(parameterStringMap.get("pageSize"));
 			    Page<Object> startPage = PageHelper.startPage(pageNumber, pageSize,"nj_d*1,sxc_d*1,dy_d*1");
 			    List<Map<String, Object>> list=sm.findAll(parameterStringMap);
 			    res = Result.resultSuccess(list, "success");
			    res.setPageInfo(startPage.getTotal());
			    res.setCode(200);
     		}else {
     			Map<String, Object> list=sm.findRead(parameterStringMap);
     			  res = Result.resultSuccess(list, "success");
  			    res.setPageInfo(list.size());
  			    res.setCode(200);
     		}
     		return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}

	//删除
	@RequestMapping("/delRead")
	@ResponseBody
	public Result delRead(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
     	Result res = null;
     	try {
     		 Map<String, Object> map=sm.delRead(parameterStringMap);
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
	//修改、保存
	@RequestMapping("/addRead")
	@ResponseBody
	public Result addRead(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
     	Result res = null;
     	try {
     		 Map<String, Object> map=sm.addRead(parameterStringMap);
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
