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
import com.xljy.kwld.servers.FollowText_sm;
import com.xljy.util.RequestParamsToMap;

@Controller
@RequestMapping("/followText")
public class FollowText_cm {

	@Autowired
	private FollowText_sm ft_sm;
	
	@RequestMapping("/findByFollowText")
	@ResponseBody
	public Result findByFollowText(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
     	Result res = null;
     	try {
     		if(parameterStringMap.containsKey("pageNumber")==true&& parameterStringMap.containsKey("pageSize")==true) {
     		
     			int pageNumber = Integer.parseInt(parameterStringMap.get("pageNumber"));
 			    int pageSize = Integer.parseInt(parameterStringMap.get("pageSize"));
 			    Page<Object> startPage = PageHelper.startPage(pageNumber, pageSize,"nj_d*1,sxc_d*1,dy_d*1");
 			    List<Map<String, Object>> list=ft_sm.findByFollowTextAll(parameterStringMap);
 			    res = Result.resultSuccess(list, "success");
			    res.setPageInfo(startPage.getTotal());
			    res.setCode(200);
     		}else {
     			Map<String, Object> list=ft_sm.findByFtid(parameterStringMap);
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
	
	@RequestMapping("/addFollowText")
	@ResponseBody
	public Result addFollowText(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
 	Result res = null;
 	try {
 		 Map<String, Object> map=ft_sm.addFollowText(parameterStringMap);
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

	@RequestMapping("/delFollowText")
	@ResponseBody
	public Result delFollowText(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
 	Result res = null;
 	try {
 		 Map<String, Object> map=ft_sm.delFollowText(parameterStringMap);
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
