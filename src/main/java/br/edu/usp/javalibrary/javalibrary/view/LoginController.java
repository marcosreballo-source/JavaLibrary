package br.edu.usp.javalibrary.javalibrary.view;

import br.edu.usp.javalibrary.javalibrary.service.SessionService;
import br.edu.usp.javalibrary.javalibrary.service.utils.Cripto;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    public void onSubmit() {
        final String email = emailField.getText();
        if (isPatternMatches(email, EMAIL_REGEX)) {
            showErrorDialog("E-mail inválido");
            return;
        }

        final String password = passwordField.getText();
        if (isPatternMatches(password, PASSWORD_REGEX)) {
            showErrorDialog("Senha inválida");
            return;
        }

        try {
            final String criptoPassword = Cripto.getMD5(password);
            if (!SessionService.getInstance().login(email, criptoPassword)) {
                showErrorDialog("E-mail ou senha incorretos!");
                return;
            }
        } catch (NoSuchAlgorithmException e) {
            showErrorDialog("Erro interno, tente mais tarde");
            return;
        }

        try {
            final Stage stage = (Stage) emailField.getScene().getWindow();
            new HomeView(stage);
        } catch (IOException ignored) {}
    }

    @FXML
    public void onSignup() {
        try {
            final Stage stage = (Stage) emailField.getScene().getWindow();
            new SignupView(stage);
        } catch (IOException ignored) {}
    }

    private boolean isPatternMatches(String text, String regex) {
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(text);
        return !matcher.matches();
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Falha no Login");
        alert.setHeaderText(message);
        alert.setResizable(false);
        alert.showAndWait();
    }
}
