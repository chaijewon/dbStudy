package com.sist.board;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sist.dao.*;
import java.util.*;
@WebServlet("/BoardListServlet")
public class BoardListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 *   request  : 사용자 요청정보 
		 *   response : 응답 (HTML=>사용자에게 전송)
		 *   
		 *    ==> html/xml
		 *    
		 *    http://localhost/BoardProject/BoardInsert
		 *    =============================
		 *      root => WebContent=>css
		 */
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		// 사용자 요청한 페이지를 받는다  =>  
		String strPage=request.getParameter("page");
		if(strPage==null)
		    strPage="1";
		int curpage=Integer.parseInt(strPage);
		
		BoardDAO dao=new BoardDAO();
		ArrayList<BoardVO> list=dao.boardListData(curpage);
		// 총페이지 
		int totalpage=dao.boardTotalPage();
		
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=stylesheet href=\"css/table.css\">");
		out.println("</head>");
		out.println("<body>");
		out.println("<center>");
		out.println("<h1>자유게시판</h1>");
		
		out.println("<table id=\"table_content\" width=700>");
		out.println("<tr>");
		out.println("<td align=left>");
		out.println("<a href=\"BoardInsert\">새글</a>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		
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
		
		out.println("<form method=post action=BoardFind>");//추가
		out.println("<table id=\"table_content\" width=700>");
		out.println("<tr>");
		out.println("<td align=left>");
		out.println("Search:");
		//// 변경
		out.println("<select name=fs>");
		out.println("<option value=name>이름</option>");
		out.println("<option value=subject>제목</option>");
		out.println("<option value=content>내용</option>");
		out.println("</select>");
		out.println("<input type=text size=15 name=ss>");
		out.println("<input type=submit value=찾기>");
		/// 변경
		out.println("</td>");
		out.println("<td align=right>");
		out.println("<a href=\"BoardListServlet?page="+(curpage>1?curpage-1:curpage)+"\">&lt;이전&gt;</a>");
		/*
		 *   특수문자 
		 *     &nbsp; " "
		 *     &lt; <
		 *     &gt; >
		 */
		out.println(curpage+" page / "+totalpage+" pages");
		out.println("<a href=\"BoardListServlet?page="+(curpage<totalpage?curpage+1:curpage)+"\">&lt;다음&gt;</a>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</form>");
		out.println("</center>");
		out.println("</body>");
		out.println("</html>");
	}

}











