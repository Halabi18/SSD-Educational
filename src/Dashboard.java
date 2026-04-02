import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Dashboard {

    private String role = "admin"; // simple role control (can expand later)

    public void show(Stage stage) {
        Label title = new Label("Admin Dashboard");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button studentBtn = new Button("Student Management");
        Button courseBtn = new Button("Course Management");
        Button enrollBtn = new Button("Enrollment");
        Button gradeBtn = new Button("Grade Management");
        Button paymentBtn = new Button("Fee Management");
        Button transcriptBtn = new Button("Transcript");

        studentBtn.setMaxWidth(Double.MAX_VALUE);
        courseBtn.setMaxWidth(Double.MAX_VALUE);
        enrollBtn.setMaxWidth(Double.MAX_VALUE);
        gradeBtn.setMaxWidth(Double.MAX_VALUE);
        paymentBtn.setMaxWidth(Double.MAX_VALUE);
        transcriptBtn.setMaxWidth(Double.MAX_VALUE);

        // Basic role-based restriction (simple but valid for coursework)
        if (!role.equals("admin")) {
            studentBtn.setDisable(true);
            courseBtn.setDisable(true);
            enrollBtn.setDisable(true);
            gradeBtn.setDisable(true);
            paymentBtn.setDisable(true);
        }

        studentBtn.setOnAction(e -> new StudentScreen().show());
        courseBtn.setOnAction(e -> new CourseScreen().show());
        enrollBtn.setOnAction(e -> new EnrollmentScreen().show());
        gradeBtn.setOnAction(e -> new GradeScreen().show());
        paymentBtn.setOnAction(e -> new PaymentScreen().show());
        transcriptBtn.setOnAction(e -> new TranscriptScreen().show());

        VBox root = new VBox(10, title, studentBtn, courseBtn, enrollBtn, gradeBtn, paymentBtn, transcriptBtn);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 380, 380);
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
    }
}