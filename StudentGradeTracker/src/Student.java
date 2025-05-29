import java.util.ArrayList;
import java.util.List;

public class Student {
    private String name;
    private List<Double> grades;

    public Student(String name) {
        this.name = name;
        this.grades = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addGrade(double grade) {
        if (grade >= 0 && grade <= 100) {
            this.grades.add(grade);
        } else {
            System.out.println("Invalid grade: " + grade + ". Grade must be between 0 and 100.");
        }
    }

    public List<Double> getGrades() {
        return grades;
    }

    public double calculateAverage() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (double grade : grades) {
            sum += grade;
        }
        return sum / grades.size();
    }

    public double getHighestGrade() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double max = grades.get(0);
        for (double grade : grades) {
            if (grade > max) {
                max = grade;
            }
        }
        return max;
    }

    public double getLowestGrade() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double min = grades.get(0);
        for (double grade : grades) {
            if (grade < min) {
                min = grade;
            }
        }
        return min;
    }
}