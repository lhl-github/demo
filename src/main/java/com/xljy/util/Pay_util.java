
package com.xljy.util;

import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.xljy.util.WX_Count.WX_SignType;
import com.xljy.util.WX_Count.WX_param_m;
import com.xljy.util.ZFB_Count.ZFB_SIGN_TYPE;
import com.xljy.util.ZFB_Count.ZFB_param_m;
import com.xljy.util.wxpay.sdk.WXPayConstants;
import com.xljy.util.wxpay.sdk.WXPayUtil;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpUtil;

/**
 * 支付工具类
 * @author lhl
 *
 */
public class Pay_util {

	/**
	 * 微信签名加密类型
	 * @author Administrator
	 *
	 */
	public enum Pay_body {
		E卡一年套餐, E卡两年套餐, E卡三年套餐
	}

	/**
	 * 微信预付单 接口地址
	 */
	public static String WX_TYXD_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	/**
	 * 微信支付结果查询 接口地址
	 */
	public static String WX_ZF_RES_url = "https://api.mch.weixin.qq.com/pay/orderquery";
	/**
	 * 一年的钱
	 */
	public static String TC1 = "29800";
	/**
	 * 两年的钱
	 */
	public static String TC2 = "50600";
	/**
	 * 三年的钱
	 */
	public static String TC3 = "62600";

	public static Map<String, String> wx_yfd(Map<String, String> paramMap) {// 参数数Map<String,
		// String>

		Map<String, String> wx_yfd_config5param = Pay_util.wx_yfd_config5param();
		// 需要动态处理的参数sign,sign_type,body,
		// attach(附加参数可以空),out_trade_no,total_fee,spbill_create_ip,notify_url
		wx_yfd_config5param.put(WX_param_m.sign_type.toString(), WX_SignType.HMACSHA256.toString());//
		wx_yfd_config5param.put(WX_param_m.body.toString(), paramMap.get("body"));//
		wx_yfd_config5param.put(WX_param_m.out_trade_no.toString(), paramMap.get("out_trade_no"));// 商户订单单号
		wx_yfd_config5param.put(WX_param_m.total_fee.toString(), paramMap.get("total_fee"));// 金额(分)
		wx_yfd_config5param.put(WX_param_m.spbill_create_ip.toString(), paramMap.get("spbill_create_ip"));// 请求端ip
		wx_yfd_config5param.put(WX_param_m.notify_url.toString(), paramMap.get("notify_url"));// 回调入口

		try {
			String createSign = Pay_util.createSign(wx_yfd_config5param);
			wx_yfd_config5param.put(WX_param_m.sign.toString(), createSign);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("微信签名异常");
		}

		String mapToXml = "";
		try {
			mapToXml = WXPayUtil.mapToXml(wx_yfd_config5param);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("mapToXml异常");
		}
		//
		String post = HttpUtil.post(WX_TYXD_url, mapToXml);

		return wx_yfd_config5param;
	}

	/**
	 * 微信预付单 参数封装
	 * @return
	 */
	private static Map<String, String> wx_yfd_config5param() {
		Map<String, String> treeMap = new TreeMap<>();
		treeMap.put(WX_param_m.appid.toString(), WX_Count.appid);
		treeMap.put(WX_param_m.mch_id.toString(), WX_Count.mch_id);
		treeMap.put(WX_param_m.device_info.toString(), WX_Count.device_info);
		treeMap.put(WX_param_m.nonce_str.toString(), WX_Count.getNonce_str());// 随机字符串，不长于32位。推荐随机数生成算法
		// treeMap.put(WX_param_m.sign.toString(), WX_Count.sign);
		// treeMap.put(WX_param_m.sign_type.toString(), WX_Count.sign_type);
		// treeMap.put(WX_param_m.body.toString(), WX_Count.body);
		treeMap.put(WX_param_m.detail.toString(), WX_Count.detail);// 默认空
		// treeMap.put(WX_param_m.attach.toString(), WX_Count.attach);//附加数据
		// 原样返回
		// treeMap.put(WX_param_m.out_trade_no.toString(),
		// WX_Count.out_trade_no);
		treeMap.put(WX_param_m.fee_type.toString(), WX_Count.fee_type);
		// treeMap.put(WX_param_m.total_fee.toString(), WX_Count.total_fee);
		// treeMap.put(WX_param_m.spbill_create_ip.toString(),
		// WX_Count.spbill_create_ip);

		DateTime dateTime = new DateTime();
		String time_expire = DateUtil.offset(dateTime, DateField.MINUTE, 2).toString("yyyyMMddHHmmss");// 交易结束时间

		treeMap.put(WX_param_m.time_start.toString(), dateTime.toString("yyyyMMddHHmmss"));// 订单生成时间，格式为yyyyMMddHHmmss，
		treeMap.put(WX_param_m.time_expire.toString(), time_expire);// 交易结束时间，

		// treeMap.put(WX_param_m.notify_url.toString(),
		// WX_Count.notify_url);//回调入口 ,动态传吧
		treeMap.put(WX_param_m.trade_type.toString(), WX_Count.trade_type);// 默认APP
		treeMap.put(WX_param_m.limit_pay.toString(), WX_Count.limit_pay);

		return treeMap;
	}

