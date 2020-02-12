package com.sist.dao;
import java.util.*; // ArrayList
import java.sql.*; // Connection,PreparedStatement,ResultSet
/*
 *    Connection => 오라클 연결 
 *    PreparedStatement => SQL문장 전송
 *    ResultSet => 결과값 저장  ============> SELECT 
 *       = ResultSet executeQuery("SQL")=>SELECT => COMMIT이 필요 없다 
 *       = executeUpdate("SQL") => INSERT,UPDATE,DELETE
 *           => COMMIT 첨부 
 *           
 *           
 *           JDBC => DBCP => ORM => JPA
 *           ====    ====    ===
 *                           MyBatis,Hibernate
 *                           
 *          ==========================                 
 *           1. 데이터 저장소 => Oracle
 *           2. 데이터 수집   
 *           ==========================
 *           3. 자바 연동      => 자바   ==============> 관리하는 프로그램 (Spring)
 *           ==========================
 *           4. HTML 화면 
 *           5. HTML 데이터 출력 => HTML
 *           6. CSS  => CSS
 *           7. JavaScript 
 */
public class BoardDAO {
    private Connection conn;
    private PreparedStatement ps;
    private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
    
    // 드라이버 등록 
    public BoardDAO()
    {
    	try
    	{
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    	}catch(Exception ex)
    	{
    		System.out.println(ex.getMessage());
    	}
    }
    // 연결
    public void getConnection()
    {
    	try
    	{
    		conn=DriverManager.getConnection(URL,"hr","happy");
    	}catch(Exception ex) {}
    }
    // 연결 해제
    public void disConnection()
    {
    	try
    	{
    		if(ps!=null) ps.close();
    		if(conn!=null) conn.close();
    	}catch(Exception ex) {}
    }
    
    // 게시판 목록 읽기   =====> SELECT ~ OBDER BY
    public ArrayList<BoardVO> boardListData(int page)
    {
    	ArrayList<BoardVO> list=
    			new ArrayList<BoardVO>();
    	try
    	{
    		getConnection();
    		String sql="SELECT no,subject,name,regdate,hit "
    				  +"FROM board "
    				  +"ORDER BY no DESC";
    		int rowSize=10;
    		/*
    		 *     1page : 0~9
    		 *     2page : 10~19
    		 */
    		int i=0;// 10개씩 나눠주는 변수 
    		int j=0;// while 돌아가는 횟수
    		int pageStart=(page*rowSize)-rowSize;// 출력위치 
    		
    		ps=conn.prepareStatement(sql);
    		ResultSet rs=ps.executeQuery();
    		
    		while(rs.next())
    		{
    			if(i<10 && j>=pageStart)
    			{
    				BoardVO vo=new BoardVO();
    				vo.setNo(rs.getInt(1));
    				vo.setSubject(rs.getString(2));
    				vo.setName(rs.getString(3));
    				vo.setRegdate(rs.getDate(4));
    				vo.setHit(rs.getInt(5));
    				list.add(vo);
    				i++;
    			}
    			j++;
    		}
    		rs.close();
    		
    	}catch(Exception ex)
    	{
    		System.out.println(ex.getMessage());
    	}
    	finally
    	{
    		disConnection();
    	}
    	return list;
    }
    
