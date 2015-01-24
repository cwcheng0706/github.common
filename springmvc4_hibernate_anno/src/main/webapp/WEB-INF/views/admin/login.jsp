<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="format-detection" content="telephone=no">
<meta name="renderer" content="webkit">
<meta http-equiv="Cache-Control" content="no-siteapp" />

<link rel="alternate icon" type="image/png" href="${ctx }/static/assets/i/favicon.png">
<link rel="stylesheet" href="${ctx }/static/assets/css/amazeui.min.css" />

<title>登录</title>

<style>
.header {
	text-align: center;
}

.header h1 {
	font-size: 200%;
	color: #333;
	margin-top: 30px;
}

.header p {
	font-size: 14px;
}
</style>

</head>
<body>
	<div class="header">
		<div class="am-g">
			<h1>DS@DM</h1>
			<p>
				Integrated Development Environment<br />
			</p>
		</div>
		<hr />
	</div>
	<div class="am-g">
		<div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
		
			<h3>登录</h3>
			<hr>
			<div class="am-btn-group">
				<a href="#" class="am-btn am-btn-secondary am-btn-sm"><i class="am-icon-github am-icon-sm"></i> Test</a> 
				<a href="#" class="am-btn am-btn-success am-btn-sm"><i class="am-icon-google-plus-square am-icon-sm"></i> Test2</a> 
				<a href="#" class="am-btn am-btn-primary am-btn-sm"><i class="am-icon-stack-overflow am-icon-sm"></i> Test3</a>
			</div>
			<br> <br>

			<form method="post" action="${ctx }/logon" class="am-form">
				<label for="email">邮箱:</label> <input type="email" name="name" id="email" value=""> <br> 
				<label for="password">密码:</label> <input type="password" name="password" id="password" value=""> <br> 
				<label for="remember-me"> <input id="remember-me" name="remember" type="checkbox" value="1"> 记住密码</label> <br />
				<div class="am-cf">
					<input type="submit" name="" value="登 录" class="am-btn am-btn-primary am-btn-sm am-fl"> <input type="submit" name="" value="忘记密码 ^_^? " class="am-btn am-btn-default am-btn-sm am-fr">
				</div>
			</form>
			<hr>
			<p>© 2015 AllMobilize, Inc. Licensed under MIT license.</p>
		</div>
	</div>

</body>
</html>