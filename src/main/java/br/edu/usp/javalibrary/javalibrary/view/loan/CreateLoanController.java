package br.edu.usp.javalibrary.javalibrary.view.loan;

import br.edu.usp.javalibrary.javalibrary.service.domains.Loan;
import br.edu.usp.javalibrary.javalibrary.service.repository.LoanRepository;
import br.edu.usp.javalibrary.javalibrary.service.repository.UserRepository; // Importado
import br.edu.usp.javalibrary.javalibrary.service.repository.BookRepository; // Importado
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class CreateLoanController {

    private final LoanRepository repository = LoanRepository.getInstance();
    // Instâncias dos repositórios para validação
    private final UserRepository userRepository = UserRepository.getInstance();
    private final BookRepository bookRepository = BookRepository.getInstance();

    private Loan loan;

    @FXML
    private TextField txtIsbn;
    @FXML
    private TextField txtUserId;
    @FXML
    private DatePicker dpStart;
    @FXML
    private DatePicker dpEndPrevision;
    @FXML
    private DatePicker dpEnd;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    public void setLoan(Loan loan) {
        this.loan = loan;
        
        txtIsbn.setText(loan.getBookISBN());
        txtIsbn.setDisable(true);
        txtUserId.setText(loan.getUserID().toString());
        txtUserId.setDisable(true);

        if (loan.getStart() != null) dpStart.setValue(loan.getStart().toLocalDate());
        dpStart.setDisable(true);
        if (loan.getEndPrevision() != null) dpEndPrevision.setValue(loan.getEndPrevision().toLocalDate());
        dpEndPrevision.setDisable(true);

        if (loan.getEnd() != null) dpEnd.setValue(loan.getEnd().toLocalDate());
        dpEnd.setDisable(false);
        dpEnd.setEditable(true);
    }

    @FXML
    public void initialize() {
        dpStart.setValue(LocalDate.now());
        dpEndPrevision.setValue(LocalDate.now().plusDays(14)); // Padrão: 14 dias de prazo
    }

    @FXML
    private void handleSave() {
        if (isInputInvalid()) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Preencha o ISBN, ID do Usuário e as Datas.");
            return;
        }

        try {
            UUID userId = UUID.fromString(txtUserId.getText().trim());
            String isbn = txtIsbn.getText().trim();

            if (userRepository.getUser(userId).isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Usuário Não Encontrado", "O ID do usuário informado não existe no sistema.");
                return;
            }

            if (bookRepository.getBook(isbn).isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Livro Não Encontrado", "O ISBN informado não corresponde a nenhum livro cadastrado.");
                return;
            }

            LocalDateTime start = dpStart.getValue().atStartOfDay();
            LocalDateTime endPrevision = dpEndPrevision.getValue().atStartOfDay();
            LocalDateTime end = dpEnd.getValue() != null ? dpEnd.getValue().atStartOfDay() : null;

            if (loan == null || loan.getId() == null) {
                loan = new Loan(UUID.randomUUID(), isbn, userId, start, endPrevision);
            } else {
                loan.setBookISBN(isbn);
                loan.setUserID(userId);
                loan.setStart(start);
                loan.setEndPrevision(endPrevision);
                loan.setEnd(end);
            }

            try {
                bookRepository.loanBook(isbn);
            } catch (br.edu.usp.javalibrary.javalibrary.service.exceptions.BookNotAvailableException e) {
                showAlert(Alert.AlertType.ERROR, "Erro ao Emprestar", e.getMessage());
                return;
            }

            if (repository.saveLoan(loan)) {
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Empréstimo salvo com sucesso!");
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro ao Salvar", "Ocorreu um problema de persistência.");
            }

        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Formato Inválido", "O ID do usuário precisa ser um UUID válido.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private boolean isInputInvalid() {
        return txtIsbn.getText().isBlank() || txtUserId.getText().isBlank() ||
               dpStart.getValue() == null || dpEndPrevision.getValue() == null;
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