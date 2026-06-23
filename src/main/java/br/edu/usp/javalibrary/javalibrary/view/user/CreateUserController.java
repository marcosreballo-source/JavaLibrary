package br.edu.usp.javalibrary.javalibrary.view.user;

import br.edu.usp.javalibrary.javalibrary.service.domains.User;
import br.edu.usp.javalibrary.javalibrary.service.repository.UserRepository;
import br.edu.usp.javalibrary.javalibrary.service.utils.Cripto;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateUserController {

    // Regex importadas de SignupController
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    private final UserRepository repository = UserRepository.getInstance();
    private User user;

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private Label lblPassword;
    @FXML
    private Label lblConfirmPassword;
    @FXML
    private PasswordField txtConfirmPassword;
    @FXML
    private RowConstraints rowConfirmPassword;

    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    public void setUser(User user) {
        this.user = user;
        txtName.setText(user.getName());
        txtEmail.setText(user.getEmailAddress());
        
        lblPassword.setText("Nova Senha (opcional):");
        lblConfirmPassword.setVisible(true);
        txtConfirmPassword.setVisible(true);
    }

    @FXML
    public void initialize() {
        if (user == null) {
            user = new User();
            lblPassword.setText("Senha:");
            lblConfirmPassword.setVisible(false);
            txtConfirmPassword.setVisible(false);
        }
    }

    @FXML
    private void handleSave() {
        if (isInputInvalid()) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Por favor, preencha todos os campos obrigatórios.");
            return;
        }

        try {
            if (user.getId() == null) {
                // Validações Regex para criação de novo usuário
                if (isPatternMatches(txtEmail.getText().trim(), EMAIL_REGEX)) {
                    showAlert(Alert.AlertType.ERROR, "Erro de Validação", "E-mail inválido.");
                    return;
                }
                if (isPatternMatches(txtPassword.getText(), PASSWORD_REGEX)) {
                    showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Senha inválida. A senha deve conter pelo menos 8 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais.");
                    return;
                }

                String encryptedPassword = Cripto.getMD5(txtPassword.getText());
                user = new User(txtName.getText(), txtEmail.getText(), encryptedPassword);
            } 
            else {

                // Validação do e-mail na edição
                if (isPatternMatches(txtEmail.getText().trim(), EMAIL_REGEX)) {
                    showAlert(Alert.AlertType.ERROR, "Erro de Validação", "E-mail inválido.");
                    return;
                }

                // Validação da nova senha (apenas se não estiver em branco)
                if (!txtPassword.getText().isBlank() && isPatternMatches(txtPassword.getText(), PASSWORD_REGEX)) {
                    showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Nova senha inválida. A senha deve conter pelo menos 8 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais.");
                    return;
                }

                String confirmPasswordEncrypted = Cripto.getMD5(txtConfirmPassword.getText());
                if (!user.isPasswordCorrect(confirmPasswordEncrypted)) {
                    showAlert(Alert.AlertType.ERROR, "Erro de Segurança", "A senha atual digitada está incorreta.");
                    return;
                }

                user.setName(txtName.getText());
                user.setEmailAddress(txtEmail.getText());
                
                if (!txtPassword.getText().isBlank()) {
                    String newEncryptedPassword = Cripto.getMD5(txtPassword.getText());
                    user.setPassword(newEncryptedPassword);
                }
            }

            if (repository.saveUser(user)) {
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Usuário salvo com sucesso!");
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro ao Salvar", "Houve um erro ao salvar o usuário.");
            }

        } catch (NoSuchAlgorithmException e) {
            showAlert(Alert.AlertType.ERROR, "Erro Interno", "Erro ao processar a criptografia da senha.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private boolean isInputInvalid() {
        if (user.getId() == null) {
            return txtName.getText().isBlank() || txtEmail.getText().isBlank() || txtPassword.getText().isBlank();
        }
        return txtName.getText().isBlank() || txtEmail.getText().isBlank() || txtConfirmPassword.getText().isBlank();
    }

    // Método de validação igual ao do SignupController
    private boolean isPatternMatches(String text, String regex) {
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(text);
        return !matcher.matches();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}