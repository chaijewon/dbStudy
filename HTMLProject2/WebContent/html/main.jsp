<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
  <table border=1 width=900 height=700>
    <tr>
      <td colspan="2" height=100>
        <%
            pageContext.include("header.jsp");
        %>
      </td>
    </tr>
    <tr>
      <td width=200 height=500></td>
      <td width=700 height=500></td>
    </tr>
    <tr>
      <td colspan="2" height=100></td>
    </tr>
  </table>
</body>
</html>