public class Student {
    private String studentId;
    private String name;
    private String email;
    private String program;

    public Student(String studentId, String name, String email, String program) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.program = program;
    }

    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getProgram() { return program; }

    // Basic validation method (useful for security/use cases)
    public boolean isValid() {
        return studentId != null && !studentId.isEmpty()
                && name != null && !name.isEmpty()
                && email != null && email.contains("@")
                && program != null && !program.isEmpty();
    }

    @Override
    public String toString() {
        return studentId + " - " + name;
    }
}