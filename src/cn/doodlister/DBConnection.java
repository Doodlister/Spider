package cn.doodlister;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private String url;
	private String driver;
	private String user;
	private String password;
	
	private Connection conn=null;
	//主要功能是连接和关闭数据库
	public DBConnection(){
		url="jdbc:mysql://127.0.0.1/spider";
		driver="com.mysql.jdbc.Driver";
		user="root";
		password="";
		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Connection getConnection(){
		return conn;
	}
	public void close(){
		try {
			if(!conn.isClosed())
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
