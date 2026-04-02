public class Payment {
    private Student student;
    private double amount;

    public Payment(Student student, double amount) {
        this.student = student;
        this.amount = amount;
    }

    public Student getStudent() {
        return student;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return student.getName() + " paid $" + amount;
    }
}