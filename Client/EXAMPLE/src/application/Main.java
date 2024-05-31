package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import Control.ChatManager;
import Control.Client;
import Control.ControllerLogin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/login.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("/assets/iconApp.png"));
        ControllerLogin controller = loader.getController();

        Client client = new Client();
        primaryStage.getScene().getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());
        primaryStage.show();
        try {
            Socket socket = client.connect();
            PrintWriter output = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ChatManager manager = new ChatManager();
            controller.setAssetsForConnection(socket, output, in, manager);
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Errore apertura connessione con il server!");
            alert.setContentText("Il server è offline");
            alert.showAndWait();
            primaryStage.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
