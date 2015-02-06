<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<style type="text/css">
.am-form-success .am-form-field, .am-field-valid {
	border-color: #5eb95e !important;
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
}

.am-breadcrumb>li [class*=am-icon-]:before {
	color: #999;
	margin-right: 5px;
}
</style>

<script type="text/javascript">
	
</script>

</head>
<body>
	<form action="">
		<form class="am-form">
			<div class="am-form-group am-form-success am-form-icon am-form-feedback">
				<input type="text" class="am-form-field" placeholder="验证成功"> <span class="am-icon-check"></span>
			</div>
			<div class="am-form-group am-form-warning am-form-icon am-form-feedback">
				<input type="text" class="am-form-field" placeholder="验证警告"> <span class="am-icon-warning"></span>
			</div>
			<div class="am-form-group am-form-error am-form-icon am-form-feedback">
				<input type="text" class="am-form-field" placeholder="验证失败"> <span class="am-icon-times"></span>
			</div>
		</form>
		<input type="text" name="" />
	</form>



	<ol class="am-breadcrumb">
		<li><a href="#" class="am-icon-home">首页</a></li>
		<li><a href="#">分类</a></li>
		<li class="am-active">内容</li>
	</ol>

</body>
</html>