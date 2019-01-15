package com.xljy.sys.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.tencent.xinge.Style;
import com.tencent.xinge.XingeApp;
import com.xljy.bean.Result;
import com.xljy.sys.servers.DiQu_s;
import com.xljy.util.Const;
import com.xljy.util.Push_ts;
import com.xljy.util.RequestParamsToMap;

@Controller
@RequestMapping("/diqu")
public class DiQu_c {
	
	@Autowired
	private  DiQu_s diqu_s;
	@RequestMapping("/ChaDiQuListByJiBie")
	@ResponseBody
	public Result ChaDiQuListByJiBie(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res = null;
		try {
			List<Map<String, Object>> chazidian = diqu_s.ChaDiQuListByJiBie(parameterStringMap);
			res=Result.resultSuccess(chazidian, "");
			
			return res;
		} catch (Exception e) {
			res=Result.resultError(e.getMessage());
			return res;
		}
		
		
	}
	
	@RequestMapping("/test")
	@ResponseBody
	public Result test(HttpServletRequest request) {
		Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res = null;
		try {
			if (StringUtils.isEmpty(parameterStringMap.get("title")) ) {
				return Result.resultSuccess(null, "title不能空");
			}
			if (StringUtils.isEmpty(parameterStringMap.get("content")) ) {
				return Result.resultSuccess(null, "content不能空");
			}
			JSONObject demoPushSingleAccount=null;
			//demoPushSingleAccount = Push_ts.demoPushSingleAccount("123", "呵呵", "斤斤计较");
			List<String> tagList=new ArrayList<>();
			tagList.add("Student");
			//tagList.add("100063137259");
			//tagList.add("100063137260");
			Message message2 = new Message();
	        message2.setType(Message.TYPE_NOTIFICATION);
	        message2.setTitle(parameterStringMap.get("title"));
	        message2.setContent(parameterStringMap.get("content"));
	        //Style style = new Style(1);
	        ClickAction action = new ClickAction();
	        action.setActionType(ClickAction.TYPE_INTENT);
	        //hwid=cd546973-a47d-4b6f-a55a-531dc99c698c, scoreAll=0.0, flag=已结束, homework_cishu=1
	        action.setIntent("xgscheme://com.xg.push/notify_detail?param1=aa&param2=bb");
	        //message2.setStyle(style);
	        message2.setAction(action);
	        
			//demoPushSingleAccount = Push_ts.demoPushTags(tagList, "呵呵", "123,456,");
			demoPushSingleAccount = Push_ts.demoPushTags(tagList, message2);
			
			//JSONObject pushAccountAndroid = XingeApp.pushAccountAndroid(Const.TS_A_ACCESSID, Const.TS_A_ACCESSKEY, "oadshf", "tenadusfnase", "123");
			//demoPushSingleAccount.toString()
			res=Result.resultSuccess(demoPushSingleAccount.toString(), "");			
			return res;
		} catch (Exception e) {
			res=Result.resultError(e.getMessage());
			return res;
		}	
	}
	
	@RequestMapping("/test2")
	@ResponseBody
	public Result test2(HttpServletRequest request) {
		//Map<String, String> parameterStringMap = RequestParamsToMap.getParameterStringMap(request);
		Result res = null;
		try {
			JSONObject demoPushSingleAccount=null;
			//demoPushSingleAccount = Push_ts.demoPushSingleAccount("123", "呵呵", "斤斤计较");
			/*List<String> tagList=new ArrayList<>();
			tagList.add("Student");*/
		
	        
			//demoPushSingleAccount = Push_ts.demoPushTags(tagList, "呵呵", "123,456,");
			demoPushSingleAccount = Push_ts.demoQueryTags();
			System.out.println("QueryTags>>>>>:"+demoPushSingleAccount);
			demoPushSingleAccount = Push_ts.demoQueryDeviceCount();
			System.out.println("DeviceCount>>>>>:"+demoPushSingleAccount);
			demoPushSingleAccount = Push_ts.demoQueryTagTokenNum("Student");
			System.out.println("Student>>>>>:"+demoPushSingleAccount);
			res=Result.resultSuccess(demoPushSingleAccount.toString(), "");		
			
			return res;
		} catch (Exception e) {
			res=Result.resultError(e.getMessage());
			return res;
		}	
	}
	

}
