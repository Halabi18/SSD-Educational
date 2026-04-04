import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataStore {

    public static ObservableList<Student> students = FXCollections.observableArrayList();
    public static ObservableList<Course> courses = FXCollections.observableArrayList();
    public static ObservableList<Enrollment> enrollments = FXCollections.observableArrayList();
    public static ObservableList<Grade> grades = FXCollections.observableArrayList();
    public static ObservableList<Payment> payments = FXCollections.observableArrayList();

    // load everything from the database into memory
    public static void loadFromDatabase() {
        students.clear();
        students.addAll(DBHelper.getStudents());

        courses.clear();
        courses.addAll(DBHelper.getCourses());

        enrollments.clear();
        enrollments.addAll(DBHelper.getEnrollments());

        grades.clear();
        grades.addAll(DBHelper.getGrades());

        payments.clear();
        payments.addAll(DBHelper.getPayments());
    }

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
}
