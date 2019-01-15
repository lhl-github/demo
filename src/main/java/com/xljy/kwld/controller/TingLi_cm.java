package com.xljy.kwld.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xljy.bean.Result;
import com.xljy.bean.testlhl;
import com.xljy.kwld.servers.TingLi_sm;
import com.xljy.util.RequestParamsToMap;

@Controller
@RequestMapping("/TingLi")
public class TingLi_cm {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TingLi_sm tingli_sm;

	@RequestMapping("/FindAll")
	@ResponseBody
	public Result FindAll(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {
			int pageNumber = Integer.parseInt(parameterStringMap.get("pageNumber"));
			int pageSize = Integer.parseInt(parameterStringMap.get("pageSize"));
			Page<Object> startPage = PageHelper.startPage(pageNumber, pageSize, "nianji_d*1,shangxiace_d*1,danyuan_d*1");
			List<Map<String, Object>> find = tingli_sm.findall(parameterStringMap);
			res = Result.resultSuccess(find, "");
			res.setPageInfo(startPage.getTotal());
			res.setCode(200);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}

	}
	
	@RequestMapping("/Find_id")
	@ResponseBody
	public Result Find_id(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			List<Map<String, Object>> find = tingli_sm.Find_id(parameterStringMap);
			res = Result.resultSuccess(find, "");
			res.setCode(200);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}

	}
	
	@RequestMapping("/add")
	@ResponseBody
	public Result add(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = tingli_sm.add(parameterStringMap);
			
			res = Result.resultSuccess(find, "添加听力成功");
			res.setCode(200);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}

	}
	
	@RequestMapping("/xiugai")
	@ResponseBody
	public Result xiugai(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = tingli_sm.xiugai(parameterStringMap);
			res = Result.resultSuccess(find, "修改听力成功");
			res.setCode(200);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}

	}
	
	@RequestMapping("/del")
	@ResponseBody
	public Result del(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = tingli_sm.del(parameterStringMap);
			res = Result.resultSuccess(find, "删除成功");
			res.setCode(200);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}

	}
	
	
	@RequestMapping("/addtest")
	@ResponseBody
	public Result addtest(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		System.out.println(parameterStringMap.get("json"));
		System.out.println(parameterStringMap);
		
		String json = parameterStringMap.get("json");
		ObjectMapper mapper = new ObjectMapper();
		Map map = null;//获取到的数据
		try {
			map = mapper.readValue(json, Map.class);
			System.out.println(map);
		} catch (JsonParseException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}
		List<Map> object = (List<Map>) map.get("hhh");
		
		Result res = null;
		res=Result.resultSuccess(null, "");
		
		return res;
		

	}

}
