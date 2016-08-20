<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

错误JSP页面提示：<br/>
    <p th:text="'Url:' + ${URL}" />
    <p th:text="'Error:' + ${error}" />
    <p th:text="'Status:' + ${status}" />
    <p th:text="'Timestamp:' + ${timestamp}" />

</body>
</html>