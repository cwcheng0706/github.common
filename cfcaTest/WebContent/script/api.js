var CryptoAgent = "";
function OnLoad() {
	if (navigator.appName.indexOf("Internet") >= 0 || navigator.appVersion.indexOf("Trident") >= 0) {
		
		if (window.navigator.cpuClass == "x86") {
			document.getElementById("FakeCryptoAgent").innerHTML = "<object id=\"CryptoAgent\" codebase=\"CryptoKit.CertEnrollment.Pro.x86.cab\" classid=\"clsid:71BB5253-EF2B-4C5B-85FF-1FD6A03C29A6\" ></object>";
		} else {
			document.getElementById("FakeCryptoAgent").innerHTML = "<object id=\"CryptoAgent\" codebase=\"CryptoKit.CertEnrollment.Pro.x64.cab\" classid=\"clsid:9E7B8F05-ADBE-4067-ABC6-28E0230A5C18\" ></object>";
		}
	} else {
		document.getElementById("FakeCryptoAgent").innerHTML = "<embed id=\"CryptoAgent\" type=\"application/npCryptoKit.CertEnrollment.Pro.x86\" style=\"height: 0px; width: 0px\">";
	}
	var KenAlgorithm = document.getElementById("KenAlgorithm")

	KenAlgorithm[0].selected = "selected";
	KenAlgorithm[1].selected = "";
	CryptoAgent = document.getElementById("CryptoAgent");

}

function GetSelectedItemIndex(itemName) {
	var ele = document.getElementsByName(itemName);
	for (i = 0; i < ele.length; i++) {
		if (ele[i].checked) {
			return i;
		}
	}
}

function FindProvider() {
	try {
		indexEnhanced = 0;
		var cryptprov = document.getElementById("TextCSPName");

		for (var i = 0; i < cryptprov.length; i++) {
			cryptprov.remove(i);
		}
		var cspInfo = CryptoAgent.CFCA_GetCSPInfo();
		if (!cspInfo) {
			var errorDesc = CryptoAgent.GetLastErrorDesc();
			alert(errorDesc);
			return;
		}
		var csps = cspInfo.split("||");

		if (cspInfo.indexOf('Microsoft Enhanced Cryptographic Provider v1.0') != -1) {
			var opt = document.createElement("OPTION");
			opt.value = "Microsoft Enhanced Cryptographic Provider v1.0";
			opt.text = "Microsoft Enhanced Cryptographic Provider v1.0";
			cryptprov.options.add(opt);
		}

		for (var i = 0; i < csps.length; i++) {
			if ((-1 == csps[i].indexOf("Microsoft"))) {
				var opt = document.createElement("OPTION");
				opt.value = csps[i];
				opt.text = csps[i];
				cryptprov.options.add(opt);
			}
		}

		cryptprov.selectedIndex = indexEnhanced;
	} catch (e) {
		if (CryptoAgent) {
			var lastErrorDesc = CryptoAgent.GetLastErrorDesc();
			if (lastErrorDesc) {
				if (!(-1 == lastErrorDesc.indexOf("0x00000000"))) {
					alert(e.number + e.message);
				} else {
					alert("=====" + lastErrorDesc);
				}
			}
		}

	}
}

function VerifyCertificateIntegrity() {
	try {
		var result = CryptoAgent.CFCA_CheckCertificateChain();
		if (!result) {
			var errorDesc = CryptoAgent.GetLastErrorDesc();
			alert("VerifyCertificateIntegrity errorDesc " + errorDesc);
			return;
		}
		//alert("证书链完整");
	} catch (e) {
		var LastErrorDesc = CryptoAgent.GetLastErrorDesc();
		alert(LastErrorDesc);
	}
}

function GetCSPInfo() {
	try {
		var cspNames = CryptoAgent.CFCA_GetCSPInfo();
		if (!cspNames) {
			var errorDesc = CryptoAgent.GetLastErrorDesc();
			alert("GetCSPInfo errorDesc " + errorDesc);
			return;
		}
		document.getElementById("textareaCSPInfo").value = cspNames;
	} catch (e) {
		var LastErrorDesc = CryptoAgent.GetLastErrorDesc();
		alert("LastErrorDesc " + LastErrorDesc);
	}
}

