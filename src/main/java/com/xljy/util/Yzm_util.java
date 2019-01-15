package com.xljy.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class Yzm_util {
	/**
	 * 删除班级的 验证码的 处理
	 * 
	 * @param request
	 * @param yzm
	 * @return
	 */
	public static boolean delbanji_yzm(HttpServletRequest request, Map<String, String> parameterStringMap) {
		/*boolean ret = false;
		HttpSession session = request.getSession();
		String[] attribute = (String[]) session.getAttribute(Const.KEY_DEL_BANJI);
		ret = StringUtils.equals(attribute[0], yzm);
		if (ret) {
			session.removeAttribute(Const.KEY_DEL_BANJI);
		}
		// System.out.println(session.getId());
		return ret;*/
		boolean ret = false;
		if (parameterStringMap.get("user_yanzhengma") == null) {//验证码不能空
			throw new RuntimeException("验证码是必填的!");
		}
		String[] DEL_BANJI_yzm = (String[]) request.getSession().getAttribute(Const.KEY_DEL_BANJI);
		if (DEL_BANJI_yzm == null) {//Session中无验证码
			throw new RuntimeException("验证码不存在!");
		}
		if (!(StringUtils.equals(parameterStringMap.get("user_dianhua"), DEL_BANJI_yzm[1]))) {// 手机号码对比
			throw new RuntimeException("验证的手机号不匹配: " + parameterStringMap.get("user_dianhua"));
		}

		ret = StringUtils.equals(DEL_BANJI_yzm[0], parameterStringMap.get("user_yanzhengma"));//对比验证码
		if (ret) {
			request.getSession().removeAttribute(Const.KEY_DEL_BANJI);
		}
		// System.out.println(session.getId());
		return ret;
	}

	/**
	 * 修改密码的 验证码的 处理
	 * 
	 * @param request
	 * @param parameterStringMap
	 * @return
	 */
	public static boolean update_mima_yzm(HttpServletRequest request, Map<String, String> parameterStringMap) {
		boolean ret = false;
		if (parameterStringMap.get("user_yanzhengma") == null) {//验证码不能空
			throw new RuntimeException("验证码是必填的!");
		}
		String[] UPD_MIMA_yzm = (String[]) request.getSession().getAttribute(Const.KEY_UPD_MIMA);
		if (UPD_MIMA_yzm == null) {//Session中无验证码
			throw new RuntimeException("验证码不存在!");
		}
		if (!(StringUtils.equals(parameterStringMap.get("user_dianhua"), UPD_MIMA_yzm[1]))) {// 手机号码对比
			throw new RuntimeException("验证的手机号不匹配: " + parameterStringMap.get("user_dianhua"));
		}

		ret = StringUtils.equals(UPD_MIMA_yzm[0], parameterStringMap.get("user_yanzhengma"));//对比验证码
		if (ret) {
			request.getSession().removeAttribute(Const.KEY_UPD_MIMA);
		}
		// System.out.println(session.getId());
		return ret;
	}

	/**
	 * 通用的验证码对比方法 验证正确返回true 否则false
	 * @param request
	 * @param parameterStringMap
	 * @return
	 */
	public static boolean db_yzm(HttpServletRequest request, Map<String, String> parameterStringMap) {
		boolean ret = false;
		if (parameterStringMap.get("user_yanzhengma") == null) {//验证码不能空
			throw new RuntimeException("验证码是必填的!");
		}
		
		String[] UPD_MIMA_yzm = (String[]) request.getSession().getAttribute(Const.session_Attribute_key_use_d(parameterStringMap.get("use_type_d")));
		
		
		if (UPD_MIMA_yzm == null) {//Session中无验证码
			throw new RuntimeException("验证码不存在!");
		}
		if (!(StringUtils.equals(parameterStringMap.get("user_dianhua"), UPD_MIMA_yzm[1]))) {// 手机号码对比
			throw new RuntimeException("验证的手机号不匹配: " + parameterStringMap.get("user_dianhua"));
		}

		ret = StringUtils.equals(UPD_MIMA_yzm[0], parameterStringMap.get("user_yanzhengma"));//对比验证码
		if (ret) {
			request.getSession().removeAttribute(Const.session_Attribute_key_use_d(parameterStringMap.get("use_type_d")));
		}
		// System.out.println(session.getId());
		return ret;
	}

}
