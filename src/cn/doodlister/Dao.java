package cn.doodlister;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import cn.doodlister.Entity;



public class Dao {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public boolean setEntity(Entity e){
		String sql="INSERT INTO spiderdata (title,abstract) VALUES (?,?)";
		DBConnection dbc=null;
		PreparedStatement pstmt=null;
		Entity en=e;
		dbc=new DBConnection();
		try {
			pstmt=dbc.getConnection().prepareStatement(sql);
			pstmt.setString(1,e.getTitle());
			pstmt.setString(2, e.getTheAbstract());
			return pstmt.execute()?true:false;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
			try {
				pstmt.close();
				dbc.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
		return false;
		
	}
}
