package com.xljy.sys.servers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.xljy.sys.dao.Login_di;
import com.xljy.util.Const;
import com.xljy.util.RequestParamsToMap;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;

@Service
public class Login_sm {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	Login_di login_di;

	public List<Map<String, Object>> find(Map<String, String> parameterStringMap) {

		return login_di.getLogUser(parameterStringMap);

	}

	public List<Map<String, Object>> findjuese(String userid) {
		return login_di.findjuese(userid);
	}

	public List<Map<String, Object>> findcaidan(List<Map<String, Object>> juese) {
		return login_di.findcaidan(juese);

	}

	public Map<String, Object> sjyzm_fa(HttpServletRequest request, Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();

		int yanzhengma = NumberUtil.generateRandomNumber(100000, 999999, 1)[0];// 6位验证码

		// 短信应用SDK AppID
		int appid = Const.DX_SDK_AppID; // 1400开头
		// 短信应用SDK AppKey
		String appkey = Const.DX_SDK_appkey;
		// 需要发送短信的手机号码
		String[] phoneNumbers = { parameterStringMap.get("shoujihao") };
		// 短信模板ID，需要在短信应用中申请
		// NOTE: 这里的模板ID`7839`只是一个示例，
		// 真实的模板ID需要在短信控制台中申请
		int templateId = Const.DX_TemplateId_YZM;
		// int templateId = 45645454;
		// 签名
		// NOTE: 这里的签名"腾讯云"只是一个示例，
		// 真实的签名需要在短信控制台中申请，另外
		// 签名参数使用的是`签名内容`，而不是`签名ID`
		// String smsSign = "腾讯云";
		String smsSign = Const.DX_smsSign;
		// 指定模板ID单发短信

		try {
			String[] params = { Integer.toString(yanzhengma), "10" };// 模板中的替换参数

			SmsMultiSender msender = new SmsMultiSender(appid, appkey);
			SmsMultiSenderResult result = msender.sendWithParam("86", phoneNumbers, templateId, params, smsSign, "", ""); // 签名参数未提供或者为空时，会使用默认签名发送短信
			// 获取验证码 从session
			if (result.result == 0) {

				HttpSession session = request.getSession();

				String[] yzm = { params[0], phoneNumbers[0] };// 模板中的替换参数
				session.setAttribute(Const.session_Attribute_key_use_d(parameterStringMap.get("use_type_d")), yzm);
				Map_ret.put("msg0", "发送成功");
				Map_ret.put("SessionID", session.getId());
				// System.out.println(result);
				// System.out.println(session.getId());

			} else {
				Map_ret.put("msg0", "发送失败");
				Map_ret.put("result", result);
			}

			// logger.info("指定模板ID单发短信", mapper.writeValueAsString(result));
			// logger.debug(result.errMsg);
			// System.out.println("----------------------------");
			// System.out.println(result.errMsg);
		} catch (HTTPException e) {
			// HTTP响应码错误
			e.printStackTrace();
		} catch (JSONException e) {
			// json解析错误
			e.printStackTrace();
		} catch (IOException e) {
			// 网络IO错误
			e.printStackTrace();
		}
		return Map_ret;

	}

	public int find_dianhuahao_cz(String shoujihao) {

		return login_di.find_dianhuahao_cz(shoujihao);
	}