function GetAllCertInfo() {
	try {
		var certInfo = CryptoAgent.CFCA_GetAllCertInfo();
		if (!certInfo) {
			var errorDesc = CryptoAgent.GetLastErrorDesc();
			//alert("GetAllCertInfo errorDesc " +errorDesc);
			return;
		}
		document.getElementById("TextAllCertInfo").value = certInfo;
	} catch (e) {
		var LastErrorDesc = CryptoAgent.GetLastErrorDesc();
		alert(e + " ==: " + LastErrorDesc);
	}
}
function PKCS10Requisition_SingleCert() {
	try {
		var keyAlgorithm = document.getElementById("KenAlgorithm").value;
		var selectValue = document.getElementById("KenLength").value;
		var keyLength = 0;
		if (selectValue == "1024") {
			keyLength = 1024;
		} else if (selectValue == "2048") {
			keyLength = 2048;
		} else if (selectValue == "4096") {
			keyLength = 4096;
		} else {
			keyLength = 256;
		}
		//var cspName = document.getElementById("TextCSPName").value;
		var cspName = "Microsoft Enhanced Cryptographic Provider v1.0";
		if ("" == cspName) {
			alert("请选择“生成密钥的CSP名称”");
			FindProvider();
			return;
		}

		var strSubjectDN = document.getElementById("TextSubjectDN").value;
		var res1 = CryptoAgent.CFCA_SetCSPInfo(keyLength, cspName);
		if (!res1) {
			var errorDesc = CryptoAgent.GetLastErrorDesc();
			alert(errorDesc);
			return;
		}
		var res2 = CryptoAgent.CFCA_SetKeyAlgorithm(keyAlgorithm);
		if (!res2) {
			var errorDesc = CryptoAgent.GetLastErrorDesc();
			alert(errorDesc);
			return;
		}

		var pkcs10Requisition = 0;
		if (keyAlgorithm == "RSA") {
			var selectedIndex = GetSelectedItemIndex("radio_DigestAlgorithm");
			if (selectedIndex == 1) {
				// RSA单证 md5
				pkcs10Requisition = CryptoAgent.CFCA_PKCS10CertRequisitionwithMD5(strSubjectDN, 1, 0);
			} else {
				// RSA单证 sha1
				pkcs10Requisition = CryptoAgent.CFCA_PKCS10CertRequisition(strSubjectDN, 1, 0);
			}
		} else {
			// SM2单证
			pkcs10Requisition = CryptoAgent.CFCA_PKCS10CertRequisition(strSubjectDN, 1, 0);
		}

		if (!pkcs10Requisition) {
			var errorDesc = CryptoAgent.GetLastErrorDesc();
			alert(errorDesc);
			return;
		}
		document.getElementById("textareaP10RSASingleCert").value = pkcs10Requisition;

		var contianerName = CryptoAgent.CFCA_GetContainer();
		if (!contianerName) {
			var errorDesc = CryptoAgent.GetLastErrorDesc();
			alert(errorDesc);
			return;
		}
		document.getElementById("TextContianerName").value = contianerName;
		// ...need to sent pkcs10 requisition to CA
	} catch (e) {
		var LastErrorDesc = CryptoAgent.GetLastErrorDesc();
		alert("安装证书" + LastErrorDesc);
	}
}

//1.生成单证p10证书申请请求
function PKCS10Requisition_DoubleCert() {
	// P10 生成
	var pkcs10Requisition = 0;
	try {
		var keyAlgorithm = document.getElementById("KenAlgorithm").value;
		var selectValue = document.getElementById("KenLength").value;
		var keyLength = 0;
		if (selectValue == "1024") {
			keyLength = 1024;
		} else if (selectValue == "2048") {
			keyLength = 2048;
		} else if (selectValue == "4096") {
			keyLength = 4096;
		} else {
			keyLength = 256;
		}
		var cspName = document.getElementById("TextCSPName").value;
		if ("" == cspName) {
			alert("请选择“生成密钥的CSP名称”");
			FindProvider();
			return;
		}
		var strSubjectDN = document.getElementById("TextSubjectDN").value;
		var res1 = CryptoAgent.CFCA_SetCSPInfo(keyLength, cspName);
		if (!res1) {
			var errorDesc = CryptoAgent.GetLastErrorDesc();
			alert(errorDesc);
			return;
		}
		var res2 = CryptoAgent.CFCA_SetKeyAlgorithm(keyAlgorithm);
		if (!res2) {
			var errorDesc = CryptoAgent.GetLastErrorDesc();
			alert(errorDesc);
			return;
		}

		if (keyAlgorithm == "RSA") {
			var selectedIndex = GetSelectedItemIndex("radio_DigestAlgorithm");
			if (selectedIndex == 1) {
				// RSA双证 md5
				pkcs10Requisition = CryptoAgent.CFCA_PKCS10CertRequisitionwithMD5(strSubjectDN, 2, 0);
			} else {
				// RSA双证 sha1
				pkcs10Requisition = CryptoAgent.CFCA_PKCS10CertRequisition(strSubjectDN, 2, 0);
			}
		} else {
			// SM2双证
			pkcs10Requisition = CryptoAgent.CFCA_PKCS10CertRequisition(strSubjectDN, 2, 0);
		}
		if (!pkcs10Requisition) {
			var errorDesc = CryptoAgent.GetLastErrorDesc();
			alert(errorDesc);
			return;
		}

		document.getElementById("textareaP10RSASingleCert").value = pkcs10Requisition;
		var contianerName = CryptoAgent.CFCA_GetContainer();
		if (!contianerName) {
			var errorDesc = CryptoAgent.GetLastErrorDesc();
			alert(errorDesc);
			return;
		}
		document.getElementById("TextContianerName").value = contianerName;
		// ...need to sent pkcs10 requisition to CA
	} catch (e) {
		var LastErrorDesc = CryptoAgent.GetLastErrorDesc();
		alert(LastErrorDesc);
	}
	return pkcs10Requisition;
}

