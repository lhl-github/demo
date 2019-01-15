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
import com.xljy.kwld.dao.TextType_di;
import com.xljy.kwld.servers.Talk_sm;
import com.xljy.util.RequestParamsToMap;

@Controller
@RequestMapping("/talk")
public class Talk_cm {

	@Autowired
	private Talk_sm talk_sm;
	
//查询
	@RequestMapping("/findByTid")
	@ResponseBody
	public Result findByTid(HttpServletRequest request) {
		Result res=null;
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		try {
			if(parameterStringMap.containsKey("pageNumber")==true&& parameterStringMap.containsKey("pageSize")==true) {
			    int pageNumber = Integer.parseInt(parameterStringMap.get("pageNumber"));
			    int pageSize = Integer.parseInt(parameterStringMap.get("pageSize"));
			    Page<Object> startPage = PageHelper.startPage(pageNumber, pageSize,"nj_d*1,sxc_d*1,dy_d*1");
			    List<Map<String, Object>> list=talk_sm.findByTalkByTid(parameterStringMap);
			    res = Result.resultSuccess(list, "");
			    res.setPageInfo(startPage.getTotal());
			    res.setCode(200);
			}else {
				Map<String, Object> list=talk_sm.findByTk(parameterStringMap);
			    res = Result.resultSuccess(list, "");
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
//删除角色对话
	@RequestMapping("/delByJsTalk")
	@ResponseBody
	public Result delByJsTalk(HttpServletRequest request) {
		Result res=null;
		Map<String, String>  parameterStringMap=RequestParamsToMap.getParameterStringMap(request);
		try {
			int num=talk_sm.delByJsTalk(parameterStringMap);
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
//删除对话
	@RequestMapping("/delByTalk")
	@ResponseBody
	public Result delByTalk(HttpServletRequest request) {
		Result res=null;
		Map<String, String>  parameterStringMap=RequestParamsToMap.getParameterStringMap(request);
		try {
			Map<String, Object> talk=talk_sm.delByTalk(parameterStringMap);
			res=Result.resultSuccess(talk, "success");
			res.setCode(200);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
//修改或者添加对话
	@RequestMapping("/addTalk")
	@ResponseBody
	public Result addTalk(HttpServletRequest request) {
		Result res=null;
		Map<String, String>  parameterStringMap=RequestParamsToMap.getParameterStringMap(request);
		try {
			Map<String, Object> map=talk_sm.addTalk(parameterStringMap);
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
