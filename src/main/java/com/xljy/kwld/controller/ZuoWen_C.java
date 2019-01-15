package com.xljy.kwld.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xljy.bean.Result;
import com.xljy.kwld.servers.ZuoWen_S;
import com.xljy.util.RequestParamsToMap;

@Controller
@RequestMapping("/zuowen")
public class ZuoWen_C {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ZuoWen_S zuowen_s;

	@RequestMapping("/find_all")
	@ResponseBody
	public Result login(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {
			int pageNumber = Integer.parseInt(parameterStringMap.get("pageNumber"));
			int pageSize = Integer.parseInt(parameterStringMap.get("pageSize"));
			Page<Object> startPage = PageHelper.startPage(pageNumber, pageSize, "nianji_m,shangxiace_m,danyuan_m");//按单元排序
			List<Map<String, Object>> find = zuowen_s.findall(parameterStringMap);
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
	
	@RequestMapping("/find_id")
	@ResponseBody
	public Result Find_id(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			List<Map<String, Object>> find = zuowen_s.Find_id(parameterStringMap);
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

			Map<String, Object> find = zuowen_s.add(parameterStringMap);
			
			res = Result.resultSuccess(find, "添加作文成功");
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

			Map<String, Object> find = zuowen_s.xiugai(parameterStringMap);
			res = Result.resultSuccess(find, "修改作文成功");
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

			Map<String, Object> find = zuowen_s.del(parameterStringMap);
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
	
	

}
