<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试开发平台</title>

<script type="text/javascript">

	var req; //定义变量，用来创建xmlhttprequest对象
	function creatReq() {// 创建xmlhttprequest,ajax开始
		var url = "";  //要请求的服务端地址
		if (window.XMLHttpRequest) { //非IE浏览器及IE7(7.0及以上版本)，用xmlhttprequest对象创建
			req = new XMLHttpRequest();
		} else if (window.ActiveXObject) { //IE(6.0及以下版本)浏览器用activexobject对象创建,如果用户浏览器禁用了ActiveX,可能会失败.
			req = new ActiveXObject("Microsoft.XMLHttp");
		}
		if (req) {//成功创建xmlhttprequest
			req.open("GET", url, true); //与服务端建立连接(请求方式post或get，地址,true表示异步)
			req.onreadystatechange = callback; //指定回调函数
			req.send(null); //发送请求
		}
	}

	function callback() {//回调函数，对服务端的响应处理，监视response状态
		if (req.readystate == 4) {//请求状态为4表示成功
			if (req.status == 200) {//http状态200表示OK
				show(); //所有状态成功，执行此函数，显示数据
			} else {//http返回状态失败
				alert("服务端返回状态" + req.statusText);
			}
		} else {//请求状态还没有成功，页面等待
			document.getElementById("div_resp").innerHTML = "数据加载中";
		}
	}

	function show() { //接受服务端返回的数据，对其进行显示
		document.getElementById("div_resp").innerHTML = req.responseText;
	}
</script>

</head>
<body>
	<form action="ssl"enctype="multipart/form-data" method="post">
		<table>
			<tr>
				<td>URL地址：</td>
				<td width="100">
					<textarea rows="3" cols="50" name="url"></textarea>
				</td>
			</tr>
			<tr>
				<td>
					客户端密钥库：
				</td>
				<td>
					<input type="file" name="clientCerFile" />
				</td>
			</tr>
			<tr>
				<td>
					客户端密钥库密码：
				</td>
				<td>
					<input type="password" name="clientCerPasswd" />
				</td>
			</tr>
			<tr>
				<td>
					服务器证书：
				</td>
				<td>
					<input type="file" name="serverCerFile" />
				</td>
			</tr>
			<tr>
				<td>
					服务器证书密码：
				</td>
				<td>
					<input type="password" name="serverCerPasswd" />
				</td>
			</tr>
			<tr >
				<td colspan=2>
					<input type="submit" value="提       交" />
				</td>
			</tr>
		</table>
	</form>
	
	<hr/>
	
	<div id="myTime"></div>
</body>
</html>