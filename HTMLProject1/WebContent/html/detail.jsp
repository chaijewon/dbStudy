<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.sist.dao.*"%>
<%
     String mno=request.getParameter("mno");
     MusicDAO dao=new MusicDAO();
     MusicVO vo=dao.musicDetailData(Integer.parseInt(mno));
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<style type="text/css">
.row{
   margin: 0px auto;
   width:600px;
}
</style>
</head>
<body>
  <div class="container">
    <h1 class="text-center"><%=vo.getTitle() %> 상세보기</h1>
    <div class="row">
      <table class="table">
        <tr>
          <td colspan="2" class="text-center">
            <embed src="http://youtube.com/embed/<%=vo.getKey()%>" width=600 height="450">
          </td>
        </tr>
        <tr>
          <td width=30% class="text-center" rowspan="5">
            <img src="<%=vo.getPoster() %>">
          </td>
          <td width="70%"><%=vo.getTitle() %></td>
        </tr>
        <tr>
          <td width="70%"><%=vo.getSinger() %></td>
        </tr>
        <tr>
          <td width="70%"><%=vo.getAlbum() %></td>
        </tr>
        <tr>
          <td width="70%"><%=vo.getState() %></td>
        </tr>
        <tr>
          <td width="70%"><%=vo.getIdcrement() %></td>
        </tr>
      </table>
    </div>
  </div>
</body>
</html>














