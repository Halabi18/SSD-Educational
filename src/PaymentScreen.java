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

                // reasonable max amount check
                if (amount > 100000) {
                    message.setText("Amount seems too large. Please check.");
                    return;
                }

                Payment payment = new Payment(student, amount);

                if (!payment.isValid()) {
                    message.setText("Invalid payment data.");
                    return;
                }

                // save to DB
                boolean saved = DBHelper.addPayment(student, amount);
                if (!saved) {
                    message.setText("Error saving payment.");
                    return;
                }

                DataStore.payments.add(payment);
                message.setText("Payment recorded successfully.");

                amountField.clear();
                studentBox.setValue(null);

            } catch (NumberFormatException ex) {
                message.setText("Amount must be a valid number.");
            }
        });

        VBox root = new VBox(10,
                new Label("Fee Management"),
                studentBox, amountField,
                addBtn, message,
                new Label("Payment History:"), paymentList);

        root.setPadding(new Insets(15));

        stage.setScene(new Scene(root, 450, 500));
        stage.setTitle("Payments");
        stage.show();
    }
}
