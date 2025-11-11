import java.io.*;
import java.util.*;

public class StudentManagement {
    private static final String DATA_FILE = "students.txt";
    private static List<Student> students = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadFromFile();
        while (true) {
            System.out.println("\n=== Student Management ===");
            System.out.println("1. Add student");
            System.out.println("2. View all students");
            System.out.println("3. Search student by ID");
            System.out.println("4. Update student by ID");
            System.out.println("5. Delete student by ID");
            System.out.println("6. Save & Exit");
            System.out.print("Choose (1-6): ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": addStudent(); break;
                case "2": viewAll(); break;
                case "3": searchById(); break;
                case "4": updateById(); break;
                case "5": deleteById(); break;
                case "6": saveToFile(); System.out.println("Saved. Exiting."); return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void addStudent() {
        try {
            System.out.print("Enter ID (int): ");
            int id = Integer.parseInt(sc.nextLine().trim());
            if (findIndexById(id) != -1) {
                System.out.println("ID already exists.");
                return;
            }
            System.out.print("Enter name: ");
            String name = sc.nextLine().trim();
            System.out.print("Enter branch: ");
            String branch = sc.nextLine().trim();
            System.out.print("Enter CGPA (e.g. 8.4): ");
            double cgpa = Double.parseDouble(sc.nextLine().trim());
            students.add(new Student(id, name, branch, cgpa));
            System.out.println("Added.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number input.");
        }
    }

    private static void viewAll() {
        if (students.isEmpty()) {
            System.out.println("No students.");
            return;
        }
        System.out.printf("%-6s %-20s %-12s %-5s%n", "ID", "Name", "Branch", "CGPA");
        for (Student s : students) {
            System.out.printf("%-6d %-20s %-12s %-5.2f%n", s.getId(), s.getName(), s.getBranch(), s.getCgpa());
        }
    }

    private static void searchById() {
        try {
            System.out.print("Enter ID to search: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            int idx = findIndexById(id);
            if (idx == -1) System.out.println("Not found.");
            else System.out.println(students.get(idx));
        } catch (NumberFormatException e) { System.out.println("Invalid ID."); }
    }

    private static void updateById() {
        try {
            System.out.print("Enter ID to update: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            int idx = findIndexById(id);
            if (idx == -1) { System.out.println("Not found."); return; }
            Student s = students.get(idx);
            System.out.print("New name (blank to keep " + s.getName() + "): ");
            String name = sc.nextLine().trim(); if (!name.isEmpty()) s.setName(name);
            System.out.print("New branch (blank to keep " + s.getBranch() + "): ");
            String br = sc.nextLine().trim(); if (!br.isEmpty()) s.setBranch(br);
            System.out.print("New CGPA (blank to keep " + s.getCgpa() + "): ");
            String cg = sc.nextLine().trim(); if (!cg.isEmpty()) s.setCgpa(Double.parseDouble(cg));
            System.out.println("Updated.");
        } catch (NumberFormatException e) { System.out.println("Invalid number."); }
    }

    private static void deleteById() {
        try {
            System.out.print("Enter ID to delete: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            int idx = findIndexById(id);
            if (idx == -1) System.out.println("Not found.");
            else { students.remove(idx); System.out.println("Deleted."); }
        } catch (NumberFormatException e) { System.out.println("Invalid ID."); }
    }

    private static int findIndexById(int id) {
        for (int i = 0; i < students.size(); i++) if (students.get(i).getId() == id) return i;
        return -1;
    }

    private static void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Student s : students) {
                pw.printf("%d,%s,%s,%.2f%n", s.getId(), escape(s.getName()), escape(s.getBranch()), s.getCgpa());
            }
        } catch (IOException e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    private static void loadFromFile() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length >= 4) {
                    int id = Integer.parseInt(p[0]);
                    String name = unescape(p[1]);
                    String branch = unescape(p[2]);
                    double cgpa = Double.parseDouble(p[3]);
                    students.add(new Student(id, name, branch, cgpa));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Load failed: " + e.getMessage());
        }
    }

    private static String escape(String s) { return s.replace(",", "\,"); }
    private static String unescape(String s) { return s.replace("\,", ","); }
}

class Student {
    private int id;
    private String name;
    private String branch;
    private double cgpa;

    public Student(int id, String name, String branch, double cgpa) {
        this.id = id; this.name = name; this.branch = branch; this.cgpa = cgpa;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getBranch() { return branch; }
    public double getCgpa() { return cgpa; }
    public void setName(String name) { this.name = name; }
    public void setBranch(String branch) { this.branch = branch; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }
    public String toString() {
        return String.format("ID:%d | Name:%s | Branch:%s | CGPA:%.2f", id, name, branch, cgpa);
    }
}
