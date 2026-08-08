# ZUS Coffee Beverage Database - Year 1 Sem 2 (SQL)

An Oracle SQL database project for a fictional coffee chain (ZUS Coffee), 
covering database design, table creation, sample data population, and 
management reports for sales, membership, and inventory analysis.

## Features

- **Database Design** — Five normalized tables (Customer, Branch, Product, 
  Orders, Order_Item) with primary/foreign key relationships and check 
  constraints
- **Sample Data** — Populated with realistic sample records for customers, 
  branches, products, and orders
- **Sales Reports** — SQL*Plus interactive reports for branch performance, 
  top-selling products, and payment status monitoring
- **Membership Analysis** — Sales contribution breakdown by customer 
  membership tier
- **Operational Insights** — Peak hour sales analysis and low/no-sales 
  product exception reporting

## Project Structure

```
year1-sem2-sql/
├── ZUS_BEVERAGE_FULL.sql   # Full script: DDL + sample data + reports
├── 01_ddl.sql              # Table creation (Customer, Branch, Product, Orders, Order_Item)
├── 02_inserts.sql          # Sample data INSERT statements
└── 03_queries.sql          # SQL*Plus report scripts (Task 6)
```

## Database Schema

| Table | Description |
|---|---|
| `CUSTOMER` | Customer details and membership tier |
| `BRANCH` | Branch outlet details and manager info |
| `PRODUCT` | Menu items with category, price, and availability |
| `ORDERS` | Order header (date, time, payment, customer, branch) |
| `ORDER_ITEM` | Order line items (product, quantity) |

## Reports Included

1. Branch Sales Performance Analysis
2. Top-Selling Products by Revenue
3. Sales Contribution by Member Tier
4. Payment Status Monitoring by Branch
5. Peak Hour Sales Analysis
6. Low and No Sales Products Exception Report

## How to Run

```sql
-- In SQL*Plus or Oracle SQL Developer
@ZUS_BEVERAGE_FULL.sql
```

Reports in `03_queries.sql` prompt for parameters (date range, branch, 
payment method, etc.) using `ACCEPT`, so run them interactively in 
SQL*Plus rather than as a silent batch job.

## What I Learned

Working on this project helped me practice relational database design — 
defining primary/foreign keys and constraints across related tables — as 
well as writing multi-table joins, aggregate functions, and parameterized 
SQL*Plus reports with `ACCEPT`, `COLUMN`, `BREAK`, and `COMPUTE` for 
formatted output.
