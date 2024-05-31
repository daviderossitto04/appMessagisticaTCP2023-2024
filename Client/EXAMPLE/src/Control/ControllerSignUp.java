package Control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import Model.MessageBuilder;
import Model.PasswordHasher;
import Model.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ControllerSignUp {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label cambiaLogin;

    @FXML
    private TextField cognome;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private TextField email;

    @FXML
    private TextField nome;

    @FXML
    private PasswordField password;

    @FXML
    private Button sendButton;

    @FXML
    private TextField username;
    
    Socket socket;
    PrintWriter output;
    BufferedReader input;
    ChatManager manager;
    Utente io;
    
    public void setAssetsForConnection(Socket socket, PrintWriter output, BufferedReader input, ChatManager manager) {
    	this.socket = socket;
    	this.output = output;
    	this.input = input;
    	this.manager = manager;
    }

    @FXML
    void cambiaScena(MouseEvent event) {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/login.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ControllerLogin controller = loader.getController();
		controller.setAssetsForConnection(socket, output, input, manager);
		Scene scene = new Scene(root);
		Stage primaryStage = (Stage) username.getScene().getWindow();
		primaryStage.setTitle("Registrazione");
		primaryStage.setScene(scene);
		primaryStage.show();
    }

    @FXML
    void registration(ActionEvent event) {
    	if(socket != null && socket.isConnected()) {
    		if(email.getText().isEmpty() || username.getText().isEmpty() || nome.getText().isEmpty() || cognome.getText().isEmpty() || confirmPassword.getText().isEmpty() || password.getText().isEmpty()) {
    			Alert mex = new Alert(AlertType.ERROR);
        		mex.setTitle("Non hai inserito tutti i campi");
        		mex.setContentText("Errore.");
        		mex.showAndWait();
    		}else {
    			if(password.getText().equals(confirmPassword.getText())){
        			if(signUp(username.getText(), confirmPassword.getText(), nome.getText(), cognome.getText(), email.getText())) {
        				FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/gui.fxml"));
        				Parent root = null;
        				try {
        					root = loader.load();
        				} catch (IOException e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				}
        				Scene scene = new Scene(root);
        				Stage primaryStage = (Stage) cambiaLogin.getScene().getWindow();
        				primaryStage.setTitle("Chat Application");
        				primaryStage.setScene(scene);
        				primaryStage.show();
        				
        				Controller control = loader.getController();
        				
        				control.setSocket(socket);
        				control.setOutput(output);	
        				control.setMyUser(io);
        				control.setManager(manager);
        				
        				ListenAndRoutingMessages th = new ListenAndRoutingMessages(socket, manager, control, input);
        			
        				th.start();
        				
        				
        				 // Aggiungi il listener per l'evento di chiusura
        		        primaryStage.setOnCloseRequest((WindowEvent event1) -> {
        		            control.handleWindowClose(event1);
        		        });
        			}else {
        				// messaggio di errore
            			Alert mex = new Alert(AlertType.ERROR);
                		mex.setTitle("Errore");
                		mex.setContentText("Errore generico o user gia inserito con questo username.");
                		mex.showAndWait();
        			}
        		}else {
        			Alert mex = new Alert(AlertType.ERROR);
            		mex.setTitle("Password diversa");
            		mex.setContentText("Errore.");
            		mex.showAndWait();
        		}
    		}
    	}else {
    		Alert mex = new Alert(AlertType.ERROR);
    		mex.setTitle("Non connessi al server");
    		mex.setContentText("Errore.");
    		mex.showAndWait();
    		Stage primaryStage = (Stage) cambiaLogin.getScene().getWindow();
    		primaryStage.close();
    	}
    	}

    private boolean signUp(String username, String password, String name, String cognome, String email) {
    	String initialMessage = MessageBuilder.generateMessageForRegister(username, PasswordHasher.hashPasswordWithSHA256(password), name, cognome, email);
    	if(socket != null && socket.isConnected()) {
    		output.print(initialMessage);
    		output.flush();
    		String response = null;
    		while(socket.isConnected() && response == null) {
				try {
					response = input.readLine();
				} catch (IOException e) {
					System.out.println("Problema lettura risposta da parte del server");
					break;
				}
			}
    		String[] split = MessageBuilder.onlySplitMessage(response);
    		if(split[0].equalsIgnoreCase("true")) {
    			// cambio stage e imposto dati per connessione e user
    			io = new Utente();
    			io.setCognome(cognome);
    			io.setNome(name);
    			io.setEmail(email);
    			io.setUsername(username);
    			io.setPassword(password);
    			return true;
    		}else {
        		return false;
    		}
    		
    	}
		return false;
    }
    @FXML
    void initialize() {
        assert cambiaLogin != null : "fx:id=\"cambiaLogin\" was not injected: check your FXML file 'registration.fxml'.";
        assert cognome != null : "fx:id=\"cognome\" was not injected: check your FXML file 'registration.fxml'.";
        assert confirmPassword != null : "fx:id=\"confirmPassword\" was not injected: check your FXML file 'registration.fxml'.";
        assert email != null : "fx:id=\"email\" was not injected: check your FXML file 'registration.fxml'.";
        assert nome != null : "fx:id=\"nome\" was not injected: check your FXML file 'registration.fxml'.";
        assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'registration.fxml'.";
        assert sendButton != null : "fx:id=\"sendButton\" was not injected: check your FXML file 'registration.fxml'.";
        assert username != null : "fx:id=\"username\" was not injected: check your FXML file 'registration.fxml'.";

    }
}