function KeyAlgorithm_onchange(selectedItemIndex) {
	try {
		var kenLength_select = document.getElementById("KenLength");
		var optionNum = document.getElementById("KenLength").length;
		// remove select's options
		for (var i = optionNum; i >= 0; i--) {
			kenLength_select.remove(i);
		}

		if (selectedItemIndex == 0) {
			// RSA
			// show digest algorithm
			document.getElementById("tr_DigestAlgorithm").style.display = "";

			// key length
			var optionTag = document.createElement('option');
			optionTag.text = "1024";
			optionTag.value = "1024";
			kenLength_select.options.add(optionTag);
			optionTag = document.createElement('option');
			optionTag.text = "2048";
			optionTag.value = "2048";
			kenLength_select.options.add(optionTag);
			optionTag = document.createElement('option');
			optionTag.text = "4096";
			optionTag.value = "4096";
			kenLength_select.options.add(optionTag);
			document.getElementById("TextCSPName").value = "Microsoft Enhanced Cryptographic Provider v1.0";
		} else {
			// SM2
			// hide digest algorithm
			document.getElementById("tr_DigestAlgorithm").style.display = "none";

			var optionTag = document.createElement('option');
			optionTag.text = "256";
			optionTag.value = "256";
			kenLength_select.options.add(optionTag);
			// document.getElementById("TextCSPName").value = "CIDC
			// Cryptographic Service Provider v3.0.0";
		}

	} catch (e) {
		alert(e.number + e.message);
	}
}

function GetTempPublickey() {
	try {
		// 申请RSA加密证书
		var keyAlgorithm = document.getElementById("KenAlgorithm").value;
		var selectValue = document.getElementById("KenLength").value;
		var keyLength = 0;
		if (selectValue == "1024") {
			keyLength = 1024;
		} else if (selectValue == "2048") {
			keyLength = 2048;
		} else if (selectValue == "4096") {
			keyLength = 4096;
		} else {
			keyLength = 256;
		}
		var cspName = document.getElementById("TextCSPName").value;
		if ("" == cspName) {
			alert("请选择“生成密钥的CSP名称”");
			FindProvider();
			return;
		}
		var strSubjectDN = document.getElementById("TextSubjectDN").value;
		var res1 = CryptoAgent.CFCA_SetCSPInfo(keyLength, cspName);
		if (!res1) {
			var errorDesc = CryptoAgent.GetLastErrorDesc();
			alert(errorDesc);
			return;
		}
		var res2 = CryptoAgent.CFCA_SetKeyAlgorithm(keyAlgorithm);
		if (!res2) {
			var errorDesc = CryptoAgent.GetLastErrorDesc();
			alert(errorDesc);
			return;
		}
		var tmpPublicKey = CryptoAgent.CFCA_GetTempPublickey();
		if (!tmpPublicKey) {
			var errorDesc = CryptoAgent.GetLastErrorDesc();
			alert(errorDesc);
			return;
		}
		document.getElementById("textareaP10RSASingleCert").value = tmpPublicKey;
		var contianerName = CryptoAgent.CFCA_GetContainer();
		if (!contianerName) {
			var errorDesc = CryptoAgent.GetLastErrorDesc();
			alert(errorDesc);
			return;
		}
		document.getElementById("TextContianerName").value = contianerName;
	} catch (e) {
		var LastErrorDesc = CryptoAgent.GetLastErrorDesc();
		alert(LastErrorDesc);
	}

}

function VerifyGenerateKeyPairPermission() {
	try {
		var result = CryptoAgent.CFCA_HavePermissiontoGenerateKeyPair();
		if (result) {
			//alert("您有权限生成密钥对");
		} else {
			alert("没有权限生成密钥对");
		}

	} catch (e) {
		var LastErrorDesc = CryptoAgent.GetLastErrorDesc();
		alert(LastErrorDesc);
	}
}

