package br.edu.usp.javalibrary.javalibrary.view.user;

import br.edu.usp.javalibrary.javalibrary.service.domains.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateUserView {
    public CreateUserView() throws IOException {
        openWindow(null);
    }

    public CreateUserView(User user) throws IOException {
        openWindow(user);
    }

    private void openWindow(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/usp/javalibrary/javalibrary/user-create.fxml"));
        Scene scene = new Scene(loader.load());

        if (user != null){
            final CreateUserController controller = loader.getController();
            controller.setUser(user);
        }

        final Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Creating User");
        stage.showAndWait();
    }
}