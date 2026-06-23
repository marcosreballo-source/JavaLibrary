package br.edu.usp.javalibrary.javalibrary.view;

import java.io.IOException;
import br.edu.usp.javalibrary.javalibrary.service.SessionService;
import br.edu.usp.javalibrary.javalibrary.view.book.BookView;
import br.edu.usp.javalibrary.javalibrary.view.user.UserView;
import br.edu.usp.javalibrary.javalibrary.view.loan.LoanView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HomeController {
    
    private final SessionService session = SessionService.getInstance();

    @FXML
    private Label username;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            if (session.isLogged()) {
                username.setText("Bem-vindo, " + session.getUsername() + "!");
            } else {
                redirectToLogin();
            }
        });
    }

    private void redirectToLogin() {
        try {
            final Stage stage = (Stage) username.getScene().getWindow();
            new LoginView(stage);
        } catch (IOException e) {}
    }

    @FXML
    private void handleButtonLogout(ActionEvent event) {
        session.logout();
        redirectToLogin();
    }

    @FXML
    private void handleButtonUsers(ActionEvent event) {
        try {
            final Stage stage = (Stage) username.getScene().getWindow();
            new UserView(stage); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonBooks(ActionEvent event) {
        try {
            final Stage stage = (Stage) username.getScene().getWindow();
            new BookView(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonLoans(ActionEvent event) {
        try {
            final Stage stage = (Stage) username.getScene().getWindow();
            new LoanView(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}