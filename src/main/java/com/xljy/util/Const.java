package com.xljy.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Editor;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpUtil;

public class Const {

	// restful code
	/**
	 * Errcode 内部用错误代码
	 * @author lhl
	 */
	public static class Errcode {

		/**
		 * 参数错误
		 */
		public static final int parameter_err = 10100;
		/**
		 * 成功 ,正常
		 */
		public static final int ok = 200;

		/**
		 * 登录失败
		 */
		public static final int login_err = 10001;

		/**
		 * 未登录或登录超时
		 */
		public static final int login_out = 10002;
		/**
		 * 您已在其他设备登录
		 */
		public static final int login_out_o = 10003;

		/**
		 * 查询异常
		 */
		public static final int query_err = 10200;
		/**
		 * 添加异常
		 */
		public static final int insert_err = 10201;
		/**
		 * 修改异常
		 */
		public static final int update_err = 10202;

		/**
		 * 删除异常
		 */
		public static final int del_err = 10203;
		/**
		 * 文件不存在
		 */
		public static final int file_err = 10400;

	}

	/**
	 * 登录的用户key
	 */
	public static final String LOGIN_USER = "login_user";
	// session_Attribute_key----------------
	/**
	 * 用户登录验证码
	 */
	public static final String KEY_DENGLV = "user_denglv_yanzhengma";
	/**
	 * 用户登注册证码
	 */
	public static final String KEY_ZHUCE = "user_zhuce_yanzhengma";
	/**
	 * 用户修改密码
	 */
	public static final String KEY_UPD_MIMA = "user_upd_mima_yanzhengma";
	/**
	 * 用户删除班级
	 */
	public static final String KEY_DEL_BANJI = "user_del_banji_yanzhengma";
	/**
	 * 家长解除学生绑定
	 */
	public static final String KEY_UBD_STU = "user_ubd_stu_yanzhengma";
	/**
	 * 用户修改手机号
	 */
	public static final String KEY_UPD_SJH = "user_upd_sjh_yanzhengma";

	// 小程序
	/**
	 * 教师小程序 AppID
	 */
	public static final String XCX_AppID_js = "wxd0458176ae8563b1";
	/**
	 * 教师小程序 appSecret
	 */
	public static final String XCX_AppSecret_js = "65dc70a8f93d8f97058986d1ea1779ac";
	/**
	 * 家长小程序 AppID
	 */
	public static final String XCX_AppID_jz = "wx00483d20d24f2b86";
	//public static final String XCX_AppID_jz = "wx8d90010a6016f169";//lhl
	/**
	 * 家长小程序 appSecret
	 */
	//public static final String XCX_AppSecret_jz ="958ffbf1b480ef55032e07fea2fc80d1"; //lhl
	public static final String XCX_AppSecret_jz = "45e0b2a76acc02979e25944d755ab05f";

	// 短息---------
	/**
	 * 短信应用SDK AppID
	 */
	public static final int DX_SDK_AppID = 1400155655;
	/**
	 * 短信应用SDK AppKey
	 */
	public static final String DX_SDK_appkey = "9f08ccbe7bf562eb42e18c6cea811760";
	/**
	 * 短信模板ID
	 */
	public static final int DX_TemplateId_YZM = 234350;
	/**
	 * 短信签名
	 */
	public static final String DX_smsSign = "学乐教育";
	// --------------------------------------------------
	// 推送消息 安卓
	/**
	 * 信鸽APPID
	 */
	public static final String TS_A_APPID = "b9ddf90e75733";
	/**
	 * 信鸽SECRETKEY
	 */
	public static final String TS_A_SECRETKEY = "24ea72ab662a7c21f86479c07f324af4";
	/**
	 * 信鸽accessId
	 */
	public static final long TS_A_ACCESSID = 2100322577L;
	/**
	 * 信鸽accessKey
	 */
	public static final String TS_A_ACCESSKEY = "AZ998UTKC49Q";

