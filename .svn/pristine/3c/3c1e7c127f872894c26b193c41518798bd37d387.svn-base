package com.frame.utils.interceptor;

import java.lang.reflect.Method;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.frame.dto.AuthorizationDto;
import com.frame.service.AuthorizationService;
import com.frame.utils.Constant;
import com.frame.utils.HttpCode;
import com.frame.utils.StringUtil;
import com.frame.utils.annotation.Login;
import com.frame.utils.annotation.Open;
import com.frame.utils.exception.FrameException;
import com.frame.utils.pager.SystemContext;

public class AuthInterceptor extends HandlerInterceptorAdapter {
	private AuthorizationService authorizationService;
	private int defaultPageSize = 15;/*默认一页多少条*/

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 模拟session开始
		// 从header中取出登录凭证
		String authorization = request.getHeader(Constant.AUTHORIZATION);
		AuthorizationDto authorizationDto = null;
		if (StringUtil.isNotEmpty(authorization)) {
			authorizationDto = authorizationService.findAuthorizationDtoByAuthorization(authorization);
			if (authorizationDto != null) {
				authorizationService.expire(authorization, request.getIntHeader(Constant.DEVICE_TYPE));
			}
		}
		// 模拟session结束

		// 直接从aop中处理得到实际访问的uri，并且是形式上定义的uri
		String uri = null;
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		String[] rmValue;
		// 得到controller的映射
		Class<?> beanType = handlerMethod.getBeanType();
		rmValue = beanType.getAnnotation(RequestMapping.class).value();
		uri = rmValue[0];
		// 得到method的映射
		Method method = handlerMethod.getMethod();
		rmValue = method.getAnnotation(RequestMapping.class).value();
		if (rmValue.length != 0) {
			uri += rmValue[0];
		}
		/* 分页特殊处理 */
		if (uri.endsWith("/page")) {
			excutePageInfo(request);
		}
		// 未登录直接允许访问的
		if (beanType.getAnnotation(Open.class) != null || method.getAnnotation(Open.class) != null) {
			return true;
		}
		/* 如果未登录，则直接返回提示 */
		if (authorizationDto == null) {
			throw new FrameException("登录失效，请登录", HttpCode.NOT_LOGGED_IN);
		}
		// 登录之后，直接允许访问
		if (beanType.getAnnotation(Login.class) != null || method.getAnnotation(Login.class) != null) {
			return true;
		}
		/* 登录之后，校验校验具体权限 */
		if (!validAuth(uri + request.getMethod(), authorizationDto.getFunctions())) {
			throw new FrameException("权限不匹配", HttpCode.FORBBIDEN);
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		String uri = request.getRequestURI();
		if (uri.endsWith("/page")) {
			removePageInfo();
		}

	}

	public boolean validAuth(String flag, Set<String> functions) {
		if (functions == null) {
			throw new FrameException("权限不匹配", HttpCode.FORBBIDEN);
		}
		return functions.contains(flag);
	}

	/**
	 * 处理分页信息
	 */
	public void excutePageInfo(HttpServletRequest req) {
		Integer offset = 0;
		try {
			offset = Integer.parseInt(req.getParameter("offset"));
		} catch (NumberFormatException e) {
		}
		String pageSizeStr = req.getParameter("pageSize");
		Integer pageSizeParam = defaultPageSize;
		if (pageSizeStr != null) {
			try {
				pageSizeParam = Integer.parseInt(pageSizeStr);
			} catch (NumberFormatException e) {
			}
		}
		SystemContext.setOrder(req.getParameter("order"));
		SystemContext.setSort(req.getParameter("sort"));
		SystemContext.setPageOffset(offset);
		SystemContext.setPageSize(pageSizeParam);
	}

	/**
	 * 删除分页信息
	 */
	public void removePageInfo() {
		SystemContext.removeOrder();
		SystemContext.removeSort();
		SystemContext.removePageOffset();
		SystemContext.removePageSize();
	}

	public void setAuthorizationService(AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	public void setDefaultPageSize(int defaultPageSize) {
		this.defaultPageSize = defaultPageSize;
	}
}
