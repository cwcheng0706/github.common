<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jfreechart</title>
</head>
<body>

	<div style="text-align: center">
		jfreechart _3D柱状图 <br/>
		点击显示柱状图<a href="getColumnChart">getMajorChart</a><br/>
		<img src="${chartURL}" width=600 height=400 border=0 color=gray />
		
		<img width="600" height="400" src="data:image/jpg;base64,${imgBase64 }" />
	
	</div>
</body>
</html>