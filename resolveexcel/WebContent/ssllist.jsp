<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<form action="sslGen" method="post" >
	证书别名：<input name="alias" type="text" value="client"/><br/>
	目录：<input name="folder" type="text" value="/root/zy"/><br/>
	证书密码：<input name="storepass" type="text" value="client123456"/><br/>
	有效期： <input name="validity" type="text" value="1"/><br/>
	<input type="submit" value="生成证书并授权"/><br/>
</form>
</body>
</html>