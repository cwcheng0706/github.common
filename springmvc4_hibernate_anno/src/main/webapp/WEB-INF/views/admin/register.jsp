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

<title>注册</title>

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
			<h1>注册会员</h1>
<!-- 			<p> -->
<!-- 				Integrated Development Environment<br /> -->
<!-- 			</p> -->
		</div>
		<hr />
	</div>

		<ol class="am-breadcrumb">
		<li><a href="#">首页</a></li>
	  	<li><a href="#">分类</a></li>
	  	<li class="am-active">内容</li>
		</ol>


<a data-am-collapse="{parent: '#accordion'}" href="#do-not-say-1">
				fdfffffffffffff
			</a>


	<div class="am-g">
	
		<div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
		
		<ol class="am-breadcrumb">
		  <li><a href="#" class="am-icon-home">首页</a></li>
		  <li><a href="#">分类</a></li>
		  <li class="am-active">内容</li>
		</ol>

			<%--  --%>
			<form class="am-form" action="${ctx }/registeron" method="post"  data-am-validator>
				<label for="name">用户名:</label> <input type="text" name="name" id="name" value="" required /> <br> 
				<label for="email">邮箱:</label> <input type="email" name="mail" id="email" value="" required /> <br> 
				<label for="password">输入密码:</label> <input type="password" name="password" id="password" value="" required /> <br> 
				<label for="passwordConfirm">确认密码:</label> <input type="password" name="passwordConfirm" id="passwordConfirm" value="" required /> <br> 
				<button type="submit" class="am-btn am-btn-primary am-btn-block">注册账号</button>
			</form>
			
			
			<hr />
			
			<%-- 
			
			<form class="am-form  am-form-horizontal" action="${ctx }/registeron" method="post"  data-am-validator>
				<div class="am-form-group">
					<label class="am-u-sm-2 am-form-label" for="name" >用户名:</label>
				    <div class="am-u-sm-10">
				      <input type="text" name="name" id="name" placeholder="输入你的用户名" required/>
				    </div>
			    </div>
			    <div class="am-form-group">
					<label class="am-u-sm-2 am-form-label" for="mail" >邮箱:</label>
				    <div class="am-u-sm-10">
				      <input type="email" id="mail" placeholder="输入你的电子邮件" required/>
				    </div>
				</div>
			    <div class="am-form-group">
					<label class="am-u-sm-2 am-form-label" for="password" >密码:</label>
				    <div class="am-u-sm-10">
				      <input type="password" id="password" required/>
				    </div>
			    </div>
				
				<div class="am-form-group">
					<div class="am-u-sm-10 am-u-sm-offset-2">
						<button type="submit" class="am-btn am-btn-primary am-btn-block">注册账号</button>
				    </div>
				</div>
			</form>
			
			--%>
			 
			 
		</div>
	</div>

































</body>
</html>