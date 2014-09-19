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

<form action="uploadExcelServlet" enctype="multipart/form-data" method="post" >
	<input name="file" type="file"/><br/>
	<input type="submit" value="提 交"/><br/>
</form>

<table width="600" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td>上传临时目录名</td>
		<td>原文件名</td>
		<td>操作</td>
	</tr>
	<c:forEach var="file" items="${requestScope.files }">
	<tr align="center">
		<td>${file.tempFolderName }</td>
		<td>${file.fileName }</td>
		<td>
			<a href="resolveExcelServlet?fileName=${file.fileName }&tempFolderName=${file.tempFolderName }">解析</a>
			<c:if test="${file.download }">
				<a href="downloadExcelServlet?tempFolderName=${file.tempFolderName }" >下载</a>
			</c:if>
		</td>
	</tr>
	</c:forEach>
</table>

</body>
</html>