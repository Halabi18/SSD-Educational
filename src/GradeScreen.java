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
                message.setText("Enter valid grade (A, B, C, D, or F).");
                return;
            }

            // check enrollment
            boolean enrolled = false;
            for (Enrollment en : DataStore.enrollments) {
                if (en.getStudent().getStudentId().equals(student.getStudentId()) &&
                    en.getCourse().getCourseId().equals(course.getCourseId())) {
                    enrolled = true;
                    break;
                }
            }

            if (!enrolled) {
                message.setText("Student is not enrolled in this course.");
                return;
            }

            // prevent duplicate grade
            for (Grade g : DataStore.grades) {
                if (g.getStudent().getStudentId().equals(student.getStudentId()) &&
                    g.getCourse().getCourseId().equals(course.getCourseId())) {
                    message.setText("Grade already recorded for this course.");
                    return;
                }
            }

            Grade newGrade = new Grade(student, course, grade);

            if (!newGrade.isValid()) {
                message.setText("Invalid grade data.");
                return;
            }

            // save to DB
            boolean saved = DBHelper.addGrade(student, course, grade);
            if (!saved) {
                message.setText("Error saving grade to database.");
                return;
            }

            DataStore.grades.add(newGrade);
            message.setText("Grade saved.");
            gpaLabel.setText("GPA: " + String.format("%.2f", calculateGPA(student)));

            gradeField.clear();
            studentBox.setValue(null);
            courseBox.setValue(null);
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
            if (g.getStudent().getStudentId().equals(student.getStudentId())) {
                total += g.getGradePoint();
                count++;
            }
        }

        return count == 0 ? 0.0 : total / count;
    }
}
