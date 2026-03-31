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

            try {
                int credits = Integer.parseInt(creditText);
                DataStore.courses.add(new Course(id, name, credits));
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