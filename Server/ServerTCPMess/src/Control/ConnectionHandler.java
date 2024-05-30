package Control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import Model.ChatDatabase;
import Model.Login;
import Model.MessageBuilder;
import Model.Messaggio;
import Model.Registration;
import Model.Utente;
import Model.UtenteDatabase;

public class ConnectionHandler implements Runnable {
    Server sr;
    private Socket socketDedicata;
    Utente user;
    UtenteDatabase checkUserDatabase = new UtenteDatabase();
    ChatDatabase insertChatOnDatabase = new ChatDatabase();
    public ConnectionHandler(Socket socket, Server sr) {
        this.sr = sr;
        this.socketDedicata = socket;
    }

	public synchronized boolean validationLogin(BufferedReader input, PrintWriter output) {
    	 String[] initialMessage = null;

    	    try {
    	    	int i = 0;
    	    	while(i < 10) {
    	    		String response = input.readLine();
    	    	    initialMessage = MessageBuilder.onlySplitMessage(response);
    	    	    System.out.println(initialMessage[0]+"ciao");

    	            if (initialMessage.length == 2) {
    					Login login = new Login(initialMessage[0], initialMessage[1]);
    					if(login.CheckIfUserExists()) {
    						 user = new Utente();
    			             user.setUsername(initialMessage[0]);
    			             user.setPassword(initialMessage[1]);
    				         System.out.println("Login effettuato con successo: "+ user.getUsername());
    				         sr.addClient(user, output);
    				         if(socketDedicata != null && socketDedicata.isConnected()) {
    				        	  // costruisco mex e invio
    				        	 String responseTrue = MessageBuilder.generateForLoginCorrect(login.getNome(), login.getCognome(), login.getEmail(), user.getUsername());
    				        	 System.out.println(responseTrue);
    				        	 output.print(responseTrue);	
    				        	 output.flush();
    				             return true;
    				         }else {
    				        	 System.out.println("User disconnesso");
    				        	 return false;
    				         }
    					}else {
    						if(socketDedicata != null && socketDedicata.isConnected()) {
  				        	  // costruisco mex e invio
  				        	 String responseFalse = MessageBuilder.generateForLoginUncorrect();
  				        	 System.out.println(responseFalse);
  				        	 output.print(responseFalse);	
  				        	 output.flush();
  				        	 i++;
    						}else {
  				        	 System.out.println("User disconnesso");
  				        	 return false;
    						}
    						
    					}
    				   
    				} else if (initialMessage.length == 5) {
    					System.out.println("Registrazione---");
    					Registration reg = new Registration(initialMessage[0], initialMessage[1], initialMessage[2], initialMessage[3], initialMessage[4]);
    					if(reg.TakeUserAndInsert()) {
    						
    						if(socketDedicata != null && socketDedicata.isConnected()) {
    							user = new Utente();
        						user.setUsername(initialMessage[0]);
        						user.setNome(initialMessage[2]);
        						user.setCognome(initialMessage[3]);
        						user.setEmail(initialMessage[4]);
        						user.setPassword(initialMessage[1]);
        						output.print(MessageBuilder.signUpCorrect());
        						output.flush();
        						sr.addClient(user, output);
        						return true;
    						}else {
    							System.out.println("User disconnesso");
    							return false;
    						}
    					}else {
    						// errore generico inserimento, scrivere sull output il messaggio, incrementare la i
    						if(socketDedicata != null && socketDedicata.isConnected()) {
        						output.print(MessageBuilder.signUpIncorrect());
        						output.flush();
        						i++;
    						}else {
    							System.out.println("User disconnesso");
    							return false;
    						}
    					}
    				} else{
    					System.out.println("ERRORE MESSAGGIO NON RICONOSCIUTO.");
    					return false;
    				}
    	    	}
    	    	return false;
 	        
        } catch (IOException e) {
            System.out.println("Errore durante la lettura del messaggio di inizio: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
			return false; 
    }
       

    

    @Override
    public void run() {
    	try( BufferedReader input = new BufferedReader(new InputStreamReader(socketDedicata.getInputStream())); PrintWriter output = new PrintWriter(socketDedicata.getOutputStream())){
        synchronized (System.out) {
            System.out.println("Ciao dal thread appena creato");
        }

        if(validationLogin(input, output)) {
        	 synchronized (System.out) {
                 System.out.println("Mi dedico a: " + user.getUsername());
             }

             String message, response;
             // send Message another client
                 while (socketDedicata.isConnected()&&((message = input.readLine()) != null) ) {
                 	String[] split = MessageBuilder.onlySplitMessage(message);
                 	System.out.println("ciao sono entrato nel ciclo per leggere tutti i messaggi che mi arrivano da questo client" + user.getUsername());
                 	System.out.println("Contenuto messaggio arrivato al server: [0] mittente [1] destinatario");
                 	for(int i = 0; i < split.length; i++) {
                 		System.out.println(split[i]);
                 	}
                     if (split.length == 5) {
                    	 // invio messaggio
                         Messaggio mx = new Messaggio();
                         mx = MessageBuilder.fromSplitMessageToMex(split);
                         sr.sendMessage(mx);
                     } else if(split.length == 2) {
                    	 // ricerca user se esiste
                    	 if(checkUserDatabase.ifUserExist(split[1])) {
                    		 System.out.println("Esiste lo user con quel determianto username");
                    		 insertChatOnDatabase.setUsers(split[0], split[1]);
                    		 if(insertChatOnDatabase.CreateChatAndConnectUsers()) {
                    			 System.out.println("Chat creata");
                    			 System.out.println(split[0] + "split 0 e "+ split[1] + "split 1");
                        		 response = MessageBuilder.existUserTrue(split[1]);
                        		 output.print(response);
                        		 output.flush();
                    		 }else {
                    			 System.out.println("Errore database");
                        		 response = MessageBuilder.existUserFalse();
                        		 output.print(response);
                        		 output.flush();
                    		 }
                    	 }else {
                    		 System.out.println("Errore non esiste user con quel determinato username");
                    		 response = MessageBuilder.existUserFalse();
                    		 output.print(response);
                    	 }
                        
                     }else {
                    	 System.out.println("Messaggio non valido");
                     }
                 }
        }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	if(user != null) {
        		  sr.removeClient(user);
                  System.out.println("Connessione chiusa con: " + user.getUsername());
        	}
            if(socketDedicata.isClosed()) {
            	synchronized (System.out) {
					System.out.println("Socket gia chiusa");
				}
            	return;
            }else {
            	System.out.println("Socket chiusa---");
            	try {
    				socketDedicata.close();
    			} catch (IOException e) {
    			System.out.println("Errore chiusura socket");
    			}
            }
              Thread.currentThread().interrupt();
            }
        }
    }

