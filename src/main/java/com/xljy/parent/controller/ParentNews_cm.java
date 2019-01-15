package com.xljy.parent.controller;

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
import com.xljy.parent.service.ParentNews_sm;
import com.xljy.util.RequestParamsToMap;

@Controller
@RequestMapping("/parent/news")
public class ParentNews_cm {

	@Autowired
	private ParentNews_sm new_sm;
	
	@RequestMapping("/findAllByNews")
	@ResponseBody
	public Result findAllByNews(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			List<Map<String, Object>> list = new_sm.findAllByNews(parameterStringMap);
			res=Result.resultSuccess(list, "success");
			res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
}
