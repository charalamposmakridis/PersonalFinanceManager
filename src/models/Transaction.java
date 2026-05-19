package models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {

    private int id;
    private int userId;
    private int categoryId;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private LocalDate transactionDate;
    private LocalDateTime createdAt;

    public Transaction() {
        this.createdAt = LocalDateTime.now();
    }

    public Transaction(int id, int userId, int categoryId, BigDecimal amount,
                       TransactionType type, String description,
                       LocalDate transactionDate, LocalDateTime createdAt) {

        validateId(id);
        validateUserId(userId);
        validateCategoryId(categoryId);
        validateAmount(amount);
        validateType(type);
        validateDescription(description);
        validateTransactionDate(transactionDate);
        validateCreatedAt(createdAt);

        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.type = type;
        this.description = description == null ? "" : description.trim();
        this.transactionDate = transactionDate;
        this.createdAt = createdAt;
    }

    public Transaction(int userId, int categoryId, BigDecimal amount,
                       TransactionType type, String description,
                       LocalDate transactionDate) {

        validateUserId(userId);
        validateCategoryId(categoryId);
        validateAmount(amount);
        validateType(type);
        validateDescription(description);
        validateTransactionDate(transactionDate);

        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.type = type;
        this.description = description == null ? "" : description.trim();
        this.transactionDate = transactionDate;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
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
            throw new IllegalArgumentException("Transaction ID cannot be negative.");
        }
    }

    private void validateUserId(int userId) {
        if (userId < 0) {
            throw new IllegalArgumentException("User ID cannot be negative.");
        }
    }

    private void validateCategoryId(int categoryId) {
        if (categoryId < 0) {
            throw new IllegalArgumentException("Category ID cannot be negative.");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null.");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
    }

    private void validateType(TransactionType type) {
        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null.");
        }
    }

    private void validateDescription(String description) {
        if (description != null && description.trim().length() > 255) {
            throw new IllegalArgumentException("Description cannot exceed 255 characters.");
        }
    }

    private void validateTransactionDate(LocalDate transactionDate) {
        if (transactionDate == null) {
            throw new IllegalArgumentException("Transaction date cannot be null.");
        }
    }

    private void validateCreatedAt(LocalDateTime createdAt) {
        if (createdAt == null) {
            throw new IllegalArgumentException("Created at cannot be null.");
        }
    }

    @Override
    public String toString() {
        return type + " | " + amount + " | " + transactionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction transaction)) return false;
        return id == transaction.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}