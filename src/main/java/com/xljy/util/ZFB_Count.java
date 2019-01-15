package com.xljy.util;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;

public class ZFB_Count {
	/**
	 * 支付宝签名加密类型
	 * @author Administrator
	 *
	 */
	public enum ZFB_SIGN_TYPE {
		RSA2, RSA
	}

	/**
	 * ZFB参数名枚举
	 * @author Administrator
	 *
	 */
	public enum ZFB_param_m {
		URL, ALIPAY_PUBLIC_KEY, APP_PRIVATE_KEY, app_id, method, format, charset, sign_type, sign, timestamp, version, notify_url, biz_content, body, subject, out_trade_no, timeout_express, total_amount, product_code, goods_type, passback_params, promo_params, extend_params, enable_pay_channels, disable_pay_channels, store_id, ext_user_info
	}

	/**
	 * 支付宝 网关
	 */
	public static String URL = "https://openapi.alipay.com/gateway.do";
	/**必填.
	 *  APPID即创建应用后生成
	 */
	public static String app_id = "";
	/**开发者应用私钥，由开发者自己生成
	 */
	public static String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCoVRvub/wurpRq" + "6aeS/15Jped32WXcIIBrO1rhpWtmVrvNz8ufDtP7TD0zCxT3x0uQfEnncH/A6J/G"
			+ "SY28bdiVD8QoJVsK42kqJdHxu9hgcbxeY68OCwAKhzyuZyu4fn9f53jOhQdDx1A4" + "/iVav1W2HVTsU8EU4obK7T5OKcts1EWII8zolE6XndHzEY+LivdhuasMQ0ikAeBN" + "C4hHN45K86QeZvBgss4VqvCkbaCZ4/pQxQOZrpD776EDwMeY6gVrstHe16pulfo2"
			+ "tKloMQGtRNAZZs0LNV+xdvlR0leVBVINiB8zf134AtFe9cSDp2u8MTZxI8frDQ6/" + "R/7OBkBhAgMBAAECggEACTsxnu/ROpYkWYCayfrrJsrMWMw5Q8scf8TvhPRXYXNc" + "NTsyBXUAtwVFTelLqxLo1/GY8/I9b0zShkM+XJkBiZslvLXfukciW0dbrhAJG7Dh"
			+ "DaQV04VS6UDlWQMbaLC3N4EwJNMyxMrHYXi+f62Ce8w7/gHR0t1hWvuLsSmlmEAt" + "C8JxHl5cFR8jvcm13oakOOjDecQ4TY1sqeXliSd2z5vuJmL3dJrL7puuqfZ+/pRC" + "rSoYUF1AqqkVs3QVDo5C7ubjPIKPp/aUcaudnfs5tW2s/jA9agDg648yNcvFn+lK"
			+ "ZE2xfdbEb8gavLMIgKoJW5QBArc6qRstnZ6fmOvH4QKBgQDaZ/ESjTfpzOlfR/Cq" + "OyhY6wvE59ph7YMettnamjOIGBUNgA846um4HqdLYfcTU6BxWirscrnvABH3pPyS" + "Wo8qSefVHEYCSL6/jgo+mEUxZgi0O60SB+YXoQcSTZeH6fJX7MXlxJ3CunjMYIXC"
			+ "CswVskxBg3EJ0Eeeb9KBwZ3otQKBgQDFTq0uQuUfc265QywIMyUF69p2IZUPC5Ae" + "8i7em2wFseQE3BcxlByHZ7owLt0N+z2az8xF/Rp0Hjdiq/64+SsGpEx5K5N/OXu9" + "tDNfKMGGcAWoxLUOwwofbpg8IwCWIxo4jeBwvaSdMhqUQEUvp0KeJpMAIa3NCfj1"
			+ "Kx1cmaEgfQKBgQDQUKW6aSG0ibLqI2bkJ2qzbPUbyX0dPHp82DDwdXXDUBz8dHGQ" + "e1eTOwGcQAN21qPImhUo08COuoVorGAPg/rbfgR22vHXieUONf+Fdp1Aoa6ZofXQ" + "peIMFZX2GjX29+gjfv2+ywsiSdOgNExAKSZBZ3+eWS1/C77T2ppxEuh3pQKBgFlG"
			+ "KPu2jt81JpMyvniTU5wjRDyRI1WkYLhv0h8s6aqF++rq/p+TKXlmyIrGmYSa+hvT" + "C9VbGR4TEfJWajGdpAckTenBesJk6wVBxMC4ux+uDhF3t4iPXUQtkTociEZmaZXP" + "NA8u742kvvv3PJa8MltGjtrPYklUys17jZR5erstAoGBAMgt9S6RNasruOYDvH69"
			+ "NeB+bjLjK5kSikaBfWYkpVfOlB3xmGX2CkwqCrlg29f5JRMtKX6HdHP5vCpzo42X" + "+tXhNudH1KrioXhFtZOhdI2u2x5fSmz/1inxl2siPFdVKhXV/Ctlh/QCVnqZ4YXI" + "ngfCPWNka87ZLDgCh/Z+/q01";

