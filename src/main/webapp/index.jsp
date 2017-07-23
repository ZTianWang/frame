<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
请求的页面错误！
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试分页组件</title>
<script type="text/javascript">
 

</script>
</head>
<body>
 
	<table>
		<tr>
			<td>序号</td>
			<td>用户名</td>
			<td>创建时间</td>
		</tr>
		 
		<c:forEach items="${pager.data}" var="user" varStatus="status">
			<tr>
				<td>${status.index+pager.offset+1}</td>
				<td>${user.userName}||${user.oorder}</td>
				<td>${user.createDate}</td>
			</tr>
		</c:forEach>
	</table>
	<jsp:include page="pager.jsp">
		<jsp:param name="total" value="${pager.total}" />
		<jsp:param name="url" value="/user/list" />
	</jsp:include>
</body>
</html> 