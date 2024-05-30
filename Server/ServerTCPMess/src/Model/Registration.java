package Model;
import java.sql.*;
public class Registration
{
	private String username;
	private String password;
	private String name;
	private String surname;
	private String email;
	
	public Registration (String username, String password , String name , String surname , String email) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.email = email;
	}
	
	public boolean TakeUserAndInsert() {
	    String checkUserSql = "SELECT COUNT(*) FROM Utente WHERE username = ? OR email = ?";
	    String insertUserSql = "INSERT INTO Utente(username, email, password, nome, cognome) VALUES(?, ?, ?, ?, ?)";
	   
	    boolean response = false;
	   
	    try (
	        Connection conn = DriverManager.getConnection(
	            "jdbc:mysql://localhost:3306/messaggistica",
	            "root",
	            ""
	        );
	        PreparedStatement checkUserStmt = conn.prepareStatement(checkUserSql);
	        PreparedStatement insertUserStmt = conn.prepareStatement(insertUserSql);
	    ) {
	        // Check if the user already exists
	        checkUserStmt.setString(1, username);
	        checkUserStmt.setString(2, email);
	        ResultSet rs = checkUserStmt.executeQuery();
	       
	        rs.next();
	        int userCount = rs.getInt(1);
	       
	        if (userCount == 0) {
	            // User does not exist, insert the new user
	            insertUserStmt.setString(1, username);
	            insertUserStmt.setString(2, email);
	            insertUserStmt.setString(3, password);
	            insertUserStmt.setString(4, name);
	            insertUserStmt.setString(5, surname);
	            int rowsAffected = insertUserStmt.executeUpdate();
	           
	            if (rowsAffected > 0) {
	                response = true;
	            }
	        }
	       
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	   
	    return response;
	}
}
