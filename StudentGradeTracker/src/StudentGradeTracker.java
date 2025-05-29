import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentGradeTracker {

    private List<Student> students;
    private Scanner scanner;

    public StudentGradeTracker() {
        students = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        StudentGradeTracker tracker = new StudentGradeTracker();
        tracker.run();
    }

    public void run() {
        int choice;
        do {
            displayMenu();
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    addGradesToStudent();
                    break;
                case 3:
                    displayStudentReport();
                    break;
                case 4:
                    displayAllStudentsSummary();
                    break;
                case 5:
                    System.out.println("Exiting Student Grade Tracker. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println();
        } while (choice != 5);

        scanner.close();
    }

    private void displayMenu() {
        System.out.println("--- Student Grade Tracker Menu ---");
        System.out.println("1. Add New Student");
        System.out.println("2. Add Grades to a Student");
        System.out.println("3. View Individual Student Report");
        System.out.println("4. View All Students Summary Report");
        System.out.println("5. Exit");
    }

    private void addStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        Student newStudent = new Student(name);
        students.add(newStudent);
        System.out.println("Student '" + name + "' added successfully.");
    }

    private void addGradesToStudent() {
        if (students.isEmpty()) {
            System.out.println("No students added yet. Please add a student first.");
            return;
        }

        System.out.print("Enter the name of the student to add grades to: ");
        String studentName = scanner.nextLine();
        Student foundStudent = findStudentByName(studentName);

        if (foundStudent != null) {
            System.out.println("Enter grades for " + studentName + " (enter -1 to stop):");
            double grade;
            while (true) {
                System.out.print("Grade: ");
                grade = scanner.nextDouble();
                if (grade == -1) {
                    break;
                }
                foundStudent.addGrade(grade);
            }
            scanner.nextLine();
            System.out.println("Grades added for " + studentName + ".");
        } else {
            System.out.println("Student '" + studentName + "' not found.");
        }
    }

    private void displayStudentReport() {
        if (students.isEmpty()) {
            System.out.println("No students added yet.");
            return;
        }

        System.out.print("Enter the name of the student to view report: ");
        String studentName = scanner.nextLine();
        Student foundStudent = findStudentByName(studentName);

        if (foundStudent != null) {
            System.out.println("\n--- Report for " + foundStudent.getName() + " ---");
            System.out.println("Grades: " + foundStudent.getGrades());
            System.out.printf("Average Score: %.2f\n", foundStudent.calculateAverage());
            System.out.printf("Highest Score: %.2f\n", foundStudent.getHighestGrade());
            System.out.printf("Lowest Score: %.2f\n", foundStudent.getLowestGrade());
        } else {
            System.out.println("Student '" + studentName + "' not found.");
        }
    }

    private void displayAllStudentsSummary() {
        if (students.isEmpty()) {
            System.out.println("No students added yet.");
            return;
        }

        System.out.println("\n--- All Students Summary Report ---");
        System.out.printf("%-20s %-10s %-10s %-10s\n", "Student Name", "Average", "Highest", "Lowest");
        System.out.println("----------------------------------------------------------");

        for (Student student : students) {
            System.out.printf("%-20s %-10.2f %-10.2f %-10.2f\n",
                    student.getName(),
                    student.calculateAverage(),
                    student.getHighestGrade(),
                    student.getLowestGrade());
        }
        System.out.println("----------------------------------------------------------");
    }

    private Student findStudentByName(String name) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                return student;
            }
        }
        return null;
    }
}