    public ArrayList<BoardVO> boardFindData(String fs,String ss)
    {
    	ArrayList<BoardVO> list=
    			new ArrayList<BoardVO>();
    	try
    	{
    		getConnection();
    		String sql="SELECT no,subject,name,regdate,hit "
    				  +"FROM board "
    				  +"WHERE "+fs+" LIKE '%"+ss+"%'";
    		ps=conn.prepareStatement(sql);
    		ResultSet rs=ps.executeQuery();
    		
    		while(rs.next())
    		{
    			    BoardVO vo=new BoardVO();
    				vo.setNo(rs.getInt(1));
    				vo.setSubject(rs.getString(2));
    				vo.setName(rs.getString(3));
    				vo.setRegdate(rs.getDate(4));
    				vo.setHit(rs.getInt(5));
    				list.add(vo);
    		}
    		rs.close();
    		
    	}catch(Exception ex)
    	{
    		System.out.println(ex.getMessage());
    	}
    	finally
    	{
    		disConnection();
    	}
    	return list;
    }
    public int boardTotalPage()
    {
    	int total=0;
    	try
    	{
    		getConnection();
    		String sql="SELECT CEIL(COUNT(*)/10.0) FROM board";
    		ps=conn.prepareStatement(sql);//오라클 전송 
    		// 실행명령
    		ResultSet rs=ps.executeQuery();
    		rs.next();
    		total=rs.getInt(1);
    		rs.close();
    	}catch(Exception ex)
    	{
    		System.out.println(ex.getMessage());
    	}
    	finally
    	{
    		disConnection();
    	}
    	return total;
    }
    // 게시판 => 내용보기 ====> SELECT ~ WHERE
    public BoardVO boardDetailData(int no)
    {
    	BoardVO vo=new BoardVO();
    	try
    	{
    		getConnection();
    		
    		/// 조회수 증가 
    		String sql="UPDATE board SET "
    				  +"hit=hit+1 "
    				  +"WHERE no=?";
    		ps=conn.prepareStatement(sql);
    		// ?에 값을 채운다 
    		ps.setInt(1, no);
    		// 실행요청 
    		ps.executeUpdate(); // COMMIT
    		
    		// 상세보기 
    		sql="SELECT no,name,subject,content,regdate,hit "
    		   +"FROM board "
    		   +"WHERE no=?";
    		
    		ps=conn.prepareStatement(sql);
    		ps.setInt(1, no);
    		
    		ResultSet rs=ps.executeQuery();
    		rs.next();
    		
    		vo.setNo(rs.getInt(1));
    		vo.setName(rs.getString(2));
    		vo.setSubject(rs.getString(3));
    		vo.setContent(rs.getString(4));
    		vo.setRegdate(rs.getDate(5));
    		vo.setHit(rs.getInt(6));
    		
    		rs.close();
    		
    	}catch(Exception ex)
    	{
    		System.out.println(ex.getMessage());
    	}
    	finally
    	{
    		disConnection();
    	}
    	return vo;
    }
    // 추가하기 => INSERT
    public void boardInsert(BoardVO vo)
    {
    	try
    	{
    		getConnection();
    		String sql="INSERT INTO board(no,name,subject,content,pwd) "
    				  +"VALUES((SELECT NVL(MAX(no)+1,1) FROM board),?,?,?,?)";
    		ps=conn.prepareStatement(sql);
    		// 실행전에 => ?에 값을 채운다
    		ps.setString(1, vo.getName());
    		ps.setString(2, vo.getSubject());
    		ps.setString(3, vo.getContent());
    		ps.setString(4, vo.getPwd());
    		// 실행
    		ps.executeUpdate();// commit()포함
    	}catch(Exception ex)
    	{
    		System.out.println(ex.getMessage());
    	}
    	finally
    	{
    		disConnection();
    	}
    }
    // 수정하기 => UPDATE
    public BoardVO boardUpdateData(int no)
    {
    	BoardVO vo=new BoardVO();
    	try
    	{
    		getConnection();
    		
    		String sql="SELECT no,name,subject,content "
    		   +"FROM board "
    		   +"WHERE no=?";
    		
    		ps=conn.prepareStatement(sql);
    		ps.setInt(1, no);
    		
    		ResultSet rs=ps.executeQuery();
    		rs.next();
    		
    		vo.setNo(rs.getInt(1));
    		vo.setName(rs.getString(2));
    		vo.setSubject(rs.getString(3));
    		vo.setContent(rs.getString(4));
    		
    		rs.close();
    		
    	}catch(Exception ex)
    	{
    		System.out.println(ex.getMessage());
    	}
    	finally
    	{
    		disConnection();
    	}
    	return vo;
    }
    // 실제 수정 => UPDATE~SET
    public boolean boardUpdate(BoardVO vo)
    {
    	boolean bCheck=false;
    	try
    	{
    		getConnection();
    		String sql="SELECT pwd FROM board "
    				  +"WHERE no=?";
    		ps=conn.prepareStatement(sql);
    		ps.setInt(1, vo.getNo());
    		ResultSet rs=ps.executeQuery();
    		rs.next();
    		String db_pwd=rs.getString(1);
    		rs.close();
    		
    		if(db_pwd.equals(vo.getPwd()))
    		{
    			bCheck=true;
    			sql="UPDATE board SET "
    			   +"name=?,subject=?,content=? "
    			   +"WHERE no=?";
    			ps=conn.prepareStatement(sql);
    			// ?
    			ps.setString(1,vo.getName());
    			ps.setString(2,vo.getSubject());
    			ps.setString(3,vo.getContent());
    			ps.setInt(4, vo.getNo());
    			
    			// 실행
    			ps.executeUpdate();
    		}
    		else
    		{
    			bCheck=false;
    		}
    	}catch(Exception ex)
    	{
    		System.out.println(ex.getMessage());
    	}
    	finally
    	{
    		disConnection();
    	}
    	return bCheck;
    }
    // 삭제하기  => DELETE
    public boolean boardDelete(int no,String pwd)
    {
    	boolean bCheck=false;
    	try
    	{
    		getConnection();
    		String sql="SELECT pwd FROM board "
    				  +"WHERE no=?";
    		ps=conn.prepareStatement(sql);
    		ps.setInt(1, no);
    		ResultSet rs=ps.executeQuery();
    		rs.next();
    		String db_pwd=rs.getString(1);
    		rs.close();
    		
    		if(db_pwd.equals(pwd))
    		{
    			bCheck=true;
    			sql="DELETE FROM board "
    			   +"WHERE no=?";
    			ps=conn.prepareStatement(sql);
    			ps.setInt(1, no);
    			// 실행
    			ps.executeUpdate();
    		}
    		else
    		{
    			bCheck=false;
    		}
    	}catch(Exception ex)
    	{
    		System.out.println(ex.getMessage());
    	}
    	finally
    	{
    		disConnection();
    	}
    	return bCheck;
    }
    // 찾기  => SELECT ~ LIKE
}










