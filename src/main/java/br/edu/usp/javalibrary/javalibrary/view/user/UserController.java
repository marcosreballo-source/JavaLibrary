package br.edu.usp.javalibrary.javalibrary.view.user;

import br.edu.usp.javalibrary.javalibrary.service.domains.User;
import br.edu.usp.javalibrary.javalibrary.service.SessionService;
import br.edu.usp.javalibrary.javalibrary.service.repository.UserRepository;
import br.edu.usp.javalibrary.javalibrary.service.repository.LoanRepository;
import br.edu.usp.javalibrary.javalibrary.view.HomeView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserController {

    private final UserRepository userRepository = UserRepository.getInstance();

    @FXML
    private TextField search;

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, UUID> id;

    @FXML
    private TableColumn<User, String> name;

    @FXML
    private TableColumn<User, String> email;

    private final ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        email.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));

        updateList();
        usersTable.setItems(userList);
    }

    private void updateList() {
        userList.clear();
        userList.addAll(userRepository.getUsers());
    }

    private void redirectToHome() {
        try {
            final Stage stage = (Stage) search.getScene().getWindow();
            new HomeView(stage);
        } catch (IOException ignored) {
        }
    }

    @FXML
    private void handleButtonHome(ActionEvent event) {
        redirectToHome();
    }

    private Optional<ButtonType> showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    @FXML
    private void handleButtonAddUser(ActionEvent event) {
        try {
            new CreateUserView();
            updateList();
        } catch (IOException ignored) {
        }
    }

    @FXML
    private void handleButtonUpdateUser(ActionEvent event) {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert(Alert.AlertType.INFORMATION, "Nenhum usuário selecionado", null, "Por favor, selecione um usuário na tabela para editar.");
            return;
        }

        try {
            new CreateUserView(selectedUser);
            updateList();
        } catch (IOException ignored) {
        }
    }

    @FXML
    private void handleButtonRemoveUser(ActionEvent event) {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert(Alert.AlertType.INFORMATION, "Nenhum usuário selecionado", null, "Por favor, selecione um usuário na tabela para remover.");
            return;
        }

        Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION, "Confirmar Exclusão", "Você está prestes a remover um usuário.", "Tem certeza que deseja remover o usuário: \"" + selectedUser.getName() + "\"?");
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (LoanRepository.getInstance().hasLoanByUserId(selectedUser.getId())) {
                showAlert(Alert.AlertType.ERROR, "Erro ao Remover", null, "Não é possível remover o usuário, pois ele possui empréstimos cadastrados.");
                return;
            } else if (SessionService.getInstance().isLogged() && SessionService.getInstance().getUserId().equals(selectedUser.getId())) {
                showAlert(Alert.AlertType.ERROR, "Erro ao Remover", null, "Não é possível remover o usuário atualmente logado.");
                return;

            } else {
                userList.remove(selectedUser);
                userRepository.removeUser(selectedUser.getId());
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", null, "Usuário removido com sucesso!");
            }
        }
    }

    @FXML
    private void handleButtonSearch(ActionEvent event) {
        String query = search.getText();

        if (query == null || query.isBlank()) {
            updateList();
            return;
        }

        String lowerCaseFilter = query.toLowerCase().trim();

        List<User> filteredUsers = userRepository.getUsers()
            .stream()
            .filter(user -> {
                boolean matchesName = user.getName() != null && user.getName().toLowerCase().contains(lowerCaseFilter);
                boolean matchesEmail = user.getEmailAddress() != null && user.getEmailAddress().toLowerCase().contains(lowerCaseFilter);
                return matchesName || matchesEmail;
            })
            .toList();

        userList.clear();
        userList.addAll(filteredUsers);
    }

    @FXML
    private void handleButtonClear(ActionEvent event) {
        search.clear();
        updateList();
    }
}