	// 推送消息 ios
	/**
	 * 信鸽APPID
	 */
	public static final String TS_I_APPID = "16f3602a1d49c";
	/**
	 * 信鸽SECRETKEY
	 */
	public static final String TS_I_SECRETKEY = "561f1fa6d1ecf9bd902301d8b3787ba6";
	/**
	 * 信鸽accessId
	 */
	public static final long TS_I_ACCESSID = 2200322646L;
	/**
	 * 信鸽accessKey
	 */
	public static final String TS_I_ACCESSKEY = "IX6DH6513TBP";
	// ------------------------------------------------

	// TOMCAT_HOME
	// 这里需要转义 并把\(捺)换成/(撇) 必须换 不然 访问被拒绝 或 路径不存在错误 烦人的很
	public static final String TOMCAT_HOME = StringUtils.replace(System.getProperty("catalina.home") + "/", "\\", "/");//
	// 基础文件夹
	public static final String BATH = "UploadFiles/";//
	// 图片的
	public static final String TUPIAN = "tupian/";//
	// 音频的
	public static final String YINPIN = "yinpin/"; //
	// 视频的
	public static final String SHIPIN = "shipin/"; //

	// =================================================
	public static final String KEWEN = "kewen/";//

	public static final String DANCI = "danci/";//

	public static final String JVZI = "jvzi/";//

	public static final String DUIHUA = "duihua/"; //

	public static final String TINGLI = "tingli/"; //

	public static final String YUEDU = "yuedu/"; //

	public static final String ZUOWEN = "zuowen/"; //

	public static final String DUANYU = "duanyu/"; //

	public static final String FOLLOWTEXT = "followText/"; //

	// 创建图片类型数组
	private static String img[] = { "bmp", "jpg", "jpeg", "png", "tiff", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd", "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "wmf" };
	// 创建视频类型数组
	private static String video[] = { "mp4", "avi", "mov", "wmv", "asf", "navi", "3gp", "mkv", "f4v", "rmvb", "webm" };

	// 创建音乐类型数组
	private static String music[] = { "mp3", "wma", "wav", "mod", "ra", "cd", "md", "asf", "aac", "vqf", "ape", "mid", "ogg", "m4a", "vqf" };

	/**
	 * 返回: 文件保存的真实路径
	 * 
	 * @param save_path
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static String getFilePath_Real(String save_path, String type) throws RuntimeException {

		if (null == type) {
			throw new RuntimeException("参数type 不能空");
		} else if (type.equals("1")) { // 图
			return TOMCAT_HOME + BATH + save_path + TUPIAN;
		} else if (type.equals("2")) {// 音频
			return TOMCAT_HOME + BATH + save_path + YINPIN;
		} else if (type.equals("3")) {// 视频
			return TOMCAT_HOME + BATH + save_path + SHIPIN;
		} else {
			throw new RuntimeException("参数type 解析不到对应的路径");

		}

	}

	/**
	 * 返回: 文件保存的真实路径的父级路径
	 * 
	 * @param save_path
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static String getFilePath_Real(String save_path) {
		return TOMCAT_HOME + BATH + save_path;
	}

	public static String getFilePath(String type) throws RuntimeException {
		if (null == type) {
			throw new RuntimeException("参数type 不能空");
		} else if (type.equals("1")) { // 图
			return TUPIAN;
		} else if (type.equals("2")) {// 音频
			return YINPIN;
		} else if (type.equals("3")) {// 音频
			return SHIPIN;
		} else {
			throw new RuntimeException("参数type 解析不到对应的路径");
		}
	}

	/**
	 * 判目录是否存在 不存在就创建
	 * 
	 * @param filePath
	 */
	public static void exists_LuJing(String filePath) {
		File wenjianjia = new File(filePath);
		if (!wenjianjia.exists()) { // 判目录是否存在

			boolean mkdirs = wenjianjia.mkdirs();
			System.out.println("创建文件路径:" + filePath);
			System.out.println("创建文件路径状态:" + mkdirs);

		}

	}

