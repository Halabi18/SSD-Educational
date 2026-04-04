import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen {

    private int attempts = 0;

    public void show(Stage stage) {
        Label title = new Label("Educational Course Management System");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Label message = new Label();

        Button loginBtn = new Button("Login");
        loginBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            // empty input check
            if (username.isEmpty() || password.isEmpty()) {
                message.setText("Please enter username and password.");
                return;
            }

            // lock after 3 failed attempts
            if (attempts >= 3) {
                message.setText("Too many failed attempts. Please restart.");
                loginBtn.setDisable(true);
                return;
            }

            // validate against DB (password is hashed inside validateLogin)
            if (DBHelper.validateLogin(username, password)) {
                new Dashboard().show(stage);
            } else {
                attempts++;
                message.setText("Invalid username or password. Attempts: " + attempts + "/3");
            }
        });

        VBox root = new VBox(10, title, usernameField, passwordField, loginBtn, message);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 420, 260);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}
