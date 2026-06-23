module br.edu.usp.javalibrary.javalibrary {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    exports br.edu.usp.javalibrary.javalibrary;

    opens br.edu.usp.javalibrary.javalibrary.view to javafx.fxml;
    opens br.edu.usp.javalibrary.javalibrary.service.domains to com.google.gson, javafx.base, javafx.fxml;
    opens br.edu.usp.javalibrary.javalibrary.view.book to javafx.fxml;
    opens br.edu.usp.javalibrary.javalibrary.view.user to javafx.fxml;
    opens br.edu.usp.javalibrary.javalibrary.view.loan to javafx.fxml;
}