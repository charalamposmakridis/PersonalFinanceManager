package models;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {

    private int id;
    private String username;
    private String passwordHash;
    private String fullName;
    private LocalDateTime createdAt;

    public User() {
        this.createdAt = LocalDateTime.now();
    }

    public User(int id, String username, String passwordHash, String fullName, LocalDateTime createdAt) {
        setId(id);
        setUsername(username);
        setPasswordHash(passwordHash);
        setFullName(fullName);
        setCreatedAt(createdAt);
    }

    public User(String username, String passwordHash, String fullName) {
        setUsername(username);
        setPasswordHash(passwordHash);
        setFullName(fullName);
        this.createdAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        validateId(id);
        this.id = id;
    }

    public void setUsername(String username) {
        validateUsername(username);
        this.username = username.trim();
    }

    public void setPasswordHash(String passwordHash) {
        validatePasswordHash(passwordHash);
        this.passwordHash = passwordHash;
    }

    public void setFullName(String fullName) {
        validateFullName(fullName);
        this.fullName = fullName == null ? "" : fullName.trim();
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        validateCreatedAt(createdAt);
        this.createdAt = createdAt;
    }

    private void validateId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("User ID cannot be negative.");
        }
    }

    private void validateUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null.");
        }

        String trimmedUsername = username.trim();

        if (trimmedUsername.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        if (trimmedUsername.length() < 3) {
            throw new IllegalArgumentException("Username must contain at least 3 characters.");
        }

        if (trimmedUsername.length() > 30) {
            throw new IllegalArgumentException("Username cannot exceed 30 characters.");
        }
    }

    private void validatePasswordHash(String passwordHash) {
        if (passwordHash == null) {
            throw new IllegalArgumentException("Password hash cannot be null.");
        }

        if (passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Password hash cannot be empty.");
        }
    }

    private void validateFullName(String fullName) {
        if (fullName != null && fullName.trim().length() > 100) {
            throw new IllegalArgumentException("Full name cannot exceed 100 characters.");
        }
    }

    private void validateCreatedAt(LocalDateTime createdAt) {
        if (createdAt == null) {
            throw new IllegalArgumentException("Created at cannot be null.");
        }
    }

    @Override
    public String toString() {
        if (fullName == null || fullName.isBlank()) {
            return username;
        }

        return fullName + " (" + username + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}