package com.xljy.student.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xljy.bean.Result;
import com.xljy.student.user.servers.StuLogin_sm;
import com.xljy.util.RequestParamsToMap;

import cn.hutool.core.codec.Base64;

@Controller
@RequestMapping("/student")
public class StuLogin_cm {

	@Autowired
	private StuLogin_sm login_sm;
	@Autowired
	private TencentCloudAPIDemo_cm td;
	
	public static final Map<String,HttpSession> USR_SESSION=new HashMap<String, HttpSession>();// 用户和Session绑定关系
	public static final Map<String, String> SESSIONID_USR = new HashMap<String, String>();	// SessionId和用户的绑定关系
	/*
因为用户登录成功后，用户的数据是放在Session中保管的，所以要保存session与用户的关系及sessionId与用户的关系。
当用户登录后，我们要把用户数据放到session之前检查一下当前用户是否登录 ps:这步是实现踢掉用户的关键

如果用户已登录，则将用户与session及sessionid与用户关系的两个参数中相关数据通过key的形式清掉，并且向这个session发送消息。 
	 */
	//检查用户是否登录
	public static void userLoginHandle(HttpServletRequest request) {
		// 当前登录的用户
		String userName = request.getParameter("login_user");
		// 当前sessionId
		String sessionId = request.getSession().getId();
		// 删除当前sessionId绑定的用户，用户--HttpSession
		USR_SESSION.remove(SESSIONID_USR.remove(sessionId));
		// 删除当前登录用户绑定的HttpSession
		HttpSession session = USR_SESSION.remove(userName);
		if (session != null) {
			SESSIONID_USR.remove(session.getId());
			session.setAttribute("msg", "您的账号已经在另一处登录了,您被迫下线!");
		}
 
	}
	@RequestMapping("/login")
	@ResponseBody
	public Result login(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			if(StringUtils.isEmpty(parameterStringMap.get("username"))) {
				res=Result.resultError("用户名不可为空");
				res.setCode(500);
				return res;
			}
			if(StringUtils.isEmpty(parameterStringMap.get("password"))) {
				res=Result.resultError("密码不可为空");
				res.setCode(500);
				return res;
			}
			List<Map<String, Object>> username = login_sm.findByUsername(parameterStringMap);
				if(username==null || username.isEmpty()) {
					res=Result.resultError("用户名不存在");
					res.setCode(500);
					return res;
				}
				List<Map<String, Object>> map = login_sm.login(parameterStringMap);
				if(map == null || map.isEmpty()) {
					res=Result.resultError("密码输入错误");
					res.setCode(500);
					return res;
				}else if(map.size()>1) {
					res=Result.resultError("存在多条数据");
					res.setCode(500);
					return res;
				}
				
				HttpSession session = request.getSession();

				session.setAttribute("login_user", map);
				res=Result.resultSuccess(map, "登录成功");
				res.setCode(200);
				res.setToken(session.getId());//相当于tookie 主要是判断是否存在二次登录
				res.setData2(session.getId());
				Cookie cookie = new Cookie("xljy_cookie", (String) res.getData2());
				response.addCookie(cookie);
				return res;
			 
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
			return res;
		}	
	}
	
	@RequestMapping("/findByStuid")
	@ResponseBody
	public Result findByStuid(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num = login_sm.findByStuid(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/updateStuInfo")
	@ResponseBody
	public Result updateStuInfo(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num = login_sm.updateStuInfo(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/updateStuVip")
	@ResponseBody
	public Result updateStuVip(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			int num = login_sm.updateStuVip(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/addClass")
	@ResponseBody
	public Result addClass(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			List<Map<String, Object>> num = login_sm.addClass(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/addTeaClass")
	@ResponseBody
	public Result addTeaClasss(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			int num = login_sm.addTeaClass(parameterStringMap);
			res=Result.resultSuccess("添加班级成功,数据为"+num+"条", "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/orders")
	@ResponseBody
	public Result orders(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			List<Map<String, Object>> num = login_sm.orders(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/updatePassword")
	@ResponseBody
	public Result updatePassword(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			int num = login_sm.updatePassword(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	
	
	@RequestMapping("/findBySheng")
	@ResponseBody
	public Result findBySheng(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			List<Map<String, Object>> num = login_sm.findBySheng(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	@RequestMapping("/findByShi")
	@ResponseBody
	public Result findByShi(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			List<Map<String, Object>> num = login_sm.findByShi(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	@RequestMapping("/findByQu")
	@ResponseBody
	public Result findByQu(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			List<Map<String, Object>> num = login_sm.findByQu(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	@RequestMapping("/findBySchool")
	@ResponseBody
	public Result findBySchool(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			List<Map<String, Object>> num = login_sm.findBySchool(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/addCard")
	@ResponseBody
	public Result addCard(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			Map<String, Object> num = login_sm.addCard(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	
	
	@RequestMapping("/updateSchool")
	@ResponseBody
	public Result updateSchool(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			int num = login_sm.updateSchool(parameterStringMap);
			res=Result.resultSuccess(num, "success");
		    res.setCode(200);
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			res.setCode(500);
		}
		return res;
	}
	
	@RequestMapping("/delClass")
	@ResponseBody
	public Result delClass(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res=null;
		try {
			int num = login_sm.delClass(parameterStringMap);
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
