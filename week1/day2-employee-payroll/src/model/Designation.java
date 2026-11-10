package model;

public enum Designation {

    SOFTWARE_ENGINEER("Software Engineer", 1),
    SENIOR_ENGINEER("Senior Software Engineer", 2),
    TECH_LEAD("Tech Lead", 3),
    ENGINEERING_MANAGER("Engineering Manager", 4),
    HR_EXECUTIVE("HR Executive", 1);

    private final String title;
    private final int level;

    private Designation(String title, int level) {
        this.title = title;
        this.level = level;
    }
    
    /** Getters */
    
    public String getTitle() {
        return title;
    }

    public int getLevel() {
        return level;
    }

    
}
