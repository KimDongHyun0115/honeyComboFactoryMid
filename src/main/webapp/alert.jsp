<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ALERT창</title>
</head>
<body>
<script type="text/javascript">
console.log("로그 [${url}]");
alert('${msg}');
if(${flag}){
	var url = "${url}";
	location.href = url;
}
else{
	history.go(-1);
}
</script>
</body>
</html>