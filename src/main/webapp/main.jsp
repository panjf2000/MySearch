<%@ page language="java" pageEncoding="utf8"%> 
<!doctype html public "-//w3c//dtd xhtml 1.0 transitional//en" "http://www.w3.org/tr/xhtml1/dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>数码搜索引擎</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf8">
<link rel="stylesheet" href="css/list.css">
<style>
html{font-family:sans-serif;-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}body{margin:0}article,aside,details,figcaption,figure,footer,header,hgroup,main,nav,section,summary{display:block}audio,canvas,progress,video{display:inline-block;vertical-align:baseline}audio:not([controls]){display:none;height:0}[hidden],template{display:none}a{background:transparent}a:active,a:hover{outline:0}abbr[title]{border-bottom:1px dotted}b,strong{font-weight:bold}dfn{font-style:italic}h1{font-size:2em;margin:0.67em 0}mark{background:#ff0;color:#000}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sup{top:-0.5em}sub{bottom:-0.25em}img{border:0}svg:not(:root){overflow:hidden}figure{margin:1em 40px}hr{-moz-box-sizing:content-box;box-sizing:content-box;height:0}pre{overflow:auto}code,kbd,pre,samp{font-family:monospace, monospace;font-size:1em}button,input,optgroup,select,textarea{color:inherit;font:inherit;margin:0}button{overflow:visible}button,select{text-transform:none}button,html input[type="button"],input[type="reset"],input[type="submit"]{-webkit-appearance:button;cursor:pointer}button[disabled],html input[disabled]{cursor:default}button::-moz-focus-inner,input::-moz-focus-inner{border:0;padding:0}input{line-height:normal}input[type="checkbox"],input[type="radio"]{-moz-box-sizing:border-box;box-sizing:border-box;padding:0}input[type="number"]::-webkit-inner-spin-button,input[type="number"]::-webkit-outer-spin-button{height:auto}input[type="search"]{-webkit-appearance:textfield;-moz-box-sizing:content-box;box-sizing:content-box}input[type="search"]::-webkit-search-cancel-button,input[type="search"]::-webkit-search-decoration{-webkit-appearance:none}fieldset{border:1px solid #c0c0c0;margin:0 2px;padding:0.35em 0.625em 0.75em}legend{border:0;padding:0}textarea{overflow:auto}optgroup{font-weight:bold}table{border-collapse:collapse;border-spacing:0}td,th{padding:0}
</style>
<style>
    h1 {
        text-align: center;
        text-transform: uppercase;
        font-size: 3em;
        letter-spacing: 0.1em;
        color: #EEE;
        animation: rotate 2s ease-in-out alternate infinite;
    }
    
    h1:before {
        content: attr(data-shadow);
        color: transparent;
        text-shadow: 0 0 15px #111;
        position: absolute;
        z-index: -1;
        margin: -0.1em 0 0 0;
        animation: skew 2s ease-in-out alternate infinite;
        transform-origin: bottom;
    }
    
    @keyframes rotate {
        from {
            transform: rotateY(-10deg);
            text-shadow: 1px -1px #CCC, 2px -1px #BBB, 3px -2px #AAA, 4px -2px #999, 5px -3px #888, 6px -3px #777;
        }
        to {
            transform: rotateY(10deg);
            text-shadow: -1px -1px #CCC, -2px -1px #BBB, -3px -2px #AAA, -4px -2px #999, -5px -3px #888, -6px -3px #777;
        }
    }
    
    @keyframes skew {
        from {
            transform: scaleY(0.3) skewX(-15deg);
        }
        to {
            transform: scaleY(0.3) skewX(-20deg);
        }
    }
</style>
<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/engine.js"></script>
<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/util.js"></script>
<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/interface/searchService.js"></script>
<script language="javascript" charset="utf8">

var request;
var minpage;
var maxpage;
var startindex;
var hasnext;

function doSearch(type){

	var result = document.getElementById("result");
	var pagingdiv = document.getElementById('paging');
	result.innerHTML="";
	pagingdiv.innerHTML = "";

	if (type != 'paging') {
		var startindexinput = document.getElementById('startindex');
		startindexinput.value = "1";
	}
	
	request = {startindex:1, query:""};
	if (typeof window['DWRUtil'] == 'undefined') 
	      window.DWRUtil = dwr.util;
	//DWRUtil.getValues,这里可以得到input名为startindex\query的值
	DWRUtil.getValues(request);

	searchService.getSearchResults(request, fillPage);
}

function fillPage(data){

	var list = data.results;
	var resultdiv = document.getElementById('result');
	var pagingdiv = document.getElementById('paging');

	resultdiv.innerHTML = "";
	pagingdiv.innerHTML = "";

	if (list.length == 0) {
		resultdiv.innerHTML = "<span>Sorry, we can't find what you want...</span>";
		return;
	}
	var ul = document.createElement('ul');
	ul.setAttribute('class',"product-list ");
	ul.setAttribute('id',"Jproduct-list");
	resultdiv.appendChild(ul);
	for(var i=0; i<list.length; i++) {	
		var li = document.createElement('li');
		li.setAttribute('id','info' + list[i]);
		li.innerHTML = "<span>loading..</span>";
		ul.appendChild(li);
		console.log("获取商品数据id："+list[i]);
		searchService.getSearchResultById(list[i], fillDetailResult);
	}
	
	minpage = data.minpage;
	maxpage = data.maxpage;
	startindex = data.startindex;
	hasnext = data.hasnext;
	
	if (minpage != 1) {
		var link = document.createElement('a');
		link.setAttribute("href","javascript:paging('" + ((minpage-11)*10+1) + "')");
		link.innerHTML = "前10页<<";
		pagingdiv.appendChild(link);
	}
	
	for (var j=minpage; j <=maxpage; j++)
	{
		if ((j-1)*10+1 != startindex)
		{
			var link = document.createElement('a');
			link.setAttribute("href","javascript:paging('" + ((j-1)*10+1) + "')");
			link.innerHTML = " " + j + " ";
			pagingdiv.appendChild(link);
		}
		else {
			pagingdiv.innerHTML += (" " + j + " ");
		}
	}
	
	if (hasnext == 1) {
		var link = document.createElement('a');
		link.setAttribute("href","javascript:paging('" + (maxpage*10+1) + "')");
		link.innerHTML = ">>后10页";
		pagingdiv.appendChild(link);
	}
	
}

function fillDetailResult(record) {
	console.log("渲染条目，id："+record.id);
	var li = document.getElementById('info' + record.id);

	var absContent = record.abstractContent;
	var lines = absContent.split("\r\n");
	var namePicDiv;
	var paramDiv;
	var paramDl;
	if (lines.length > 0) {
		console.log("lines.length > 0");
		li.innerHTML="";
		namePicDiv =  document.createElement('div');
		namePicDiv.setAttribute('class','hd');
		namePicDiv.innerHTML = "<p><a class='name' href='detail.jsp?id="
	        + record.id +"' target='_blank'>"+ record.name +"</a>"+
	        "</p><div class='product-pic'><a class='aPic' href='detail.jsp?id="
	        + record.id +"' target='_blank' title='"+ record.name 
	        +"'><img src='showpic?id=" + record.imageUrl + "' width='120' height='90'/>"
	        +"</a></div>";
	    li.appendChild(namePicDiv);
	    
	    paramDiv =  document.createElement('div');
	    paramDiv.setAttribute('class',"bd clearfix");
		li.appendChild(paramDiv);
		
		paramDl = document.createElement('dl');
		paramDl.setAttribute('class',"param clearfix");
		paramDiv.appendChild(paramDl);
	}
	else {
		console.log("lines.length <= 0");
		return;
	}	
	
	for(var i=0;i<lines.length;i++){
		var dd = document.createElement('dd');
	
		var line = lines[i];
		var data1 = line.substring(0, line.indexOf(":")+1);

		var data2 = line.substring(line.indexOf(":") + 1);
		if((i==lines.length-1)&&((data1.indexof(":")==-1)||data2.length<3))
			break;
		var iLable = document.createElement('i');
		iLable.setAttribute("class", "tit");
		iLable.innerHTML=data1;
		dd.appendChild(iLable);
		
		var span = document.createElement('span');
		span.innerHTML=data2;
		dd.appendChild(span);
		
		paramDl.appendChild(dd);
	}
	li.innerHTML = li.innerHTML;
}

function paging(newindex) {
	document.getElementById('startindex').value = newindex;
	doSearch('paging');
}

function handlekey(){
	if (document.getElementById('query').value == '')
		return;
	var intkey = -1;
	if(window.event) {
		intKey = event.keyCode;
		if(intKey == 13){
			doSearch('');
		}
	}
}

</script>
<script src="js/prefixfree.min.js"></script>
</head>
<body>
<script src='js/jquery.js'></script>
<input type="hidden" name="startindex" id="startindex" value="1">
<!-- 这是搜索栏 -->
<div id="searchbar">
	<table align="center">
		<tr align="center">
			<td>
				<h1 data-shadow="爱Phone搜索">爱Phone搜索</h1>
			</td>
		</tr>
		<tr align="center">
			<td><input size="50" type="text" name="query" id="query" value=""  onkeyup="handlekey()">
			<input type="button" value="Search"  id="search" onclick="javascript:doSearch('')"></td>
		</tr>
	</table>
</div>

<hr>

<!-- 这是结果栏 -->
<div id="result" class="content">
	<ul class="product-list" id="Jproduct-list">
	<%-- 
		<li class="">
                <div class="hd">
                    <p><a class="name" href="http://product.pconline.com.cn/mobile/huawei/573367.html" target="_blank">华为Mate7</a>
                    </p>
                    <div class="product-pic">
                        <a class="aPic" href="http://product.pconline.com.cn/mobile/huawei/573367.html" target="_blank" title="华为Mate7"><img src="http://img.pconline.com.cn/images/product/5733/573367/q0_sn.jpg" alt="华为Mate7" data-src="http://img.pconline.com.cn/images/product/5733/573367/q0_sn8.jpg" width="120" height="90">
                        </a>
                    </div>
                </div>
                <div class="bd clearfix">
                    <dl class="param clearfix">
                        <dd><i class="tit">2G/3G网络：</i>GSM,TD-SCDMA(移动3G),双卡双待,WCDMA仅支持国际漫游</dd>
                        <dd><i class="tit">4G网络：</i>移动4G,TD-LTE</dd>
                        <dd><i class="tit">主屏尺寸：</i>6英寸</dd>
                        <dd><i class="tit">屏幕分辨率：</i>1920×1080像素(FHD)</dd>
                        <dd><i class="tit">系统：</i>EMUI 3.0(基于Android 4.4.2)</dd>
                        <dd><i class="tit">电池容量：</i>不可更换,4100mAH</dd>
                        <dd><i class="tit">CPU：</i>海思Kirin 925 1.8GHz(8核)</dd>
                        <dd><a class="blue" href="http://product.pconline.com.cn/mobile/huawei/573367_detail.html" target="_blank" title="华为Mate7参数">更多参数&gt;&gt;</a>
                        </dd>
                    </dl>
                </div>
            </li>
	--%>
	
	</ul>
</div>
<!-- 
<hr>
 -->
<!-- 这是分页栏 -->
<div id="paging" style="float:right;font-size:25px;">

</div>

<!-- 这是Footer -->
<div id="footer">
	<table align="center">
		<tr align="center">
			<td>
				
			</td>
		</tr>
	</table>
</div>
</body>
</html>
