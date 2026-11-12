package search_filter_engine;

import model.Department;
import model.Designation;
import model.Employee;
import model.EmploymentType;
import search_filter_engine.aggregate.Aggregator;
import search_filter_engine.duplicate.DuplicateDetector;
import search_filter_engine.filter.CompositeFilter;
import search_filter_engine.filter.Filter;
import search_filter_engine.filter.Filters;
import search_filter_engine.search.SearchEngine;
import search_filter_engine.search.Searcher;
import search_filter_engine.sort.Sorters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

public class SearchFilterMain {

    private static final Scanner scanner = new Scanner(System.in);

    private static final Department ENG = new Department("D1", "Engineering");
    private static final Department HR = new Department("D2", "Human Resources");
    private static final Department FIN = new Department("D3", "Finance");
    private static final List<Department> DEPARTMENTS = List.of(ENG, HR, FIN);

    private static SearchEngine<Employee> engine;

    public static void main(String[] args) {
        Searcher<Employee> searcher = new Searcher<Employee>(
                Employee::getId,
                Employee::getName,
                Employee::getEmail,
                e -> e.getDepartment().name(),
                e -> e.getDesignation().getTitle());

        engine = new SearchEngine<>(sampleData(), searcher);

        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> show("All employees", engine.results());
                    case "2" -> searchMenu();
                    case "3" -> filterMenu();
                    case "4" -> sortMenu();
                    case "5" -> groupMenu();
                    case "6" -> duplicateMenu();
                    case "7" -> salarySummary();
                    case "8" -> demoChain();
                    case "0" -> {
                        System.out.println("Exit");
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again");
                }
            } catch (RuntimeException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    // ------------------------------------------------------------------ data

    private static List<Employee> sampleData() {
        return List.of(
            new Employee("E001", "Asha Rao", "asha@co.com", 900000, LocalDate.of(2021, 3, 15).atStartOfDay(), ENG, Designation.SENIOR_ENGINEER, EmploymentType.FULL_TIME),
            new Employee("E002", "Ravi Kumar", "ravi@co.com", 15000, LocalDate.of(2025, 6, 1).atStartOfDay(), HR, Designation.HR_EXECUTIVE, EmploymentType.INTERN),
            new Employee("E003", "Meena Iyer", "meena@co.com", 450000, LocalDate.of(2023, 1, 10).atStartOfDay(), FIN, Designation.SOFTWARE_ENGINEER, EmploymentType.CONTRACT),
            new Employee("E004", "Arjun Rao", "arjun@co.com", 1200000, LocalDate.of(2019, 8, 20).atStartOfDay(), ENG, Designation.TECH_LEAD, EmploymentType.FULL_TIME),
            new Employee("E005", "Asha Rao", "asha.r@co.com", 300000, LocalDate.of(2024, 2, 5).atStartOfDay(), ENG, Designation.SOFTWARE_ENGINEER, EmploymentType.PART_TIME),
            new Employee("E006", "Sneha Nair", "sneha@co.com", 500000, LocalDate.of(2022, 11, 1).atStartOfDay(), HR, Designation.HR_EXECUTIVE, EmploymentType.FULL_TIME),
            new Employee("E007", "Ravi Kumar", "ravi@co.com", 400000, LocalDate.of(2024, 9, 12).atStartOfDay(), HR, Designation.HR_EXECUTIVE, EmploymentType.CONTRACT));
    }

    // ------------------------------------------------------------------ menu

    private static void printMenu() {
        System.out.println("\n===== EMPLOYEE SEARCH & FILTER ENGINE =====");
        System.out.println("1. View all employees");
        System.out.println("2. Search by keyword");
        System.out.println("3. Filter");
        System.out.println("4. Sort");
        System.out.println("5. Group report");
        System.out.println("6. Find duplicates");
        System.out.println("7. Salary summary (aggregation)");
        System.out.println("8. Demo: filter + sort + group chain");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    private static void searchMenu() {
        System.out.print("Keyword (id / name / email / department / designation): ");
        String keyword = scanner.nextLine();
        show("Search results for '" + keyword.trim() + "'", engine.search(keyword).results());
    }

    private static void filterMenu() {
        System.out.println("Filter by:");
        System.out.println("  1. Department");
        System.out.println("  2. Employment type");
        System.out.println("  3. Salary range");
        System.out.println("  4. NOT in a department");
        System.out.println("  5. Department AND employment type");
        System.out.println("  6. Contract OR Intern");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();

        Filter<Employee> filter = switch (choice) {
            case "1" -> Filters.byDepartment(pickDepartment());
            case "2" -> Filters.byEmploymentType(pickType());
            case "3" -> {
                double min = readDouble("Min salary: ");
                double max = readDouble("Max salary: ");
                yield Filters.salaryBetween(min, max);
            }
            case "4" -> CompositeFilter.not(Filters.byDepartment(pickDepartment()));
            case "5" -> CompositeFilter.allOf(
                    Filters.byDepartment(pickDepartment()),
                    Filters.byEmploymentType(pickType()));
            case "6" -> CompositeFilter.anyOf(
                    Filters.byEmploymentType(EmploymentType.CONTRACT),
                    Filters.byEmploymentType(EmploymentType.INTERN));
            default -> throw new IllegalArgumentException("Invalid filter option");
        };

        show("Filter result", engine.filter(filter).results());
    }

    private static void sortMenu() {
        System.out.println("Sort by:");
        System.out.println("  1. Name");
        System.out.println("  2. Salary");
        System.out.println("  3. Joining date");
        System.out.println("  4. Department, then salary (high to low)");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();

        Comparator<Employee> comparator = switch (choice) {
            case "1" -> Sorters.<Employee, String>asc(Employee::getName);
            case "2" -> Sorters.<Employee, Double>asc(Employee::getSalary);
            case "3" -> Sorters.<Employee, LocalDateTime>asc(Employee::getJoiningDate);
            case "4" -> Sorters.<Employee>chain(
                    Sorters.<Employee, String>asc(e -> e.getDepartment().name()),
                    Sorters.<Employee, Double>desc(Employee::getSalary));
            default -> throw new IllegalArgumentException("Invalid sort option");
        };

        if (!choice.equals("4")) {
            System.out.print("Order (1 = ascending, 2 = descending): ");
            if (scanner.nextLine().trim().equals("2")) {
                comparator = comparator.reversed();
            }
        }
        show("Sorted result", engine.sortBy(comparator).results());
    }

    private static void groupMenu() {
        System.out.println("Group by: 1. Department  2. Employment type  3. Designation");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();

        Map<String, List<Employee>> groups = switch (choice) {
            case "1" -> engine.groupBy(e -> e.getDepartment().name());
            case "2" -> engine.groupBy(e -> e.getEmploymentType().getDisplayLabel());
            case "3" -> engine.groupBy(e -> e.getDesignation().getTitle());
            default -> throw new IllegalArgumentException("Invalid group option");
        };
        printGroups("Group report", groups);
    }

    private static void duplicateMenu() {
        System.out.println("Duplicates by: 1. Email  2. Name  3. Name + Department");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();

        Map<?, List<Employee>> duplicates = switch (choice) {
            case "1" -> engine.duplicatesBy(DuplicateDetector.<Employee>normalized(Employee::getEmail));
            case "2" -> engine.duplicatesBy(DuplicateDetector.<Employee>normalized(Employee::getName));
            case "3" -> engine.duplicatesBy(DuplicateDetector.<Employee>compositeKey(
                    Employee::getName, e -> e.getDepartment().name()));
            default -> throw new IllegalArgumentException("Invalid duplicate option");
        };

        if (duplicates.isEmpty()) {
            System.out.println("No duplicates found.");
            return;
        }
        duplicates.forEach((key, members) -> {
            System.out.println("\nDuplicate key: " + key);
            show("Employees sharing this key", members);
        });
    }

    private static void salarySummary() {
        System.out.println("\n--- Salary summary ---");
        System.out.println("Employees      : " + engine.count());
        System.out.printf("Total salary   : %.2f%n", engine.sum(Employee::getSalary));
        System.out.printf("Average salary : %.2f%n", engine.average(Employee::getSalary).orElse(0));
        engine.max(Employee::getSalary).ifPresent(e ->
                System.out.printf("Highest paid   : %s (%.2f)%n", e.getName(), e.getSalary()));
        engine.min(Employee::getSalary).ifPresent(e ->
                System.out.printf("Lowest paid    : %s (%.2f)%n", e.getName(), e.getSalary()));

        System.out.println("Average salary per department:");
        engine.averageBy(e -> e.getDepartment().name(), Employee::getSalary)
              .forEach((dept, avg) -> System.out.printf("  %-16s %.2f%n", dept, avg));
    }

    /** Engineering AND salary > 300000 -> highest salary first -> grouped by designation. */
    private static void demoChain() {
        SearchEngine<Employee> step = engine
                .filter(CompositeFilter.allOf(
                        Filters.byDepartmentName(ENG),
                        Filters.salaryAbove(300000)))
                .sortBy(Sorters.<Employee, Double>desc(Employee::getSalary));

        show("Engineering, salary > 300000, highest first", step.results());
        printGroups("Grouped by designation", step.groupBy(e -> e.getDesignation().getTitle()));
    }

    /** <-- helpers --> */

    private static void show(String title, List<Employee> list) {
        System.out.println("\n--- " + title + " (" + list.size() + ") ---");
        if (list.isEmpty()) {
            System.out.println("No results.");
            return;
        }
        System.out.printf("%-6s %-12s %-16s %-26s %-10s %12s%n",
                "ID", "Name", "Department", "Designation", "Type", "Salary");
        for (Employee e : list) {
            System.out.printf("%-6s %-12s %-16s %-26s %-10s %12.2f%n",
                    e.getId(), e.getName(), e.getDepartment().name(),
                    e.getDesignation().getTitle(), e.getEmploymentType().getDisplayLabel(),
                    e.getSalary());
        }
    }

    private static void printGroups(String title, Map<String, List<Employee>> groups) {
        System.out.println("\n--- " + title + " ---");
        groups.forEach((key, members) -> {
            double total = Aggregator.sum(members, Employee::getSalary);
            double avg = Aggregator.average(members, Employee::getSalary).orElse(0);
            System.out.printf("%s: count=%d, total=%.2f, average=%.2f%n", key, members.size(), total, avg);
            members.forEach(m -> System.out.println("    " + m.getId() + " " + m.getName()));
        });
    }

    private static Department pickDepartment() {
        return pick("Department", DEPARTMENTS, Department::name);
    }

    private static EmploymentType pickType() {
        return pick("Employment type", Arrays.asList(EmploymentType.values()),
                EmploymentType::getDisplayLabel);
    }

    private static <T> T pick(String label, List<T> options, Function<T, String> show) {
        System.out.println(label + ":");
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, show.apply(options.get(i)));
        }
        System.out.print("Select: ");
        int idx;
        try {
            idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Selection must be a number");
        }
        if (idx < 0 || idx >= options.size()) {
            throw new IllegalArgumentException("Invalid " + label.toLowerCase() + " selection");
        }
        return options.get(idx);
    }

    private static double readDouble(String prompt) {
        System.out.print(prompt);
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Enter a valid number");
        }
    }
}