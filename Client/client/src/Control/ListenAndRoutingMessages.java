package Control;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import Model.Chat;
import Model.MessageBuilder;
import Model.Messaggio;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ListenAndRoutingMessages extends Thread {

    Socket mySocket;
    ChatManager manager;
    Controller control;
    BufferedReader input;

    public ListenAndRoutingMessages(Socket socket, ChatManager manager, Controller control, BufferedReader input) {
        mySocket = socket;
        this.manager = manager;
        this.control = control;
        this.input = input;
    }

    @Override
    public void run() {
        try {
            if (mySocket != null) {
                // Finché la socket è connessa continuo a ciclare i messaggi in arrivo
                while (mySocket.isConnected()) {
                    String response = null;
                    String[] split;
                    while ((response = input.readLine()) != null) {
                        System.out.println("Ciao dal thread");
                        split = MessageBuilder.onlySplitMessage(response);
                        if(split.length == 5) {
                        	Messaggio mex = MessageBuilder.generateMessageForReceive(response);
                            manager.routeMessageInChat(mex);
                            Chat chat = manager.getChat(mex.getUsernameMittente());

                            Platform.runLater(() -> {
                                control.createChatView(chat);
                                if (chat.getIdChat() == manager.getIdChatSelezionata()) {
                                    control.createMessageView(mex);
                                    control.impostaUltimoMex(chat, mex);
                                } else {
                                    control.impostaUltimoMex(chat, mex);
                                }
                            });
                        }else if(split.length == 2) {
                        	System.out.println("messaggio uguale a 2");
                        	if(split[0].equalsIgnoreCase("true")) {
                        		System.out.println("split 0 uguale a trueee");
                        		System.out.println(split[1]);
                        		Chat chat = manager.getChatAndCreate(split[1],control.io.getUsername());
                                Platform.runLater(() -> {
                                    control.createChatView(chat);
                                });
                        	}else {
                        		Platform.runLater(() -> {
                                    Alert alert = new Alert(AlertType.ERROR);
                                    alert.setTitle("Errore user non esiste");
                                    alert.setContentText("Inserire uno user diverso.");
                                });
                        	}
                        	 
                        }
                    }
                }
            } else {
                System.out.println("Dal thread la socket è null");
            }

        } catch (IOException e1) {
            System.out.println("Errore durante la connessione nel thread di lettura messaggi: " + e1.getMessage());
        }
    }

}
