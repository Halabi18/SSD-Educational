import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        // setup the database first
        DBHelper.initializeDatabase();

        // load all data from DB into DataStore
        DataStore.loadFromDatabase();

        new LoginScreen().show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
