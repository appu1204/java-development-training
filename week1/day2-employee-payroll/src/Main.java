import model.*;
import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ArrayList<Employee> employees = new ArrayList<>();
    private static final ArrayList<Department> departments = new ArrayList<>();
    public static void main(String[] args) {

        while(true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch(choice) {
                case "1" -> addEmployee();
                case "2" -> viewAll();
                case "3" -> {System.out.println("Exit"); return;}
                default -> {System.out.println("Invalid Option. Try again");}
            }
        }
    }

    public static void printMenu() {
        System.out.println("\n===== EMPLOYEE PAYROLL =====");
        System.out.println("1. Add Employee");
        System.out.println("2. View all Employee");
        System.out.println("3. Exit");
        System.out.println("choose : ");
    }

    private static void addEmployee() {
        try {
            System.out.print("ID: ");
            String id = scanner.nextLine().trim();
            
            System.out.print("Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Email: ");
            String email = scanner.nextLine().trim();

            System.out.println("Salary: ");
            double salary = Double.parseDouble(scanner.nextLine().trim());

            Department dept = pick("Department", departments, Department::name);
        
            Designation des = pick("Designation", 
                    Arrays.asList(Designation.values()),
                    Designation::getTitle);

            EmploymentType type = pick("EmploymentType", 
                    Arrays.asList(EmploymentType.values()),
                    EmploymentType::getDisplayLabel);

            Employee e = new Employee(id, name, email, salary, dept, des, type);
            if (employees.contains(e)) {
                System.out.println("Employee with ID " + id + " already exists");
                return;
            }
            employees.add(e);
            System.out.println("Added: " + e);

        } catch (NumberFormatException ex) {
            System.out.println("Error: salary must be number.");
        } catch (IllegalArgumentException | NullPointerException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }


    private static <T> T pick(String label, List<T> options, java.util.function.Function<T, String> show) {
        System.out.println(label + ":");
        for (int i = 0; i < options.size(); i++)
            System.out.printf("  %d. %s%n", i + 1, show.apply(options.get(i)));
        System.out.print("Select: ");
        int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (idx < 0 || idx >= options.size())
            throw new IllegalArgumentException("Invalid " + label.toLowerCase() + " selection");
        return options.get(idx);
    }


    private static void viewAll() {
        if (employees.isEmpty()) {
            System.out.println("No employees.");
            return;
        }
        employees.forEach(System.out::println);
    }


}
