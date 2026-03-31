import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen {

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

            if (username.equals("admin") && password.equals("1234")) {
                new Dashboard().show(stage);
            } else {
                message.setText("Invalid username or password.");
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