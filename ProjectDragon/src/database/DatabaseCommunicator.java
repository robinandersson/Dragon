package database;
import java.sql.*;


public class DatabaseCommunicator {
	private static DatabaseCommunicator dbc;
	Connection conn;
	
	private DatabaseCommunicator() {
		try{
		    Class.forName("org.postgresql.Driver");
		    } catch (ClassNotFoundException cnfe){
		      System.out.println("Could not find the JDBC driver!");
		      System.exit(1);
		    }
		try {
		    conn = DriverManager.getConnection
		                   ("jdbc:postgresql://djupfeldt.se:5432/tda367dragon",
		                		   "tda367dragon", "tda367dragon");
		     } catch (SQLException sqle) {
		    	 sqle.printStackTrace();
		    	 System.out.println("Could not connect");
		       System.exit(1);
		     }
	}
	
	public static DatabaseCommunicator getInstance() {
		if(dbc == null) {
			dbc = new DatabaseCommunicator();
		}
		return dbc;
	}
	
	public Connection getConnection() {
		return conn;
	}
}
