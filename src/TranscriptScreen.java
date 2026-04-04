import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TranscriptScreen {

    public void show() {
        Stage stage = new Stage();

        ComboBox<Student> studentBox = new ComboBox<>(DataStore.students);
        studentBox.setPromptText("Select Student");

        TextArea transcriptArea = new TextArea();
        transcriptArea.setEditable(false);
        transcriptArea.setPrefHeight(400);

        Button generateBtn = new Button("Generate Transcript");
        generateBtn.setOnAction(e -> {
            Student student = studentBox.getValue();

            if (student == null) {
                transcriptArea.setText("Please select a student.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("===== TRANSCRIPT =====\n");
            sb.append("Student ID: ").append(student.getStudentId()).append("\n");
            sb.append("Name: ").append(student.getName()).append("\n");
            sb.append("Email: ").append(student.getEmail()).append("\n");
            sb.append("Program: ").append(student.getProgram()).append("\n\n");

            sb.append("Courses and Grades:\n");
            double total = 0;
            int count = 0;

            for (Grade g : DataStore.grades) {
                if (g.getStudent().getStudentId().equals(student.getStudentId())) {
                    sb.append("- ").append(g.getCourse().getCourseName())
                      .append(" : ").append(g.getLetterGrade()).append("\n");
                    total += g.getGradePoint();
                    count++;
                }
            }

            double gpa = count == 0 ? 0.0 : total / count;
            sb.append("\nGPA: ").append(String.format("%.2f", gpa));

            transcriptArea.setText(sb.toString());
        });

        VBox root = new VBox(10,
                new Label("Transcript"),
                studentBox, generateBtn, transcriptArea);

        root.setPadding(new Insets(15));

        stage.setScene(new Scene(root, 500, 550));
        stage.setTitle("Transcript");
        stage.show();
    }
}