	/**
	 * 获取文件拓展名 带点的
	 * 
	 * @param filename
	 * @param i
	 *            //只接受0,1 (0表示带点的 1表示不带点的)
	 * @return
	 * @throws Exception
	 */
	public static String getExtensionName(String filename, int i) throws Exception {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + i);
			}
		} else {
			throw new Exception("无法解析的文件名 缺少拓展名");
		}
		return filename;
	}

	public String getfileType(String fileName) {
		if (fileName == null) {
			fileName = "文件名为空！";
			return null;

		} else {
			// 获取文件后缀名并转化为写，用于后续比较
			String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();

			for (int i = 0; i < img.length; i++) {
				if (img[i].equals(fileType)) {
					return "1";
				}
			}

			/*
			 * // 创建文档类型数组 String document[] = { "txt", "doc", "docx", "xls", "htm", "html",
			 * "jsp", "rtf", "wpd", "pdf", "ppt" }; for (int i = 0; i < document.length;
			 * i++) { if (document[i].equals(fileType)) { return "文档"; } }
			 */

			for (int i = 0; i < video.length; i++) {
				if (video[i].equals(fileType)) {
					return "3";
				}
			}

			for (int i = 0; i < music.length; i++) {
				if (music[i].equals(fileType)) {
					return "2";
				}
			}

		}
		return "404";// 未知类型
	}

	public static String[] getyinpin(String[] array) {
		return ArrayUtil.filter(array, new Editor<String>() {// 过滤出音频的
			@Override
			public String edit(String t) {

				// 获取文件后缀名并转化为写，用于后续比较
				String fileType = t.substring(t.lastIndexOf(".") + 1, t.length()).toLowerCase();

				for (int i = 0; i < music.length; i++) {
					if (music[i].equals(fileType)) {
						return t;
					}
				}
				return null;
			}
		});

	}

	public static String[] gettupian(String[] array) {
		return ArrayUtil.filter(array, new Editor<String>() {// 过滤出图片的
			@Override
			public String edit(String t) {

				// 获取文件后缀名并转化为写，用于后续比较
				String fileType = t.substring(t.lastIndexOf(".") + 1, t.length()).toLowerCase();

				for (int i = 0; i < img.length; i++) {
					if (img[i].equals(fileType)) {
						return t;
					}
				}
				return null;
			}
		});
	}

	public static String[] getshipin(String[] array) {
		return ArrayUtil.filter(array, new Editor<String>() {// 过滤出音频的
			@Override
			public String edit(String t) {

				// 获取文件后缀名并转化为写，用于后续比较
				String fileType = t.substring(t.lastIndexOf(".") + 1, t.length()).toLowerCase();

				for (int i = 0; i < video.length; i++) {
					if (video[i].equals(fileType)) {
						return t;
					}
				}
				return null;
			}
		});
	}

	public static void delfile_lujingxiabubaohan(HashMap<String, Object> hashMap, String[] arr_filename, String filePath, String string) {
		if (arr_filename.length == 0) {
			hashMap.put(string + ":路径无文件,做删除", filePath);
			boolean del = FileUtil.del(filePath);
			if (!del) {
				hashMap.put(string + ":文件路径不存在了", filePath);
			} else {
				hashMap.put(string + ":删除文件及目录成功", filePath);
			}
		} else {
			hashMap.put(string + ":路径有文件,做对比删除", filePath);
			String FileNamesString = ArrayUtil.toString(arr_filename);
			List<File> loopFiles = FileUtil.loopFiles(filePath);
			for (int i = 0; i < loopFiles.size(); i++) {
				File file = loopFiles.get(i);
				String name = file.getName();
				if (!org.apache.commons.lang3.StringUtils.contains(FileNamesString, name)) {// 比较包含
					file.delete();// 删除这个不包含的文件
					hashMap.put("msg" + i, "多余文件已删除:" + name);
					// System.out.println(hashMap);
				}
			}
		}

	}

	/**
	 * 通过key_use_d解析对应的Attribute_key
	 * @param key_use_d
	 * @return
	 */
	public static String session_Attribute_key_use_d(String key_use_d) {
		if (StringUtils.isEmpty(key_use_d)) {
			throw new RuntimeException("验证码用途码不能空");

		} else if (StringUtils.equals(key_use_d, "0")) {// zhuce
			return KEY_ZHUCE;
		} else if (StringUtils.equals(key_use_d, "1")) {// 登录
			return KEY_DENGLV;
		} else if (StringUtils.equals(key_use_d, "2")) {// 密码
			return KEY_UPD_MIMA;
		} else if (StringUtils.equals(key_use_d, "3")) {// 删班级
			return KEY_DEL_BANJI;
		} else if (StringUtils.equals(key_use_d, "4")) {// 家长解除学生绑定
			return KEY_UBD_STU;
		} else if (StringUtils.equals(key_use_d, "5")) {// 修改手机号
			return KEY_UPD_SJH;
		} else {
			throw new RuntimeException("验证码用途解析失败:解析码为" + key_use_d);
		}

	}

	/**
	 * 通过js_code解析 微信的openid
	 * @param js_code
	 * @param usertype_code 
	 * @return
	 */
	public static HashMap<String, Object> weixin_jscode2session(String js_code, String usertype_code) {
		HashMap<String, Object> Map_ret = new HashMap<>();
		Map<String, Object> paramMap = new HashMap<>();
		if (StringUtils.equals(usertype_code, "2")) {
			paramMap.put("appid", Const.XCX_AppID_js);
			paramMap.put("secret", Const.XCX_AppSecret_js);
		} else if (StringUtils.equals(usertype_code, "4")) {
			paramMap.put("appid", Const.XCX_AppID_jz);
			paramMap.put("secret", Const.XCX_AppSecret_jz);
		}

		paramMap.put("js_code", js_code);
		paramMap.put("grant_type", "authorization_code");
		String post = HttpUtil.post("https://api.weixin.qq.com/sns/jscode2session", paramMap);

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> post_resp = null;
		try {
			post_resp = mapper.readValue(post, HashMap.class);
		} catch (JsonParseException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}

		Map_ret.put("weixin_result", post_resp);
		if (!post_resp.containsKey("errcode")) {// 获取成功
			Map_ret.put("sf_ok", true);
		} else {// 失败
			Map_ret.put("sf_ok", false);
		}
		return Map_ret;
	}

	// 测试
	/*
	 * public static void main(String[] args) { HashMap<String, Object> hashMap =
	 * new HashMap<>(); hashMap.put("save_path",
	 * "D:/kf_ser/apache-tomcat-8.5.34/UploadFiles/9_3_5_1_3/tingli/8c748cf8-8e61-4c75-a9ff-7d1faf723f49"
	 * ); FileUtil.del(
	 * "D:/kf_ser/apache-tomcat-8.5.34/UploadFiles/9_3_5_1_3/tingli/8c748cf8-8e61-4c75-a9ff-7d1faf723f49"
	 * );
	 * 
	 * if(true) { return ; } String[] arr_file = { "123.Mp3", "345.mp3","346.mp3",
	 * "654.jpg", "623444.jpg","876.jpg", "876.mp4"}; String[] arr_shipin =
	 * getshipin(arr_file); String[] arr_yinpin = getyinpin(arr_file); String[]
	 * arr_tupian = gettupian(arr_file);
	 * 
	 * 
	 * System.err.println(ArrayUtil.toString(arr_file));
	 * 
	 * String yinpinfilePath = hashMap.get("save_path").toString()+"yinpin";// 2音频
	 * String tupianfilePath = hashMap.get("save_path").toString()+"tupian";// 1图片
	 * String shipinfilePath = hashMap.get("save_path").toString()+"shipin";// 3视频
	 * 
	 * Const.delfile_lujingxiabubaohan(hashMap, arr_yinpin, yinpinfilePath, "音频");
	 * Const.delfile_lujingxiabubaohan(hashMap, arr_tupian, tupianfilePath, "图片");
	 * Const.delfile_lujingxiabubaohan(hashMap, arr_shipin, shipinfilePath, "视频");
	 * System.out.println(hashMap);
	 * 
	 * }
	 */
}
