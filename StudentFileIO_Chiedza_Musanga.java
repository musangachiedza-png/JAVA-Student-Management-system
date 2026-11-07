/*
 * Student: Chiedza Musanga
 * Registration Number: B250879A
 * Course: CS203 / SWE211
 * Assignment: Student File Input/Output
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * CS203 / SWE211 Assignment 2 - Student File Input/Output
 * Keep student.doc in the program's working folder.
 * Each row must contain exactly 9 values:
 * Surname Name SchoolName Province Age Gender Test1Mark Test2Mark Test3Mark
 */
public class StudentFileIO {
    private static final Scanner keyboard = new Scanner(System.in);
    private static final Path INPUT_FILE = Paths.get("student.doc");
    private static final Path SUMMARY_FOLDER = Paths.get("StudentRecords");
    private static final Path SUMMARY_FILE = SUMMARY_FOLDER.resolve("StudentSummary.doc");

    static class Student {
        String surname, name, schoolName, province, gender;
        int age;
        double test1Mark, test2Mark, test3Mark;

        Student(String surname, String name, String schoolName, String province,
                int age, String gender, double test1Mark, double test2Mark, double test3Mark) {
            this.surname = surname;
            this.name = name;
            this.schoolName = schoolName;
            this.province = province;
            this.age = age;
            this.gender = gender;
            this.test1Mark = test1Mark;
            this.test2Mark = test2Mark;
            this.test3Mark = test3Mark;
        }

        double calculateAverage() {
            return (test1Mark + test2Mark + test3Mark) / 3.0;
        }

        String getGrade() {
            double average = calculateAverage();
            if (average < 25.0) return "BELOW AVERAGE";
            else if (average < 27.0) return "AVERAGE";
            else return "GOOD";
        }

        String toFileLine() {
            return surname + " " + name + " " + schoolName + " " + province + " " + age
                    + " " + gender + " " + test1Mark + " " + test2Mark + " " + test3Mark;
        }
    }

