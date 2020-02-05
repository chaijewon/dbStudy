package com.sist.dao;
// ����Ŭ ���� 
import java.util.*;
import java.sql.*;
public class MusicDAO {
   private Connection conn;
   private PreparedStatement ps;
   
   private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
   public MusicDAO()
   {
	   try
	   {
		   Class.forName("oracle.jdbc.driver.OracleDriver");
	   }catch(Exception ex)
	   {
		   ex.printStackTrace();
	   }
   }
   
   public void getConnection()
   {
	   try
	   {
		   conn=DriverManager.getConnection(URL,"hr","happy");
	   }catch(Exception ex) {}
   }
   
   public void disConnection()
   {
	   try
	   {
		   if(ps!=null) ps.close();
		   if(conn!=null) conn.close();
	   }catch(Exception ex) {}
   }
   
   public void musicInsert(MusicVO vo)
   {
	   try
	   {
		   getConnection();
		   String sql="INSERT INTO music_genie VALUES("
				     +"(SELECT NVL(MAX(mno)+1,1) FROM music_genie),?,?,?,?,?,?,?,?)";// ���� 
		   ps=conn.prepareStatement(sql);
		   
		   ps.setInt(1, vo.getRank());
		   ps.setString(2, vo.getTitle());// => 'abcd'
		   ps.setString(3, vo.getSinger());
		   ps.setString(4, vo.getAlbum());
		   ps.setString(5, vo.getPoster());
		   ps.setInt(6, vo.getIdcrement());
		   ps.setString(7, vo.getState());
		   ps.setString(8, vo.getKey());
		   
		  
		   ps.executeUpdate(); // commit()
		   
	   }catch(Exception ex)
	   {
		   ex.printStackTrace();
	   }
	   finally
	   {
		   disConnection();
	   }
   }
   
   public ArrayList<MusicVO> musicListData()
   {
	   ArrayList<MusicVO> list=new ArrayList<MusicVO>();
	   try
	   {
		   getConnection();
		   String sql="SELECT mno,title,singer,poster,album,idcrement,state FROM music_genie "
				     +"ORDER BY rank ASC";
		   ps=conn.prepareStatement(sql);
		   ResultSet rs=ps.executeQuery();
		   while(rs.next())
		   {
			   MusicVO vo=new MusicVO();
			   vo.setMno(rs.getInt(1));
			   vo.setTitle(rs.getString(2));
			   vo.setSinger(rs.getString(3));
			   vo.setPoster(rs.getString(4));
			   vo.setAlbum(rs.getString(5));
			   vo.setIdcrement(rs.getInt(6));
			   vo.setState(rs.getString(7));
			   
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
   public MusicVO musicDetailData(int mno)
   {
	   MusicVO vo=new MusicVO();
	   try
	   {
		   getConnection();
		   String sql="SELECT mno,title,singer,poster,album,idcrement,state,key FROM music_genie "
				     +"WHERE mno=?";
		   ps=conn.prepareStatement(sql);
		   ps.setInt(1, mno);
		   ResultSet rs=ps.executeQuery();
		   rs.next();
	
			   vo.setMno(rs.getInt(1));
			   vo.setTitle(rs.getString(2));
			   vo.setSinger(rs.getString(3));
			   vo.setPoster(rs.getString(4));
			   vo.setAlbum(rs.getString(5));
			   vo.setIdcrement(rs.getInt(6));
			   vo.setState(rs.getString(7));
			   vo.setKey(rs.getString(8));
		   rs.close();
	   }catch(Exception ex)
	   {
		   ex.printStackTrace();
	   }
	   finally
	   {
		   disConnection();
	   }
	   return vo;
   }
   public ArrayList<MusicVO> musicFindData(String title)
   {
	   ArrayList<MusicVO> list=new ArrayList<MusicVO>();
	   try
	   {
		   getConnection();
		   String sql="SELECT title,singer FROM music_genie "
				     +"WHERE title LIKE '%"+title+"%'";
		   ps=conn.prepareStatement(sql);
		   //ps.setString(1, title);
		   ResultSet rs=ps.executeQuery();
		   while(rs.next())
		   {
			   MusicVO vo=new MusicVO();
			   vo.setTitle(rs.getString(1));
			   vo.setSinger(rs.getString(2));
			   
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
   
   public static void main(String[] args) {
	   MusicDAO dao=new MusicDAO();
	   ArrayList<MusicVO> list=dao.musicFindData("사랑");
	   
	   for(MusicVO vo:list)
	   {
		   System.out.println(vo.getTitle()+"-"+vo.getSinger());
	   }
   }
}










