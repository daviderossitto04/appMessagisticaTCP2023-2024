package Control;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ControllerProfile {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label cognome;

    @FXML
    private Label email;

    @FXML
    private ImageView imageView;

    @FXML
    private Label nome;

    @FXML
    private Label username;

    public void setText(String username, String nome, String cognome, String email) {
    	this.username.setText(username);
    	this.nome.setText(nome);
    	this.cognome.setText(cognome);
    	this.email.setText(email);
    }
    
    
    @FXML
    void initialize() {
        assert cognome != null : "fx:id=\"cognome\" was not injected: check your FXML file 'profile.fxml'.";
        assert email != null : "fx:id=\"email\" was not injected: check your FXML file 'profile.fxml'.";
        assert imageView != null : "fx:id=\"imageView\" was not injected: check your FXML file 'profile.fxml'.";
        assert nome != null : "fx:id=\"nome\" was not injected: check your FXML file 'profile.fxml'.";
        assert username != null : "fx:id=\"username\" was not injected: check your FXML file 'profile.fxml'.";

    }

}
