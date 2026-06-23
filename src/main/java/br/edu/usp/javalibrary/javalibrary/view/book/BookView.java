package br.edu.usp.javalibrary.javalibrary.view.book;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BookView {
    public BookView(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/usp/javalibrary/javalibrary/book.fxml"));
        Scene scene = new Scene(loader.load(), 740, 440);
        stage.setScene(scene);
        stage.setTitle("Book");
        stage.show();
    }
}
