package DAOlayer;

import DatabaseHandling.DatabaseConnection;
import models.Transaction;
import models.TransactionType;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public void addTransaction(Transaction transaction) {
        String sql = """
                INSERT INTO transactions(user_id, category_id, amount, type, description, transaction_date, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, transaction.getUserId());
            pstmt.setInt(2, transaction.getCategoryId());
            pstmt.setBigDecimal(3, transaction.getAmount());
            pstmt.setString(4, transaction.getType().name());
            pstmt.setString(5, transaction.getDescription());
            pstmt.setString(6, transaction.getTransactionDate().toString());
            pstmt.setString(7, transaction.getCreatedAt().toString());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating transaction failed, no rows affected.");
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    transaction.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while adding transaction.", e);
        }
    }

    public boolean deleteTransactionById(int id) {
        String sql = """
                DELETE FROM transactions
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting transaction by id.", e);
        }
    }

    public Transaction findTransactionById(int id) {
        String sql = """
                SELECT * FROM transactions
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToTransaction(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while finding transaction by id.", e);
        }

        return null;
    }

    public List<Transaction> findAllByUserId(int userId) {
        String sql = """
                SELECT * FROM transactions
                WHERE user_id = ?
                ORDER BY transaction_date DESC
                """;

        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapRowToTransaction(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while finding transactions by user id.", e);
        }

        return transactions;
    }

    public boolean updateTransactionById(Transaction transaction, int id) {
        String sql = """
                UPDATE transactions
                SET category_id = ?, amount = ?, type = ?, description = ?, transaction_date = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, transaction.getCategoryId());
            pstmt.setBigDecimal(2, transaction.getAmount());
            pstmt.setString(3, transaction.getType().name());
            pstmt.setString(4, transaction.getDescription());
            pstmt.setString(5, transaction.getTransactionDate().toString());
            pstmt.setInt(6, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error while updating transaction by id.", e);
        }
    }

    public List<Transaction> findByUserIdAndType(int userId, TransactionType type) {
        String sql = """
                SELECT * FROM transactions
                WHERE user_id = ? AND type = ?
                ORDER BY transaction_date DESC
                """;

        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, type.name());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapRowToTransaction(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while finding transactions by user id and type.", e);
        }

        return transactions;
    }

    public List<Transaction> findByUserIdAndCategoryId(int userId, int categoryId) {
        String sql = """
                SELECT * FROM transactions
                WHERE user_id = ? AND category_id = ?
                ORDER BY transaction_date DESC
                """;

        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, categoryId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapRowToTransaction(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while finding transactions by user id and category id.", e);
        }

        return transactions;
    }

    public List<Transaction> findByUserIdAndDateRange(int userId, LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT * FROM transactions
                WHERE user_id = ?
                AND transaction_date BETWEEN ? AND ?
                ORDER BY transaction_date DESC
                """;

        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, startDate.toString());
            pstmt.setString(3, endDate.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapRowToTransaction(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while finding transactions by date range.", e);
        }

        return transactions;
    }

    private Transaction mapRowToTransaction(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("category_id"),
                BigDecimal.valueOf(rs.getDouble("amount")),
                TransactionType.valueOf(rs.getString("type")),
                rs.getString("description"),
                LocalDate.parse(rs.getString("transaction_date")),
                LocalDateTime.parse(rs.getString("created_at"))
        );
    }
}