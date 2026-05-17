package DAOlayer;

import DatabaseHandling.DatabaseConnection;
import models.Category;
import models.TransactionType;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public void addCategory(Category category) {

        String sql = """
                INSERT INTO categories (user_id, name, type, created_at)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, category.getUserId());
            pstmt.setString(2, category.getName());
            pstmt.setString(3, category.getType().name());
            pstmt.setString(4, category.getCreatedAt().toString());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating category failed, no rows affected.");
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {

                if (rs.next()) {
                    category.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while adding category.", e);
        }
    }

    public boolean existsByNameAndUserId(String name, int userId) {
        String sql = """
            SELECT 1 FROM categories
            WHERE name = ? AND user_id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while checking category existence.", e);
        }
    }

    public boolean deleteCategoryById(int id) {

        String sql = """
                DELETE FROM categories
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting category by id.", e);
        }
    }

    public Category findCategoryById(int id) {

        String sql = """
                SELECT * FROM categories
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    return mapRowToCategory(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while finding category by id.", e);
        }

        return null;
    }

    public List<Category> findAllByUserId(int userId) {

        String sql = """
                SELECT * FROM categories
                WHERE user_id = ?
                ORDER BY name ASC
                """;

        List<Category> categories = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    categories.add(mapRowToCategory(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while finding categories by user id.", e);
        }

        return categories;
    }

    public boolean updateCategoryById(Category category, int id) {

        String sql = """
                UPDATE categories
                SET name = ?, type = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category.getName());
            pstmt.setString(2, category.getType().name());
            pstmt.setInt(3, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error while updating category by id.", e);
        }
    }

    private Category mapRowToCategory(ResultSet rs) throws SQLException {

        return new Category(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("name"),
                TransactionType.valueOf(rs.getString("type")),
                LocalDateTime.parse(rs.getString("created_at"))
        );
    }
}