package model;

import java.util.Objects;
import java.time.LocalDateTime;

public class Employee implements Comparable<Employee> {
    private final String id; 
    private String name;
    private String email;
    private double salary;
    private final LocalDateTime joiningDate;
    private Department department;
    private Designation designation;
    private EmploymentType employmentType;

    public Employee(String id, String name, String email, double salary, LocalDateTime joiningDate,
            Department department, Designation designation, EmploymentType employmentType) {
        
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Employee Name cannot be null or empty");
        }

        validateEmail(email);
        validateSalary(salary);

        this.id = id;
        this.name = name;
        this.email = email;
        this.salary = salary;
        this.joiningDate = Objects.requireNonNull(joiningDate, "Joining date cannot be null");
        this.department = Objects.requireNonNull(department, "Department cannot be null");
        this.designation = Objects.requireNonNull(designation, "Designation cannot be null");
        this.employmentType = Objects.requireNonNull(employmentType, "EmploymentType cannot be null");
    }

    public Employee(String id, String name, String email, double salary,
        Department department, Designation designation, EmploymentType employmentType) {
            this(id, name, email, salary, LocalDateTime.now(), department, designation, employmentType);
    }

    private void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email must not be null and it should contains @");
        }
    }
    
    private void validateSalary(double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Salary can not be negative");
        }
    }

    /** Getters */

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public double getSalary() {
        return salary;
    }

    public LocalDateTime getJoiningDate() {
        return joiningDate;
    }

    public Department getDepartment() {
        return department;
    }

    public Designation getDesignation() {
        return designation;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    /** Setters */

    public void setSalary(double salary) {
        validateSalary(salary);
        this.salary = salary;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    /** Overiiden method */

    @Override
    public int compareTo(Employee other) {
        return this.id.compareTo(other.id);
    }
    
    @Override 
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        return id.equals(((Employee) o).id);
    }

    @Override 
    public int hashCode() {
        return id.hashCode();
    }

    @Override 
    public String toString() {
        return String.format(
            "Employee{id=%s, name=%s, email=%s, salary=%.2f, joined=%s, dept=%s, designation=%s, type=%s}",
            id, name, email, salary, joiningDate, department.name(),
            designation.getTitle(), employmentType.getDisplayLabel());
    }
}