    // Reads all valid student rows from student.doc.
    private static List<Student> readStudents() throws IOException {
        List<Student> students = new ArrayList<>();
        if (!Files.exists(INPUT_FILE)) {
            throw new IOException("student.doc was not found. Put it in the program's working folder.");
        }
        try (BufferedReader reader = Files.newBufferedReader(INPUT_FILE)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split("\\s+");
                if (parts.length != 9) {
                    System.out.println("Warning: row " + lineNumber + " skipped; expected 9 values.");
                    continue;
                }
                try {
                    students.add(new Student(parts[0], parts[1], parts[2], parts[3],
                            Integer.parseInt(parts[4]), parts[5],
                            Double.parseDouble(parts[6]), Double.parseDouble(parts[7]),
                            Double.parseDouble(parts[8])));
                } catch (NumberFormatException ex) {
                    System.out.println("Warning: row " + lineNumber + " skipped; invalid age or mark.");
                }
            }
        }
        return students;
    }

    // (a) Display all details of the first student.
    public static void DisplayStudentDetails() {
        try {
            List<Student> students = readStudents();
            if (students.isEmpty()) { System.out.println("No valid student records."); return; }
            System.out.println(students.get(0).toFileLine());
        } catch (IOException ex) { System.out.println("File error: " + ex.getMessage()); }
    }

    // (b) Display all details of every student.
    public static void DisplayAllStudentDetails() {
        try {
            List<Student> students = readStudents();
            if (students.isEmpty()) { System.out.println("No valid student records."); return; }
            for (Student student : students) System.out.println(student.toFileLine());
        } catch (IOException ex) { System.out.println("File error: " + ex.getMessage()); }
    }

    // (c) Count the valid student rows.
    public static void countStudents() {
        try { System.out.println("Number of students: " + readStudents().size()); }
        catch (IOException ex) { System.out.println("File error: " + ex.getMessage()); }
    }

    // (d) Display surname, first-name initial, school, age and Test3 mark for first student.
    public static void DisplayImportantDetails() {
        try {
            List<Student> students = readStudents();
            if (students.isEmpty()) { System.out.println("No valid student records."); return; }
            displayOneImportantRecord(students.get(0));
        } catch (IOException ex) { System.out.println("File error: " + ex.getMessage()); }
    }

    private static void displayOneImportantRecord(Student student) {
        System.out.printf("%s %s %s %d %.2f%n", student.surname,
                student.name.substring(0, 1), student.schoolName, student.age, student.test3Mark);
    }

    // (e) Display the important details for all students.
    public static void DisplayImportantDetailsAll() {
        try {
            List<Student> students = readStudents();
            if (students.isEmpty()) { System.out.println("No valid student records."); return; }
            for (Student student : students) displayOneImportantRecord(student);
        } catch (IOException ex) { System.out.println("File error: " + ex.getMessage()); }
    }

    // (f) Calculate average age for all students.
    public static void DisplayAverageAge() {
        try {
            List<Student> students = readStudents();
            if (students.isEmpty()) { System.out.println("Cannot calculate average age: no students."); return; }
            int totalAge = 0;
            for (Student student : students) totalAge += student.age;
            System.out.printf("Average age: %.2f years%n", (double) totalAge / students.size());
        } catch (IOException ex) { System.out.println("File error: " + ex.getMessage()); }
    }

    // (g) Calculate average of the three tests for the first student.
    public static void calculateAverageMark() {
        try {
            List<Student> students = readStudents();
            if (students.isEmpty()) { System.out.println("No valid student records."); return; }
            Student first = students.get(0);
            System.out.printf("Average mark for %s %s: %.2f%n", first.surname, first.name,
                    first.calculateAverage());
        } catch (IOException ex) { System.out.println("File error: " + ex.getMessage()); }
    }

    // (h) Calculate average of the three tests for every student.
    public static void calculateAverageMarkAll() {
        try {
            List<Student> students = readStudents();
            if (students.isEmpty()) { System.out.println("No valid student records."); return; }
            for (Student student : students)
                System.out.printf("%s %s: %.2f%n", student.surname, student.name, student.calculateAverage());
        } catch (IOException ex) { System.out.println("File error: " + ex.getMessage()); }
    }

    // (i) Create the folder and write the first student's summary.
    public static void createSummary() {
        try {
            List<Student> students = readStudents();
            if (students.isEmpty()) { System.out.println("No valid student records to summarise."); return; }
            Files.createDirectories(SUMMARY_FOLDER);
            try (BufferedWriter writer = Files.newBufferedWriter(SUMMARY_FILE,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                writeSummaryLine(writer, students.get(0));
            }
            System.out.println("First student's summary saved to " + SUMMARY_FILE.toAbsolutePath());
        } catch (IOException ex) { System.out.println("Could not create summary: " + ex.getMessage()); }
    }

    // (j) Write summary details for all students.
    public static void createSummaryAll() {
        try {
            List<Student> students = readStudents();
            if (students.isEmpty()) { System.out.println("No valid student records to summarise."); return; }
            Files.createDirectories(SUMMARY_FOLDER);
            try (BufferedWriter writer = Files.newBufferedWriter(SUMMARY_FILE,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                writer.write("SURNAME NAME SCHOOL AVERAGE_MARK GRADE");
                writer.newLine();
                for (Student student : students) writeSummaryLine(writer, student);
            }
            System.out.println("All summaries saved to " + SUMMARY_FILE.toAbsolutePath());
        } catch (IOException ex) { System.out.println("Could not create summary: " + ex.getMessage()); }
    }

    private static void writeSummaryLine(BufferedWriter writer, Student student) throws IOException {
        writer.write(String.format("%-12s %-12s %-15s %.2f %s", student.surname, student.name,
                student.schoolName, student.calculateAverage(), student.getGrade()));
        writer.newLine();
    }

    // (k) Add one student to the end of student.doc.
    public static void addStudents() {
        try {
            System.out.println("Enter the student's details (use one word for each text field).");
            String surname = readSingleWord("Surname: ");
            String name = readSingleWord("Name: ");
            String school = readSingleWord("School name: ");
            String province = readSingleWord("Province: ");
            int age = readPositiveInteger("Age: ");
            String gender = readSingleWord("Gender/Sex: ");
            double test1 = readMark("Test 1 mark (0-100): ");
            double test2 = readMark("Test 2 mark (0-100): ");
            double test3 = readMark("Test 3 mark (0-100): ");
            Student student = new Student(surname, name, school, province, age, gender, test1, test2, test3);
            try (BufferedWriter writer = Files.newBufferedWriter(INPUT_FILE,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write(student.toFileLine());
                writer.newLine();
            }
            System.out.println("Student added successfully.");
        } catch (IOException ex) { System.out.println("Could not add student: " + ex.getMessage()); }
    }

    private static String readSingleWord(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = keyboard.nextLine().trim();
            if (!value.isEmpty() && !value.matches(".*\\s+.*")) return value;
            System.out.println("Enter one word only (no spaces).");
        }
    }

    private static int readPositiveInteger(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(keyboard.nextLine().trim());
                if (value > 0) return value;
            } catch (NumberFormatException ex) { /* Ask again below. */ }
            System.out.println("Enter a whole number greater than zero.");
        }
    }

    private static double readMark(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(keyboard.nextLine().trim());
                if (Double.isFinite(value) && value >= 0 && value <= 100) return value;
            } catch (NumberFormatException ex) { /* Ask again below. */ }
            System.out.println("Enter a numeric mark from 0 to 100.");
        }
    }

    // (l) Menu uses switch and repeats until the user chooses Exit.
    public static void main(String[] args) {
        System.out.println("Student: Chiedza Musanga | Registration Number: B250879A");
        int choice;
        do {
            System.out.println("\n========== STUDENT RECORDS MENU ==========");
            System.out.println("1. Display first student's full details (a)");
            System.out.println("2. Display all students' full details (b)");
            System.out.println("3. Count students (c)");
            System.out.println("4. Display first student's important details (d)");
            System.out.println("5. Display all students' important details (e)");
            System.out.println("6. Display average age (f)");
            System.out.println("7. Calculate first student's average mark (g)");
            System.out.println("8. Calculate every student's average mark (h)");
            System.out.println("9. Create summary for first student (i)");
            System.out.println("10. Create summaries for all students (j)");
            System.out.println("11. Add a student (k)");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            try { choice = Integer.parseInt(keyboard.nextLine().trim()); }
            catch (NumberFormatException ex) { choice = -1; }

            switch (choice) {
                case 1: DisplayStudentDetails(); break;
                case 2: DisplayAllStudentDetails(); break;
                case 3: countStudents(); break;
                case 4: DisplayImportantDetails(); break;
                case 5: DisplayImportantDetailsAll(); break;
                case 6: DisplayAverageAge(); break;
                case 7: calculateAverageMark(); break;
                case 8: calculateAverageMarkAll(); break;
                case 9: createSummary(); break;
                case 10: createSummaryAll(); break;
                case 11: addStudents(); break;
                case 0: System.out.println("Program ended. Goodbye."); break;
                default: System.out.println("Invalid choice. Enter a number from 0 to 11.");
            }
        } while (choice != 0);
        keyboard.close();
    }
}
