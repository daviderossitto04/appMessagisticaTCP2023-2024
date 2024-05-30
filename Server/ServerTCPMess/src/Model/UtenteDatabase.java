package Model;
import java.sql.*;
public class UtenteDatabase
{
	public boolean ifUserExist(String username) {
	    String checkUserSql = "SELECT COUNT(*) FROM Utente WHERE username = ?";
	    String controlloUsername = username;
	    boolean response = false;
	   
	    try (
	        Connection conn = DriverManager.getConnection(
	            "jdbc:mysql://localhost:3306/messaggistica",
	            "root",
	            ""
	        );
	        PreparedStatement checkUserStmt = conn.prepareStatement(checkUserSql);
	    ) {
	        // Check if the user already exists
	        checkUserStmt.setString(1, controlloUsername);
	        ResultSet rs = checkUserStmt.executeQuery();
	       
	        rs.next();
	        int userCount = rs.getInt(1);
	       
	        if (userCount == 0) {
	            // User does not exist, return false
	        	response = false; }
	        	else {
	                response = true;
	            }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	   
	    return response;
	}
}

