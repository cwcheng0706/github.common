<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>模拟第三方平台</title>
<script type="text/javascript">
	function installCer(v) {
		//http://172.16.2.146:8080/cfcaTest/verify
		//http://172.16.2.146:8080/cfcaTest/seal
		var src
		if(1 == v) {
			src = 'http://172.16.2.52:8080/cfcaTest/verify?userId=admin1';
			
			//document.getElementById("myid1").style.display = "none";   //隐藏
			//document.getElementById("myid").style.display = "block";  //显示
		}else if(2 == v) {
			src = 'http://172.16.2.52:8080/cfcaTest/seal';
			
			//document.getElementById("myid").style.display = "none";   //隐藏
			//document.getElementById("myid1").style.display = "block";  //显示
		}
		var _innerHtml = '<iframe src=\"' + src + '\" width=\"100%\" height=\"100%\" frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" scrolling=\"auto\" allowtransparency=\"true\"></iframe>';
		document.getElementById("myid").innerHTML = _innerHtml;
	}
</script>

</head>
<body>

	<table width="100%" border="1">
		<tr>
			<td colspan="2" align="center"><h2>易木科技</h2></td>
		</tr>
		<tr >
			<td width="20%">
				<a href="javascript:installCer(2);">签章</a><br/>
			</td>
			<td>
				<div  id="myid" style="height: 700px ">
					<iframe src="http://172.16.2.52:8080/cfcaTest/verify?userId=8160" width="100%" height="100%" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="auto" allowtransparency="yes"></iframe>;
				</div>
<!-- 				<div  id="myid1" style="height: 700px ;display: none"> -->
<!-- 					<iframe src="http://172.16.2.146:8080/cfcaTest/seal" width="100%" height="100%" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="auto" allowtransparency="yes"></iframe>; -->
<!-- 				</div> -->
			</td>
		</tr>

	</table>

</body>
</html>