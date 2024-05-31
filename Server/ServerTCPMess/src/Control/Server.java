package Control;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import Model.MessageBuilder;
import Model.Messaggio;
import Model.MessaggioDatabase;
import Model.Utente;

public class Server{
	//client con il value come print per scrivere nella sua socket
	Map<Utente, PrintWriter> clients;
	ServerSocket welcomeSocket;
	MessaggioDatabase insertDatabaseMessaggio;
	// classi per inserire nel database i vari messaggi e chat.
	public Server(){
		clients = new HashMap<Utente, PrintWriter>();
		insertDatabaseMessaggio = new MessaggioDatabase();
	}

	public void start() {
		try {
			welcomeSocket = new ServerSocket(20000);
		} catch (IOException e) {
			System.out.println("Errore apertura socket");
			e.printStackTrace();
		}
		if(welcomeSocket != null) {
			try {
		        while (true) {
		            	Socket socket = welcomeSocket.accept();
		                System.out.println("Ciao dalla socket" + socket.getLocalPort() + " " + socket.getPort() + " " + socket.getInetAddress() + "\n");
		                Thread th1 = new Thread(new ConnectionHandler(socket, this));
		                th1.start();
		        }
			}catch (IOException e) {
				e.printStackTrace();
            }
		}
	    
	}

	
	public synchronized void addClient(Utente user, PrintWriter output) {
			// se user è istanziato e non è gia presente dentro clients cioè i clienti connessi allora lo aggiungo
			if(user != null && (!clients.containsKey(user))) {
				System.out.println("Sto aggiungendo ai maps questo client:" + user.getUsername());
				clients.put(user, output);
			}else {
				System.out.println("C'è un altro dispositivo connesso con il tuo account oppure user non istanziato"+"\n");
			}
		
		
	}
	public synchronized void sendMessage(Messaggio mess) {
		// creo utente temporaneo tramite il mess passandogli l'USERNAME 
		//che sarà univoco per ogni utente e ciò mi permette di poi con il metodo che vado a sovrascrivere nella classe utente
		// equals che questo metodo viene richiamato nel metodo .get() della classe hashmap
;		Utente dest = new Utente(mess.getUsernameDestinatario());
	
		if(clients.containsKey(dest)) {
			System.out.println("Sto inviando il messaggio a:"+ dest);
			// ottengo tramite la chiave il valore che sarebbe lo stream per scrivere a quel determinato clients
			PrintWriter outputDest = clients.get(dest);
			// messaggio standard ex:
			//Mittente|£$%&Destinatario|£$%&ciao io sono pippo|£$%&0|£$%&1|£$%&
			String message = MessageBuilder.splitAndGenerateMessageForSend(mess);
			insertDatabaseMessaggio.setMessaggio(mess);
			if(insertDatabaseMessaggio.InsertMessage()) {
				if(outputDest != null) {
					System.out.println("Messaggio inviato---");
					outputDest.print(message);
					outputDest.flush();
				}else {
					System.out.println("Destinatario sconnesso \nusername:"+mess.getUsernameDestinatario()+"\n");
				}
			}else {
				System.out.println("Errore inserimento database");
			}		
		}else {
			System.out.println("Destinatario non connesso. \nusername:"+mess.getUsernameDestinatario()+"\n");
		}
	
		
		
		
	}
	public synchronized void removeClient(Utente user) {

			if(user != null && clients.containsKey(user)) {
					System.out.println("Sto eliminando:" + user.getUsername() + " dall'hashMap");
					clients.remove(user);
					System.out.println("User: "+user.getUsername() + " Disconnesso."+"\n");
			}else {
				System.out.println("User non connesso");
			}
		
	}
	public static void main(String[] args) {
		Server sr = new Server();
		sr.start();

	}

}
