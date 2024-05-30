package Model;

public class Messaggio {
	String contenuto;
	String usernameMittente;
	String usernameDestinatario;
	private int idMessaggio;
	private int idChat;
	public String getContenuto() {
		return contenuto;
	}
	public void setContenuto(String contenuto) {
		this.contenuto = contenuto;
	}
	public String getUsernameMittente() {
		return usernameMittente;
	}
	public void setUsernameMittente(String usernameMittente) {
		this.usernameMittente = usernameMittente;
	}
	public String getUsernameDestinatario() {
		return usernameDestinatario;
	}
	public void setUsernameDestinatario(String usernameDestinatario) {
		this.usernameDestinatario = usernameDestinatario;
	}
	public int getIdMessaggio() {
		return idMessaggio;
	}
	public void setIdMessaggio(int idMessaggio) {
		this.idMessaggio = idMessaggio;
	}
	public int getIdChat() {
		return idChat;
	}
	public void setIdChat(int idChat) {
		this.idChat = idChat;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Messaggio) {
			Messaggio temp = (Messaggio) obj;
			if(temp.getContenuto() == this.getContenuto()) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	@Override
	public String toString() {
		synchronized (System.out) {
			System.out.println("Mittente:" + this.usernameMittente + 
					"\nDestinatario:"+ 
					this.usernameDestinatario+ 
					"\nContenuto:"+this.contenuto);
		}
		return "";
	}
	
}
