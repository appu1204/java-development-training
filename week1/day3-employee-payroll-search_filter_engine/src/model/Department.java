package model;

public record Department(String id, String name) {
    public Department {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Department ID cannot be null or empty");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Department Name cannot be null or empty");
        }
    }
}
