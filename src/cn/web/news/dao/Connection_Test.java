package cn.web.news.dao;

import java.sql.DriverManager;



public class Connection_Test {

	public static final String URL = "jdbc:mysql://localhost:3306/newsdb";
    public static final String USER = "root";
    public static final String PASSWORD = "root";
	public static void main(String[] args) {
		
		 try {
	            //1.加载驱动程序
	            Class.forName("com.mysql.jdbc.Driver");
	            //2. 获得数据库连接
	          DriverManager.getConnection(URL, USER, PASSWORD);
	          System.out.println("MySQL数据库连接成功！！！");
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	}
