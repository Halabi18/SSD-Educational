import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EnrollmentScreen {

    public void show() {
        Stage stage = new Stage();

        ComboBox<Student> studentBox = new ComboBox<>(DataStore.students);
        studentBox.setPromptText("Select Student");

        ComboBox<Course> courseBox = new ComboBox<>(DataStore.courses);
        courseBox.setPromptText("Select Course");

        Label message = new Label();

        ListView<Enrollment> enrollmentList = new ListView<>(DataStore.enrollments);

        Button enrollBtn = new Button("Enroll");
        enrollBtn.setOnAction(e -> {
            Student student = studentBox.getValue();
            Course course = courseBox.getValue();

            // Validation
            if (student == null || course == null) {
                message.setText("Select student and course.");
                return;
            }

            // Duplicate check using DataStore helper
            if (DataStore.isAlreadyEnrolled(student, course)) {
                message.setText("Student already enrolled.");
                return;
            }

            Enrollment enrollment = new Enrollment(student, course);

            if (!enrollment.isValid()) {
                message.setText("Invalid enrollment data.");
                return;
            }

            DataStore.enrollments.add(enrollment);
            message.setText("Enrollment successful.");

            // Optional clear after success
            studentBox.setValue(null);
            courseBox.setValue(null);
        });

        VBox root = new VBox(10,
                new Label("Enrollment"),
                studentBox, courseBox,
                enrollBtn, message,
                new Label("Enrollment List:"), enrollmentList);

        root.setPadding(new Insets(15));

        stage.setScene(new Scene(root, 450, 500));
        stage.setTitle("Enrollment");
        stage.show();
    }
}