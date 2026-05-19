package DAOlayer;

import DatabaseHandling.DatabaseConnection;
import models.Budget;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetDAO {

    public void addBudget(Budget budget) {
        String sql = """
                INSERT INTO budgets(user_id, category_id, month, year, limit_amount)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, budget.getUserId());
            pstmt.setInt(2, budget.getCategoryId());
            pstmt.setInt(3, budget.getMonth());
            pstmt.setInt(4, budget.getYear());
            pstmt.setBigDecimal(5, budget.getLimitAmount());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating budget failed, no rows affected.");
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    budget.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while adding budget.", e);
        }
    }

    public List<Budget> findAllByUserId(int userId) {
        String sql = """
                SELECT * FROM budgets
                WHERE user_id = ?
                ORDER BY year DESC, month DESC
                """;

        List<Budget> budgets = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    budgets.add(mapRowToBudget(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while finding budgets.", e);
        }

        return budgets;
    }

    public boolean deleteBudgetById(int id) {
        String sql = """
                DELETE FROM budgets
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting budget.", e);
        }
    }

    private Budget mapRowToBudget(ResultSet rs) throws SQLException {
        return new Budget(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("category_id"),
                rs.getInt("month"),
                rs.getInt("year"),
                rs.getBigDecimal("limit_amount")
        );
    }
}