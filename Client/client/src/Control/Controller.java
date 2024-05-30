package Control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JOptionPane;
import Model.Chat;
import Model.MessageBuilder;
import Model.Messaggio;
import Model.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox ParentChat;

    @FXML
    private ImageView buttonCreaChat;

    @FXML
    private ImageView buttonEsci;

    @FXML
    private ImageView buttonImpostazioni;

    @FXML
    private ImageView buttonProfilo;

    @FXML
    private ImageView buttonSendMessage;

    @FXML
    private ImageView moreImpostazioni;

    @FXML
    private Label nomeChatSelezionata;

    @FXML
    private VBox parentMessage;

    @FXML
    private ImageView pictureProfiloChatSelezionata;

    @FXML
    private TextField textSend;
    
    @FXML
    private TextField textFieldRicercaChat;
    
    @FXML
    private ImageView buttonRicerca;
    
    @FXML
    private ImageView addImageIcon;
    
    @FXML
    private ImageView deleteChatIcon;
    
    @FXML
    private ImageView cancelButtonRicerca;

    
    ChatManager manager;
    
    Socket socket;
    
    Utente io;
    
    Map<Chat, HBox> chatsView;
    
    PrintWriter output;
    // al thread che riceve messaggi aggiungo la chiamata per aggiungere graficamente la chat, passandogli la chat e aggiungendo tutte le informazioni
 public void setSocket(Socket socket) {
	 if(socket != null && socket.isConnected()) {
		 this.socket = socket;
		 System.out.println("Socket associata");
	 }
 }
 public void setMyUser(Utente user) {
	 this.io = user;
 }
    
 public void setManager(ChatManager manager) {
	 this.manager = manager;
 }
 public void setOutput(PrintWriter output) {
	 this.output = output;
 }
 
 	@FXML
 	void cancelSearchChat(MouseEvent event) {
 		System.out.println("Ricreo chatsView");
        for (HBox chatBox : chatsView.values()) {
        	if(!ParentChat.getChildren().contains(chatBox)) {
        		ParentChat.getChildren().add(chatBox);
        	}
        }
 	}
 
    @FXML
    void PressCreateChat(MouseEvent event) {
    	String nomeUser = JOptionPane.showInputDialog("Inserisci username destinatario:");
    	// vedere se esiste dentro il database quindi scrivendo al server cercando.
    	if(socket != null && socket.isConnected()) {
    		if(nomeUser != null && !nomeUser.equalsIgnoreCase("")) {
    			String request = MessageBuilder.requestForAskIfExistUsername(nomeUser, io.getUsername());
    			output.print(request);
    			output.flush();
        	}else {
        		Alert alert = new Alert(AlertType.ERROR);
        		alert.setTitle("Errore inserimento");
        		alert.setContentText("Non hai inserito niente!.");
        		alert.showAndWait();
        	}
    	}else {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Errore connessione al server");
    		alert.setContentText("Chiusura Applicazione");
    		alert.showAndWait();
    		Stage stage = (Stage) buttonSendMessage.getScene().getWindow();
        	stage.close();
    	}
    	
    	
    }

    @FXML
    void PressLogOut(MouseEvent event) {
    	if(socket != null && socket.isConnected()) {
    		try {
    			output.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	Stage stage = (Stage) buttonSendMessage.getScene().getWindow();
    	stage.close();
    }

    @FXML
    void inviaMessaggio(MouseEvent event) {
    	// se la socket è connessa scrivo se anche il testo non è vuoto
    	if(socket != null && socket.isConnected()) {
    		if(nomeChatSelezionata.getText().equalsIgnoreCase("")) {
    			textSend.setPromptText("Seleziona una chat");
    		}else {
    			if(!textSend.getText().isEmpty()) {
    				Messaggio mex = new Messaggio();
    				mex.setUsernameMittente(io.getUsername());
					mex.setUsernameDestinatario(nomeChatSelezionata.getText());
    				mex.setContenuto(textSend.getText());
    				
    				//visto che siamo noi a mandarlo, il metodo è in "locale"
    				manager.invioChatLocale(mex);
    				Chat tempChat = manager.getChat(nomeChatSelezionata.getText());
    				createMessageView(mex);
    				VBox temp = (VBox) chatsView.get(tempChat).getChildren().get(1);
    				Label ultimoMex = (Label) temp.getChildren().get(1);
    				ultimoMex.setText(textSend.getText());
    				String message = MessageBuilder.splitAndGenerateMessageForSend(mex);
    				output.print(message);
    				output.flush();
    				textSend.setText("");
    			}else {
    				textSend.setPromptText("Scrivi qualcosa, non invio se non scrivi!");
    			}
    		}
    			
    	}else {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Errore connessione al server");
    		alert.setContentText("Chiusura Applicazione");
    		alert.showAndWait();
    		Stage stage = (Stage) buttonSendMessage.getScene().getWindow();
        	stage.close();
    	}
    }


    @FXML
    void inviaMessaggioEnter(ActionEvent event) {
    	inviaMessaggio(null);
    }
    
    @FXML
    void searchChatEnter(ActionEvent event) {
    	searchChat(null);
    }
    
    
    // clicco impostazioni
    @FXML
    void pressImpostazioni(MouseEvent event) {

    }

    // visualizzo le mie informazioni
    @FXML
    void pressProfille(MouseEvent event) {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/profile.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ControllerProfile controller = loader.getController();
		controller.setText(io.getUsername(), io.getNome(), io.getCognome(), io.getEmail());
		Scene scene = new Scene(root);
		Stage secondaryStage = new Stage();
		secondaryStage.setTitle("Profilo");
		secondaryStage.setScene(scene);
		secondaryStage.setResizable(false);
		secondaryStage.centerOnScreen();
		secondaryStage.show();
    }

    // cerco una determinata chat
    @FXML
    void searchChat(MouseEvent event) {
        if (textFieldRicercaChat.getText() != null && !textFieldRicercaChat.getText().isEmpty()) {
           	int size = ParentChat.getChildren().size();
            if (size > 2) {
                // Rimuovi gli elementi a partire dall'indice 2
                for(int i = size - 1; i >= 2; i--) {
                    ParentChat.getChildren().remove(i);
                }
            }
            // Altrimenti, filtra le chat in base al testo inserito
            for (Map.Entry<Chat, HBox> entry : chatsView.entrySet()) {
                Chat chat = entry.getKey();
                HBox chatBox = entry.getValue();
                if (chat.getActor2().getUsername().toLowerCase().startsWith(textFieldRicercaChat.getText().toLowerCase())) {
                    ParentChat.getChildren().add(chatBox);
                }
            }
        }
    }
    
    // quando premo una chat da tutte le chat disponibili
    @FXML
    void viewChat(MouseEvent event) {
    	parentMessage.getChildren().clear();
    	HBox box = (HBox) event.getSource();
    	System.out.println(box.getChildren().toString());
    	VBox info = (VBox) box.getChildren().get(1);
    	Label username = (Label) info.getChildren().get(0);
    	ImageView img = (ImageView) box.getChildren().get(0);
    	
    	nomeChatSelezionata.setText(username.getText());
    	
    	pictureProfiloChatSelezionata.setImage(img.getImage());
    	pictureProfiloChatSelezionata.setVisible(true);
    	
    	Chat temp = manager.getChat(username.getText());
    	Vector<Messaggio> mex = temp.getMessaggi();
    	// creo la view per ogni messaggio presente dentro la chat model
    	for(int i = 0; i < mex.size(); i++) {
    		createMessageView(mex.get(i));
    	}
   	 	pictureProfiloChatSelezionata.setVisible(true);
   	 	textSend.setVisible(true);
   	 	buttonSendMessage.setVisible(true);
   	 	addImageIcon.setVisible(true);
   	 	deleteChatIcon.setVisible(true);
    	manager.setChatSelezionata(temp.getIdChat());
    	
    }

    // creo a livello grafico la chat
    public synchronized void createChatView(Chat chat) {
    	if(chat != null) {
    	    if (chatsView.containsKey(chat)) {
                return;
            } else {
                // HBox con dentro altra roba
                HBox box = new HBox();
                box.setAlignment(Pos.CENTER_LEFT);
                box.setPrefWidth(200);
                box.setPrefHeight(100);
  
                // Image di default
                 ImageView image = new ImageView("assets/user.png");
                 image.setFitWidth(69);
                 image.setFitHeight(60);
                 HBox.setMargin(image, new Insets(0, 0, 0, 20));
                 box.getChildren().add(image);
                
                // Creo VBox per nome e ultimo messaggio
                VBox boxVerticaleNomeUltimoMessaggio = new VBox();
                boxVerticaleNomeUltimoMessaggio.setAlignment(Pos.CENTER_LEFT);
                HBox.setMargin(boxVerticaleNomeUltimoMessaggio, new Insets(0, 0, 0, 10));
                boxVerticaleNomeUltimoMessaggio.setPrefHeight(80);
                boxVerticaleNomeUltimoMessaggio.setPrefWidth(206);
                
                Label nomeChat = new Label(chat.getActor2().getUsername());
                nomeChat.setFont(Font.font("Ebrima", FontWeight.BOLD, FontPosture.REGULAR, 20));
                nomeChat.setTextFill(Color.WHITE);
                nomeChat.setAlignment(Pos.CENTER_LEFT);
                nomeChat.setPrefHeight(21);
                nomeChat.setPrefWidth(153);
                
                // Aggiungi il nome della chat
                boxVerticaleNomeUltimoMessaggio.getChildren().add(nomeChat);
                Label lastMessage;
                // Aggiungi l'ultimo messaggio se disponibile
                if (!chat.getMessaggi().isEmpty()) {
                    lastMessage = new Label(chat.getMessaggi().lastElement().getContenuto());
                    lastMessage.setFont(Font.font("Ebrima", 18));
                    lastMessage.setTextFill(Color.web("#9da7a7"));
                    lastMessage.setAlignment(Pos.CENTER_LEFT);
                    lastMessage.setPrefHeight(26);
                    lastMessage.setPrefWidth(207);
                    boxVerticaleNomeUltimoMessaggio.getChildren().add(lastMessage);
                }else {
                	lastMessage = new Label("");
                	lastMessage.setFont(Font.font("Ebrima", 18));
                    lastMessage.setTextFill(Color.web("#9da7a7"));
                    lastMessage.setAlignment(Pos.CENTER_LEFT);
                    lastMessage.setPrefHeight(26);
                    lastMessage.setPrefWidth(207);
                    boxVerticaleNomeUltimoMessaggio.getChildren().add(lastMessage);
                }
                
                box.getChildren().add(boxVerticaleNomeUltimoMessaggio);
                box.setOnMouseClicked(event -> viewChat(event));
                box.setCursor(Cursor.HAND);
                chatsView.put(chat, box);
                ParentChat.getChildren().add(box);
            }
        }else {
    		return;
    	}
    }
    public synchronized void createMessageView(Messaggio mex) {
    	if(mex.getUsernameMittente().equalsIgnoreCase(io.getUsername())) {
    		// siamo noi che inviamo mex
    		HBox messaggioBox = new HBox();
    		messaggioBox.setAlignment(Pos.CENTER_LEFT);
    		
    		
    		Label messaggio = new Label(mex.getContenuto());
    		messaggio.setWrapText(true);
    		messaggio.setAlignment(Pos.BASELINE_LEFT);
    		messaggio.getStyleClass().add("myMessage");
    		messaggio.setFont(Font.font("System", 12));
    		messaggio.setPadding(new Insets(5,20,10,20));
    		
    		messaggioBox.getChildren().add(messaggio);
    		HBox.setMargin(messaggio, new Insets(10,0,10,0));
    		
    		
    		parentMessage.getChildren().add(messaggioBox);
    	}else {
    		// siamo noi che inviamo mex
    		HBox messaggioBox = new HBox();
    		messaggioBox.setAlignment(Pos.CENTER_LEFT);
    		
    		
    		Label messaggio = new Label(mex.getContenuto());
    		messaggio.setWrapText(true);
    		messaggio.setAlignment(Pos.BASELINE_LEFT);
    		messaggio.getStyleClass().add("messageDestinatario");
    		messaggio.setFont(Font.font("System", 12));
    		messaggio.setPadding(new Insets(5,20,10,20));
    		
    		messaggioBox.getChildren().add(messaggio);
    		HBox.setMargin(messaggio, new Insets(10,0,10,0));
    		
    		parentMessage.getChildren().add(messaggioBox);
    	}
    }
    
    public void handleWindowClose(WindowEvent event) {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
                System.out.println("Socket chiusa correttamente.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void impostaUltimoMex(Chat chat, Messaggio mex) {
    	if(chatsView.containsKey(chat)) {
    		HBox box = chatsView.get(chat);
    		VBox boxVerticale = (VBox) box.getChildren().get(1);
    		Label ultimoMex = (Label) boxVerticale.getChildren().get(1);
    		ultimoMex.setText(mex.getContenuto());
    	}else {
    		System.out.println("Non esiste la chatView");
    	}
    }
    
    @FXML
    void addImage(MouseEvent event) {
    	FileChooser fileChooser = new FileChooser();
        
        // Imposta il titolo della finestra del file chooser
        fileChooser.setTitle("Seleziona un'immagine");
        
        // Imposta i filtri per mostrare solo file immagine
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        Stage primaryStage = (Stage) buttonSendMessage.getScene().getWindow();
    	
        // Mostra il file chooser e ottieni il file selezionato
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            try {
                // Carica l'immagine selezionata
                Image image = new Image(new FileInputStream(selectedFile));
                
                // Imposta l'immagine nell'ImageView
                pictureProfiloChatSelezionata.setImage(image);
                Chat temp = manager.getChat(nomeChatSelezionata.getText());
                HBox box = chatsView.get(temp);
                ImageView img = (ImageView) box.getChildren().get(0);
                img.setImage(image);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    void deleteChatSelezionata(MouseEvent event) {
    	if(nomeChatSelezionata.getText().isEmpty()) {
    		return;
    	}else {
    		 Chat tempChat = manager.getChat(nomeChatSelezionata.getText());
    		 HBox box = chatsView.get(tempChat);
    		 
    		 ParentChat.getChildren().remove(box);
    		 parentMessage.getChildren().clear();
    		 manager.deleteChat(nomeChatSelezionata.getText());
    		 chatsView.remove(tempChat);
    		 
    		 
    		 nomeChatSelezionata.setText("");
    		 pictureProfiloChatSelezionata.setVisible(false);
    		 textSend.setVisible(false);
    		 buttonSendMessage.setVisible(false);
    		 addImageIcon.setVisible(false);
    		 deleteChatIcon.setVisible(false);
    		 
    	}
   
    	
    }
    @FXML
    void initialize() {
    	  assert ParentChat != null : "fx:id=\"ParentChat\" was not injected: check your FXML file 'gui.fxml'.";
          assert addImageIcon != null : "fx:id=\"addImageIcon\" was not injected: check your FXML file 'gui.fxml'.";
          assert buttonCreaChat != null : "fx:id=\"buttonCreaChat\" was not injected: check your FXML file 'gui.fxml'.";
          assert buttonEsci != null : "fx:id=\"buttonEsci\" was not injected: check your FXML file 'gui.fxml'.";
          assert buttonImpostazioni != null : "fx:id=\"buttonImpostazioni\" was not injected: check your FXML file 'gui.fxml'.";
          assert buttonProfilo != null : "fx:id=\"buttonProfilo\" was not injected: check your FXML file 'gui.fxml'.";
          assert buttonRicerca != null : "fx:id=\"buttonRicerca\" was not injected: check your FXML file 'gui.fxml'.";
          assert buttonSendMessage != null : "fx:id=\"buttonSendMessage\" was not injected: check your FXML file 'gui.fxml'.";
          assert deleteChatIcon != null : "fx:id=\"deleteChatIcon\" was not injected: check your FXML file 'gui.fxml'.";
          assert nomeChatSelezionata != null : "fx:id=\"nomeChatSelezionata\" was not injected: check your FXML file 'gui.fxml'.";
          assert parentMessage != null : "fx:id=\"parentMessage\" was not injected: check your FXML file 'gui.fxml'.";
          assert pictureProfiloChatSelezionata != null : "fx:id=\"pictureProfiloChatSelezionata\" was not injected: check your FXML file 'gui.fxml'.";
          assert textFieldRicercaChat != null : "fx:id=\"textFieldRicercaChat\" was not injected: check your FXML file 'gui.fxml'.";
          assert textSend != null : "fx:id=\"textSend\" was not injected: check your FXML file 'gui.fxml'.";
  	 	pictureProfiloChatSelezionata.setVisible(false);
   	 	textSend.setVisible(false);
   	 	buttonSendMessage.setVisible(false);
   	 	addImageIcon.setVisible(false);
   	 	deleteChatIcon.setVisible(false);
        chatsView = new HashMap<Chat, HBox>();
        
    }

}
