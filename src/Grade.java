public class Grade {
    private Student student;
    private Course course;
    private String letterGrade;

    public Grade(Student student, Course course, String letterGrade) {
        this.student = student;
        this.course = course;
        this.letterGrade = letterGrade;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public double getGradePoint() {
        switch (letterGrade.toUpperCase()) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            default: return 0.0;
        }
    }

    @Override
    public String toString() {
        return student.getName() + " - " + course.getCourseName() + " : " + letterGrade;
    }
}