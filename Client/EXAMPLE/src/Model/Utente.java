package Model;

public class Utente {
	String username;
	String nome;
	String cognome;
	String email;
	private String password;
	public Utente(String username) {
		this.username = username;
	}
public Utente() {
	// TODO Auto-generated constructor stub
}	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Utente) {
			return this.hashCode()==obj.hashCode();
		}else {
			return false;
		}
	}
	@Override
	public int hashCode() {
		return username.hashCode();
	}
		
	
	
}
