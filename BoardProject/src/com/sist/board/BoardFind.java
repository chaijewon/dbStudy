package com.sist.board;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import com.sist.dao.*;
@WebServlet("/BoardFind")
public class BoardFind extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		try
		{
			request.setCharacterEncoding("UTF-8");
		}catch(Exception ex) {}
		
		String fs=request.getParameter("fs");
		String ss=request.getParameter("ss");
		// 사용자 요청한 페이지를 받는다  =>  
		BoardDAO dao=new BoardDAO();
		ArrayList<BoardVO> list=dao.boardFindData(fs, ss);
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=stylesheet href=\"css/table.css\">");
		out.println("</head>");
		out.println("<body>");
		out.println("<center>");
		out.println("<h1>검색결과</h1>");
		out.println("<table id=\"table_content\" width=700>");
		out.println("<tr>");
		out.println("<th width=10%>번호</th>");
		out.println("<th width=45%>제목</th>");
		out.println("<th width=15%>이름</th>");
		out.println("<th width=20%>작성일</th>");
		out.println("<th width=10%>조회수</th>");
		out.println("</tr>");
		for(BoardVO vo:list)
		{
			out.println("<tr class=dataTr>");
			out.println("<td width=10% align=center>"+vo.getNo()+"</td>");
			out.println("<td width=45% align=left>");
			out.println("<a href=BoardDetailServlet?no="+vo.getNo()+">");
			out.println(vo.getSubject()+"</a>");
			out.println("</td>");
			out.println("<td width=15% align=center>"+vo.getName()+"</td>");
			out.println("<td width=20% align=center>"+vo.getRegdate()+"</td>");
			out.println("<td width=10% align=center>"+vo.getHit()+"</td>");
			out.println("</tr>");
		}
		out.println("</table>");
		
		out.println("<table id=\"table_content\" width=700>");
		out.println("<tr>");
		out.println("<td align=right>");
		out.println("<a href=BoardListServlet>목록</a>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		
		out.println("</center>");
		out.println("</body>");
		out.println("</html>");
	}

}
