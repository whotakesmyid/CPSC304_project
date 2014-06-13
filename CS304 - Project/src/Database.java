
import java.sql.*; //To use JDBC

import javax.swing.JOptionPane;

/**
 * This class is the class that connects to the Database.
 * The controllers already get a pointer to the database connection, 
 * so don't worry about this class.
 * 
 * For this class to work, it is necessary to add to the library the Oracle driver.
 */

public class Database {
	
	private static final String DATABASE_URL = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug";
	private static final int TOTAL_LOGIN_ATTEMPTS = 4; //number of logins to try a connection
	
	/*
	 * All available logins and passwords from all 4 group members:
	 * PS: We are aware this isn't "safe," but it's very practical for a class project.
	 * We don't want to have to type the password all the time. Please, consider.
	 */
	private static final String[] USERNAME = {"ora_s9a9", "ora_y9s7"};
	private static final String[] PASSWORD = {"a13842133", "a50142108"};
	
	private Connection con;
	
	public Connection getCon() {
		return con;
	}
	
	/**
	 * Called by classes outside to connect to the database.
	 * @return true if it's connected to the database and the user can already call queries.
	 */
	public boolean connect() {
		try  {
			// Load the Oracle JDBC driver
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			
			//Try log in with all usernames
			for (int i = 0; i < TOTAL_LOGIN_ATTEMPTS; i++)
				if ( connect(USERNAME[i], PASSWORD[i]) )
					return true;

		}
		catch (SQLException ex) {
			/* If you get to this, maybe you forgot to add class12.zip to the library. */
			JOptionPane.showMessageDialog(null, "Could not open Oracle Driver: " + ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}

		return false;
	}
	
	
	
	/* 
	 *   PRIVATE HELPER METHODS BELOW: 
	 */
	
	private boolean connect(String username, String password) {
		try {
			con = DriverManager.getConnection(DATABASE_URL,username,password);
		}
		catch (SQLException ex) {
			System.out.println("Attempt to login with username '" + username + "' failed.");
			System.out.println("Error: " + ex.getMessage());
			return false;
		}
		System.out.println("Logged in with username " + username + ".");
		return true;
	}

}
