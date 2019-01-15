package com.xljy.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.Style;
import com.tencent.xinge.TimeInterval;
import com.tencent.xinge.XingeApp;

/**
 * 推送的
 * @author lhl
 *
 */
public class Push_ts {

	/**a下发 单个账号Account*/
	public static JSONObject demoPushSingleAccount(String Account, String title, String content) {
		Message message = new Message();
		message.setExpireTime(86400);
		// message.setTitle("title");
		// message.setContent("content");
		message.setTitle(title);
		message.setContent(content);
		//TYPE_NOTIFICATION通知栏消息  TYPE_MESSAGE应用内消息
		message.setType(Message.TYPE_NOTIFICATION);
		XingeApp xinge = new XingeApp(Const.TS_A_ACCESSID, Const.TS_A_SECRETKEY);
		JSONObject ret = xinge.pushSingleAccount(0, Account, message);
		return ret;
	}

	/**a下发多个账号*/
	public static JSONObject demoPushAccountList(List<String> accountList, String title, String content) {
		Message message = new Message();
		message.setExpireTime(86400);
		// message.setTitle("title");
		// message.setContent("content");
		message.setTitle(title);
		message.setContent(content);
		//TYPE_NOTIFICATION通知栏消息  TYPE_MESSAGE应用内消息
		message.setType(Message.TYPE_NOTIFICATION);
		// 账号list
		/*
		 * List<String> accountList = new ArrayList<String>();
		 * accountList.add("joelliu");
		 */
		XingeApp xinge = new XingeApp(Const.TS_A_ACCESSID, Const.TS_A_SECRETKEY);
		JSONObject ret = xinge.pushAccountList(0, accountList, message);
		return ret;
	}

	/**a下发多标签 选中的设备*/
	public static JSONObject demoPushTags(List<String> tagList, String title, String content) {

		/*Message message1 = new Message();
		message1.setType(Message.TYPE_NOTIFICATION);
		Style style = new Style(1);
		style = new Style(3, 1, 0, 1, 0);
		ClickAction action = new ClickAction();
		action.setActionType(ClickAction.TYPE_URL);
		action.setUrl("http://xg.qq.com");
		Map<String, Object> custom = new HashMap<String, Object>();
		custom.put("key1", "value1");
		custom.put("key2", 2);
		message1.setTitle("title");
		message1.setContent("大小");
		message1.setStyle(style);
		message1.setAction(action);
		message1.setCustom(custom);
		TimeInterval acceptTime1 = new TimeInterval(0, 0, 23, 59);
		message1.addAcceptTime(acceptTime1);*/

		Message message = new Message();
		message.setExpireTime(86400);
		// message.setTitle("title");
		// message.setContent("content");
		message.setTitle(title);
		message.setContent(content);
		//TYPE_NOTIFICATION通知栏消息  TYPE_MESSAGE应用内消息
		message.setType(Message.TYPE_NOTIFICATION);

		// 通过标签下发的标签列表
		/*
		 * List<String> tagList = new ArrayList<String>();
		 * tagList.add("joelliu");
		 * tagList.add("phone");
		 */
		XingeApp xinge = new XingeApp(Const.TS_A_ACCESSID, Const.TS_A_SECRETKEY);
		JSONObject ret = xinge.pushTags(0, tagList, "OR", message);
		return ret;
	}
	
	public static JSONObject demoPushTags(List<String> tagList, Message message) {
		XingeApp xinge = new XingeApp(Const.TS_A_ACCESSID, Const.TS_A_SECRETKEY);
		JSONObject ret = xinge.pushTags(0, tagList, "OR", message);
		return ret;
	}
	


	/**a查询标签 ,查询应用所有的tags*/
	public static JSONObject demoQueryTags() {
		XingeApp xinge = new XingeApp(Const.TS_A_ACCESSID, Const.TS_A_SECRETKEY);
		JSONObject ret = xinge.queryTags();
		return ret;
	}

	/**a查询某个tag下token的数量*/
	public static JSONObject demoQueryTagTokenNum(String tag) {
		XingeApp xinge = new XingeApp(Const.TS_A_ACCESSID, Const.TS_A_SECRETKEY);
		// JSONObject ret = xinge.queryTagTokenNum("tag");
		JSONObject ret = xinge.queryTagTokenNum(tag);
		return ret;
	}

	/**a查询设备数量*/
	public static JSONObject demoQueryDeviceCount() {
		XingeApp xinge = new XingeApp(Const.TS_A_ACCESSID, Const.TS_A_SECRETKEY);
		JSONObject ret = xinge.queryDeviceCount();
		return ret;
	}

