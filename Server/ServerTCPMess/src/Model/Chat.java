package Model;

import java.util.Date;
import java.util.Vector;

public class Chat{
	int idChat;
	Date dataInizio;
	Date dataFine;
	
	Utente[] utenti = new Utente[2];
	Vector<Messaggio> mex;
	public Chat(Utente actor1, Utente actor2, int idChat) {
		this.idChat = idChat;
		utenti[0] = actor1;
		utenti[1] = actor2;
		mex = new Vector<Messaggio>(2,2);
	}
	public synchronized Vector<Messaggio> getMessaggi() {
		return mex;
	}
	public synchronized void addMessage(Messaggio mx) {
		if(!mex.isEmpty()) {
			mx.setIdMessaggio(mex.lastElement().getIdMessaggio()+1);
			mex.add(mx);
		}else {
			mx.setIdMessaggio(0);
			mex.add(mx);
		}
	}
	public synchronized void deleteMessage(Messaggio temp) {
		if(mex.contains(temp)) {
			mex.remove(temp);
		}else {
			synchronized (System.out) {
				System.out.println("Messaggio non trovato.");
			}
			
		}
	}
	public synchronized int getIdChat() {
		return idChat;
	}
	public synchronized void setIdChat(int idChat) {
		this.idChat = idChat;
	}
	public synchronized Date getInitializeChat() {
		return dataInizio;
	}
	public synchronized void setInitializeChat(Date dataInizio) {
		this.dataInizio = dataInizio;
	}
	public synchronized Date getFinishChat() {
		return dataFine;
	}
	public synchronized void setFinishChat(Date dataFine) {
		this.dataFine = dataFine;
	}
	public synchronized Utente[] getUserChat() {
		return utenti;
	}
	public synchronized void setUserChat(Utente[] utenti) {
		this.utenti = utenti;
	}
	public synchronized void setUserChat(Utente actor1, Utente actor2) {
		utenti[0] = actor1;
		utenti[1] = actor2;
	}
	public synchronized Utente getActor1() {
		// siamo noi
		return utenti[0];
	}
	public synchronized Utente getActor2() {
		// L'altro utente
		return utenti[1];
	}
	public synchronized void stampaLastMessage() {
		mex.lastElement().toString();
	}
	public synchronized void displayMessages() {
		synchronized (System.out) {
			for (int i = 0; i < mex.size(); i++) {
		    	Messaggio temp = mex.get(i);
		        temp.toString();
		    }
		}
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Chat) {
			Chat temp = (Chat) obj;
			if(temp.getActor1().equals(this.getActor1()) && temp.getActor2().equals(this.getActor2())) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	

}
