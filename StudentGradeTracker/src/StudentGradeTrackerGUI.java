import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class StudentGradeTrackerGUI extends JFrame {

    private List<Student> students;
    private JTextField studentNameField;
    private JTextField gradeInputField;
    private JComboBox<String> studentSelectComboBox;
    private JTextArea individualReportArea;
    private JTable allStudentsTable;
    private StudentTableModel studentTableModel;

    public StudentGradeTrackerGUI() {
        students = new ArrayList<>();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Student Grade Tracker");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
            }
        }

        JPanel contentPane = new JPanel(new BorderLayout(20, 20)); 
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20)); 
        contentPane.setBackground(new Color(240, 242, 245)); 
        setContentPane(contentPane);

        JLabel titleLabel = new JLabel("Student Grade Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(44, 62, 80)); 
        contentPane.add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS)); 
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                BorderFactory.createEmptyBorder(15, 15, 15, 15) 
        ));
        inputPanel.setBackground(Color.WHITE);

        JPanel addStudentSubPanel = new JPanel(new GridLayout(3, 1, 10, 5)); 
        addStudentSubPanel.setOpaque(false); 
        JLabel addStudentLabel = new JLabel("Add New Student:");
        addStudentLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        studentNameField = new JTextField(20);
        studentNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentNameField.putClientProperty("JTextField.variant", "search"); 
        JButton addStudentButton = new JButton("Add Student");
        addStudentButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addStudentButton.setBackground(new Color(46, 204, 113)); 
        addStudentButton.setForeground(Color.WHITE);
        addStudentButton.setFocusPainted(false); 
        addStudentButton.setBorderPainted(false); 
        addStudentButton.setRolloverEnabled(true);
        addStudentButton.addActionListener(new AddStudentActionListener());

        addStudentSubPanel.add(addStudentLabel);
        addStudentSubPanel.add(studentNameField);
        addStudentSubPanel.add(addStudentButton);

        JSeparator separator1 = new JSeparator(SwingConstants.HORIZONTAL);
        separator1.setBorder(new EmptyBorder(10, 0, 10, 0)); 

        JPanel addGradeSubPanel = new JPanel(new GridLayout(4, 1, 10, 5)); 
        addGradeSubPanel.setOpaque(false);
        JLabel addGradeLabel = new JLabel("Add Grade to Student:");
        addGradeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        studentSelectComboBox = new JComboBox<>();
        studentSelectComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentSelectComboBox.setPreferredSize(new Dimension(200, 30));
        updateStudentComboBox(); 

        gradeInputField = new JTextField(20);
        gradeInputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gradeInputField.putClientProperty("JTextField.variant", "search");
        JButton addGradeButton = new JButton("Add Grade");
        addGradeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addGradeButton.setBackground(new Color(41, 128, 185));
        addGradeButton.setForeground(Color.WHITE);
        addGradeButton.setFocusPainted(false);
        addGradeButton.setBorderPainted(false);
        addGradeButton.setRolloverEnabled(true);
        addGradeButton.addActionListener(new AddGradeActionListener());

        addGradeSubPanel.add(addGradeLabel);
        addGradeSubPanel.add(studentSelectComboBox);
        addGradeSubPanel.add(gradeInputField);
        addGradeSubPanel.add(addGradeButton);

        // Add Delete Student Button
        JButton deleteStudentButton = new JButton("Delete Student");
        deleteStudentButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deleteStudentButton.setBackground(new Color(231, 76, 60));
        deleteStudentButton.setForeground(Color.WHITE);
        deleteStudentButton.setFocusPainted(false);
        deleteStudentButton.setBorderPainted(false);
        deleteStudentButton.addActionListener(e -> {
            String selectedStudentName = (String) studentSelectComboBox.getSelectedItem();
            if (selectedStudentName == null || students.isEmpty() || selectedStudentName.equals("No students yet")) {
                JOptionPane.showMessageDialog(this, "Please select a student to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Student s = findStudentByName(selectedStudentName);
            if (s != null) {
                students.remove(s);
                updateStudentComboBox();
                studentTableModel.fireTableDataChanged();
                individualReportArea.setText("");
                JOptionPane.showMessageDialog(this, "Student deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        addGradeSubPanel.add(deleteStudentButton);

        // Add Delete Last Grade Button
        JButton deleteGradeButton = new JButton("Delete Last Grade");
        deleteGradeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deleteGradeButton.setBackground(new Color(241, 196, 15));
        deleteGradeButton.setForeground(Color.WHITE);
        deleteGradeButton.setFocusPainted(false);
        deleteGradeButton.setBorderPainted(false);
        deleteGradeButton.addActionListener(e -> {
            String selectedStudentName = (String) studentSelectComboBox.getSelectedItem();
            if (selectedStudentName == null || students.isEmpty() || selectedStudentName.equals("No students yet")) {
                JOptionPane.showMessageDialog(this, "Please select a student.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Student s = findStudentByName(selectedStudentName);
            if (s != null && !s.getGrades().isEmpty()) {
                s.getGrades().remove(s.getGrades().size() - 1);
                studentTableModel.fireTableDataChanged();
                JOptionPane.showMessageDialog(this, "Last grade deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        addGradeSubPanel.add(deleteGradeButton);

        inputPanel.add(addStudentSubPanel);
        inputPanel.add(separator1);
        inputPanel.add(addGradeSubPanel);

        contentPane.add(inputPanel, BorderLayout.WEST);

        JPanel reportPanel = new JPanel(new BorderLayout(15, 15));
        reportPanel.setBackground(new Color(240, 242, 245));

        JPanel individualReportPanel = new JPanel(new BorderLayout(5, 5));
        individualReportPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)), "Individual Student Report"));
        individualReportPanel.setBackground(Color.WHITE);
        individualReportPanel.setFont(new Font("Segoe UI", Font.BOLD, 14)); 

        individualReportArea = new JTextArea(16, 50); // Increased rows and columns for a bigger report area
        individualReportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        individualReportArea.setEditable(false);
        individualReportArea.setLineWrap(true);
        individualReportArea.setWrapStyleWord(true);
        individualReportArea.setBackground(new Color(255, 255, 230));
        individualReportArea.setForeground(new Color(44, 62, 80));
        individualReportArea.setFont(new Font("Consolas", Font.BOLD, 15));
        JScrollPane individualScrollPane = new JScrollPane(individualReportArea);
        individualScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JButton viewIndividualReportButton = new JButton("View Individual Report");
        viewIndividualReportButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        viewIndividualReportButton.setBackground(new Color(230, 126, 34)); 
        viewIndividualReportButton.setForeground(Color.WHITE);
        viewIndividualReportButton.setFocusPainted(false);
        viewIndividualReportButton.setBorderPainted(false);
        viewIndividualReportButton.setRolloverEnabled(true);
        viewIndividualReportButton.addActionListener(new ViewReportActionListener());

        individualReportPanel.add(individualScrollPane, BorderLayout.CENTER);
        individualReportPanel.add(viewIndividualReportButton, BorderLayout.SOUTH);

        JPanel allStudentsSummaryPanel = new JPanel(new BorderLayout(5, 5));
        allStudentsSummaryPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)), "All Students Summary"));
        allStudentsSummaryPanel.setBackground(Color.WHITE);
        allStudentsSummaryPanel.setFont(new Font("Segoe UI", Font.BOLD, 14)); 

        studentTableModel = new StudentTableModel(students);
        allStudentsTable = new JTable(studentTableModel);
        allStudentsTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        allStudentsTable.setRowHeight(28);
        allStudentsTable.setFillsViewportHeight(true); 
        allStudentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        allStudentsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        allStudentsTable.getTableHeader().setBackground(new Color(52, 152, 219));
        allStudentsTable.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < allStudentsTable.getColumnCount(); i++) {
            allStudentsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane allStudentsScrollPane = new JScrollPane(allStudentsTable);
        allStudentsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        allStudentsScrollPane.getViewport().setBackground(new Color(236, 240, 241)); 

        JButton viewAllSummaryButton = new JButton("Refresh All Students Summary");
        viewAllSummaryButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        viewAllSummaryButton.setBackground(new Color(155, 89, 182)); 
        viewAllSummaryButton.setForeground(Color.WHITE);
        viewAllSummaryButton.setFocusPainted(false);
        viewAllSummaryButton.setBorderPainted(false);
        viewAllSummaryButton.setRolloverEnabled(true);
        viewAllSummaryButton.addActionListener(new ViewAllSummaryActionListener());

        allStudentsSummaryPanel.add(allStudentsScrollPane, BorderLayout.CENTER);
        allStudentsSummaryPanel.add(viewAllSummaryButton, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, individualReportPanel, allStudentsSummaryPanel);
        splitPane.setResizeWeight(0.4); 
        splitPane.setDividerSize(10); 
        splitPane.setBorder(BorderFactory.createEmptyBorder()); 

        reportPanel.add(splitPane, BorderLayout.CENTER);
        contentPane.add(reportPanel, BorderLayout.CENTER);

        JLabel footerLabel = new JLabel("© 2025 Student Grade Tracker. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footerLabel.setForeground(new Color(100, 100, 100));
        contentPane.add(footerLabel, BorderLayout.SOUTH);
    }

    private void updateStudentComboBox() {
        studentSelectComboBox.removeAllItems();
        if (students.isEmpty()) {
            studentSelectComboBox.addItem("No students yet");
            studentSelectComboBox.setEnabled(false);
            return;
        }
        studentSelectComboBox.setEnabled(true);
        for (Student student : students) {
            studentSelectComboBox.addItem(student.getName());
        }
        studentSelectComboBox.setSelectedIndex(0); 
    }

    private Student findStudentByName(String name) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                return student;
            }
        }
        return null;
    }

    private class AddStudentActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = studentNameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(StudentGradeTrackerGUI.this,
                        "Student name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (findStudentByName(name) != null) {
                JOptionPane.showMessageDialog(StudentGradeTrackerGUI.this,
                        "Student '" + name + "' already exists.", "Duplicate Student", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Student newStudent = new Student(name);
            students.add(newStudent);
            updateStudentComboBox(); 
            studentTableModel.fireTableDataChanged(); 
            studentNameField.setText("");
            JOptionPane.showMessageDialog(StudentGradeTrackerGUI.this,
                    "Student '" + name + "' added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class AddGradeActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedStudentName = (String) studentSelectComboBox.getSelectedItem();
            if (selectedStudentName == null || students.isEmpty() || selectedStudentName.equals("No students yet")) {
                JOptionPane.showMessageDialog(StudentGradeTrackerGUI.this,
                        "Please select a student first.", "Selection Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Student selectedStudent = findStudentByName(selectedStudentName);
            if (selectedStudent == null) {
                JOptionPane.showMessageDialog(StudentGradeTrackerGUI.this,
                        "Selected student not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String gradeText = gradeInputField.getText().trim();
            if (gradeText.isEmpty()) {
                JOptionPane.showMessageDialog(StudentGradeTrackerGUI.this,
                        "Grade cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double grade = Double.parseDouble(gradeText);
                if (grade < 0 || grade > 100) {
                    JOptionPane.showMessageDialog(StudentGradeTrackerGUI.this,
                            "Grade must be between 0 and 100.", "Invalid Grade", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                selectedStudent.addGrade(grade);
                gradeInputField.setText("");
                studentTableModel.fireTableDataChanged(); 
                JOptionPane.showMessageDialog(StudentGradeTrackerGUI.this,
                        "Grade " + grade + " added for " + selectedStudent.getName() + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(StudentGradeTrackerGUI.this,
                        "Invalid grade format. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ViewReportActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedStudentName = (String) studentSelectComboBox.getSelectedItem();
            if (selectedStudentName == null || students.isEmpty() || selectedStudentName.equals("No students yet")) {
                individualReportArea.setText("No student selected or no students added yet.");
                return;
            }

            Student selectedStudent = findStudentByName(selectedStudentName);
            if (selectedStudent != null) {
                StringBuilder report = new StringBuilder();
                report.append("--- Report for ").append(selectedStudent.getName()).append(" ---\n\n");
                report.append("Grades: ").append(selectedStudent.getGrades()).append("\n");
                report.append(String.format("Average Score: %.2f\n", selectedStudent.calculateAverage()));
                report.append(String.format("Highest Score: %.2f\n", selectedStudent.getHighestGrade()));
                report.append(String.format("Lowest Score: %.2f\n", selectedStudent.getLowestGrade()));
                individualReportArea.setText(report.toString());
            } else {
                individualReportArea.setText("Error: Selected student not found.");
            }
        }
    }

    private class ViewAllSummaryActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (students.isEmpty()) {
                JOptionPane.showMessageDialog(StudentGradeTrackerGUI.this,
                        "No students added yet to display summary.", "No Data", JOptionPane.INFORMATION_MESSAGE);
                studentTableModel.setStudents(new ArrayList<>()); 
                return;
            }
            studentTableModel.fireTableDataChanged(); 
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentGradeTrackerGUI gui = new StudentGradeTrackerGUI();
            gui.setVisible(true);
        });
    }
}