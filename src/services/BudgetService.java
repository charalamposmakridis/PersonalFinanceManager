package services;

import DAOlayer.BudgetDAO;
import DAOlayer.CategoryDAO;
import models.Budget;
import models.Category;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;

public class BudgetService {

    private final BudgetDAO budgetDAO;
    private final CategoryDAO categoryDAO;

    public BudgetService() {
        this.budgetDAO = new BudgetDAO();
        this.categoryDAO = new CategoryDAO();
    }

    public Budget createBudget(int userId, int categoryId, int month, int year, BigDecimal limitAmount) {
        validateUserId(userId);
        validateCategoryId(categoryId);
        validateMonth(month);
        validateYear(year);
        validateLimitAmount(limitAmount);

        Category category = categoryDAO.findCategoryById(categoryId);

        if (category == null) {
            throw new IllegalArgumentException("Category not found.");
        }

        if (category.getUserId() != userId) {
            throw new IllegalArgumentException("Category does not belong to this user.");
        }

        Budget budget = new Budget(userId, categoryId, month, year, limitAmount);
        budgetDAO.addBudget(budget);

        return budget;
    }

    public List<Budget> getBudgetsByUserId(int userId) {
        validateUserId(userId);
        return budgetDAO.findAllByUserId(userId);
    }

    public boolean deleteBudget(int budgetId) {
        if (budgetId <= 0) {
            throw new IllegalArgumentException("Budget ID must be greater than zero.");
        }

        return budgetDAO.deleteBudgetById(budgetId);
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

    private void validateMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12.");
        }
    }

    private void validateYear(int year) {
        if (year < 2000 || year > Year.now().getValue() + 5) {
            throw new IllegalArgumentException("Invalid year.");
        }
    }

    private void validateLimitAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Limit amount must be greater than zero.");
        }
    }
}