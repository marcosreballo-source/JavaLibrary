package br.edu.usp.javalibrary.javalibrary.view.loan;

import br.edu.usp.javalibrary.javalibrary.service.domains.Loan;
import br.edu.usp.javalibrary.javalibrary.service.domains.Book;
import br.edu.usp.javalibrary.javalibrary.service.domains.User;
import br.edu.usp.javalibrary.javalibrary.service.repository.LoanRepository;
import br.edu.usp.javalibrary.javalibrary.service.repository.UserRepository;
import br.edu.usp.javalibrary.javalibrary.service.repository.BookRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class CreateLoanController {

    private final LoanRepository repository = LoanRepository.getInstance();
    private final UserRepository userRepository = UserRepository.getInstance();
    private final BookRepository bookRepository = BookRepository.getInstance();

    private Loan loan;

    @FXML
    private ComboBox<Book> cmbBook;
    @FXML
    private ComboBox<User> cmbUser;
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
        
        // Find and select the book
        bookRepository.getBook(loan.getBookISBN()).ifPresent(book -> cmbBook.setValue(book));
        cmbBook.setDisable(true);
        
        // Find and select the user
        userRepository.getUser(loan.getUserID()).ifPresent(user -> cmbUser.setValue(user));
        cmbUser.setDisable(true);

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

        // Populate ComboBoxes
        cmbBook.setItems(FXCollections.observableArrayList(bookRepository.getBooks()));
        cmbUser.setItems(FXCollections.observableArrayList(userRepository.getUsers()));

        // Set Converters for nice rendering in ComboBox
        cmbBook.setConverter(new StringConverter<Book>() {
            @Override
            public String toString(Book book) {
                return book == null ? "" : book.getTitle() + " (" + book.getIsbn() + ")";
            }

            @Override
            public Book fromString(String string) {
                return null;
            }
        });

        cmbUser.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                return user == null ? "" : user.getName() + " (" + user.getEmailAddress() + ")";
            }

            @Override
            public User fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    private void handleSave() {
        if (isInputInvalid()) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Selecione o Livro, o Usuário e as Datas.");
            return;
        }

        User selectedUser = cmbUser.getValue();
        Book selectedBook = cmbBook.getValue();

        UUID userId = selectedUser.getId();
        String isbn = selectedBook.getIsbn();

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
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private boolean isInputInvalid() {
        return cmbBook.getValue() == null || cmbUser.getValue() == null ||
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