	/**
	 * 微信支付结果查询  参数封装
	 * @return
	 */
	private static Map<String, String> wx_zf_res_config5param() {
		Map<String, String> treeMap = new TreeMap<>();
		treeMap.put(WX_param_m.appid.toString(), WX_Count.appid);
		treeMap.put(WX_param_m.mch_id.toString(), WX_Count.mch_id);
		// treeMap.put(WX_param_m.device_info.toString(), WX_Count.device_info);
		treeMap.put(WX_param_m.nonce_str.toString(), WX_Count.getNonce_str());// 随机字符串，不长于32位。推荐随机数生成算法
		// treeMap.put(WX_param_m.sign.toString(), WX_Count.sign);
		// treeMap.put(WX_param_m.sign_type.toString(), WX_Count.sign_type);
		// treeMap.put(WX_param_m.body.toString(), WX_Count.body);
		// treeMap.put(WX_param_m.detail.toString(), WX_Count.detail);// 默认空
		// treeMap.put(WX_param_m.attach.toString(), WX_Count.attach);//附加数据
		// 原样返回
		// treeMap.put(WX_param_m.out_trade_no.toString(),
		// WX_Count.out_trade_no);
		// treeMap.put(WX_param_m.fee_type.toString(), WX_Count.fee_type);
		// treeMap.put(WX_param_m.total_fee.toString(), WX_Count.total_fee);
		// treeMap.put(WX_param_m.spbill_create_ip.toString(),
		// WX_Count.spbill_create_ip);

		// DateTime dateTime = new DateTime();
		// String time_expire = DateUtil.offset(dateTime, DateField.MINUTE,
		// 2).toString("yyyyMMddHHmmss");// 交易结束时间

		// treeMap.put(WX_param_m.time_start.toString(),
		// dateTime.toString("yyyyMMddHHmmss"));// 订单生成时间，格式为yyyyMMddHHmmss，
		// treeMap.put(WX_param_m.time_expire.toString(), time_expire);//
		// 交易结束时间，

		// treeMap.put(WX_param_m.notify_url.toString(),
		// WX_Count.notify_url);//回调入口 ,动态传吧
		treeMap.put(WX_param_m.trade_type.toString(), WX_Count.trade_type);// 默认APP
		// treeMap.put(WX_param_m.limit_pay.toString(), WX_Count.limit_pay);

		return treeMap;
	}

	
	/**
	 * 支付宝预付单 参数封装(实际只做签名)
	 * @return
	 */
	private static Map<String, String> zfb_yfd_config5param() {
		Map<String, String> treeMap = new TreeMap<>();
		treeMap.put(ZFB_param_m.app_id.toString(), ZFB_Count.app_id);
		treeMap.put(ZFB_param_m.format.toString(), ZFB_Count.format);//json
		treeMap.put(ZFB_param_m.charset.toString(), ZFB_Count.charset);//utf-8
		treeMap.put(ZFB_param_m.sign_type.toString(), ZFB_Count.ZFB_SIGN_TYPE.RSA2.toString());
		treeMap.put(ZFB_param_m.timestamp.toString(), new DateTime().toString("yyyy-MM-dd HH:mm:ss"));// 订单生成时间，格式为"yyyy-MM-dd HH:mm:ss"		
		treeMap.put(ZFB_param_m.version.toString(), ZFB_Count.version);// 默认1.0
		return treeMap;
	}
	/**
	 * 获取支付宝预付单
	 * @param parameterStringMap
	 * @return
	 */

