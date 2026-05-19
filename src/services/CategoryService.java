package services;

import DAOlayer.CategoryDAO;
import models.Category;
import models.TransactionType;

import java.util.List;

public class CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public Category createCategory(int userId, String name, TransactionType type) {
        validateUserId(userId);
        validateCategoryName(name);
        validateTransactionType(type);

        if (categoryDAO.existsByNameAndUserId(name.trim(), userId)) {
            throw new IllegalArgumentException("Category already exists for this user.");
        }

        Category category = new Category(userId, name, type);
        categoryDAO.addCategory(category);

        return category;
    }

    public boolean updateCategory(int categoryId, int userId, String name, TransactionType type) {
        validateCategoryId(categoryId);
        validateUserId(userId);
        validateCategoryName(name);
        validateTransactionType(type);

        Category existingCategory = categoryDAO.findCategoryById(categoryId);

        if (existingCategory == null) {
            throw new IllegalArgumentException("Category not found.");
        }

        if (existingCategory.getUserId() != userId) {
            throw new IllegalArgumentException("You cannot update another user's category.");
        }

        Category updatedCategory = new Category(
                categoryId,
                userId,
                name,
                type,
                existingCategory.getCreatedAt()
        );

        return categoryDAO.updateCategoryById(updatedCategory, categoryId);
    }

    public boolean deleteCategory(int categoryId, int userId) {
        validateCategoryId(categoryId);
        validateUserId(userId);

        Category category = categoryDAO.findCategoryById(categoryId);

        if (category == null) {
            throw new IllegalArgumentException("Category not found.");
        }

        if (category.getUserId() != userId) {
            throw new IllegalArgumentException("You cannot delete another user's category.");
        }

        return categoryDAO.deleteCategoryById(categoryId);
    }

    public Category getCategoryById(int categoryId, int userId) {
        validateCategoryId(categoryId);
        validateUserId(userId);

        Category category = categoryDAO.findCategoryById(categoryId);

        if (category == null) {
            return null;
        }

        if (category.getUserId() != userId) {
            throw new IllegalArgumentException("You cannot access another user's category.");
        }

        return category;
    }

    public List<Category> getAllCategoriesByUserId(int userId) {
        validateUserId(userId);
        return categoryDAO.findAllByUserId(userId);
    }

    private void validateCategoryId(int categoryId) {
        if (categoryId <= 0) {
            throw new IllegalArgumentException("Category ID must be greater than zero.");
        }
    }

    private void validateUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than zero.");
        }
    }

    private void validateCategoryName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty.");
        }

        if (name.trim().length() > 50) {
            throw new IllegalArgumentException("Category name cannot exceed 50 characters.");
        }
    }

    private void validateTransactionType(TransactionType type) {
        if (type == null) {
            throw new IllegalArgumentException("Category type cannot be null.");
        }
    }
}