import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    private static final String URL = "jdbc:sqlite:ecms.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // simple SHA-256 hashing
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return password;
        }
    }

    public static void initializeDatabase() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    username TEXT PRIMARY KEY,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS students (
                    studentId TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    email TEXT NOT NULL,
                    program TEXT NOT NULL
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS courses (
                    courseId TEXT PRIMARY KEY,
                    courseName TEXT NOT NULL,
                    creditHours INTEGER NOT NULL
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS enrollments (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    studentId TEXT NOT NULL,
                    courseId TEXT NOT NULL,
                    UNIQUE(studentId, courseId)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS grades (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    studentId TEXT NOT NULL,
                    courseId TEXT NOT NULL,
                    letterGrade TEXT NOT NULL
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS payments (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    studentId TEXT NOT NULL,
                    amount REAL NOT NULL
                )
            """);

            // insert default admin with hashed password
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT OR IGNORE INTO users(username, password, role) VALUES (?, ?, ?)");
            ps.setString(1, "admin");
            ps.setString(2, hashPassword("1234"));
            ps.setString(3, "admin");
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean validateLogin(String username, String password) {
        String hashed = hashPassword(password);
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hashed);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ---------------- STUDENTS ----------------
    public static boolean addStudent(Student student) {
        String sql = "INSERT INTO students(studentId, name, email, program) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getStudentId());
            ps.setString(2, student.getName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getProgram());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Student> getStudents() {
        List<Student> list = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {
            while (rs.next()) {
                list.add(new Student(
                        rs.getString("studentId"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("program")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ---------------- COURSES ----------------
    public static boolean addCourse(Course course) {
        String sql = "INSERT INTO courses(courseId, courseName, creditHours) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, course.getCourseId());
            ps.setString(2, course.getCourseName());
            ps.setInt(3, course.getCreditHours());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Course> getCourses() {
        List<Course> list = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM courses")) {
            while (rs.next()) {
                list.add(new Course(
                        rs.getString("courseId"),
                        rs.getString("courseName"),
                        rs.getInt("creditHours")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ---------------- ENROLLMENTS ----------------
    public static boolean addEnrollment(Student student, Course course) {
        String sql = "INSERT INTO enrollments(studentId, courseId) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getStudentId());
            ps.setString(2, course.getCourseId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Enrollment> getEnrollments() {
        List<Enrollment> list = new ArrayList<>();
        String sql = """
            SELECT s.studentId, s.name, s.email, s.program,
                   c.courseId, c.courseName, c.creditHours
            FROM enrollments e
            JOIN students s ON e.studentId = s.studentId
            JOIN courses c ON e.courseId = c.courseId
        """;
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student s = new Student(rs.getString("studentId"), rs.getString("name"),
                        rs.getString("email"), rs.getString("program"));
                Course c = new Course(rs.getString("courseId"), rs.getString("courseName"),
                        rs.getInt("creditHours"));
                list.add(new Enrollment(s, c));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ---------------- GRADES ----------------
    public static boolean addGrade(Student student, Course course, String grade) {
        String sql = "INSERT INTO grades(studentId, courseId, letterGrade) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getStudentId());
            ps.setString(2, course.getCourseId());
            ps.setString(3, grade);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Grade> getGrades() {
        List<Grade> list = new ArrayList<>();
        String sql = """
            SELECT s.studentId, s.name, s.email, s.program,
                   c.courseId, c.courseName, c.creditHours,
                   g.letterGrade
            FROM grades g
            JOIN students s ON g.studentId = s.studentId
            JOIN courses c ON g.courseId = c.courseId
        """;
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student s = new Student(rs.getString("studentId"), rs.getString("name"),
                        rs.getString("email"), rs.getString("program"));
                Course c = new Course(rs.getString("courseId"), rs.getString("courseName"),
                        rs.getInt("creditHours"));
                list.add(new Grade(s, c, rs.getString("letterGrade")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ---------------- PAYMENTS ----------------
    public static boolean addPayment(Student student, double amount) {
        String sql = "INSERT INTO payments(studentId, amount) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getStudentId());
            ps.setDouble(2, amount);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Payment> getPayments() {
        List<Payment> list = new ArrayList<>();
        String sql = """
            SELECT s.studentId, s.name, s.email, s.program, p.amount
            FROM payments p
            JOIN students s ON p.studentId = s.studentId
        """;
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student s = new Student(rs.getString("studentId"), rs.getString("name"),
                        rs.getString("email"), rs.getString("program"));
                list.add(new Payment(s, rs.getDouble("amount")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
