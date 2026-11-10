package model;

public enum EmploymentType {
    FULL_TIME("Full Time", 40),
    PART_TIME("Part Time", 20),
    CONTRACT("Contract", 40),
    INTERN("Intern", 20);
 
    private final String displayLabel;
    private final int standardHoursPerWeek;
 
    EmploymentType(String displayLabel, int standardHoursPerWeek) {
        this.displayLabel = displayLabel;
        this.standardHoursPerWeek = standardHoursPerWeek;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }

    public int getStandardHoursPerWeek() {
        return standardHoursPerWeek;
    }
    
}
