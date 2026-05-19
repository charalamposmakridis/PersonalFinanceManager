package models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Category {

    private int id;
    private int userId;
    private String name;
    private TransactionType type;
    private LocalDateTime createdAt;

    public Category() {
        this.createdAt = LocalDateTime.now();
    }

    public Category(int id, int userId, String name, TransactionType type, LocalDateTime createdAt) {
        validateId(id);
        validateUserId(userId);
        validateName(name);
        validateType(type);
        validateCreatedAt(createdAt);

        this.id = id;
        this.userId = userId;
        this.name = name.trim();
        this.type = type;
        this.createdAt = createdAt;
    }

    public Category(int userId, String name, TransactionType type) {
        validateUserId(userId);
        validateName(name);
        validateType(type);

        this.userId = userId;
        this.name = name.trim();
        this.type = type;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public TransactionType getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        validateId(id);
        this.id = id;
    }

    private void validateId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Category ID cannot be negative.");
        }
    }

    private void validateUserId(int userId) {
        if (userId < 0) {
            throw new IllegalArgumentException("User ID cannot be negative.");
        }
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Category name cannot be null.");
        }

        String trimmedName = name.trim();

        if (trimmedName.isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty.");
        }

        if (trimmedName.length() > 50) {
            throw new IllegalArgumentException("Category name cannot exceed 50 characters.");
        }
    }

    private void validateType(TransactionType type) {
        if (type == null) {
            throw new IllegalArgumentException("Category type cannot be null.");
        }
    }

    private void validateCreatedAt(LocalDateTime createdAt) {
        if (createdAt == null) {
            throw new IllegalArgumentException("Created at cannot be null.");
        }
    }

    @Override
    public String toString() {
        return name + " - " + type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;
        return id == category.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}