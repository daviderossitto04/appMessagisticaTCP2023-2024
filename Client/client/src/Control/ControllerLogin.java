package Control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import Model.MessageBuilder;
import Model.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ControllerLogin {

	private Socket socket;
	private PrintWriter output;
	private BufferedReader in;
	private ChatManager manager;
	
	public void setAssetsForConnection(Socket socket, PrintWriter output, BufferedReader in, ChatManager manager) {
		this.socket = socket;
		this.output = output;
		this.in = in;
		this.manager = manager;
	}
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button buttonLogin;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;
    
    @FXML
    private Label cambiaRegistrazione;
    
    Utente io;

    @FXML
    void login(ActionEvent event) {
    	if(socket != null && socket.isConnected()) {
    		if(username.getText().isEmpty() || password.getText().isEmpty()) {
    			Alert alert = new Alert(AlertType.INFORMATION);
        		alert.setTitle("Password o Username vuoti");
        		alert.setContentText("Inserire informazioni.");
        		alert.showAndWait();
    		}else {
    			if(connection(username.getText(), password.getText())) {
    				FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/gui.fxml"));
    				Parent root = null;
    				try {
    					root = loader.load();
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    				Scene scene = new Scene(root);
    				Stage primaryStage = (Stage) username.getScene().getWindow();
    				primaryStage.setTitle("Chat Application");
    				primaryStage.setScene(scene);
    				primaryStage.show();
    				
    				Controller control = loader.getController();
    				
    				control.setSocket(socket);
    				control.setOutput(output);	
    				control.setMyUser(io);
    				control.setManager(manager);
    				
    				ListenAndRoutingMessages th = new ListenAndRoutingMessages(socket, manager, control, in);
    			
    				th.start();
    				
    				
    				 // Aggiungi il listener per l'evento di chiusura
    		        primaryStage.setOnCloseRequest((WindowEvent event1) -> {
    		            control.handleWindowClose(event1);
    		        });
    			
    			}else {
    				Alert alert = new Alert(AlertType.WARNING);
            		alert.setTitle("Credenziali Errate");
            		alert.setContentText("Riprovare.");
            		alert.showAndWait();
            		username.setText("");
            		password.setText("");
    			}
    		}
    	}else {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("Errore connessione, socket chiusa");
    		alert.setContentText("Riavviare applicazione.");
    		alert.showAndWait();
    		Stage stage = (Stage) username.getScene().getWindow();
        	stage.close();
    	}
    }
    
    @FXML
    void cambiaScena(MouseEvent event) {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/registration.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ControllerSignUp controller = loader.getController();
		controller.setAssetsForConnection(socket, output, in, manager);
		Scene scene = new Scene(root);
		Stage primaryStage = (Stage) username.getScene().getWindow();
		primaryStage.setTitle("Registrazione");
		primaryStage.setScene(scene);
		primaryStage.show();
    }
    
    public boolean connection(String username, String password) {
    	System.out.println("username:" + username+ "password:" + password);
		if(socket != null && socket.isConnected()) {
			String request = MessageBuilder.generateMessageForLogin(username, password);
			output.print(request);
			output.flush();
			String response = null;
			
			while(socket.isConnected() && response == null) {
				try {
					response = in.readLine();
				} catch (IOException e) {
					System.out.println("Problema lettura risposta da parte del server");
					break;
				}
			}
			if(response != null) {
				String[] split = MessageBuilder.onlySplitMessage(response);
				System.out.println(split[0]);
				if(split[0].equalsIgnoreCase("true")) {
					io = MessageBuilder.generateUserFromInitialMessage(split);
					System.out.println("Split '0 è uguale a true");
					return true;
				}else {
					System.out.println("Split '0 non è uguale a true");
					System.out.println(split[0]);
					return false;
				}
			}else {
				System.out.println("Response == null");
				Alert alert = new Alert(AlertType.WARNING);
	    		alert.setTitle("Errore troppe volte");
	    		alert.setContentText("Riavviare applicazione.");
	    		alert.showAndWait();
				Stage primaryStage = (Stage) buttonLogin.getScene().getWindow();
				primaryStage.close();
				return false;
			}
		}else {
			Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Errore connessione al server");
    		alert.setContentText("Chiusura Applicazione");
    		alert.showAndWait();
    		Stage stage = (Stage) buttonLogin.getScene().getWindow();
        	stage.close();
			return false;
		}
    	
    
    }

    @FXML
    void initialize() {
        assert buttonLogin != null : "fx:id=\"buttonLogin\" was not injected: check your FXML file 'login.fxml'.";
        assert cambiaRegistrazione != null : "fx:id=\"cambiaRegistrazione\" was not injected: check your FXML file 'login.fxml'.";
        assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'login.fxml'.";
        assert username != null : "fx:id=\"username\" was not injected: check your FXML file 'login.fxml'.";

    }

}
