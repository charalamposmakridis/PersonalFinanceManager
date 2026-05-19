package models;

import java.math.BigDecimal;
import java.util.Objects;

public class Budget {

    private int id;
    private int userId;
    private int categoryId;
    private int month;
    private int year;
    private BigDecimal limitAmount;

    public Budget(int id, int userId, int categoryId, int month, int year, BigDecimal limitAmount) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.month = month;
        this.year = year;
        this.limitAmount = limitAmount;
    }

    public Budget(int userId, int categoryId, int month, int year, BigDecimal limitAmount) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.month = month;
        this.year = year;
        this.limitAmount = limitAmount;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getCategoryId() { return categoryId; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
    public BigDecimal getLimitAmount() { return limitAmount; }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return month + "/" + year + " - " + limitAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Budget budget)) return false;
        return id == budget.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}