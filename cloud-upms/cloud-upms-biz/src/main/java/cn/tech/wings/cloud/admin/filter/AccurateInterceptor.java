package cn.tech.wings.cloud.admin.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AccurateInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		//空实现即可，目的让agent能够注入进来
//		Enumeration<String> headerNames = request.getHeaderNames();
//		//使用循环遍历请求头，并通过getHeader()方法获取一个指定名称的头字段
//		while (headerNames.hasMoreElements()){
//			String headerName = headerNames.nextElement();
//			log.info("header中信息,headerName:{},headerValue:{}",headerName ,request.getHeader(headerName));
//		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
								Exception ex) {
	}

}
