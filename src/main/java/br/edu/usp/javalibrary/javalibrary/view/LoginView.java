package br.edu.usp.javalibrary.javalibrary.view;

// import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginView  {

    public LoginView(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/usp/javalibrary/javalibrary/login.fxml"));
        Scene scene = new Scene(loader.load(), 400, 300);
        stage.setScene(scene);
        stage.setTitle("Entrar");
        stage.show();
    }
}
