package com.xljy.sys.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xljy.bean.Result;
import com.xljy.sys.servers.User_sm;
import com.xljy.util.RequestParamsToMap;
import com.xljy.util.Yzm_util;

import cn.hutool.extra.servlet.ServletUtil;

@Controller
@RequestMapping("/user")
public class User_cm {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private User_sm user_sm;

	/**
	 * 查全部学生
	 * @param request
	 * @return
	 */
	@RequestMapping("/FindAll")
	@ResponseBody
	public Result login(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			// 判断是否分页
			if (StringUtils.isNotEmpty(parameterStringMap.get("pageNumber"))) {// 分
				int pageNumber = Integer.parseInt(parameterStringMap.get("pageNumber"));
				int pageSize = Integer.parseInt(parameterStringMap.get("pageSize"));
				Page<Object> startPage = PageHelper.startPage(pageNumber, pageSize);

				List<Map<String, Object>> find = user_sm.findall(parameterStringMap);

				res = Result.resultSuccess(find, "");
				res.setPageInfo(startPage.getTotal());
				return res;
			} else {// 不分
				List<Map<String, Object>> find = user_sm.findall(parameterStringMap);
				res = Result.resultSuccess(find, "");
				return res;
			}

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	@RequestMapping("/updatePW")
	@ResponseBody
	public Result updatePW(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			int find = user_sm.updatePW(parameterStringMap);
			if (find == 1) {
				res = Result.resultSuccess(find, "密码修改成功");
			} else {
				res = Result.resultError("密码修改失败");
			}

			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	// 根据用户ID进行查询
	@RequestMapping("/findUserById")
	@ResponseBody
	public Result findUserById(HttpServletRequest request) {
		Result res = null;
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		try {
			Map<String, String> arm = user_sm.findUserById(parameterStringMap);
			res = Result.resultSuccess(arm, "");
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}
	}

	// 用户的添加
	@RequestMapping("/addUser")
	@ResponseBody
	public Result addUser(HttpServletRequest request) {
		Result res = null;
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		if (!StringUtils.isEmpty(parameterStringMap.get("qu"))) {
			parameterStringMap.put("user_diquma", parameterStringMap.get("qu"));
		} else if (!StringUtils.isEmpty(parameterStringMap.get("shi"))) {
			parameterStringMap.put("user_diquma", parameterStringMap.get("shi"));
		} else if (!StringUtils.isEmpty(parameterStringMap.get("sheng"))) {
			parameterStringMap.put("user_diquma", parameterStringMap.get("sheng"));
		}
		int arm = 0;
		try {

			// 添加用户
			// parameterStringMap.put("user_zhanghao",
			// ZhangHao.getZhangHaoUuid());
			Map<String, Object> addUser = user_sm.addUser(parameterStringMap);

			res = Result.resultSuccess(addUser, "添加成功");
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}
	}

	// 用户的修改
	@RequestMapping("/xiugaiUser")
	@ResponseBody
	public Result xiugaiUser(HttpServletRequest request) {
		Result res = null;
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		if (!StringUtils.isEmpty(parameterStringMap.get("qu"))) {
			parameterStringMap.put("user_diquma", parameterStringMap.get("qu"));
		} else if (!StringUtils.isEmpty(parameterStringMap.get("shi"))) {
			parameterStringMap.put("user_diquma", parameterStringMap.get("shi"));
		} else if (!StringUtils.isEmpty(parameterStringMap.get("sheng"))) {
			parameterStringMap.put("user_diquma", parameterStringMap.get("sheng"));
		}

		int arm = 0;
		try {
			Map<String, Object> updateUser = user_sm.updateUser(parameterStringMap);
			arm = (int) updateUser.get("arm");// 更新的数量
			res = Result.resultSuccess(updateUser, "修改成功,修改数据量" + arm);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}
	}

	// 用户的修改_修改手机号
	@RequestMapping("/xiugaiUser_sj")
	@ResponseBody
	public Result xiugaiUser_sj(HttpServletRequest request) {
		Result res = null;
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		int arm = 0;
		try {
			Map<String, Object> updateUser = user_sm.xiugaiUser_sj(request,parameterStringMap);
			arm = (int) updateUser.get("arm");// 更新的数量
			res = Result.resultSuccess(updateUser, "修改成功,修改数据量" + arm);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}
	}

	// 删除用户
	@RequestMapping("/delUserById")
	@ResponseBody
	public Result delUserById(HttpServletRequest request) {
		Result res = null;
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		try {
			int use = user_sm.delUserById(parameterStringMap);
			res = Result.resultSuccess(null, "删除成功,删除数据量" + use);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}
	}

	// 家长关联孩子
	@RequestMapping("/jiazhangAddhaizi")
	@ResponseBody
	public Result jiazhangAddhaizi(HttpServletRequest request) {
		Result res = null;
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		try {
			int use = user_sm.jiazhangAddhaizi(parameterStringMap);
			res = Result.resultSuccess(null, "关联成功" + use);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			if (StringUtils.contains(e.getMessage(), "Duplicate entry") && StringUtils.contains(e.getMessage(), "'PRIMARY'")) {// 联合主键重复
				res = Result.resultError("已经关联这个孩子");
			} else {
				res = Result.resultError(e.getMessage());
			}

			return res;
		}
	}

	// 老师创建班级
	@RequestMapping("/addbanji")
	@ResponseBody
	public Result addbanji(HttpServletRequest request) {
		Result res = null;
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		try {
			Map<String, Object> hh = user_sm.addbanji(parameterStringMap);
			res = Result.resultSuccess(hh, "创建班级成功");
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}
	}

	// 老师关联班级
	@RequestMapping("/laoshi9banji")
	@ResponseBody
	public Result laoshi9banji(HttpServletRequest request) {
		Result res = null;
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		try {
			int use = user_sm.laoshi9banji(parameterStringMap);
			res = Result.resultSuccess(null, "关联班级成功" + use);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			if (StringUtils.contains(e.getMessage(), "Duplicate entry") && StringUtils.contains(e.getMessage(), "'PRIMARY'")) {// 联合主键重复
				res = Result.resultError("已经关联这个班级");
			} else {
				res = Result.resultError(e.getMessage());
			}
			return res;
		}
	}

	// 学生关联班级
	@RequestMapping("/xueshengaddbanji")
	@ResponseBody
	public Result xueshengaddbanji(HttpServletRequest request) {
		Result res = null;
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		try {

			int use = user_sm.xuesheng9banji(parameterStringMap);
			res = Result.resultSuccess(null, "关联班级成功" + use);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			if (StringUtils.contains(e.getMessage(), "Duplicate entry") && StringUtils.contains(e.getMessage(), "'PRIMARY'")) {// 联合主键重复
				res = Result.resultError("已经加入这个班级");
			} else {
				res = Result.resultError(e.getMessage());
			}

			return res;
		}
	}

	// 班级列表
	@RequestMapping("/listbanji")
	@ResponseBody
	public Result listbanji(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			// 判断是否分页
			if (parameterStringMap.get("pageNumber") != null && parameterStringMap.get("pageNumber").length() > 0) {// 分
				int pageNumber = Integer.parseInt(parameterStringMap.get("pageNumber"));
				int pageSize = Integer.parseInt(parameterStringMap.get("pageSize"));
				Page<Object> startPage = PageHelper.startPage(pageNumber, pageSize);
				List<Map<String, Object>> find = user_sm.findall_banji(parameterStringMap);

				res = Result.resultSuccess(find, "");
				res.setPageInfo(startPage.getTotal());
				return res;
			} else {// 不分
				List<Map<String, Object>> find = user_sm.findall_banji(parameterStringMap);

				res = Result.resultSuccess(find, "");

				return res;
			}

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	// 删除班级
	@RequestMapping("/delbanji")
	@ResponseBody
	public Result delbanji(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {
			if (StringUtils.isEmpty(parameterStringMap.get("banji_d"))) {
				throw new RuntimeException("班级代码不能空");
			}
			if (StringUtils.isEmpty(parameterStringMap.get("user_yanzhengma"))) {
				throw new RuntimeException("验证码不能空");
			}

			if (Yzm_util.delbanji_yzm(request, parameterStringMap)) {
				Map<String, Object> hh = user_sm.delbanji(parameterStringMap);
				res = Result.resultSuccess(hh, "删除班级成功");
			} else {
				res = Result.resultSuccess(null, "验证失败:验证码错误");
			}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}
	}

	// 修改班级
	@RequestMapping("/update_banji")
	@ResponseBody
	public Result update_banji(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {
			Map<String, Object> hh = user_sm.update_banji(parameterStringMap);
			res = Result.resultSuccess(hh, "修改班级成功");
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}
	}

	// 班级移除学生
	@RequestMapping("/delxuesheng4banji")
	@ResponseBody
	public Result delxuesheng4banji(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {
			Map<String, Object> hh = user_sm.delxuesheng4banji(parameterStringMap);
			res = Result.resultSuccess(hh, "学生移除成功");
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}
	}

	// 班级下学生列表
	@RequestMapping("/liststu")
	@ResponseBody
	public Result liststu(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			// 判断是否分页
			if (parameterStringMap.get("pageNumber") != null && parameterStringMap.get("pageNumber").length() > 0) {// 分
				int pageNumber = Integer.parseInt(parameterStringMap.get("pageNumber"));
				int pageSize = Integer.parseInt(parameterStringMap.get("pageSize"));
				Page<Object> startPage = PageHelper.startPage(pageNumber, pageSize);
				Map<String, Object> find = user_sm.findall_stu5banji(parameterStringMap);

				res = Result.resultSuccess(find, "");
				res.setPageInfo(startPage.getTotal());
				return res;
			} else {// 不分
				Map<String, Object> find = user_sm.findall_stu5banji(parameterStringMap);

				res = Result.resultSuccess(find, "");

				return res;
			}

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	// 老师的汇总页的
	@RequestMapping("/laoshi_huizong")
	@ResponseBody
	public Result laoshi_huizong(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = user_sm.laoshi_huizong(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	// 老师的汇总页的
	@RequestMapping("/list_zx_bz")
	@ResponseBody
	public Result list_zx_bz(HttpServletRequest request) {
		// Map<String, String> parameterStringMap =
		// RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			List<Map<String, Object>> find = user_sm.list_zx_bz();

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	/*
	 * 
	 * 布置作业的=-==================================================================
	 * ====
	 * 
	 */

	@RequestMapping("/list_danyuan")
	@ResponseBody
	public Result list_danyuan(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			List<Map<String, Object>> find = user_sm.list_danyuan(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	@RequestMapping("/list_ti")
	@ResponseBody
	public Result list_ti(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			List<Map<String, Object>> find = user_sm.list_ti(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	@RequestMapping("/xiangqing_ti")
	@ResponseBody
	public Result xiangqing_ti(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			List<Map<String, Object>> find = user_sm.xiangqing_ti(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	@RequestMapping("/buzhi_zuoye")
	@ResponseBody
	public Result buzhi_zuoye(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = user_sm.buzhi_zuoye(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	@RequestMapping("/buzhi_zuoye_add")
	@ResponseBody
	public Result buzhi_zuoye_add(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = user_sm.buzhi_zuoye_add(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	@RequestMapping("/buzhi_zuoye_del")
	@ResponseBody
	public Result buzhi_zuoye_del(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = user_sm.buzhi_zuoye_del(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	@RequestMapping("/buzhi_zuoye_over")
	@ResponseBody
	public Result buzhi_zuoye_over(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = user_sm.buzhi_zuoye_over(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	@RequestMapping("/list_zuoye")
	@ResponseBody
	public Result list_zuoye(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			List<Map<String, Object>> find = user_sm.list_zuoye(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	@RequestMapping("/del_zuoye")
	@ResponseBody
	public Result del_zuoye(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = user_sm.del_zuoye(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	@RequestMapping("/upd_zuoye_banji")
	@ResponseBody
	public Result upd_zuoye_banji(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = user_sm.upd_zuoye_banji(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	@RequestMapping("/clean_zuoye")
	@ResponseBody
	public Result clean_zuoye(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = user_sm.clean_zuoye(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	/*
	 * @RequestMapping("/upd_zuoye")
	 * 
	 * @ResponseBody public Result upd_zuoye(HttpServletRequest request) {
	 * Map<String, String> parameterStringMap =
	 * RequestParamsToMap.getParameterStringMap(request);
	 * 
	 * Result res = null; try {
	 * 
	 * Map<String, Object> find = user_sm.upd_zuoye(parameterStringMap);
	 * 
	 * res = Result.resultSuccess(find, "");
	 * 
	 * return res;
	 * 
	 * } catch (Exception e) { e.printStackTrace(); res =
	 * Result.resultError(e.getMessage()); return res; }
	 * 
	 * }
	 */

	@RequestMapping("/jc_zuoye_list")
	@ResponseBody
	public Result jc_zuoye_list(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = user_sm.jc_zuoye_list(parameterStringMap);

			res = Result.resultSuccess(find.get("date"), "");
			res.setPageInfo(find.get("Total"));
			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	/**
	 * 作业的整体情况
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/zuoye_ztqk")
	@ResponseBody
	public Result zuoye_ztqk(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = user_sm.zuoye_ztqk(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	/**
	 * 作业学生成绩 包含所有学生
	 * @param request
	 * @return
	 */
	@RequestMapping("/zuoye_stu_cj")
	@ResponseBody
	public Result zuoye_stu_cj(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			List<Map<String, Object>> find = user_sm.zuoye_stu_cj(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	/**
	 * 教师未读消息数
	 * @param request
	 * @return
	 */
	@RequestMapping("/xx_count")
	@ResponseBody
	public Result xx_count(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = user_sm.xx_count(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	/**
	 * 教师消息list
	 * @param request
	 * @return
	 */
	@RequestMapping("/xx_list")
	@ResponseBody
	public Result xx_list(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {
			List<Map<String, Object>> find = user_sm.xx_list(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	/**
	 * 教师删除消息
	 * @param request
	 * @return
	 */
	@RequestMapping("/del_laoshi_xx")
	@ResponseBody
	public Result del_xx(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = user_sm.del_laoshi_xx(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	/**
	 * 教师改消息状态 (全已读)
	 * @param request
	 * @return
	 */
	@RequestMapping("/upd_xx_yidu")
	@ResponseBody
	public Result upd_xx_yidu(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = user_sm.upd_xx_yidu(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	/**
	 * 学生端忘记密码
	 * @param request
	 * @return
	 */
	@RequestMapping("/stu_update_mima")
	@ResponseBody
	public Result stu_update_mima(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = user_sm.stu_update_mima(request, parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	/**
	 * 家长解绑学生
	 * @param request
	 * @return
	 */
	@RequestMapping("/p_ubd_stu")
	@ResponseBody
	public Result p_ubd_stu(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = user_sm.p_ubd_stu(request, parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	/**
	 * 解绑微信
	 * @param request
	 * @return
	 */
	@RequestMapping("/out_weixin_id")
	@ResponseBody
	public Result out_weixin_id(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = user_sm.out_weixin_id(request, parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	/**
	 * 绑定微信
	 * @param request
	 * @return
	 */
	@RequestMapping("/bd_weixin_id")
	@ResponseBody
	public Result bd_weixin_id(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = user_sm.bd_weixin_id(request, parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	/**
	 * 支付请求订单
	 * @param request
	 * @return
	 */
	@RequestMapping("/do_dingdan")
	@ResponseBody
	public Result do_dingdan(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {
			// 给它ip
			parameterStringMap.put("spbill_create_ip", ServletUtil.getClientIP(request, ""));
			Map<String, Object> find = user_sm.do_dingdan(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	/**
	 * 支付结果查询
	 * @param request
	 * @return
	 */
	@RequestMapping("/find_zf_Res")
	@ResponseBody
	public Result find_zf_Res(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = user_sm.find_zf_Res(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

	/**
	 * 支付_退款
	 * @param request
	 * @return
	 */
	@RequestMapping("/do_zf_bak")
	@ResponseBody
	public Result do_zf_bak(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);

		Result res = null;
		try {

			Map<String, Object> find = user_sm.do_zf_bak(parameterStringMap);

			res = Result.resultSuccess(find, "");

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			res = Result.resultError(e.getMessage());
			return res;
		}

	}

}