	public static Map<String, String> zfb_yfd(Map<String, String> paramMap) {
		Map<String, String> zfb_yfd_config5param = Pay_util.zfb_yfd_config5param();
		// 需要动态处理的参数sign,sign_type,body,
		// attach(附加参数可以空),out_trade_no,total_fee,spbill_create_ip,notify_url
		zfb_yfd_config5param.put(ZFB_param_m.sign_type.toString(), ZFB_SIGN_TYPE.RSA2.toString());//
		zfb_yfd_config5param.put(ZFB_param_m.subject.toString(), paramMap.get("body"));//
		zfb_yfd_config5param.put(ZFB_param_m.out_trade_no.toString(), paramMap.get("out_trade_no"));// 商户订单单号
		zfb_yfd_config5param.put(ZFB_param_m.total_amount.toString(), NumberUtil.div(paramMap.get("total_fee"), "100",2).toString());// 金额(元)
		zfb_yfd_config5param.put(ZFB_param_m.notify_url.toString(), paramMap.get("notify_url"));// 回调入口
		String rsaSign="";
		try {
			rsaSign = AlipaySignature.rsaSign(zfb_yfd_config5param, ZFB_Count.APP_PRIVATE_KEY, ZFB_Count.charset);
			zfb_yfd_config5param.put(ZFB_param_m.sign.toString(),rsaSign);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			throw new RuntimeException("支付宝签名异常:"+e.getErrMsg());
		}
		boolean rsaCheckV1 =false;
		try {
			rsaCheckV1 = AlipaySignature.rsaCheckV1(zfb_yfd_config5param, ZFB_Count.ALIPAY_PUBLIC_KEY, ZFB_Count.charset, ZFB_SIGN_TYPE.RSA2.toString());
		} catch (AlipayApiException e) {
			e.printStackTrace();
			throw new RuntimeException("支付宝签名验证异常:"+e.getErrMsg());
		}
		
		if(rsaCheckV1) {
			return zfb_yfd_config5param;
		}else {
			throw new RuntimeException("支付宝签名异常:验证签名返回false");
		}
		
	}

	/**
	 * 微信预付单签名算法sign
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */

	public static String createSign(Map<String, String> parameters) throws Exception {

		String generateSignature = WXPayUtil.generateSignature(parameters, WX_Count.key, WXPayConstants.SignType.HMACSHA256);

		return generateSignature;
	}

	/**
	 * 支付宝预付单签名
	 * @param paramMap
	 * @return
	 */
	public static HashMap<String, Object> zfb_yfd_qm(String paramMap) {
		String post = HttpUtil.post("https://api.weixin.qq.com/sns/jscode2session", paramMap);
		return null;
	}

	public static String get_total_fee(String string) {
		String total_fee = "";
		if (StringUtils.equals(string, "TC1")) {
			total_fee = TC1;
		} else if (StringUtils.equals(string, "TC2")) {
			total_fee = TC2;
		} else if (StringUtils.equals(string, "TC3")) {
			total_fee = TC3;
		} else {
			throw new RuntimeException("交易的商品类型解析出错,错误解析:" + string);
		}
		return total_fee;
	}

	public static Map<String, String> wx_zf_Res(Map<String, String> parameterStringMap) {
		Map<String, String> wx_zf_res_config5param = Pay_util.wx_zf_res_config5param();
		// 需要动态处理的参数sign,sign_type,out_trade_no
		wx_zf_res_config5param.put(WX_param_m.sign_type.toString(), WX_SignType.HMACSHA256.toString());//

		if (StringUtils.isNotEmpty(parameterStringMap.get("transaction_id"))) {
			wx_zf_res_config5param.put(WX_param_m.transaction_id.toString(), parameterStringMap.get("transaction_id"));// 微信订单单号
		} else {
			wx_zf_res_config5param.put(WX_param_m.out_trade_no.toString(), parameterStringMap.get("out_trade_no"));// 商户订单单号
		}
		try {
			String createSign = Pay_util.createSign(wx_zf_res_config5param);
			wx_zf_res_config5param.put(WX_param_m.sign.toString(), createSign);
		} catch (Exception e) {
			throw new RuntimeException("微信签名异常");
		}

		String mapToXml = "";
		try {
			mapToXml = WXPayUtil.mapToXml(wx_zf_res_config5param);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("mapToXml异常");
		}
		String post = HttpUtil.post(WX_ZF_RES_url, mapToXml);
		System.out.println(post);//???待测试
		
		return wx_zf_res_config5param;
	}

	public static Map<String, String> zfb_zf_Res(Map<String, String> parameterStringMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
