<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试CA证书</title>
</head>
<body>
	<H2>测试多级CA</H2>
	<H4>服务器检验结果【${clientVerify }】</H4>
	<H4>密码【${cipher }】</H4>
	<H4>客户端证书的序列号【${serial }】</H4>
	<H4>客户端证书的DN主题【${sDn }】</H4>
	<H4>客户端证书的DN发行者的主题【${iDn }】</H4>
	<H4>使用的协议【${protocol }】</H4>
	<H4>SSL会话ID【${sslSessionId }】</H4>
	<H4>SSL_CLIENT_RAW_CERT【${rawCert }】</H4>
	<H4>证书【${clientCert }】</H4>
	<hr/>
	
	
	<c:forEach items="${sessionMap }" var="entry">
	<H4>【${entry.value }】-【${entry.key }】</H4>
	</c:forEach>  
	
</body>
</html>