	/**必填.
	 * 接口名称，eg:alipay.trade.app.pay
	 */
	public static String method = "";
	/**必填.
	* 参数返回格式，只支持json
	*/
	public static String format = "json";
	/**必填.
	 * 请求使用的编码格式，如utf-8,gbk,gb2312等
	 * (定义默认utf-8)
	 */
	public static String charset = "utf-8";

	/**
	 * 支付宝公钥，由支付宝生成
	 */
	public static String ALIPAY_PUBLIC_KEY = "";
	/**必填.
	 * 商户生成 支付宝 签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
	 */
	public static String sign_type = "RSA2";
	/**必填.
	 * 支付宝生成的 签名字符串
	 */
	public static String sign = "";

	/**必填.
	 * 支付宝交易 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"//每次都是新值
	 */
	public static String timestamp = "";
	/**必填.
	 * 调用的接口版本，固定为：1.0
	 */
	public static String version = "1.0";

	/**必填.
	 * 支付宝服务器主动通知商户服务器里指定的页面http/https路径。建议商户使用https
	 */
	public static String notify_url = "";

	/**必填.
	 * 业务请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
	 */
	public static String biz_content = "";
	// --业务数据-----------------------------------------------------
	/**选填.
	 * 业务参数的:对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
	 */
	public static String body = "";
	/**必填.
	 * 商品的标题/交易标题/订单标题/订单关键字等。
	 */
	public static String subject = "";
	/**必填.
	 * 商户网站唯一订单号
	 */
	public static String out_trade_no = "";
	/**选填.
	 * 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
	注：若为空，则默认为15d。
	 */
	public static String timeout_express = "";
	/**必填.
	 * 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
	 */
	public static String total_amount = "";
	/**必填.
	 * 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
	 */
	public static String product_code = "QUICK_MSECURITY_PAY";
	/**选填.
	 * 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
	 */
	public static String goods_type = "QUICK_MSECURITY_PAY";
	/**选填.
	 * 公用回传参数，如果请求时传递了该参数，则返回给商户时会回传该参数。支付宝会在异步通知时将该参数原样返回。本参数必须进行UrlEncode之后才可以发送给支付宝
	 */
	public static String passback_params = "";
	/**选填.
	 * 优惠参数 注：仅与支付宝协商后可用
	 */
	public static String promo_params = "";
	/**选填.
	 * 业务扩展参数，详见下面的“业务扩展参数说明”
	 */
	public static String extend_params = "";
	/**选填.
	 * 可用渠道，用户只能在指定渠道范围内支付当有多个渠道时用“,”分隔
		注：与disable_pay_channels互斥
	 */
	public static String enable_pay_channels = "";
	/**选填.
	 * 禁用渠道，用户不可用指定渠道支付
		当有多个渠道时用“,”分隔
		注：与enable_pay_channels互斥
	 */
	public static String disable_pay_channels = "";
	/**选填.
	 *商户门店编号。该参数用于请求参数中以区分各门店，非必传项。
	 */
	public static String store_id = "";
	/**选填.
	 *外部指定买家，详见外部用户ExtUserInfo参数说明
	 */
	public static String ext_user_info = "";

}
