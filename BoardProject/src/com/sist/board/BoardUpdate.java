package com.sist.board;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sist.dao.*;
@WebServlet("/BoardUpdate")
public class BoardUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		    response.setContentType("text/html;charset=UTF-8");
		    PrintWriter out=response.getWriter();
		 
		    // 사용자 요청한 번호를 받는다 
		    /*
		     *  http://localhost/BoardProject/BoardUpdate?no=13
		     */
		    String no=request.getParameter("no");
		    BoardDAO dao=new BoardDAO();
		    BoardVO vo=dao.boardUpdateData(Integer.parseInt(no));
		    
		    out.println("<!DOCTYPE html>");
		    out.println("<html>");
			out.println("<head>");
			out.println("<link rel=stylesheet href=\"css/table.css\">");
			out.println("</head>");
			out.println("<body>");
			out.println("<center>");
			out.println("<h1>수정하기</h1>");
			
			out.println("<form method=post action=BoardUpdate>");
			out.println("<table id=\"table_content\" width=500>");
			
			out.println("<tr>");
			out.println("<th width=15% align=right>이름</th>");
			out.println("<td width=85%>");
			out.println("<input type=text name=name size=15 required value="+vo.getName()+">");
			out.println("<input type=hidden name=no value="+vo.getNo()+">");
			out.println("</td>");
			out.println("</tr>");
			
			out.println("<tr>");
			out.println("<th width=15% align=right>제목</th>");
			out.println("<td width=85%>");
			out.println("<input type=text name=subject size=50 required value=\""+vo.getSubject()+"\">");
			out.println("</td>");
			out.println("</tr>");
			
			out.println("<tr>");
			out.println("<th width=15% align=right valign=top>내용</th>");
			out.println("<td width=85%>");
			out.println("<textarea rows=8 cols=55 name=content required>"+vo.getContent()+"</textarea>");
			out.println("</td>");
			out.println("</tr>");
			
			out.println("<tr>");
			out.println("<th width=15% align=right>비밀번호</th>");
			out.println("<td width=85%>");
			out.println("<input type=password name=pwd size=10 required>");
			out.println("</td>");
			out.println("</tr>");
			
			out.println("<tr>");
			out.println("<td colspan=2 align=center>");
			out.println("<input type=submit value=수정하기>");
			out.println("<input type=button value=취소 onclick=\"javascript:history.back()\">");
			out.println("</td>");
			out.println("</tr>");
			
			out.println("</table>");
			out.println("</form>");
			
			out.println("</center>");
			out.println("</body>");
			out.println("</html>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 response.setContentType("text/html;charset=UTF-8");
		 PrintWriter out=response.getWriter();
		 
		 // 사용자가 보내준 값을 저장 
		 try
		 {
			 request.setCharacterEncoding("UTF-8");
		 }catch(Exception ex) {}
		 // 저장된 값=> DAO
		 String no=request.getParameter("no");
		 String name=request.getParameter("name");
		 String subject=request.getParameter("subject");
		 String content=request.getParameter("content");
		 String pwd=request.getParameter("pwd");
		 
		 BoardVO vo=new BoardVO();
		 vo.setNo(Integer.parseInt(no));
		 vo.setName(name);
		 vo.setSubject(subject);
		 vo.setContent(content);
		 vo.setPwd(pwd);
		 // 처리 ==> DAO
		 BoardDAO dao=new BoardDAO();
		 boolean bCheck=dao.boardUpdate(vo);
		 // 이동 ==> 상세보기 이동 
		 if(bCheck==true)
		 {
		    response.sendRedirect("BoardDetailServlet?no="+no);
		 }
		 else
		 {
			 out.println("<html>");
			 out.println("<head>");
			 out.println("<script type=\"text/javascript\">");
			 out.println("alert(\"비밀번호가 틀립니다!!\");");
			 out.println("history.back();");
			 out.println("</script>");
			 out.println("</head>");
			 out.println("</html>");
		 }
	}

}

















