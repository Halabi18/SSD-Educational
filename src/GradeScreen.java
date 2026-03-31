import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GradeScreen {
    public void show() {
        Stage stage = new Stage();

        ComboBox<Student> studentBox = new ComboBox<>(DataStore.students);
        studentBox.setPromptText("Select Student");

        ComboBox<Course> courseBox = new ComboBox<>(DataStore.courses);
        courseBox.setPromptText("Select Course");

        TextField gradeField = new TextField();
        gradeField.setPromptText("Grade (A/B/C/D/F)");

        Label message = new Label();
        Label gpaLabel = new Label("GPA: N/A");

        ListView<Grade> gradeList = new ListView<>(DataStore.grades);

        Button saveBtn = new Button("Save Grade");
        saveBtn.setOnAction(e -> {
            Student student = studentBox.getValue();
            Course course = courseBox.getValue();
            String grade = gradeField.getText().trim().toUpperCase();

            if (student == null || course == null || grade.isEmpty()) {
                message.setText("Please complete all fields.");
                return;
            }

            if (!grade.matches("[ABCDF]")) {
                message.setText("Enter valid grade.");
                return;
            }

            DataStore.grades.add(new Grade(student, course, grade));
            message.setText("Grade saved.");
            gpaLabel.setText("GPA: " + String.format("%.2f", calculateGPA(student)));
            gradeField.clear();
        });

        VBox root = new VBox(10,
                new Label("Grade Management"),
                studentBox, courseBox, gradeField,
                saveBtn, message, gpaLabel,
                new Label("Grades:"), gradeList);

        root.setPadding(new Insets(15));

        stage.setScene(new Scene(root, 450, 550));
        stage.setTitle("Grades");
        stage.show();
    }

    private double calculateGPA(Student student) {
        double total = 0;
        int count = 0;

        for (Grade g : DataStore.grades) {
            if (g.getStudent().equals(student)) {
                total += g.getGradePoint();
                count++;
            }
        }

        return count == 0 ? 0.0 : total / count;
    }
}