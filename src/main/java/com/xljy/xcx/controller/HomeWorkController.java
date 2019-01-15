//package com.xljy.xcx.controller;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.xljy.bean.HomeWork;
//import com.xljy.bean.Result;
//import com.xljy.util.RequestParamsToMap;
//import com.xljy.xcx.services.HomeWorkService;
//
//@Controller
//@RequestMapping("/xcx")
//public class HomeWorkController {
//	@Autowired
//    private HomeWorkService hwService;
//	
//	@RequestMapping("/findLateWork")
//	@ResponseBody
//	public Result findLateWork(@RequestBody List<HomeWork> hw) {
//		Result res = null;
//		try {
//			
//			List<HomeWork> find =hwService.findLateWork(hw.get(0));
//			if(find==null || find.isEmpty()) {
//				res=Result.resultSuccess(find, "此用户暂无作业");
//				res.setCode(200);
//			}else {
//				Date date = new Date();
//			for(int    i=1;    i<hw.size();    i++)    { 
//				System.out.println(date.getTime());
//				
//				if(date.getTime() >= hw.get(i).getT10112().getTime() && date.getTime() <= hw.get(i).getT10113().getTime()) {
//					res=Result.resultSuccess(hw, "进行中");
//					res.setCode(200);
//				}else if(date.getTime() <= hw.get(i).getT10112().getTime()) {
//					res=Result.resultSuccess(hw, "未开始");
//					res.setCode(200);
//				}else if(date.getTime() >= hw.get(i).getT10113().getTime()) {
//					res=Result.resultSuccess(hw, "已结束");
//					res.setCode(200);
//				}
//				res=Result.resultSuccess(hw, "success");
//				res.setCode(200);
//				 }
//			}
//		} catch (Exception e) {
//			 e.printStackTrace();
//			 res = Result.resultError(e.getMessage());
//			 res.setCode(500);
//		}
//		return res;
//		
//	}
//   
//
//
//}
