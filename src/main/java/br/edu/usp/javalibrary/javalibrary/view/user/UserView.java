package br.edu.usp.javalibrary.javalibrary.view.user;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UserView {
    public UserView(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/usp/javalibrary/javalibrary/user.fxml"));
        Scene scene = new Scene(loader.load(), 740, 440);
        stage.setScene(scene);
        stage.setTitle("User");
        stage.show();
    }
}