package Model;
import java.sql.*;
public class ChatDatabase
{
	private String attore1;
	private String attore2;
	
	public void setUsers(String attore1, String attore2) {
		this.attore1 = attore1;
		this.attore2 = attore2;
	}
	
	public boolean CreateChatAndConnectUsers() {
		String getLastChatIdSql = "SELECT MAX(idChat) AS ultimoIdRegistrato FROM Chat";
		String insertChatSql = "INSERT INTO Chat(idChat) VALUES(?)";
		
	    String checkEntraSql = "SELECT COUNT(*) FROM Entra WHERE (attore1 = ? AND  attore2 = ?) OR (attore1 = ? AND attore2 = ?)";
	    String insertEntraSql = "INSERT INTO Entra(idChat ,attore1, attore2) VALUES(?, ?, ?)";
	   
	    boolean response = false;
	   
	    try (
	        Connection conn = DriverManager.getConnection(
	            "jdbc:mysql://localhost:3306/messaggistica",
	            "root",
	            ""
	        );
	    	
	    	PreparedStatement getLastChatIdStmt = conn.prepareStatement(getLastChatIdSql);
	        PreparedStatement insertChatStmt = conn.prepareStatement(insertChatSql);
	    		
	        PreparedStatement checkEntraStmt = conn.prepareStatement(checkEntraSql);
	        PreparedStatement insertEntraStmt = conn.prepareStatement(insertEntraSql);
	    ) {
	    	
	    	checkEntraStmt.setString(1, attore1);
           checkEntraStmt.setString(2, attore2);
           checkEntraStmt.setString(3, attore2);
           checkEntraStmt.setString(4, attore1);
           ResultSet rs = checkEntraStmt.executeQuery();
           rs.next();
           int chatCount = rs.getInt(1);
           if (chatCount == 0) {
	    	ResultSet lastIdRs = getLastChatIdStmt.executeQuery();
           int newChatId = 1; // Se non ci sono chat, inizia da 1
           if (lastIdRs.next()) {
               int lastChatId = lastIdRs.getInt("ultimoIdRegistrato");
               newChatId = lastChatId + 1; // Incrementa l'ID di uno
           }
          
           insertChatStmt.setInt(1, newChatId);
           int rowsAffected = insertChatStmt.executeUpdate();
           if (rowsAffected > 0) {
               // Inserisci la nuova connessione tra gli utenti e la chat
               insertEntraStmt.setInt(1, newChatId);
               insertEntraStmt.setString(2, attore1);
               insertEntraStmt.setString(3, attore2);
               rowsAffected = insertEntraStmt.executeUpdate();
               if (rowsAffected > 0) {
                   response = true;
               }
	        } }else {
	        	response = true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	   
	    return response;
	}
}
