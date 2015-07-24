<%@ page language="java" pageEncoding="utf8"%> 
<%request.setCharacterEncoding("utf8");%> 
<!doctype html public "-//w3c//dtd xhtml 1.0 transitional//en" "http://www.w3.org/tr/xhtml1/dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf8">
<style>
	body{margin:0;padding:0}
	body{font:12px/14px \5b8b\4f53,arial,sans-serif; 
		color:#333;text-align:left}
	table{border-spacing:0}
	img{border:0;vertical-align:middle}
	a{color:#05a;text-decoration:none;outline:none}
	a:hover{color:#f60;text-decoration:underline}
	.wraper{ width:960px; padding-top:35px; margin:auto; background:#fff}
	.tbWrap .btm{padding:7px 9px; color:#777; 
		text-align:right; background:#f0fadf}
	.pBtn{ text-align:center}
	.tr1Detail{border:1px dotted #6CB132;zoom:1.0001;font-weight:400;padding-top:6px; padding-bottom:7px; background:#f0fadf; text-align:center; vertical-align:middle; color:#777; border-left:none;border-top:none}
	.tr2Detail{border-bottom:1px dotted #6CB132;padding-left:10px;}
</style>
<script src="${pageContext.request.contextPath}/dwr/engine.js"></script>
<script src="${pageContext.request.contextPath}/dwr/util.js"></script>
<script src="${pageContext.request.contextPath}/dwr/interface/searchService.js"></script>
<script language="javascript" charset="utf8">

var _id = '${param.id}';

searchService.getSearchResultById(_id, fillDetailResult);

function fillDetailResult(record)
{
	var name = document.getElementById('productName');
	name.innerHTML+="<tr><td align='left'><strong style='font-size:14px; line-height:16px;'>"
	+record.name+"</strong></td></tr>";
	var image = document.getElementById("image");
	image.innerHTML+="<img height='210' width='280'"+
		"src='showpic?id="+record.imageUrl+"' style='padding:5px 0'>";
	var contentTable = document.getElementById("detail");
	var tbody;
	var content = record.content;
	var lines = content.split("\r\n");
	if (lines.length > 0) {
		tbody = document.createElement('tbody');
		contentTable.appendChild(tbody);
	}
	else {
		return;
	}
	for(var i=0;i<lines.length-1;i++){//i<lines.length,会出现空行
		
		var tr = document.createElement('tr');	
		
		var td1;
		var td2;
		
		var line = lines[i];

		var data1 = line.substring(0, line.indexOf(":"));
		var data2 = line.substring(line.indexOf(":") + 1);

		td1 = document.createElement('td');
		td1.setAttribute("width", "100");
		td1.setAttribute("class", "tr1Detail");
		td1.innerHTML = data1;
		tr.appendChild(td1);
			
		td2 = document.createElement('td');
		td2.setAttribute("class", "tr2Detail");
		td2.innerHTML = data2;
		tr.appendChild(td2);
			
		tbody.appendChild(tr);
	}
	
	contentTable.innerHTML = contentTable.innerHTML;
	
	var original = document.getElementById("original");
	original.innerHTML = "数据来源："+"<a href='" + record.url + "' target='_blank'>" + record.url + "</a>";
}
</script>
</head>
<body>
<div id="JtbWrap">	
	<table id="Jtable" style="font-size:12px; color:#333; 
		table-layout:fixed; margin:auto" class="tbWrap" width="960" 
		border="0" cellspacing="0" cellpadding="0">
		<!-- 显示型号 -->
		<thead >
		<tr>
			<td style="border:1px solid #cae5b0; border-bottom:none; padding-left:10px; padding-top:6px; padding-bottom:7px; vertical-align:middle;background:#cae5b0;">
				<table id="productName" width="100%" border="0" cellspacing="0" cellpadding="0">
				</table>
			</td>
		</tr>
		</thead>
		<tbody>
		<!--  显示图片 -->
		<tr>
			<td id="image" align="center" style="border:1px solid #6CB132;">
			</td>	
		</tr>
		<!-- 详细参数 -->
		<tr>
			<td style="border:1px solid #6CB132; border-top:none;">
			<table class="param" width="100%" style="border-spacing:0" border="0" cellspacing="0" cellpadding="0">
			
				<tbody id="detail" >
				</tbody>
			</table>
		</td>
		</tr>
		<!-- 分割线 -->
		<tr>
			<td style="border:1px solid #6CB132; border-top:none;">
			<table class="param" width="100%" style="border-spacing:0" border="0" cellspacing="0" cellpadding="0">	
			</table>
			</td>
		</tr>
		<!-- 详细参数 -->
		<tr>
			<td id="original" style="border:1px solid #6CB132; border-top:none;padding:7px 9px; color:#777; text-align:right; background:#f0fadf" class="btm">
			</td>
		</tr>
		</tbody>
	</table>	
</div>
</body>
</html>
