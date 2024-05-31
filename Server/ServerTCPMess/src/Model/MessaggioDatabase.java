package Model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class MessaggioDatabase {
		private Messaggio msg;
	
		public void setMessaggio(Messaggio msg) {
			this.msg = msg;
		}
		
		public boolean InsertMessage() {
			String insertMessageSql = "INSERT INTO message(contenuto ,mittente, destinatario, idChat) VALUES(?,?,?,?)";
			
			String contenuto;
			String mittente;
			String destinatario;
			int idChat;
		   
		    boolean response = false;
		   
		    try (
		        Connection conn = DriverManager.getConnection(
		            "jdbc:mysql://localhost:3306/messaggistica",
		            "root",
		            ""
		        )) {
		    	
		    	contenuto = msg.getContenuto();
		    	mittente = msg.getUsernameMittente();
		    	destinatario = msg.getUsernameDestinatario();
		    	idChat = msg.getIdChat();
		    	PreparedStatement insertMessageStmt = conn.prepareStatement(insertMessageSql);
		    	
		    	insertMessageStmt.setString(1, contenuto);
		    	insertMessageStmt.setString(2, mittente);
		    	insertMessageStmt.setString(3, destinatario);
		    	insertMessageStmt.setInt(4, idChat);
		    	
		    	int rowsAffected = insertMessageStmt.executeUpdate();
		    	 if (rowsAffected == 0)
		    		 response = false;
		    	else
		                 response = true;
		            
		   
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return response;
	}


}

