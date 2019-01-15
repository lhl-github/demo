package com.xljy.util;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;

public class WX_Count {
	/**
	 * 微信签名加密类型
	 * @author Administrator
	 *
	 */
	public enum WX_SignType {
		MD5, HMACSHA256
	}

	/**
	 * 微信参数名枚举
	 * @author Administrator
	 *
	 */
	public enum WX_param_m {
		appid, mch_id, device_info, nonce_str, sign, sign_type, body, detail, attach, out_trade_no, fee_type, total_fee, spbill_create_ip, time_start, time_expire, goods_tag, notify_url, trade_type, limit_pay,transaction_id

	}

	/**
	 * 上图设定好的KEY
	 */
	public static String key = "";
	/**
	 * 网关
	 */
	public static String gatewayUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	/**微信开放平台审核通过的应用APPID（请登录open.weixin.qq.com查看，注意与公众号的APPID不同）
	 * 公众号ID
	 */
	public static String appid = "";
	/**微信支付分配的商户号
	 * 公众账号ID
	 */
	public static String mch_id = "";
	/** 设备号 
	 * 终端设备号(门店号或收银设备ID)，默认请传"WEB"
	 *
	 */
	public static String device_info = "WEB";
	/**随机字符串，不长于32位。推荐随机数生成算法
	 * 随机字符串
	 */
	public static String nonce_str = "";

	/**
	 * 随机字符串，不长于32位。推荐随机数生成算法
	 * @return String 32位
	 */
	public static String getNonce_str() {
		//String simpleUUID = IdUtil.simpleUUID();
		return (UUID.randomUUID().toString()).replaceAll("-", "");
	}

	/**通过签名算法计算得出的签名值，详见签名生成算法
	 * 签名
	 */
	public static String sign = "";
	/**
	 * 签名类型
	 */
	public static String sign_type = "MD5";
	/**商品简单描述，该字段请按照规范传递，具体请见参数规定
	 * 商品描述 eg:腾讯充值中心-QQ会员充值
	 */
	public static String body = "";
	/**商品详细描述，对于使用单品优惠的商户，改字段必须按照规范上传，详见“单品优惠参数说明”
	 * 商品详情
	 */
	public static String detail = "";
	/**附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。
	 * 附加数据
	 */
	public static String attach = "";
	/**商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一。详见商户订单号
	 * 商城订单号
	 */
	public static String out_trade_no = "";
	/**
	 * 币种
	 */
	public static String fee_type = "CNY";
	/**订单总金额，单位为分，详见支付金额
	 * 交易金额（为分）例如12.53 应该（12.53*100） 放入当前值
	 */
	public static int total_fee = 0;
	/**APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
	 * 客户IP
	 */
	public static String spbill_create_ip = "";
	/**订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
	 * 交易起始时间yyyyMMddHHmmss
	 */
	public static String time_start = "";
	/**订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id。其他详见时间规则
	
	建议：最短失效时间间隔大于1分钟
	 * 交易结束时间
	 */
	public static String time_expire = "";
	/**订单优惠标记，使用代金券或立减优惠功能时需要的参数，说明详见代金券或立减优惠
	 * 这个字段空着即可（订单优惠标记）
	 */
	public static String goods_tag = "";
	/**异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
	 * 微信回调接口（重要）
	 */
	public static String notify_url = "";
	/**
	 * 支付交易类型
	 * 说明详见参数规定
	 * JSAPI -JSAPI支付
	 * NATIVE -Native支付
	 *  APP -APP支付
	 */

	public static String trade_type = "APP";
	/**当trade_type=NATIVE时，此参数必传。此参数为二维码中包含的商品ID，商户自行定义
	 * 支付订单号
	 */
	public static String product_id = "";
	/**no_credit--指定不能使用信用卡支付
	 * 指定支付方式是否限定用户用户信用卡 (这个可以空着)
	 */
	public static String limit_pay = "no_credit";
	/**
	 * 扫描支付 
	 *  不需要可以空着
	 */
	public static String openid = "";
	/**
	 * 场景信息（可以空着）
	 */
	public static String scene_info = "";

	public static void main(String[] args) {
		System.out.println(nonce_str);
	}
}
