import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentScreen {

    public void show() {
        Stage stage = new Stage();

        TextField idField = new TextField();
        idField.setPromptText("Student ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Student Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField programField = new TextField();
        programField.setPromptText("Program");

        Label message = new Label();

        ListView<Student> studentList = new ListView<>(DataStore.students);

        Button addBtn = new Button("Add Student");
        addBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String program = programField.getText().trim();

            // empty field check
            if (id.isEmpty() || name.isEmpty() || email.isEmpty() || program.isEmpty()) {
                message.setText("Please fill all fields.");
                return;
            }

            // student ID format check
            if (!id.matches("[A-Za-z0-9]+")) {
                message.setText("Student ID should contain only letters and numbers.");
                return;
            }

            // basic email check
            if (!email.contains("@") || !email.contains(".")) {
                message.setText("Enter a valid email address.");
                return;
            }

            // duplicate check
            if (DataStore.studentExists(id)) {
                message.setText("Student ID already exists.");
                return;
            }

            Student student = new Student(id, name, email, program);

            if (!student.isValid()) {
                message.setText("Invalid student data.");
                return;
            }

            // save to database first
            boolean saved = DBHelper.addStudent(student);
            if (!saved) {
                message.setText("Error saving student to database.");
                return;
            }

            // then update the in-memory list
            DataStore.students.add(student);
            message.setText("Student added successfully.");

            idField.clear();
            nameField.clear();
            emailField.clear();
            programField.clear();
        });

        VBox root = new VBox(10,
                new Label("Student Management"),
                idField, nameField, emailField, programField,
                addBtn, message,
                new Label("Students:"), studentList);

        root.setPadding(new Insets(15));

        stage.setScene(new Scene(root, 450, 500));
        stage.setTitle("Students");
        stage.show();
    }
}
