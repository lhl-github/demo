package com.xljy.student.text.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xljy.bean.Result;
import com.xljy.student.text.servers.StuLearn_sm;
import com.xljy.util.RequestParamsToMap;

@Controller
@RequestMapping("/student/learn")
public class StuLearn_cm {

	@Autowired
	private StuLearn_sm learn_sm;
	
	@RequestMapping("/saveLearnBySymbol")
	@ResponseBody
	public Result saveLearnBySymbol(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num = learn_sm.saveLearnBySymbol(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/findLearnByYbid")
	@ResponseBody
	public Result findLearnByYbid(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num = learn_sm.findLearnByYbid(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/saveLearnBySymbolWord")
	@ResponseBody
	public Result saveLearnBySymbolWord(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num =learn_sm.saveLearnBySymbolWord(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/findLearnBySymbolWord")
	@ResponseBody
	public Result findLearnBySymbolWord(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num = learn_sm.findLearnBySymbolWord(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/saveLearnWord")
	@ResponseBody
	public Result saveLearnWord(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num = learn_sm.saveLearnWord(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/findLearnByWord")
	@ResponseBody
	public Result findLearnByWord(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num = learn_sm.findLearnByWord(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/saveLearnJS")
	@ResponseBody
	public Result saveLearnJS(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num = learn_sm.saveLearnJS(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	@RequestMapping("/findLearnByTalk")
	@ResponseBody
	public Result findLearnByTalk(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			List<Map<String, Object>> num = learn_sm.findLearnByTalk(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/saveLearnText")
	@ResponseBody
	public Result saveLearnText(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num = learn_sm.saveLearnText(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/findLearnByText")
	@ResponseBody
	public Result findLearnByText (HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num = learn_sm.findLearnByText(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/saveLearnPhrase")
	@ResponseBody
	public Result saveLearnPhrase(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num = learn_sm.saveLearnPhrase(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/findLearnByPhrase")
	@ResponseBody
	public Result findLearnByPhrase(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num = learn_sm.findLearnByPhrase(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/saveLearnSentence")
	@ResponseBody
	public Result saveLearnSentence(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num = learn_sm.saveLearnSentence(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/findLearnSentence")
	@ResponseBody
	public Result findLearnSentence(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num = learn_sm.findLearnSentence(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/saveLearnRead")
	@ResponseBody
	public Result saveLearnRead(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num = learn_sm.saveLearnRead(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/findLearnRead")
	@ResponseBody
	public Result findLearnRead(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			List<Map<String, Object>> num = learn_sm.findLearnRead(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/saveLearnListen")
	@ResponseBody
	public Result saveLearnListen (HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num = learn_sm.saveLearnListen(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/findLearnListen")
	@ResponseBody
	public Result findLearnListen(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			List<Map<String, Object>> num = learn_sm.findLearnListen(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	
}
