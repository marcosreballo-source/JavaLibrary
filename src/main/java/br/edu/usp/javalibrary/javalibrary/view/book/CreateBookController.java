package br.edu.usp.javalibrary.javalibrary.view.book;

import br.edu.usp.javalibrary.javalibrary.service.domains.Book;
import br.edu.usp.javalibrary.javalibrary.service.repository.BookRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.function.UnaryOperator;

public class CreateBookController {

    private final BookRepository repository = BookRepository.getInstance();

    private Book book;

    @FXML
    private TextField txtIsbn;
    @FXML
    private TextField txtTitle;
    @FXML
    private TextArea txtDescription;
    @FXML
    private TextField txtPublisher;
    @FXML
    private TextField txtAuthor;
    @FXML
    private TextField txtCategory;
    @FXML
    private TextField txtCopies;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    public void setBook(Book book) {
        this.book = book;

        txtIsbn.setText(book.getIsbn());
        txtTitle.setText(book.getTitle());
        txtDescription.setText(book.getDescription());
        txtPublisher.setText(book.getPublisher());
        txtAuthor.setText(book.getAuthor());
        txtCategory.setText(book.getCategory());
        txtCopies.setText(String.valueOf(book.getCopiesCount()));

        txtIsbn.setEditable(false);
    }


    @FXML
    public void initialize() {
        // Filtro regex para permitir APENAS dígitos numéricos nos campos específicos
        UnaryOperator<TextFormatter.Change> filterNumbersOnly = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null; // Ignora a alteração se não for número
        };

        // Aplica as restrições numéricas aos campos solicitados
        txtIsbn.setTextFormatter(new TextFormatter<>(filterNumbersOnly));
        txtCopies.setTextFormatter(new TextFormatter<>(filterNumbersOnly));
        if (book == null) book = new Book();
    }

    @FXML
    private void handleSave() {
        if (isInputInvalid()) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Por favor, preencha todos os campos.");
            return;
        }

        book.setIsbn(txtIsbn.getText());
        book.setTitle(txtTitle.getText());
        book.setDescription(txtDescription.getText());
        book.setPublisher(txtPublisher.getText());
        book.setAuthor(txtAuthor.getText());
        book.setCategory(txtCategory.getText());
        book.setCopiesCount(txtCopies.getText().isEmpty() ? 0 : Integer.parseInt(txtCopies.getText()));

        if (repository.saveBook(book)) {
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Dados salvos com sucesso!");
            closeWindow();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro ao Salvar", "Houve um erro ao salvar.");
        }

    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private boolean isInputInvalid() {
        return txtIsbn.getText().isBlank() ||
                txtTitle.getText().isBlank() ||
                txtDescription.getText().isBlank() ||
                txtPublisher.getText().isBlank() ||
                txtAuthor.getText().isBlank() ||
                txtCategory.getText().isBlank() ||
                txtCopies.getText().isBlank();
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
