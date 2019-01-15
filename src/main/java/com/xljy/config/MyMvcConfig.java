package com.xljy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.xljy.component.LoginHandlerInterceptor;

//使用WebMvcConfigurerAdapter可以来扩展SpringMVC的功能
//测完了放开
//@Configuration 
public class MyMvcConfig implements WebMvcConfigurer {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginHandlerInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns(
						"/test/NewFile.html", //测试页面
						// "/",
						"/getVerify", //图片验证码
						"/getShouJiYanZhenMa",//获取手机验证码
						"/login/*",  //登录
						"/user/xueshengaddbanji",  //学生加入班级
						"/static/*" //静态资源
						
						
				);
		
		WebMvcConfigurer.super.addInterceptors(registry);
	}
	
	


   


}
