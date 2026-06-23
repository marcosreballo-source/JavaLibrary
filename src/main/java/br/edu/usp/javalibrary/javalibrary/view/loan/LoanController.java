package br.edu.usp.javalibrary.javalibrary.view.loan;

import br.edu.usp.javalibrary.javalibrary.service.domains.Loan;
import br.edu.usp.javalibrary.javalibrary.service.repository.BookRepository;
import br.edu.usp.javalibrary.javalibrary.service.repository.LoanRepository;
import br.edu.usp.javalibrary.javalibrary.service.repository.UserRepository;
import br.edu.usp.javalibrary.javalibrary.view.HomeView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LoanController {

    private final LoanRepository loanRepository = LoanRepository.getInstance();

    @FXML
    private TextField search;

    @FXML
    private TableView<Loan> loansTable;

    @FXML
    private TableColumn<Loan, UUID> id;

    @FXML
    private TableColumn<Loan, String> isbn;

    @FXML
    private TableColumn<Loan, String> userId;

    @FXML
    private TableColumn<Loan, LocalDateTime> start;

    @FXML
    private TableColumn<Loan, LocalDateTime> endPrevision;

    @FXML
    private TableColumn<Loan, LocalDateTime> end;

    private final ObservableList<Loan> loanList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        isbn.setCellValueFactory(cellData -> {
            String bookIsbn = cellData.getValue().getBookISBN();
            return new SimpleStringProperty(BookRepository.getInstance().getBook(bookIsbn)
                    .map(b -> b.getTitle()).orElse("Livro Desconhecido"));
        });

        userId.setCellValueFactory(cellData -> {
            UUID id = cellData.getValue().getUserID();
            return new SimpleStringProperty(UserRepository.getInstance().getUser(id)
                    .map(u -> u.getName()).orElse("Usuário Desconhecido"));
        });

        start.setCellValueFactory(new PropertyValueFactory<>("start"));
        endPrevision.setCellValueFactory(new PropertyValueFactory<>("endPrevision"));
        end.setCellValueFactory(new PropertyValueFactory<>("end"));

        updateList();
        loansTable.setItems(loanList);
    }

    private void updateList() {
        loanList.clear();
        loanList.addAll(loanRepository.getLoans());
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
    private void handleButtonAddLoan(ActionEvent event) {
        try {
            new CreateLoanView();
            updateList();
        } catch (IOException ignored) {
        }
    }

    @FXML
    private void handleButtonUpdateLoan(ActionEvent event) {
        Loan selectedLoan = loansTable.getSelectionModel().getSelectedItem();

        if (selectedLoan == null) {
            showAlert(Alert.AlertType.INFORMATION, "Nenhum empréstimo selecionado", null, "Selecione um empréstimo para editar.");
            return;
        }

        try {
            new CreateLoanView(selectedLoan);
            updateList();
        } catch (IOException ignored) {
        }
    }

    @FXML
    private void handleButtonRemoveLoan(ActionEvent event) {
        Loan selectedLoan = loansTable.getSelectionModel().getSelectedItem();

        if (selectedLoan == null) {
            showAlert(Alert.AlertType.INFORMATION, "Nenhum registro selecionado", null, "Selecione um registro na tabela para remover.");
            return;
        }

        Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION, "Confirmar Exclusão", "Remover registro de empréstimo", "Tem certeza que deseja remover o registro ID: " + selectedLoan.getId() + "?");

        if (result.isPresent() && result.get() == ButtonType.OK) {

            if (!BookRepository.getInstance().returnBook(selectedLoan.getBookISBN())) {
                showAlert(Alert.AlertType.ERROR, "Erro ao Devolver", null, "Não foi possível devolver o livro com ISBN: " + selectedLoan.getBookISBN());
                return;
            } else {
                loanList.remove(selectedLoan);
                loanRepository.removeLoan(selectedLoan.getId());
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", null, "Registro removido com sucesso!");

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

        String filter = query.trim();

        List<Loan> filteredLoans = loanRepository.getLoans()
            .stream()
            .filter(loan -> {
                boolean matchesIsbn = loan.getBookISBN() != null && loan.getBookISBN().contains(filter);
                boolean matchesUser = loan.getUserID() != null && loan.getUserID().toString().contains(filter);
                return matchesIsbn || matchesUser;
            })
            .toList();

        loanList.clear();
        loanList.addAll(filteredLoans);
    }

    @FXML
    private void handleButtonClear(ActionEvent event) {
        search.clear();
        updateList();
    }
}