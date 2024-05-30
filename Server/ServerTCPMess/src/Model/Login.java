package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    private String username;
    private String password;
    private String nome;
    private String cognome;
    private String email;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    // Metodo per verificare se un utente esiste nel database e ottenere i dettagli dell'utente
    public boolean CheckIfUserExists() {
        boolean response = false;

        // La query SQL che eseguirà la ricerca dell'utente
        String sql = "SELECT nome, cognome, email FROM utente WHERE username = ? AND password = ?";

        // Uso del try-with-resources per garantire la chiusura delle risorse
        try (
            // Connessione al database
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/messaggistica", 
                "root", 
                ""      
            );

            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            // Impostiamo i valori per i parametri della query
            pstmt.setString(1, username); // Impostiamo il primo parametro (username)
            pstmt.setString(2, password); // Impostiamo il secondo parametro (password)

            try (ResultSet rs = pstmt.executeQuery()) {
                // Se il risultato ha almeno una riga, otteniamo i dettagli dell'utente
                if (rs.next()) {
                    // Estrai i dettagli dal risultato della query
                    nome = rs.getString("nome");
                    cognome = rs.getString("cognome");
                    email = rs.getString("email");
                    
                    // L'utente esiste
                    response = true;
                }
            }
        } catch (SQLException e) {
            // Stampa lo stack trace in caso di eccezione SQL
            e.printStackTrace();
        }
        return response;
    }
}
