
var Sys = {};

var ua = navigator.userAgent.toLowerCase();

var s;

(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :

(s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :

(s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :

(s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :

(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;

if (Sys.ie){
	var s = "";
	s += "<object id='HWPostil1' classid='clsid:FF1FE7A0-0578-4FEE-A34E-FB21B277D561' style='LEFT: 0px; WIDTH: 100%; TOP: 0px; HEIGHT: 100%' codebase='HWPostil.ocx#Version=3,1,1,6'>";
	s += "</OBJECT>";
    document.write(s)
} else if (Sys.firefox||Sys.chrome){
    var s = "";
    s += "<object id='HWPostil1' TYPE='application/x-itst-activex'  codebase='HWPostil.ocx#Version=3,1,1,6' clsid='{FF1FE7A0-0578-4FEE-A34E-FB21B277D561}' progid='' height=768 width='100%' style='LEFT: 0px; TOP: 0px' >";
    s +="<param name='_ExtentX' value='6350'><param name='_ExtentY' value='6350'>";
    s +="</OBJECT>"
    document.write(s);
} else {
	
	var s = "";
	s += "<object id='HWPostil1' classid='clsid:FF1FE7A0-0578-4FEE-A34E-FB21B277D561' style='LEFT: 0px; WIDTH: 100%; TOP: 0px; HEIGHT: 100%' codebase='HWPostil.ocx#Version=3,1,1,6'>";
	s += "</OBJECT>";
	document.write(s);
	

}
