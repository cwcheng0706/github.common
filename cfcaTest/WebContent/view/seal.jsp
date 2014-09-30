<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>seal</title>
<script type="text/javascript">
	//初始化
	function load() {
		//document.getElementById("HWPostil1").ShowDefMenu=0;//隐藏菜单栏0，1显示
		//document.getElementById("HWPostil1").ShowToolBar=0;//隐藏工具栏0，1显示
		document.getElementById("HWPostil1").ShowScrollBarButton = 1;//隐藏下面的状态栏
		document.getElementById("HWPostil1").Setvalue("SET_CURRENT_COOKIE", "xxx");//设置客户端cookies
	}
	//文字查找盖章
	function autoseal() {
		var objlogin = document.getElementById("HWPostil1").Login("", 1, 32767, "", "");
		//alert(objlogin);
		if (objlogin == "-200") {
			alert("没有插入智能卡！");
		}
		var num = document.getElementById("HWPostil1").PageCount;
		var searchStr = "保险人签章"; //要查找的文字
		var set = document.getElementById("HWPostil1").SetValue("SET_SEAL_BMPDATA:50", "d:\\奥巴马签名.bmp");//如果是图片的base64值则调用方式：SetValue("SET_SEAL_BMPDATA:50", "STRDATA:图片数据" )
		//alert("set:" + set);
		var s = document.getElementById("HWPostil1").AddQifengSeal(0, "AUTO_ADD:0," + num + ",0,0,255," + searchStr + ")|(0,", "", "AUTO_ADD_SEAL_FROM_PATH");//1,50000,2说明;x轴坐标，y轴坐标,找到第几个关键字
		//alert(s);
	}
	//坐标盖章
	function zbseal(searchstring) {
		var objlogin = document.getElementById("HWPostil1").Login("", 1, 32767, "", "");
		//alert(objlogin);
		if (objlogin == "-200") {
			alert("没有插入智能卡！");
		}
		var str = searchstring.split(":");
		var set = document.getElementById("HWPostil1").SetValue("SET_SEAL_BMPDATA:100", "d:\\奥巴马签名.bmp");//如果是图片的base64值则调用方式：SetValue("SET_SEAL_BMPDATA:50", "STRDATA:图片数据" )
		//alert("set:" + set);
		var s = document.getElementById("HWPostil1").AddQifengSeal(0, 0 + "," + str[0] + ",0," + str[1] + ",50," + str[2], "", "AUTO_ADD_SEAL_FROM_PATH");
		//alert(s);
	}
	//盖章
	function seal() {
		var objlogin = document.getElementById("HWPostil1").Login("", 1, 32767, "", "");
		document.getElementById("HWPostil1").CurrAction = 2568;
	}

	//保存文档
	function save() {
		var save = document.getElementById("HWPostil1").saveTo("d:\\test11.pdf", "pdf", "1");//保存路径和类型为空则弹出一个保存浏览框
		alert(save);
		if (save != 0) {
			alert("保存成功");
			document.getElementById("HWPostil1").CloseDoc(1);//关闭文档
		}
	}
	//上传文档到服务器
	function upload() {
		document.all.HWPostil1.HttpInit(); //初始化HTTP引擎。
		document.all.HWPostil1.HttpAddPostString("name", "test.pdf"); //设置上传变量文件名。
		document.all.HWPostil1.HttpAddPostCurrFile("FileContent");//设置上传当前文件,文件标识为FileBlod。
		var ispost = document.all.HWPostil1.HttpPost("http://127.0.0.1:8080/Seal/doc/saveTo.jsp");//上传数据。
		alert(ispost);
		if (ispost == "kkkkk") {
			alert("上传成功");
		} else {
			alert("上传失败");
		}
	}
	//打开pdf文档
	function loaddoc() {
		var load = document.getElementById("HWPostil1").LoadFile("");//也支持http地址，本地路径
		//   alert(load);
	}
</script>
</head>
<body onLoad="load();">
	<table width="100%" height="100%">
		<tr>
			<td><input name="button" width="500px" height="500px" type="button" value="打开文档" onClick="loaddoc();" /> <input name="button" width="500px" height="500px" type="button" value="手动盖章" onClick="seal()" /> <input name="button" width="500px" height="500px" type="button" value="自动文字盖章" onClick="autoseal()" /> <input name="button" width="500px" height="500px" type="button" value="自动坐标盖章"
				onClick="zbseal('20000:20000:0')" /> <input name="button" width="500px" height="500px" type="button" value="保存到本地" onClick="save();" /> <input name="button" width="500px" height="500px" type="button" value="上传到服务器" onClick="upload();" /></td>
		</tr>
		<tr>
			<TD align=center style="height: 500px;">
				<!-- -----------------------------== 装载AIP控件 ==--------------------------------- --> 
				<script src="../LoadHWPostil.js"></script> 
				<!-- -----------------------------== 结束装载控件 ==-------------------------------- -->
			</TD>
		</TR>
	</table>
</body>
</html>