import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PaymentScreen {
    public void show() {
        Stage stage = new Stage();

        ComboBox<Student> studentBox = new ComboBox<>(DataStore.students);
        studentBox.setPromptText("Select Student");

        TextField amountField = new TextField();
        amountField.setPromptText("Payment Amount");

        Label message = new Label();

        ListView<Payment> paymentList = new ListView<>(DataStore.payments);

        Button addBtn = new Button("Record Payment");
        addBtn.setOnAction(e -> {
            Student student = studentBox.getValue();
            String amountText = amountField.getText().trim();

            if (student == null || amountText.isEmpty()) {
                message.setText("Please complete all fields.");
                return;
            }

            try {
                double amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    message.setText("Amount must be greater than 0.");
                    return;
                }

                DataStore.payments.add(new Payment(student, amount));
                message.setText("Payment recorded.");
                amountField.clear();
            } catch (NumberFormatException ex) {
                message.setText("Amount must be numeric.");
            }
        });

        VBox root = new VBox(10,
                new Label("Fee Management"),
                studentBox, amountField,
                addBtn, message,
                new Label("Payments:"), paymentList);

        root.setPadding(new Insets(15));

        stage.setScene(new Scene(root, 450, 500));
        stage.setTitle("Payments");
        stage.show();
    }
}