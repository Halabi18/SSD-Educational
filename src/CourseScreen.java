import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CourseScreen {

    public void show() {
        Stage stage = new Stage();

        TextField idField = new TextField();
        idField.setPromptText("Course ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Course Name");

        TextField creditField = new TextField();
        creditField.setPromptText("Credit Hours");

        Label message = new Label();

        ListView<Course> courseList = new ListView<>(DataStore.courses);

        Button addBtn = new Button("Add Course");
        addBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String creditText = creditField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || creditText.isEmpty()) {
                message.setText("Please fill all fields.");
                return;
            }

            if (!id.matches("[A-Za-z0-9]+")) {
                message.setText("Course ID should contain only letters and numbers.");
                return;
            }

            // duplicate check
            if (DataStore.courseExists(id)) {
                message.setText("Course ID already exists.");
                return;
            }

            try {
                int credits = Integer.parseInt(creditText);

                if (credits <= 0 || credits > 6) {
                    message.setText("Credit hours must be between 1 and 6.");
                    return;
                }

                Course course = new Course(id, name, credits);

                // save to DB
                boolean saved = DBHelper.addCourse(course);
                if (!saved) {
                    message.setText("Error saving course to database.");
                    return;
                }

                DataStore.courses.add(course);
                message.setText("Course added.");

                idField.clear();
                nameField.clear();
                creditField.clear();

            } catch (NumberFormatException ex) {
                message.setText("Credit hours must be a number.");
            }
        });

        VBox root = new VBox(10,
                new Label("Course Management"),
                idField, nameField, creditField,
                addBtn, message,
                new Label("Courses:"), courseList);

        root.setPadding(new Insets(15));

        stage.setScene(new Scene(root, 450, 500));
        stage.setTitle("Courses");
        stage.show();
    }
}