	@Transactional
	public Map<String, Object> yanzheng_denglu(HttpServletRequest request, Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		if (StringUtils.isNotEmpty(parameterStringMap.get("user_dianhua"))) {// 先判断手机号
			// 对比验证码
			if (StringUtils.isNotEmpty(parameterStringMap.get("user_yanzhengma"))) {// 参数验证码不能空

				String[] DENGLV_yzm = (String[]) request.getSession().getAttribute(Const.KEY_DENGLV);
				if (DENGLV_yzm == null) {
					throw new RuntimeException("验证码不存在!");
				}
				if (!(StringUtils.equals(parameterStringMap.get("user_dianhua"), DENGLV_yzm[1]))) {// 手机号码对比
					throw new RuntimeException("验证的手机号不匹配: " + parameterStringMap.get("user_dianhua"));
				}

				if (StringUtils.equals(parameterStringMap.get("user_yanzhengma"), DENGLV_yzm[0])) {// 验证码通过
					// 获取登录的用户
					List<Map<String, Object>> logUser = login_di.getLogUser_shouji(parameterStringMap);

					if (logUser.size() == 1) {// 1.1有这个手机号
						Map<String, Object> user = logUser.get(0);
						//// 验证参数 微信id 给过来时
						if (StringUtils.isNotEmpty(parameterStringMap.get("user_weixin_id"))) {// 验证参数微信id给过来时

							// 检查
							if (StringUtils.isNotEmpty((String) user.get("user_weixin_id"))) {// ?登录的用户已经绑定微信账号
								// 已绑定 检查是否一致
								if (StringUtils.equals(parameterStringMap.get("user_weixin_id"), (String) user.get("user_weixin_id"))) {// 一致
									Map_ret.put("denglu_d", 11);//
									Map_ret.put("msg0", "登录成功");
									Map_ret.put("logUser", user);
									Map_ret.put("denglu_states", true);
								} else {// 不一致

									login_di.up_bd_weixin_id(user.get("Id"), parameterStringMap);// 更新
																									// 新微信账号到这个用户
									user.put("user_weixin_id", parameterStringMap.get("user_weixin_id"));// 将新的微信号加入其中
									Map_ret.put("denglu_d", 13);//
									Map_ret.put("msg0", "登录成功,且绑定新的微信账号");//
									Map_ret.put("logUser", user);
									Map_ret.put("denglu_states", true);
								}
							} else {// 登录的用户没有绑定微信账号
									// 绑定传来的微信id
								login_di.up_bd_weixin_id(user.get("Id"), parameterStringMap);// 更新微信账号到这个用户
								Map_ret.put("msg1", "绑定了微信号码");
								Map_ret.put("denglu_d", 11);//
								Map_ret.put("msg0", "登录成功");
								user.put("user_weixin_id", parameterStringMap.get("user_weixin_id"));// 微信号加入其中
								Map_ret.put("logUser", user);
								Map_ret.put("denglu_states", true);
							}
						} else {// 没有传过来微信号
								// 有这个手机号 且
							Map_ret.put("denglu_d", 11);//
							Map_ret.put("msg0", "登录成功");
							Map_ret.put("logUser", user);
							Map_ret.put("denglu_states", true);
						}

					} else if (logUser.size() > 1) {// 1.2有这个手机号存在多个
						throw new RuntimeException("系统中存在多个用户使用个手机号: " + parameterStringMap.get("user_dianhua") + "\r\n请联系管理员处理!");
					} else {// 1.3手机号不存在系统中
						// 走注册
						int zc_user = login_di.zc_user(parameterStringMap);
						Map_ret.put("msg0", "注册成功且登录: " + zc_user + "个");
						Map_ret.put("denglu_d", 14);// 注册成功且登录
						Map_ret.put("logUser", login_di.getLogUser_shouji(parameterStringMap).get(0));
						Map_ret.put("denglu_states", true);
					}

					// 从Session移除原验证码
					request.getSession().removeAttribute(Const.KEY_DENGLV);
				} else {// 验证码不通过
					Map_ret.put("denglu_d", 21);//
					Map_ret.put("msg0", "登录失败,验证码错误!");
					Map_ret.put("denglu_states", false);
				}
			} else {
				throw new RuntimeException("验证码不能空");
			}

		} else if (StringUtils.isNotEmpty(parameterStringMap.get("user_weixin_code"))) {// 手机号为空时走微信的

			HashMap<String, Object> weixin_jscode2session = Const.weixin_jscode2session(parameterStringMap.get("user_weixin_code"),parameterStringMap.get("usertype_code"));
			Map_ret.put("weixin_result", weixin_jscode2session.get("weixin_result"));// 返回给前台jscode解析结果
			if (((boolean) weixin_jscode2session.get("sf_ok")) == true) {
				// Map_ret.put("user_weixin_id", 11);//
				parameterStringMap.put("user_weixin_id", (String) ((HashMap) weixin_jscode2session.get("weixin_result")).get("openid"));
				Map_ret.put("user_weixin_id", parameterStringMap.get("user_weixin_id"));// 返回给前台openid				
				
				List<Map<String, Object>> logUser = login_di.getLogUser_weixin(parameterStringMap);

				if (logUser.size() == 1) {// 有这个微信账号
					Map<String, Object> user = logUser.get(0);
					Object object = logUser.get(0).get("user_dianhua");
					if (object != null) {// 用户绑定手机号了
						if (object.toString().length() == 11) {
							/**
							 * denglu_d 11成功 手机号绑定且正常 10成功 手机号绑定但手机号异常 12成功 手机号未绑定 13成功 手机号绑定但微信号异常 20失败
							 * 这个微信号没注册过 21失败 验证码错误 30微信code解析异常 14手机号不存在 走 注册且登录
							 */
							Map_ret.put("denglu_d", 11);//
							Map_ret.put("msg0", "登录成功");
							Map_ret.put("logUser", user);
							Map_ret.put("denglu_states", true);
						} else {
							Map_ret.put("denglu_d", 10);
							Map_ret.put("msg0", "登录成功,但手机号有异常可能无法使用!");
							Map_ret.put("logUser", user);
							Map_ret.put("denglu_states", true);
						}
					} else {//// 用户未绑定手机号
						Map_ret.put("denglu_d", 12);
						Map_ret.put("msg0", "登录成功,但手机号未绑定");
						Map_ret.put("logUser", user);
						Map_ret.put("denglu_states", true);
					}
				} else {// 无这个微信账号
					Map_ret.put("denglu_d", 20);
					Map_ret.put("msg0", "登录失败,该微信账号还未注册");
					Map_ret.put("denglu_states", false);
				}
			} else {
				Map_ret.put("denglu_d", 30);
				Map_ret.put("msg0", "登录失败,微信号_code验证异常");
				Map_ret.put("denglu_states", false);
				Map_ret.put("msg1", weixin_jscode2session);
			}

		}
		return Map_ret;
	}

	public Map<String, Object> do_zf_bak(Map<String, String> parameterStringMap) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 解析微信jscode
	 * @param parameterStringMap
	 * @return
	 */
	public Map<String, Object> jx_jscode(Map<String, String> parameterStringMap) {
		HashMap<String, Object> weixin_jscode2session = Const.weixin_jscode2session(parameterStringMap.get("user_weixin_code"),parameterStringMap.get("usertype_code"));
		
		return weixin_jscode2session;
	}

}
