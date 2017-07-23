package com.frame.utils.exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frame.dto.ExceptionDto;
import com.frame.utils.HttpCode;

public class FrameExceptionHandler extends SimpleMappingExceptionResolver {

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		ex.printStackTrace();//如果异常，则打印出来，方便后台调试
		ExceptionDto ed=new ExceptionDto();
		if (ex instanceof FrameException) {
			ed.setStatusCode(((FrameException) ex).getStatusCode());
			ed.setMessage(ex.getMessage());
		}else if(ex instanceof DuplicateKeyException){
			ed.setStatusCode(HttpCode.RECORD_EXISTS);
			ed.setMessage("数据的唯一性字段重复");
		}else{
			ed.setMessage(ex.getMessage());
			ed.setStatusCode(HttpCode.FAILURE);
		}
		response.setStatus(ed.getStatusCode());
		String contentType=request.getHeader("Content-Type");
		if (contentType!=null&&contentType.indexOf("json")!=-1) {// 如果是json请求
			try {
				
				response.setHeader("Content-type", "text/html;charset=UTF-8"); 
				response.setCharacterEncoding("UTF-8");
				PrintWriter writer = response.getWriter();
				writer.write(new String(new ObjectMapper().writeValueAsString(ed)));
				writer.flush();
			} catch (IOException e) {

			}
			return new ModelAndView();
		} else {
			return super.doResolveException(request, response, handler, ex);
		}

	}

}
