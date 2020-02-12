<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel=stylesheet href="css/table.css">
</head>
<body>
<center>
<h1>글쓰기</h1>
<form method=post action=BoardInsert>
<table id="table_content" width=500>
<tr>
<th width=15% align=right>이름</th>
<td width=85%>
<input type=text name=name size=15 required>
</td>
</tr>
<tr>
<th width=15% align=right>제목</th>
<td width=85%>
<input type=text name=subject size=50 required>
</td>
</tr>
<tr>
<th width=15% align=right valign=top>내용</th>
<td width=85%>
<textarea rows=8 cols=55 name=content required></textarea>
</td>
</tr>
<tr>
<th width=15% align=right>비밀번호</th>
<td width=85%>
<input type=password name=pwd size=10 required>
</td>
</tr>
<tr>
<td colspan=2 align=center>
<input type=submit value=글쓰기>
<input type=button value=취소 onclick="javascript:history.back()">
</td>
</tr>
</table>
</form>
</center>
</body>
</html>