	/**a下发所有设备*/
	public static JSONObject demoPushAllDevice(String title, String content) {
		/*Message message1 = new Message();
		message1.setType(Message.TYPE_NOTIFICATION);
		Style style = new Style(1);
		style = new Style(3, 1, 0, 1, 0);
		ClickAction action = new ClickAction();
		action.setActionType(ClickAction.TYPE_URL);
		action.setUrl("http://xg.qq.com");
		Map<String, Object> custom = new HashMap<String, Object>();
		custom.put("key1", "value1");
		custom.put("key2", 2);
		message1.setTitle("title");
		message1.setContent("大小");
		message1.setStyle(style);
		message1.setAction(action);
		message1.setCustom(custom);
		TimeInterval acceptTime1 = new TimeInterval(0, 0, 23, 59);
		message1.addAcceptTime(acceptTime1);*/

		Message message = new Message();
		message.setExpireTime(86400);
		// message.setTitle("title");
		// message.setContent("content");
		message.setTitle(title);
		message.setContent(content);
		
		//TYPE_NOTIFICATION通知栏消息  TYPE_MESSAGE应用内消息
		message.setType(Message.TYPE_NOTIFICATION);
		XingeApp xinge = new XingeApp(Const.TS_A_ACCESSID, Const.TS_A_SECRETKEY);
		JSONObject ret = xinge.pushAllDevice(0, message);
		return ret;
	}

	/**i下发IOS单个账号*/
	public static JSONObject demoPushSingleAccountIOS(String Account) {
		MessageIOS message = new MessageIOS();
		message.setExpireTime(86400);
		message.setAlert("ios test");
		message.setBadge(1);
		message.setSound("beep.wav");
		TimeInterval acceptTime1 = new TimeInterval(0, 0, 23, 59);
		message.addAcceptTime(acceptTime1);
		Map<String, Object> custom = new HashMap<String, Object>();
		custom.put("key1", "value1");
		custom.put("key2", 2);
		message.setCustom(custom);
		XingeApp xinge = new XingeApp(Const.TS_I_ACCESSID, Const.TS_I_SECRETKEY);
		JSONObject ret = xinge.pushSingleAccount(0, Account, message, XingeApp.IOSENV_DEV);
		return ret;
	}

	/**i下发IOS多个账号*/
	public static JSONObject demoPushAccountListIOS(List<String> accountList) {
		MessageIOS messageIOS = new MessageIOS();
		messageIOS.setType(MessageIOS.TYPE_APNS_NOTIFICATION);
		messageIOS.setExpireTime(86400);
		messageIOS.setAlert("ios test");
		messageIOS.setBadge(1);
		messageIOS.setCategory("INVITE_CATEGORY");
		messageIOS.setSound("beep.wav");

		/*List<String> accountList = new ArrayList<String>();
		accountList.add("joelliu");*/

		XingeApp xinge = new XingeApp(Const.TS_I_ACCESSID, Const.TS_I_SECRETKEY);
		JSONObject ret = xinge.pushAccountList(0, accountList, messageIOS, XingeApp.IOSENV_DEV);
		return ret;
	}

	/**ios查询标签 ,查询应用所有的tags*/
	public static JSONObject demoQueryTags_i() {
		XingeApp xinge = new XingeApp(Const.TS_I_ACCESSID, Const.TS_I_SECRETKEY);
		JSONObject ret = xinge.queryTags();
		return ret;
	}

	/**ios查询某个tag下token的数量*/
	public static JSONObject demoQueryTagTokenNum_i(String tag) {
		XingeApp xinge = new XingeApp(Const.TS_I_ACCESSID, Const.TS_I_SECRETKEY);
		// JSONObject ret = xinge.queryTagTokenNum("tag");
		JSONObject ret = xinge.queryTagTokenNum(tag);
		return ret;
	}

	/**ios查询设备数量*/
	protected static JSONObject demoQueryDeviceCount_i() {
		XingeApp xinge = new XingeApp(Const.TS_I_ACCESSID, Const.TS_I_SECRETKEY);
		JSONObject ret = xinge.queryDeviceCount();
		return ret;
	}

	/**ios下发所有设备*/
	public static JSONObject demoPushAllDevice_i(String title, String content) {
		/*Message message1 = new Message();
		message1.setType(Message.TYPE_NOTIFICATION);
		Style style = new Style(1);
		style = new Style(3, 1, 0, 1, 0);
		ClickAction action = new ClickAction();
		action.setActionType(ClickAction.TYPE_URL);
		action.setUrl("http://xg.qq.com");
		Map<String, Object> custom = new HashMap<String, Object>();
		custom.put("key1", "value1");
		custom.put("key2", 2);
		message1.setTitle("title");
		message1.setContent("大小");
		message1.setStyle(style);
		message1.setAction(action);
		message1.setCustom(custom);
		TimeInterval acceptTime1 = new TimeInterval(0, 0, 23, 59);
		message1.addAcceptTime(acceptTime1);*/

		Message message = new Message();
		message.setExpireTime(86400);
		// message.setTitle("title");
		// message.setContent("content");
		message.setTitle(title);
		message.setContent(content);
		message.setType(Message.TYPE_MESSAGE);
		XingeApp xinge = new XingeApp(Const.TS_I_ACCESSID, Const.TS_I_SECRETKEY);
		JSONObject ret = xinge.pushAllDevice(0, message);
		return ret;
	}




}