function InstallCert(certType) {
	try {
		var cspName = document.getElementById("TextCSPName").value;
		if (cspName == "") {
			alert("CSP名称为空");
			return;
		}
		
		var keyAlgorithm = document.getElementById("KenAlgorithm").value;
		if (keyAlgorithm == "") {
			alert("密钥生成算法为空");
			return;
		}
		
		var selectValue = document.getElementById("KenLength").value;
		var keyLength = 0;
		if (selectValue == "1024") {
			keyLength = 1024;
		} else if (selectValue == "2048") {
			keyLength = 2048;
		} else if (selectValue == "4096") {
			keyLength = 4096;
		} else if (selectValue == "256") {
			keyLength = 256;
		} else {
			alert("密钥长度错误：" + keyLength);
			return;
		}
		
		var hResult = CryptoAgent.CFCA_SetKeyAlgorithm(keyAlgorithm);
		if (!hResult) {
			var errorDesc = CryptoAgent.GetLastErrorDesc();
			alert("1" + errorDesc);
			return;
		}
		var bResult = CryptoAgent.CFCA_SetCSPInfo(keyLength, cspName);
		if (!bResult) {
			var errorDesc = CryptoAgent.GetLastErrorDesc();
			alert("1" + errorDesc);
			return;
		}
		
		var signCert = document.getElementById("textareaSignCert").value;
		if (signCert == "") {
			alert("签名公钥证书内容为空");
			return;
		}

		var containerName = document.getElementById("TextContianerName").value;
		if (containerName == "") {
			alert("密钥容器名称内容为空");
			return;
		}

		if (certType == 1) {
			// sign cert
			var bResult = CryptoAgent.CFCA_ImportSignCert(1, signCert, containerName);
			if (!bResult) {
				var errorDesc = CryptoAgent.GetLastErrorDesc();
				alert("证书安装：" + errorDesc);
				return;
			}
			alert("单证书安装成功");
			return;
		} else {
			// double cert
			var encryptCert_PublickeyCert = document.getElementById("textareaEncryptCert_PublickeyCert").value;
			if (encryptCert_PublickeyCert == "") {
				alert("加密证书公钥证书内容为空");
				return;
			}

			var encryptCert_PrivateData = document.getElementById("textareaEncryptCert_PrivatekeyCert").value;
			if (encryptCert_PrivateData == "") {
				alert("加密证书私钥数据为空");
				return;
			}

			var bResult = CryptoAgent.CFCA_ImportSignCert(2, signCert, containerName);
			if (!bResult) {
				var errorDesc = CryptoAgent.GetLastErrorDesc();
				alert(errorDesc);
				return;
			}
			bResult = CryptoAgent.CFCA_ImportEncryptCert(encryptCert_PrivateData, encryptCert_PublickeyCert, containerName, 0x1 | 0x2);
			if (!bResult) {
				var errorDesc = CryptoAgent.GetLastErrorDesc();
				alert(errorDesc);
				return;
			}
			alert("双证书安装成功");
		}
	} catch (e) {
		var LastErrorDesc = CryptoAgent.GetLastErrorDesc();
		alert(e + ";" + LastErrorDesc);
		if (certType == 1)
			alert("单证书安装失败");
		else
			alert("双证书安装失败");
	}
}



var xmlHttp = false;

//实例化XmlHttpRequest。
function createXmlHttpRequest() {
	// 非IE浏览器及IE7(7.0及以上版本),用xmlhttprequest对象创建。
	if (window.XMLHttpRequest) {
		xmlHttp = new XMLHttpRequest();
	}
	// IE(6.0及以下版本)浏览器用activexobject对象创建,如果用户浏览器禁用了ActiveX,可能会失败。
	else if (window.ActiveXObject) {
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
}

//创建传送到服务端的字符串。
function createQueryString() {
	var p10 = document.getElementById("textareaP10RSASingleCert").value;
	var queryString = "&p10csr=" +p10;
	return queryString;
}

var userId ;
//使用Post的方式向服务端发送数据。
function generateP10Cert() {
	
	VerifyGenerateKeyPairPermission();
	VerifyCertificateIntegrity();
	GetCSPInfo();
	GetAllCertInfo();
	
	PKCS10Requisition_SingleCert(); 
	
	
	createXmlHttpRequest();
	var queryString = createQueryString();
	var url = "generatep10?timeStamp=" + new Date().getTime() +"&userId=" +userId + queryString;
	// 设置回调函数
	xmlHttp.onreadystatechange = callback;
	xmlHttp.open("POST", url, true);
	xmlHttp.setRequestHeader("Content-Length", queryString.length);
	xmlHttp.setRequestHeader("CONTENT-TYPE", "application/x-www-form-urlencoded");
	xmlHttp.send(queryString);
}

function callback() {
	if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
		var b = xmlHttp.responseText;
		
		var arr = b.split(",")
		//alert(arr[0] + " = " + arr[1]);
		
		//返回p10证书
		if(arr[1]) {
			//document.getElementById("TextContianerName").value = arr[1];
		}
		document.getElementById("textareaSignCert").value = arr[0];
		
		
		
		//安装证书
		InstallCert(1);
	}
}

