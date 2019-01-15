package com.xljy.sys.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xljy.bean.Result;
import com.xljy.sys.servers.ZiDian_sm;
import com.xljy.util.RequestParamsToMap;


@Controller
@RequestMapping("/zidian")
public class ZiDian_cm  {

	@Autowired
	private ZiDian_sm zidian_sm;

	
	@RequestMapping("/getall")
	@ResponseBody
	public Result chazidian(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res = null;
		try {
			List<Map<String, Object>> find = zidian_sm.chazidian(parameterStringMap);
			res=Result.resultSuccess(find, "");
			res.setCode(200);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res=Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
	@RequestMapping("/getXueXiao")
	@ResponseBody
	public Result getXueXiao(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res = null;
		try {
			List<Map<String, Object>> find = zidian_sm.getXueXiao(parameterStringMap);
			res=Result.resultSuccess(find, "");
			res.setCode(200);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res=Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}
	
	@RequestMapping("/getjiaocai")
	@ResponseBody
	public Result getjiaocai(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res = null;
		try {
			List<Map<String, Object>> find = zidian_sm.getjiaocai(parameterStringMap);
			res=Result.resultSuccess(find, "");
			res.setCode(200);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res=Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}
	}

}
