public class Course {
    private String courseId;
    private String courseName;
    private int creditHours;

    public Course(String courseId, String courseName, int creditHours) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.creditHours = creditHours;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCreditHours() {
        return creditHours;
    }

    @Override
    public String toString() {
        return courseId + " - " + courseName;
    }
}