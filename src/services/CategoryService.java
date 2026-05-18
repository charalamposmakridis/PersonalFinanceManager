package services;

import DAOlayer.CategoryDAO;
import models.Category;
import models.TransactionType;

import java.util.List;

public class CategoryService {
    private final CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = new CategoryDAO();
    }

    public Category createCategory(int user_id, String name, TransactionType type){
        validateUserId(user_id);
        validateCategoryName(name);
        validateTransactionType(type);

        if(categoryDAO.existsByNameAndUserId(name.trim(),user_id)){
            throw new IllegalArgumentException("Category already exists for this user");
        }

        Category category=new Category(user_id,name,type);
        categoryDAO.addCategory(category);

        return category;
    }

    public boolean updateCategory(int categoryId,int user_id,String name,TransactionType type){
        validateCategoryId(categoryId);
        validateCategoryName(name);
        validateTransactionType(type);
        validateUserId(user_id);

        Category existingCategory=categoryDAO.findCategoryById(categoryId);

        if(existingCategory==null){
            throw new IllegalArgumentException("Category Not found,");
        }

        if(existingCategory.getUserId()!=user_id){
            throw new IllegalArgumentException("You cannot update another user's category");
        }

        Category updatedCategory=new Category(categoryId,user_id,name,type);

        return categoryDAO.updateCategoryById(updatedCategory,categoryId);
    }

    public boolean deleteCategory(int categoryId){
        validateCategoryId(categoryId);
        return categoryDAO.deleteCategoryById(categoryId);
    }

    public Category getCategoryById(int categoryId){
        validateCategoryId(categoryId);
        return categoryDAO.findCategoryById(categoryId);
    }

    public List<Category> getAllCategoriesByUserId(int userId){
        validateCategoryId(userId);
        return categoryDAO.findAllByUserId(userId);
    }

    private void validateCategoryId(int categoryId){
        if(categoryId<=0){
            throw new IllegalArgumentException("Category Id must be greater than zero");
        }
    }

    private void validateUserId(int user_id){
        if(user_id<=0){
            throw new IllegalArgumentException("User Id must be greater than zero");
        }
    }

    public void validateCategoryName(String name){
        if(name==null || name.trim().isEmpty()){
            throw new IllegalArgumentException("Category name cannot be empty");
        }
    }

    public void validateTransactionType(TransactionType type){
        if(type==null){
            throw new IllegalArgumentException("Category type cannot be null");
        }
    }

}
