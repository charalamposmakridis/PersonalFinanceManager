# Personal Finance Manager

A desktop application built with **Java Swing** and **SQLite** for managing personal finances.

The application allows users to:

- Register and login securely
- Manage income and expense categories
- Add and manage transactions
- Create monthly budgets
- View dashboard statistics
- Generate financial reports
- Export reports to CSV

---

# Features

## Authentication

- User Registration
- User Login
- Password hashing using BCrypt
- Username uniqueness validation

---

## Dashboard

- Total Income
- Total Expenses
- Current Balance
- Recent Transactions

---

## Transactions

- Add Transactions
- Delete Transactions
- Filter by:
    - Type
    - Category
    - Date Range
- Income / Expense support

---

## Categories

- Create Categories
- Delete Categories
- Income / Expense category types

---

## Budgets

- Monthly Budget creation
- Budget management per category

---

## Reports

- Financial reports by date range
- Income / Expense summaries
- Balance calculation
- CSV export support

---

# Technologies Used

- Java 17+
- Java Swing
- SQLite
- JDBC
- BCrypt (jBCrypt)

---

# Project Structure

```text
src/
│
├── DAOlayer/
│   ├── UserDAO.java
│   ├── CategoryDAO.java
│   ├── TransactionDAO.java
│   └── BudgetDAO.java
│
├── DatabaseHandling/
│   ├── DatabaseConnection.java
│   └── DatabaseInitializer.java
│
├── models/
│   ├── User.java
│   ├── Category.java
│   ├── Transaction.java
│   ├── Budget.java
│   └── TransactionType.java
│
├── services/
│   ├── AuthService.java
│   ├── CategoryService.java
│   ├── TransactionService.java
│   └── BudgetService.java
│
├── ui/
│   ├── dialogs/
│   │   └── AddTransactionDialog.java
│   │
│   ├── frames/
│   │   ├── LoginFrame.java
│   │   ├── RegisterFrame.java
│   │   └── MainFrame.java
│   │
│   └── panels/
│       ├── DashboardPanel.java
│       ├── TransactionsPanel.java
│       ├── CategoriesPanel.java
│       ├── BudgetsPanel.java
│       └── ReportsPanel.java
│
├── resources/
│   └── schema.sql
│
└── Main.java
```

---

# Database Schema

## Users

```sql
CREATE TABLE IF NOT EXISTS users(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    full_name TEXT,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

---

## Categories

```sql
CREATE TABLE IF NOT EXISTS categories(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    type TEXT NOT NULL CHECK(type IN('INCOME','EXPENSE')),
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE(user_id,name,type)
);
```

---

## Transactions

```sql
CREATE TABLE IF NOT EXISTS transactions(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    amount REAL NOT NULL CHECK(amount > 0),
    type TEXT NOT NULL CHECK(type IN ('INCOME','EXPENSE')),
    description TEXT,
    transaction_date TEXT NOT NULL,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY(category_id) REFERENCES categories(id) ON DELETE RESTRICT
);
```

---

## Budgets

```sql
CREATE TABLE IF NOT EXISTS budgets(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    month INTEGER NOT NULL CHECK(month BETWEEN 1 AND 12),
    year INTEGER NOT NULL CHECK(year >= 2000),
    limit_amount REAL NOT NULL CHECK(limit_amount > 0),

    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY(category_id) REFERENCES categories(id) ON DELETE CASCADE,

    UNIQUE(user_id,category_id,month,year)
);
```

---

# Application Architecture

The application follows a layered architecture:

```text
UI Layer
    ↓
Service Layer
    ↓
DAO Layer
    ↓
SQLite Database
```

---

# Layer Responsibilities

## UI Layer

Responsible for:

- Frames
- Panels
- Dialogs
- User interaction
- Table rendering
- Forms and validation messages

---

## Service Layer

Responsible for:

- Business logic
- Validation
- Authentication
- Transaction rules
- Budget calculations

---

## DAO Layer

Responsible for:

- Database communication
- SQL queries
- CRUD operations
- Mapping ResultSet objects to models

---

## Database Layer

SQLite database used for:

- Persistent storage
- Foreign key relationships
- Data integrity
- Constraints

---

# Security

Passwords are never stored as plain text.

The application uses:

```text
BCrypt
```

for secure password hashing.

---

# Setup Instructions

## 1. Clone Repository

```bash
git clone https://github.com/YOUR_USERNAME/personal-finance-manager.git
```

---

## 2. Open Project

Open the project using:

- IntelliJ IDEA
- Eclipse
- VS Code

---

## 3. Add SQLite JDBC Dependency

Download:

```text
https://github.com/xerial/sqlite-jdbc
```

Add the `.jar` file to project libraries.

---

## 4. Add jBCrypt Dependency

Download:

```text
https://www.mindrot.org/projects/jBCrypt/
```

Add the `.jar` file to project libraries.

---

# Running the Application

Run:

```text
Main.java
```

The application will:

- Initialize the database
- Create tables automatically
- Open the Login Window

---

# GUI Overview

## Login Screen

- User authentication
- Secure login validation

---

## Register Screen

- Create new users
- Username uniqueness validation

---

## Dashboard

Displays:

- Total Income
- Total Expenses
- Current Balance
- Recent Transactions

---

## Transactions Panel

Features:

- Add transaction dialog
- Delete transactions
- Filter by date
- Filter by type
- Transaction table

---

## Categories Panel

Features:

- Add categories
- Delete categories
- Income / Expense types

---

## Budgets Panel

Features:

- Monthly budget limits
- Category budgets
- Budget management

---

## Reports Panel

Features:

- Reports by date range
- Income / Expense summary
- Balance calculations
- CSV export support

---

# CSV Export

Reports can be exported as:

```text
.csv
```

Compatible with:

- Microsoft Excel
- Google Sheets
- LibreOffice Calc

---

# Main Functionalities

## Register User

```text
RegisterFrame
    ↓
AuthService.register()
    ↓
UserDAO.addUser()
    ↓
SQLite Database
```

---

## Login User

```text
LoginFrame
    ↓
AuthService.login()
    ↓
MainFrame
```

---

## Add Transaction

```text
TransactionsPanel
    ↓
AddTransactionDialog
    ↓
TransactionService.createTransaction()
    ↓
TransactionDAO.addTransaction()
```

---

## Generate Reports

```text
ReportsPanel
    ↓
TransactionService
    ↓
TransactionDAO
    ↓
SQLite Queries
```

---
