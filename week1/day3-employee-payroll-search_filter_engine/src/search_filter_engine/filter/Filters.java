package search_filter_engine.filter;

import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.function.Predicate;

import model.Department;
import model.Designation;
import model.Employee;
import model.EmploymentType;

public final class Filters {

    private Filters() {}

    public static <T, V> Filter<T> where(Function<? super T, ? extends V> getter, Predicate<? super V> test) {
        return item -> test.test(getter.apply(item));
    }

    /** Employee Filters -> 
     * byDepartment
     * byDepartmentName
     * byDesignation
     * byEmploymentType
     * salaryAbove
     * salaryBelow
     * salaryBetween
     * joinedAfter
     * joineBefore
    */

    public static Filter<Employee> byDepartment(Department department) {
        return e -> e.getDepartment().equals(department);
    }

    public static Filter<Employee> byDepartmentName(Department department) {
        return e -> e.getDepartment().name().equalsIgnoreCase(department.name());
    }

    public static Filter<Employee> byDesignation(Designation designation) {
        return e -> e.getDesignation() == designation;
    }

    public static Filter<Employee> byEmploymentType(EmploymentType employmentType) {
        return e -> e.getEmploymentType() == employmentType;
    }

    public static Filter<Employee> salaryAbove(double amount) {
        return e -> e.getSalary() > amount;
    }

    public static Filter<Employee> salaryBelow(double amount) {
        return e ->e.getSalary() < amount;
    }

    public static Filter<Employee> salaryBetween(double minAmt, double maxAmt) {
        if (minAmt > maxAmt) throw new IllegalArgumentException("Minimum amount can not be more than maximum amount");
        return e -> e.getSalary() <= maxAmt && e.getSalary() >= minAmt;
    }

    public static Filter<Employee> joinedAfter(LocalDateTime date) {
        return e -> e.getJoiningDate().isAfter(date);
    }

    public static Filter<Employee> joinedBefore(LocalDateTime date) {
        return e -> e.getJoiningDate().isBefore(date);
    }
}
