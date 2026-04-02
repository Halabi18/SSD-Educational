import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataStore {

    public static ObservableList<Student> students = FXCollections.observableArrayList();
    public static ObservableList<Course> courses = FXCollections.observableArrayList();
    public static ObservableList<Enrollment> enrollments = FXCollections.observableArrayList();
    public static ObservableList<Grade> grades = FXCollections.observableArrayList();
    public static ObservableList<Payment> payments = FXCollections.observableArrayList();

    // ---------- DUPLICATE CHECKS ----------
    public static boolean studentExists(String id) {
        for (Student s : students) {
            if (s.getStudentId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public static boolean courseExists(String id) {
        for (Course c : courses) {
            if (c.getCourseId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAlreadyEnrolled(Student student, Course course) {
        for (Enrollment e : enrollments) {
            if (e.getStudent().getStudentId().equals(student.getStudentId())
                    && e.getCourse().getCourseId().equals(course.getCourseId())) {
                return true;
            }
        }
        return false;
    }

    // ---------- SAMPLE DATA (OPTIONAL BUT USEFUL) ----------
    public static void loadSampleData() {
        if (students.isEmpty()) {
            students.add(new Student("S001", "Alice", "alice@email.com", "CS"));
            students.add(new Student("S002", "Bob", "bob@email.com", "IT"));
        }

        if (courses.isEmpty()) {
            courses.add(new Course("C101", "Programming", 3));
            courses.add(new Course("C102", "Database System", 3));
        }
    }
}