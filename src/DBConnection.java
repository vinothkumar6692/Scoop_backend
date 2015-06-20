import java.io.*;
import java.sql.*;
public class DBConnection {
	public static Connection createConnection() throws Exception
	{System.out.println("Conn.........");
	Connection conn=null;
	//String dbName = System.getProperty("RDS_DB_NAME");
	  //String userName = System.getProperty("RDS_USERNAME");
	  //String password = System.getProperty("RDS_PASSWORD");
	  //String hostname = System.getProperty("RDS_HOSTNAME");
	  //String port = System.getProperty("RDS_PORT");
	  //String jdbcUrl = "jdbc:mysql://" + hostname + ":" +
	    //port + "/" + dbName + "?user=" + userName + "&password=" + password;
	Class.forName("com.mysql.jdbc.Driver");
	String url="jdbc:mysql://twitterheatmap.cjkuojdbhc2z.us-east-1.rds.amazonaws.com:3306/twitter";
	String uname="raghav";
	String pwd="raghavabc";
	DriverManager.registerDriver(new com.mysql.jdbc.Driver());
	conn=DriverManager.getConnection(url,uname,pwd);
	return conn;
	}
	public static void closeConnection(Connection con)
	{
	if(con!=null)
	{
	try{
	con.close();
	}
	catch(SQLException e){
	e.printStackTrace();
	}
	}
	}
	

		


}

