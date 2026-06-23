package br.edu.usp.javalibrary.javalibrary.view;


import br.edu.usp.javalibrary.javalibrary.service.domains.User;
import br.edu.usp.javalibrary.javalibrary.service.repository.UserRepository;
import br.edu.usp.javalibrary.javalibrary.service.utils.Cripto;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupController {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    @FXML
    private TextField emailField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField passwordField;

    @FXML
    public void onSubmit() {
        final String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showErrorDialog("Preencha o campo nome");
            return;
        }


        final String email = emailField.getText().trim();
        if (isPatternMatches(email, EMAIL_REGEX)) {
            showErrorDialog("E-mail inválido");
            return;
        }

        final String password = passwordField.getText();
        if (isPatternMatches(password, PASSWORD_REGEX)) {
            showErrorDialog("Senha inválida: a senha deve conter pelo menos 8 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais");
            return;
        }

        try {
            final String criptoPassword = Cripto.getMD5(password);
            final User user = new User(name, email, criptoPassword);
            if (!UserRepository.getInstance().saveUser(user)) {
                showErrorDialog("Erro ao salvar usuário");
                return;
            }
        } catch (NoSuchAlgorithmException e) {
            showErrorDialog("Erro interno");
            return;
        }

        try {
            final Stage stage = (Stage) emailField.getScene().getWindow();
            new LoginView(stage);
        } catch (IOException ignored) {}
    }

    @FXML
    public void onBack() {
        try {
            final Stage stage = (Stage) emailField.getScene().getWindow();
            new LoginView(stage);
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
