package br.edu.usp.javalibrary.javalibrary.view.book;

import br.edu.usp.javalibrary.javalibrary.service.domains.Book;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateBookView {
    public CreateBookView() throws IOException {
        openWindow(null);
    }

    public CreateBookView(Book book) throws IOException {
        openWindow(book);
    }

    private void openWindow(Book book) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/usp/javalibrary/javalibrary/book-create.fxml"));
        Scene scene = new Scene(loader.load());

        if (book != null){
            final CreateBookController controller = loader.getController();
            controller.setBook(book);
        }

        final Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Creating Book");
        stage.showAndWait();
    }
}
