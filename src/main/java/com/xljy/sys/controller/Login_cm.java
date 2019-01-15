package com.xljy.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xljy.bean.LoginUser;
import com.xljy.bean.Result;
import com.xljy.component.LoginMap;
import com.xljy.sys.servers.Login_sm;
import com.xljy.util.Const;
import com.xljy.util.RandomValidateCodeUtil;
import com.xljy.util.RequestParamsToMap;

@Controller
@RequestMapping("/login")
public class Login_cm {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private Login_sm login_sm;

	@RequestMapping("/user")
	@ResponseBody
	public Result login(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		String usertype = parameterStringMap.get("usertype_code");
		Result res = null;

		try {
			if (!StringUtils.isEmpty(usertype)) {
				// List<Map<String, Object>> find = login_sm.find(parameterStringMap);
				if (usertype.equals("0")) {
					// 检查账号密码
					if (StringUtils.isEmpty(parameterStringMap.get("user_zhanghao"))) {
						res = Result.resultError("账号不能空");
						return res;
					}
					if (StringUtils.isEmpty(parameterStringMap.get("user_mima"))) {
						res = Result.resultError("密码不能空");
						return res;
					}

					List<Map<String, Object>> find = login_sm.find(parameterStringMap);
					if (find.size() == 1) {
						// 登录成功
						Object object = find.get(0).get("Id");
						// 查角色
						// List<Map<String, Object>> juese = login_sm.findjuese(object.toString());
						// 查菜单
						// List<Map<String, Object>> caidan = login_sm.findcaidan(juese);
						// LoginUser loginuser = new LoginUser((Integer) object, find, juese, caidan);
						LoginUser loginuser = new LoginUser((Integer) object, find, null, null);
						res = Result.resultSuccess(loginuser, "登录成功");
						// res = Result.resultSuccess(find, "登录成功");
						HttpSession session = request.getSession();

						res.setData2(session.getId());

						Cookie cookie = new Cookie("xljy_cookie", (String) res.getData2());
						response.addCookie(cookie);
						session.setAttribute(Const.LOGIN_USER, find);
					} else {
						res = Result.resultError(find.size() == 0 ? "用户不存在/密码错误" : "验证用户有多个");
					}
				} else if (usertype.equals("5")) {// 学生
					// 检查账号密码
					if (StringUtils.isEmpty(parameterStringMap.get("user_zhanghao"))) {
						res = Result.resultError(Const.Errcode.parameter_err,"账号不能空");
						return res;
					}
					if (StringUtils.isEmpty(parameterStringMap.get("user_mima"))) {
						res = Result.resultError(Const.Errcode.parameter_err,"密码不能空");
						return res;
					}

					List<Map<String, Object>> find = login_sm.find(parameterStringMap);
					if (find.size() == 1) {
						// 登录成功
						Object object = find.get(0).get("Id");
						// 查角色
						// List<Map<String, Object>> juese = login_sm.findjuese(object.toString());
						// 查菜单
						// List<Map<String, Object>> caidan = login_sm.findcaidan(juese);
						// LoginUser loginuser = new LoginUser((Integer) object, find, juese, caidan);
						LoginUser loginuser = new LoginUser((Integer) object, find.get(0), null, null);
						res = Result.resultSuccess(loginuser, "登录成功");
						// res = Result.resultSuccess(find, "登录成功");

						HttpSession session = request.getSession();

						res.setData2(session.getId());

						Cookie cookie = new Cookie("xljy_cookie", (String) res.getData2());
						response.addCookie(cookie);
						session.setAttribute(Const.LOGIN_USER, loginuser);
						session.setAttribute("usertype_code", usertype);//用户类型码
						session.setMaxInactiveInterval(60*60*12*3);//有效时间
						LoginMap.AddSession(usertype,object.toString(),session);
					} else {
						res = Result.resultError(Const.Errcode.login_err ,find.size() == 0 ? "用户不存在/密码错误" : "验证用户有多个");
					}

				} else {
					// 检查账号手机号
					if (StringUtils.isEmpty(
							parameterStringMap.get("user_dianhua") + parameterStringMap.get("user_weixin_code"))) {
						res = Result.resultError(Const.Errcode.parameter_err,"手机号/微信号至少得填一个");
						return res;
					}
					Map<String, Object> yanzheng_denglu = login_sm.yanzheng_denglu(request, parameterStringMap);
					// List<Map<String, Object>> find = login_sm.find(parameterStringMap);
					if ((boolean) yanzheng_denglu.get("denglu_states") ) {
						// 登录成功
						//(List) yanzheng_denglu.get("logUser");
						Object object = ((Map<String, Object>) yanzheng_denglu.get("logUser")).get("Id");// 登录用户的id
						//Object object = 10;// 登录用户的id
						// 查角色
						// List<Map<String, Object>> juese = login_sm.findjuese(object.toString());
						// 查菜单
						// List<Map<String, Object>> caidan = login_sm.findcaidan(juese);
						 LoginUser loginuser = new LoginUser((Integer) object, yanzheng_denglu, null, null);

						// 获取验证码 从session
						HttpSession session = request.getSession();

						res = Result.resultSuccess(yanzheng_denglu, null);
						// res = Result.resultSuccess(find, "登录成功");

						res.setData2(session.getId());

						Cookie cookie = new Cookie("xljy_cookie", (String) res.getData2());
						cookie.setPath("/demo");
						response.addCookie(cookie);
						session.setAttribute("usertype_code", usertype);//用户类型码
						session.setAttribute(Const.LOGIN_USER, loginuser);
						
						session.setMaxInactiveInterval(60*60*12*3);//有效时间
						LoginMap.AddSession(usertype,object.toString(),session);
						//System.out.println("登录Sessionid:"+session.getId());
					} else {
						res = Result.resultError( (String) yanzheng_denglu.get("msg0"));
						res.setData(yanzheng_denglu);
					}
				}

			} else {
				Result.resultError(Const.Errcode.parameter_err, "用户类型不能空");
			}

			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}
	
	@RequestMapping("/outlogin")
	@ResponseBody
	public Result outlogin(HttpServletRequest request, HttpServletResponse response) {

		Result res = null;
		logger.info("Sessionid:" + request.getSession().getId());
		try {
			LoginUser loginuser = (LoginUser) request.getSession().getAttribute(Const.LOGIN_USER);
			String usertype_code =(String) request.getSession().getAttribute("usertype_code");
			request.getSession().removeAttribute(Const.LOGIN_USER);
			request.getSession().invalidate();
			res = Result.resultSuccess("", "退出系统成功");

			//System.out.println("登出用户:"+loginuser.getId()+(Map)loginuser.getUser());
			if(loginuser !=null) {
				LoginMap.DelSession(usertype_code, loginuser.getId().toString());
			}

			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	/**
	 * 学生加入班级前的验证 验证学生存在
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/user/inbanji_yz")
	@ResponseBody
	public Result inbanji_yz(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		String usertype = parameterStringMap.get("usertype_code");
		Result res = null;

		try {
			if (!StringUtils.isEmpty(usertype)) {
				if (usertype.equals("5")) {// 学生
					// 检查账号密码
					if (StringUtils.isEmpty(parameterStringMap.get("user_zhanghao"))) {
						res = Result.resultError("账号不能空");
						return res;
					}
					if (StringUtils.isEmpty(parameterStringMap.get("user_mima"))) {
						res = Result.resultError("密码不能空");
						return res;
					}

					List<Map<String, Object>> find = login_sm.find(parameterStringMap);
					if (find.size() == 1) {
						
						Object object = find.get(0);

						res = Result.resultSuccess(object, "验证成功");
						// res = Result.resultSuccess(find, "登录成功");
					} else {
						res = Result.resultError(find.size() == 0 ? "用户不存在/密码错误" : "验证用户有多个");
					}

				}
			} else {
				Result.resultError("用户类型不是学生");
			}

			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	

	/**
	 * 生成图片验证码
	 */
	@RequestMapping(value = "/getVerify")
	public void getVerify(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
			response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expire", 0);
			RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
			randomValidateCode.getRandcode(request, response);// 输出验证码图片方法
		} catch (Exception e) {
		
			logger.error("获取验证码失败>>>>   ", e);
			e.printStackTrace();
		}

	}

	/**
	 * 获取短信验证码
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getShouJiYanZhenMa")
	public Result getShouJiYanZhenMa(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		String shoujihao = parameterStringMap.get("shoujihao");

		Result res = null;
		try {

			if (StringUtils.isEmpty(shoujihao)) {
				throw new RuntimeException("手机号不能空");
			} else if (shoujihao.length() != 11) {
				throw new RuntimeException("手机号不正确");
			}

			if (StringUtils.isEmpty(parameterStringMap.get("use_type_d"))) {// 验证码的用途码
				throw new RuntimeException("验证码的用途码不能空");
			}

			// 验证手机号存在库中

			/*
			 * int find_dianhuahao_cz = login_sm.find_dianhuahao_cz(shoujihao); if
			 * (find_dianhuahao_cz > 0) { Map<String, Object> sjyzm_fa =
			 * login_sm.sjyzm_fa(request, parameterStringMap);// 发验证码 res =
			 * Result.resultSuccess(sjyzm_fa, "发送成功"); } else { res =
			 * Result.resultSuccess(null, "手机号码不在系统中"); }
			 */

			Map<String, Object> sjyzm_fa = login_sm.sjyzm_fa(request, parameterStringMap);// 发验证码
			res = Result.resultSuccess(sjyzm_fa, "发送成功");
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}
	
	/**
	 * 支付交易回调入口
	 */
	@RequestMapping("/zf_go_bak")
	public void zf_go_bk(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("**支付交易回调入口s**********************************");
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		
	
		
		System.out.println(parameterStringMap);
		System.out.println("**支付交易回调入口e**********************************");

	}
	
	/**
	 * 支付_退款_回调入口
	 * @param request
	 * @return
	 */
	@RequestMapping("/do_zf_bak")
	@ResponseBody
	public Result do_zf_bak(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {
			
			Map<String, Object> find = login_sm.do_zf_bak( parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}
	
	/**
	 * 解析小程序jscode
	 * @return 
	 */
	@RequestMapping("/jx_jscode")
	@ResponseBody
	public Result jx_jscode(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res = null;
		try {
			
			Map<String, Object> find = login_sm.jx_jscode( parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}


	}
	
	/**
	 * 前端程序初始化绑定session
	 * @return 
	 */
	@RequestMapping("/bind_session")
	@ResponseBody
	public Result bind_session(HttpServletRequest request, HttpServletResponse response) {
		//Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res = null;
		try {
			HashMap<String, Object> Map_ret = new HashMap<>();
			Map_ret.put("SessionID", request.getSession().getId());

			res = Result.resultSuccess(Map_ret, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}


	}

}
