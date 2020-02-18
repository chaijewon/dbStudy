package com.sist.dao;
import java.util.*;
import java.sql.*;
public class MusicDAO {
	private Connection conn;
    private PreparedStatement ps;
    private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
    
    // 드라이버 등록 
    public MusicDAO()
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
    
    // 기능 
    // 순위,state,idcrement,poster,타이틀 , 가수명 , album
    public ArrayList<MusicVO> musicListData(int page)
    {
    	ArrayList<MusicVO> list=
    			new ArrayList<MusicVO>();
    	try
    	{
    		getConnection();
    		String sql="SELECT rank,state,idcrement,poster,title,singer,album,mno "
    				  +"FROM music_genie "
    				  +"ORDER BY rank ASC";
    		ps=conn.prepareStatement(sql);
    		ResultSet rs=ps.executeQuery();
    		int rowSize=15;
    		int pageStart=(page*rowSize)-rowSize;
    		int i=0;
    		int j=0;
    		
    		while(rs.next())
    		{
    			if(i<rowSize && j>=pageStart)
    			{
    				MusicVO vo=new MusicVO();
    				vo.setRank(rs.getInt(1));
    				vo.setState(rs.getString(2));
    				vo.setIdcrement(rs.getInt(3));
    				vo.setPoster(rs.getString(4));
    				vo.setTitle(rs.getString(5));
    				vo.setSinger(rs.getString(6));
    				vo.setAlbum(rs.getString(7));
    				vo.setMno(rs.getInt(8));
    				list.add(vo);
    				i++;
    			}
    			j++;
    		}
    		
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
    public int musicTotalPage()
    {
    	int total=0;
    	try
    	{
    		getConnection();
    		String sql="SELECT CEIL(COUNT(*)/15.0) FROM music_genie";
    		ps=conn.prepareStatement(sql);
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
    // rank,state,idcrement,title,singer,poster,key
    public MusicVO musicDetailData(int no)
    {
    	MusicVO vo=new MusicVO();
    	/*
    	 *    DML  ( CURD )
    	 *     SELECT 
    	 *        = SELECT
    	 *          FROM 
    	 *          [
    	 *             WHERE
    	 *             GROUP BY
    	 *             HAVING
    	 *             ORDER BY
    	 *          ]
    	 *     INSERT
    	 *        = INSERT INTO table(컬럼명1...) VALUES(값1...) => DEFAULT
    	 *        = INSERT INTO table VALUES(값1...) => DEFAULT와 관련없이 무조건 첨부
    	 *     UPDATE
    	 *        = UPDATE table명 SET
    	 *          컬럼명=값 , 컬럼명=값....
    	 *          WHERE 조건
    	 *     DELETE
    	 *        = DELETE FROM table명
    	 *          WHERE 조건
    	 */
    	try
    	{
    		getConnection();
    		String sql="UPDATE music_genie SET "
    			      +"hit=hit+1 "
    				  +"WHERE mno=?";
    		ps=conn.prepareStatement(sql);
    		//ps.getConnection().prepareStatement(sql);
    		// ? 에 값 채우기
    		ps.setInt(1, no);
    		// 실행 요청 
    		ps.executeUpdate();
    		/*
    		 *     ps.executeUpdate(); => 오라클의 데이터가 변경 (INSERT,UPDATE,DELETE)
    		 *        => COMMIT
    		 *     
    		 *     ps.executeQuery(); => 데이터의 변경이 없는 상태 (검색:SELECT)
    		 */
    		sql="SELECT rank,state,idcrement,title,singer,poster,key,mno,album "
    				  +"FROM music_genie "
    				  +"WHERE mno=?";
    		ps=conn.prepareStatement(sql);
    		ps.setInt(1, no);
    		ResultSet rs=ps.executeQuery();
    		rs.next();
    		vo.setRank(rs.getInt(1));
    		vo.setState(rs.getString(2));
    		vo.setIdcrement(rs.getInt(3));
    		vo.setTitle(rs.getString(4));
    		vo.setSinger(rs.getString(5));
    		vo.setPoster(rs.getString(6));
    		vo.setKey(rs.getString(7));
    		vo.setMno(rs.getInt(8));
    		vo.setAlbum(rs.getString(9));
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
    
    // 로그인 
    public String isLogin(String id,String pwd)
    {
    	String result="";
    	try
    	{
    		getConnection();
    		String sql="SELECT COUNT(*) FROM music_member "
    				  +"WHERE id=?";
    		ps=conn.prepareStatement(sql);
    		ps.setString(1, id);
    		ResultSet rs=ps.executeQuery();
    		rs.next();
    		int count=rs.getInt(1);
    		rs.close();
    		
    		if(count==0)
    		{
    			result="NOID";
    		}
    		else
    		{
    			sql="SELECT pwd,name,sex FROM music_member "
    			   +"WHERE id=?";
    			ps=conn.prepareStatement(sql);
    			ps.setString(1, id);
    			rs=ps.executeQuery();
    			rs.next();
    			String db_pwd=rs.getString(1);
    			String name=rs.getString(2);
    			String sex=rs.getString(3);
    			rs.close();
    			
    			if(db_pwd.equals(pwd))
    			{
    				result=name+"|"+sex;
    			}
    			else
    			{
    				result="NOPWD";
    			}
    		}
    		
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		disConnection();
    	}
    	return result;
    }
    // 댓글쓰기  INSERT
    // 댓글수정  UPDATE
    // 댓글삭제  DELETE
    // 댓글보기  SELECT => WHERE
    public ArrayList<MusicReplyVO> replyListData(int mno)
    {
    	ArrayList<MusicReplyVO> list=new ArrayList<MusicReplyVO>();
    	try
    	{
    		getConnection();
    		String sql="SELECT no,id,name,msg,TO_CHAR(regdate,'YYYY-MM-DD HH24:MI:SS'), "
    				  +"(SELECT sex FROM music_member mm WHERE mm.id=mr.id) "
    				  +"FROM music_reply mr "
    				  +"WHERE mno=?";
    		ps=conn.prepareStatement(sql);
    		ps.setInt(1, mno);
    		// 실행 
    		ResultSet rs=ps.executeQuery();
    		while(rs.next())
    		{
    			MusicReplyVO vo=new MusicReplyVO();
    			vo.setNo(rs.getInt(1));
    			vo.setId(rs.getString(2));
    			vo.setName(rs.getString(3));
    			vo.setMsg(rs.getString(4));
    			vo.setDbDay(rs.getString(5));
    			vo.setSex(rs.getString(6));
    			list.add(vo);
    		}
    		rs.close();
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		disConnection();
    	}
    	return list;
    }
    public void replyInsert(MusicReplyVO vo)
    {
    	try
    	{
    		getConnection();
    		String sql="INSERT INTO music_reply VALUES(mr_no_seq.nextval,?,?,?,?,SYSDATE)";
    		ps=conn.prepareStatement(sql);
    		ps.setInt(1, vo.getMno());
    		ps.setString(2, vo.getId());
    		ps.setString(3, vo.getName());
    		ps.setString(4, vo.getMsg());
    		
    		ps.executeUpdate();
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		disConnection();
    	}
    }
    
    public void replyDelete(int no)
    {
    	try
    	{
    		getConnection();
    		String sql="DELETE FROM music_reply WHERE no=?";
    		ps=conn.prepareStatement(sql);
    		ps.setInt(1, no);
    		ps.executeUpdate();
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		disConnection();
    	}
    }
    
    // hit수가 많은 제목 ==> 5 (인라인뷰)
    public ArrayList<MusicVO> musicTop5()
    {
    	ArrayList<MusicVO> list=new ArrayList<MusicVO>();
    	try
    	{
    		// rownum ==> 게시판 (이전게시물,다음게시물)
    		getConnection();
    		String sql="SELECT poster,title,no,rownum "
    				  +"FROM (SELECT poster,title,RANK() OVER(ORDER BY hit DESC) as no "
    				  +"FROM music_genie ORDER BY hit DESC) "
    				  +"WHERE rownum<=5";
    		ps=conn.prepareStatement(sql);
    		// 실행 
    		ResultSet rs=ps.executeQuery();
    		while(rs.next())
    		{
    			MusicVO vo=new MusicVO();
    			vo.setPoster(rs.getString(1));
    			vo.setTitle(rs.getString(2));
    			vo.setRank(rs.getInt(3));
    			list.add(vo);
    		}
    		
    		rs.close();
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	finally
    	{
    		disConnection();
    	}
    	return list;
    }
}














