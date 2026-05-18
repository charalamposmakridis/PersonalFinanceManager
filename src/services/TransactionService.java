package services;

import DAOlayer.CategoryDAO;
import DAOlayer.TransactionDAO;
import models.Category;
import models.Transaction;
import models.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TransactionService {

    private final TransactionDAO transactionDAO;
    private final CategoryDAO categoryDAO;

    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
        this.categoryDAO = new CategoryDAO();
    }

    public Transaction createTransaction(int userId, int categoryId, BigDecimal amount, TransactionType type, String description, LocalDate transactionDate) {
        validateUserId(userId);
        validateCategoryId(categoryId);
        validateAmount(amount);
        validateType(type);
        validateDescription(description);
        validateTransactionDate(transactionDate);

        Category category = categoryDAO.findCategoryById(categoryId);

        if (category == null) {
            throw new IllegalArgumentException("Category not found.");
        }

        if (category.getUserId() != userId) {
            throw new IllegalArgumentException("Category does not belong to this user.");
        }

        if (category.getType() != type) {
            throw new IllegalArgumentException("Transaction type must match category type.");
        }

        Transaction transaction = new Transaction(userId, categoryId, amount, type, description, transactionDate);

        transactionDAO.addTransaction(transaction);

        return transaction;
    }

    public boolean updateTransaction(int transactionId, int userId, int categoryId, BigDecimal amount, TransactionType type, String description, LocalDate transactionDate) {
        validateTransactionId(transactionId);
        validateUserId(userId);
        validateCategoryId(categoryId);
        validateAmount(amount);
        validateType(type);
        validateDescription(description);
        validateTransactionDate(transactionDate);

        Transaction existingTransaction = transactionDAO.findTransactionById(transactionId);

        if (existingTransaction == null) {
            throw new IllegalArgumentException("Transaction not found.");
        }

        if (existingTransaction.getUserId() != userId) {
            throw new IllegalArgumentException("You cannot update another user's transaction.");
        }

        Category category = categoryDAO.findCategoryById(categoryId);

        if (category == null) {
            throw new IllegalArgumentException("Category not found.");
        }

        if (category.getUserId() != userId) {
            throw new IllegalArgumentException("Category does not belong to this user.");
        }

        if (category.getType() != type) {
            throw new IllegalArgumentException("Transaction type must match category type.");
        }

        Transaction updatedTransaction = new Transaction(transactionId, userId, categoryId, amount, type, description, transactionDate, existingTransaction.getCreatedAt());

        return transactionDAO.updateTransactionById(updatedTransaction, transactionId);
    }

    public boolean deleteTransaction(int transactionId, int userId) {
        validateTransactionId(transactionId);
        validateUserId(userId);

        Transaction transaction = transactionDAO.findTransactionById(transactionId);

        if (transaction == null) {
            throw new IllegalArgumentException("Transaction not found.");
        }

        if (transaction.getUserId() != userId) {
            throw new IllegalArgumentException("You cannot delete another user's transaction.");
        }

        return transactionDAO.deleteTransactionById(transactionId);
    }

    public Transaction getTransactionById(int transactionId, int userId) {
        validateTransactionId(transactionId);
        validateUserId(userId);

        Transaction transaction = transactionDAO.findTransactionById(transactionId);

        if (transaction == null) {
            return null;
        }

        if (transaction.getUserId() != userId) {
            throw new IllegalArgumentException("You cannot access another user's transaction.");
        }

        return transaction;
    }

    public List<Transaction> getTransactionsByUserId(int userId) {
        validateUserId(userId);
        return transactionDAO.findAllByUserId(userId);
    }

    public List<Transaction> getTransactionsByType(int userId, TransactionType type) {
        validateUserId(userId);
        validateType(type);

        return transactionDAO.findByUserIdAndType(userId, type);
    }

    public List<Transaction> getTransactionsByCategory(int userId, int categoryId) {
        validateUserId(userId);
        validateCategoryId(categoryId);

        Category category = categoryDAO.findCategoryById(categoryId);

        if (category == null) {
            throw new IllegalArgumentException("Category not found.");
        }

        if (category.getUserId() != userId) {
            throw new IllegalArgumentException("Category does not belong to this user.");
        }

        return transactionDAO.findByUserIdAndCategoryId(userId, categoryId);
    }

    public List<Transaction> getTransactionsByDateRange(int userId, LocalDate startDate, LocalDate endDate) {

        validateUserId(userId);
        validateDateRange(startDate, endDate);

        return transactionDAO.findByUserIdAndDateRange(userId, startDate, endDate);
    }

    private void validateUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than zero.");
        }
    }

    private void validateCategoryId(int categoryId) {
        if (categoryId <= 0) {
            throw new IllegalArgumentException("Category ID must be greater than zero.");
        }
    }

    private void validateTransactionId(int transactionId) {
        if (transactionId <= 0) {
            throw new IllegalArgumentException("Transaction ID must be greater than zero.");
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

        if (transactionDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Transaction date cannot be in the future.");
        }
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null.");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }
    }
}