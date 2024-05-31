package Control;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import Model.Chat;
import Model.Messaggio;
import Model.Utente;

public class ChatManager {
	// ogni chat viene identificata dal suo altro actor(username cioè l'username del mittente poichè il destinatario siamo noi).
	Map<String, Chat> chats;
	int num = 0;
	AtomicInteger chatSelezionata;
	
	public ChatManager() {
		chats = new HashMap<String, Chat>();
		chatSelezionata = new AtomicInteger(-1);
	}
	
	public synchronized int getNumChats() {
		return num;
	}
	public synchronized void setChatSelezionata(int id) {
		chatSelezionata.set(id);
	}
	public synchronized int getIdChatSelezionata() {
		return chatSelezionata.get();
	}
	
	// messaggio in ricezione
	public synchronized void routeMessageInChat(Messaggio mex) {
		if(mex != null) {
			System.out.println("Smisto il messaggio nella chat: " + mex.getIdChat());
			Chat tempChat = chats.get(mex.getUsernameMittente());
			if(tempChat != null) {
				if(tempChat.getIdChat() == chatSelezionata.get()) {
					mex.toString();
				}else {
					System.out.println("La chat non è quella selezionata");
				}
				tempChat.addMessage(mex);
				
			}else {
				System.out.println("Chat non trovata ora la creo!");
				//questo metodo è per quando io ricevo un mex, e quindi il dest sono io e il mittente un altro.
				Chat temp = createChat(mex.getUsernameMittente(), mex.getUsernameDestinatario());
				temp.addMessage(mex);
			}
		}
	}
	
	public synchronized void invioChatLocale(Messaggio mex) {
		if(mex != null) {
			Chat temp = chats.get(mex.getUsernameDestinatario());
			if(temp != null) {
				temp.addMessage(mex);
			}else {
				System.out.println("Chat non trovata in fase di send message da parte nostra");
				Chat chat = new Chat(new Utente(mex.getUsernameMittente()), new Utente(mex.getUsernameDestinatario()), num);
				createChat(chat, mex);
				num++;
			}
		}else {
			System.out.println("Mex == null");
		}
	}
	
	public synchronized void createChat(Chat chat, Messaggio mex) {
		if(chats.containsValue(chat)) {
				System.out.println("Chat gia presente!.");
		}else {
				System.out.println("Inserisco chat dentro la HashMap delle chats");
				chats.put(chat.getActor2().getUsername(), chat);
				num++;
				chat.addMessage(mex);
		}
	}
	
	
	public synchronized void deleteChat(String actor) {
			if(chats.containsKey(actor)) {
				System.out.println("Rimuovo la chat con: " + actor);
				chats.remove(actor);
			}else {
				synchronized (System.out) {
					System.out.println("Chat non trovata");
				}
				
			}
	}
	public synchronized Chat getChat(String key) {
			if(chats.containsKey(key)) {
				System.out.println("Esiste la chat dentro chat manager");
				return chats.get(key);
			}else {
				return null;
			}
	}
	public synchronized Chat getChatAndCreate(String key, String myUsername) {
		
			if(chats.containsKey(key)) {
				System.out.println("Chat trovata");
				return chats.get(key);
			}else {
				System.out.println("Creo la chat");
				return createChat(key, myUsername);
			}
		
		
	}
	private synchronized Chat createChat(String username, String myUsername) {
			System.out.println("Creo chat e la inserisco dentro l'hashmap"+ " con myUsername[actor1]:"+ myUsername+ " E actor 2:"+ username);
			Chat chat = new Chat(new Utente(myUsername), new Utente(username), num);
			chat.setIdChat(num);
			chats.put(username, chat);
			num++;
			return chat;
	}

	
}
