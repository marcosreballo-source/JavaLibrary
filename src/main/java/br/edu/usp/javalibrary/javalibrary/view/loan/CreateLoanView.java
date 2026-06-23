package br.edu.usp.javalibrary.javalibrary.view.loan;

import br.edu.usp.javalibrary.javalibrary.service.domains.Loan;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateLoanView {
    public CreateLoanView() throws IOException {
        openWindow(null);
    }

    public CreateLoanView(Loan loan) throws IOException {
        openWindow(loan);
    }

    private void openWindow(Loan loan) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/edu/usp/javalibrary/javalibrary/loan-create.fxml"));
        Scene scene = new Scene(loader.load());

        if (loan != null){
            final CreateLoanController controller = loader.getController();
            controller.setLoan(loan);
        }

        final Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Creating Loan");
        stage.showAndWait();
    }
}