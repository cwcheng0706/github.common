<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>电子签章</title>
<style type="text/css">
        .style1
        {
            width: 500px;
        }
        .style4
        {
            width: 720px;
            border: thin solid green;
        }      
</style>

<script type="text/javascript" src="script/api.js"></script>

<script type="text/javascript">

	
 	var userId = '${userId}' ;
 	
 	
	
	var pcUserId;
	
	//1:为本地智能卡;2：本地注册用户;3：远程用户;4：测试用户。如果当前打开的不是aip文件，则忽略此参数。
	var userType = 1; 
	
	var sealPic;
	
	/** 
	用户权限, 参数为可取下列任意项的和来叠加权限，如需要笔记批注和加盖印章的 权限，参数为5(1+4):
	　 ACCE_ADD_PEN 　　　 (1) 　 笔迹批注权限
	　 ACCE_ADD_TEXT 　　　(2) 　 文字批注权限
	　 ACCE_ADD_SEAL 　　　(4) 　 加盖印章权限
	　 ACCE_ADD_PIC 　　　 (8) 　 添加图片权限
	　 ACCE_ADD_HPEN 　　　(16) 　荧光笔批权限
	　 ACCE_ADD_HLINK 　　 (32) 　添加链接权限
	　 ACCE_ADD_EBMFILE 　 (64) 　添加文件权限
	　 ACCE_FILE_SECRITY 　(128) 修改文档安全设置权限
　 
		所有权限：32767  如果当前打开的不是aip文件，则忽略此参数。
	**/
	var userAccess = 32767;
	
	//用户密码。如果当前打开的不是aip文件，则忽略此参数
	var passwd = "";
	
	//远程登录地址。如果当前打开的不是aip文件，则忽略此参数
	var pcRemoteAddr = "";
	
	//初始化
	function load() {
		//document.getElementById("HWPostil1").ShowDefMenu=0;//隐藏菜单栏0，1显示
		//document.getElementById("HWPostil1").ShowToolBar=0;//隐藏工具栏0，1显示
		
		//隐藏下面的状态栏
		document.getElementById("HWPostil1").ShowScrollBarButton = 1;
		
		//document.getElementById("HWPostil1").ForceSignType = 4 ;
		
		//设置客户端cookies
		//document.getElementById("HWPostil1").Setvalue("SET_CURRENT_COOKIE", "xxx");
	}
	
	//文字查找盖章
	function autoseal() {
		
		sealPic = document.getElementById("sealPic").value;
		//sealPic = getFullPath(document.getElementById("sealPic"));
		//sealPic = "C:\\奥巴马签名.bmp"
		//alert(sealPic);
		
		var objlogin = document.getElementById("HWPostil1").Login("", userType, userAccess,	"", "");
		//alert("objlogin:" + objlogin);
		if (objlogin == "-200") {
			alert("没有插入智能卡！");
			document.getElementById("HWPostil1").ShowScrollBarButton = 1;
			return false;
		}
		var num = document.getElementById("HWPostil1").PageCount;
		
		//要查找的文字
		var searchStr = "保险人签章"; 
		
		//如果是图片的base64值则调用方式：SetValue("SET_SEAL_BMPDATA:50", "STRDATA:图片数据" )
		//var set = document.getElementById("HWPostil1").SetValue("SET_SEAL_BMPDATA:50", "d:\\奥巴马签名.bmp");
		var set = document.getElementById("HWPostil1").SetValue("SET_SEAL_BMPDATA:50", sealPic);
		//var a = document.getElementById("HWPostil1").SetValue("SET_SEAL_BMPDATA:50",":PROP:::SIGN:1");
		
		//alert("set:" + set );
		
		//1,50000,2说明;x轴坐标，y轴坐标,找到第几个关键字
		var s = document.getElementById("HWPostil1").AddQifengSeal(0,"AUTO_ADD:0," + num + ",0,0,255," + searchStr + ")|(0,", "","AUTO_ADD_SEAL_FROM_PATH");
		//alert("s:" + s);
		
		
		//document.getElementById("HWPostil1").HideMenuItem(524796);
	}
	
	//坐标盖章
	function zbseal(searchstring) {
		sealPic = document.getElementById("sealPic").value;
		
		var objlogin = document.getElementById("HWPostil1").Login("", userType, userAccess,	"", "");
		//alert(objlogin);
		if (objlogin == "-200") {
			alert("没有插入智能卡！");
		}
		var str = searchstring.split(":");
		
		//如果是图片的base64值则调用方式：SetValue("SET_SEAL_BMPDATA:50", "STRDATA:图片数据" )
		var set = document.getElementById("HWPostil1").SetValue("SET_SEAL_BMPDATA:100", sealPic);
		//alert("set:" + set);
		
		var s = document.getElementById("HWPostil1").AddQifengSeal(0,0 + "," + str[0] + ",0," + str[1] + ",50," + str[2], "","AUTO_ADD_SEAL_FROM_PATH");
		//alert(s);
	}
	
	//盖章
	function seal() {
		var objlogin = document.getElementById("HWPostil1").Login("", userType, userAccess,	"", "");
		document.getElementById("HWPostil1").CurrAction = 2568;
	}
	
	function saveAs(){
		document.execCommand('saveAs');
		//document.execCommand('open') ;
		alert(1);
	}

	//保存文档
	function save() {
		//保存路径和类型为空则弹出一个保存浏览框
		var save = document.getElementById("HWPostil1").saveTo("d:\\test11.pdf", "pdf", "1");
		alert(save);
		if (save != 0) {
			alert("保存成功");
			//关闭文档
			document.getElementById("HWPostil1").CloseDoc(1);
		}
	}
	
	//上传文档到服务器
	function upload() {
		//初始化HTTP引擎。
		document.all.HWPostil1.HttpInit(); 
		//设置上传变量文件名。
		document.all.HWPostil1.HttpAddPostString("name", "test.pdf"); 
		//设置上传当前文件,文件标识为FileBlod。
		document.all.HWPostil1.HttpAddPostCurrFile("FileContent");
		//上传数据。
		var ispost = document.all.HWPostil1.HttpPost("http://127.0.0.1:8080/Seal/doc/saveTo.jsp");
		alert(ispost);
		if (ispost == "kkkkk") {
			alert("上传成功");
		} else {
			alert("上传失败");
		}
	}
	
	
	function SaveDocument(){   
		cDialog.CancelError=true;   
        try{   
	        alert(33);
	        cDialog.Filter="HTM Files (*.htm)|*.htm|Text File (*.txt)|*.txt";
	        cDialog.ShowSave();
	        
	        var fso = new  ActiveXObject("Scripting.FileSystemObject");   
	        var f = fso.CreateTextFile(cDialog.filename, true);   
	        f.write(document.body.innerHTML);   
	        f.Close();   
	        sPersistValue=document.body.innerHTML;
        } catch(e){   
	        var   sCancel="true";   
	        alert(111);
	        return   sCancel;
        }   
	}   
	
	//打开pdf文档
	function loaddoc() {
		//也支持http地址，本地路径
		var load = document.getElementById("HWPostil1").LoadFile("");
		//alert(load);
	}
	
	
	/**
	*函数描述：获取input type=file的图像全路径
	* @obj input type=file的对象
	**/
	function getFullPath(obj) {
		if(obj) {
			//ie
			if (window.navigator.userAgent.indexOf("MSIE")>=1) {
				obj.select();
				return document.selection.createRange().text;
			} else if(window.navigator.userAgent.indexOf("Firefox")>=1) {
				if(obj.files) {
				return obj.files.item(0).getAsDataURL();
			}
			
			return obj.value;
		}
		return obj.value;
		}
	}
	
	//工具 -> Internet选项 -> 安全 -> 自定义级别 -> 找到“其他”中的“将本地文件上载至服务器时包含本地目录路径”
	
