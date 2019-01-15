package com.xljy.component;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xljy.bean.LoginUser;
import com.xljy.bean.Result;
import com.xljy.util.Const;

/**
 * 登陆检查，
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {
    //目标方法执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	//int sf_degnlu =0;//是否登录 0未登录 1初次登录 2之前登录过顶掉的 3正常的
        //Object user = request.getSession().getAttribute(Const.LOGIN_USER);
    		
    	String usertype_code =(String) request.getSession().getAttribute("usertype_code");
    	LoginUser loginuser = (LoginUser) request.getSession().getAttribute(Const.LOGIN_USER);
    	//LoginMap中的session	
    	
        if(loginuser==null){//
            //未登陆，返回登陆页面
            //request.setAttribute("msg","没有权限请先登陆");
            Result resultError = Result.resultError(Const.Errcode.login_out,"未登录或登录超时_请先登陆");
       
            ObjectMapper mapper=new ObjectMapper();
            String writeValueAsString = mapper.writeValueAsString(resultError);
            try {
    			/*son.put("code", code);
    			json.put("msg", "未登录或登录超时_请先登陆");
    			json.put("success",false );
    			*/
            	response.setStatus(740);
            	//返回json
    			response.setContentType("text/json;charset=utf-8");
    			PrintWriter writer = response.getWriter();
    			
    			writer.write(writeValueAsString);
    			writer.flush();
    			writer.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
            //request.getRequestDispatcher("/index.html").forward(request,response);
            return false;
        }else{
        	//之前登录过
            //request.setAttribute("msg","没有权限请先登陆");
        	HttpSession session = LoginMap.getSession(usertype_code, loginuser.getId().toString());
        	
        	if(StringUtils.equals(request.getSession().getId(), session.getId())) {
        		//登陆正常，放行请求
                return true;
        	}else {
        		Result resultError = Result.resultError(Const.Errcode.login_out_o, "您已在其他设备登录,如有异常请重新登录");
                ObjectMapper mapper=new ObjectMapper();
                String writeValueAsString = mapper.writeValueAsString(resultError);
                try {
                	response.setStatus(740);
                	//返回json
        			response.setContentType("text/json;charset=utf-8");
        			PrintWriter writer = response.getWriter();
        			writer.write(writeValueAsString);
        			writer.flush();
        			writer.close();
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
                //别地登陆，拦截
                return false;
			}
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