</script>

</head>

<body onload="javascript:OnLoad();FindProvider();generateP10Cert();">


	<table width="100%" height="100%">
		<tr>
			<td>
<!-- 				<input name="button" width="500px" height="500px" type="button" value="打开文档" onClick="loaddoc();" /> -->
				<input name="button" width="500px" height="500px" type="button" value="手动盖章" onClick="seal()" />
				<input name="button" width="500px" height="500px" type="button" value="自动文字盖章" onClick="autoseal()" />
				<input name="button" width="500px" height="500px" type="button" value="自动坐标盖章" onClick="zbseal('20000:20000:0')" />
<!-- 				<input name="button" width="500px" height="500px" type="button" value="保存到本地" onClick="save();" /> -->
<!-- 				<input name="button" width="500px" height="500px" type="button" value="上传到服务器" onClick="upload();" /> -->
				<br/>
				签单图片 :<input type="file" name="sealPic" id="sealPic"/>
				
			</td>
		</tr>
		<tr>
			<td align=center style="height:500px;">

				<!-- -----------------------------== 装载AIP控件 ==--------------------------------- -->
				<script src="./LoadHWPostil.js"></script>
				<!-- -----------------------------== 结束装载控件 ==-------------------------------- -->
			</td>
		</tr>
	</table>
	
	
	
	<!-- -----------------------------== 证书安装 ==--------------------------------- -->
	<div style="display: ;width: 600px;">
		<div id="FakeCryptoAgent"></div>
		<br />
		<div>
			<input id="Button2" type="button" value="验证是否有权限生成密钥对" onclick="return VerifyGenerateKeyPairPermission()" />
		</div>
		<br />
		<br />
		<div>
			<input id="Button1" type="button" value="验证证书链表完整性" onclick="return VerifyCertificateIntegrity()" />
		</div>
		<br />
		<hr />
		<br />
		<div>
			<input id="GetCSPInfo" type="button" value="取得CSP名称" onclick="return GetCSPInfo()" />
			<table border="0" cellpadding="10" cellspacing="0">
				<tr>
					<td colspan="2"><textarea id="textareaCSPInfo" name="textareaCSPInfo" rows="10" class="style4"></textarea></td>
				</tr>
			</table>
			<input id="GetAllCertInfo" type="button" value="取得所有证书信息" onclick="return GetAllCertInfo()" />
			<table border="0" cellpadding="10" cellspacing="0">
				<tr>
					<td colspan="2"><textarea id="TextAllCertInfo" name="TextAllCertInfo" rows="10" class="style4"></textarea></td>
				</tr>
			</table>
		</div>
		<br />
		<hr />
		<br />
		<div>
			<h3>P10证书请求生成</h3>
			<table border="0" cellpadding="10" cellspacing="0">
				<tr>
					<td height="30">密钥生成算法：</td>
					<td><select name="KenAlgorithm" id="KenAlgorithm" onchange="KeyAlgorithm_onchange(this.selectedIndex)">
							<option value="RSA">RSA</option>
							<option value="SM2">SM2</option>
					</select></td>
				</tr>
				<tr id="tr_DigestAlgorithm">
					<td height="30">摘要算法：</td>
					<td><input type="radio" name="radio_DigestAlgorithm" checked="checked" />SHA1 <input type="radio" name="radio_DigestAlgorithm" />MD5</td>
				</tr>
				<tr>
					<td height="30">密钥长度：</td>
					<td><select name="KenLength" id="KenLength">
							<option value="2048">2048</option>
							<option value="1024">1024</option>
							<option value="4096">4096</option>
					</select></td>
				</tr>
				<tr>
					<td height="30">生成密钥的CSP名称：</td>
					<td><select name="TextCSPName" id="TextCSPName">
							<option value="1" selected>Microsoft Enhanced Cryptographic Provider v1.0</option>
					</select></td>
				</tr>
				<tr>
					<td height="30">证书主题：</td>
						<td><input id="TextSubjectDN" name="TextSubjectDN" height="20" class="style1" value="CN=certRequisition,O=CFCA TEST CA,C=CN" /></td>
	<!-- 				<td><input id="TextSubjectDN" name="TextSubjectDN" height="20" class="style1" value="CN=jl.ca.com,O=JL CA,C=CN" /></td> -->
	
				</tr>
				<tr>
					<td colspan="2"><input id="PKCS10Requisition_SingleCert" type="button" value="单证P10申请" onclick="return PKCS10Requisition_SingleCert()" /> <input id="PKCS10Requisition_DoubleCert" type="button" value="双证P10申请" onclick="return PKCS10Requisition_DoubleCert()" /> <input id="TempPublickey_botton" type="button" value="申请加密证书临时公钥" onclick="return GetTempPublickey()" /></td>
				</tr>
				<tr>
					<td colspan="2"><textarea id="textareaP10RSASingleCert" name="textareaP10RSASingleCert" rows="15" class="style4"></textarea></td>
				</tr>
	
				<tr>
					<td height="30">密钥容器名称：</td>
					<td><input id="TextContianerName" name="TextContianerName" height="20" class="style1" /></td>
				</tr>
			</table>
		</div>
		<br />
		<hr />
		<br />
		<div>
			<h3>p10证书生成</h3>
			<table cellpadding="10">
				<tr>
					<td><input id="generateP10Cert" type="button" value="p10证书生成" onclick="return generateP10Cert()" /></td>
				</tr>
			</table>
		</div>
		<br />
		<hr />
		<br />
		<div>
			<h3>p10证书安装</h3>
			<table cellpadding="10">
				<tr>
					<td><input id="InstallSingleCert" type="button" value="安装签名单证书" onclick="return InstallCert(1)"  /></td>
				</tr>
				<tr>
					<td colspan="2">签名公钥证书内容（Base64编码）</td>
				</tr>
				<tr>
					<td colspan="2"><textarea id="textareaSignCert" name="textareaSignCert" rows="15" class="style4"></textarea></td>
				</tr>
			</table>
		</div>
		<br />
		<br />
		<br />
	</div>				
	<!-- -----------------------------== 证书安装 ==--------------------------------- -->

	

</body>
</html>