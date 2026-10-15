-- =============================================================================
-- HAMAD HASSAN DIGITAL STORE — MySQL Database Schema
-- Production Ready for PHP 8.2+ & cPanel MySQL / MariaDB
-- Currency: PKR | 24/7 Automated Fulfillment & Separate Accounts
-- =============================================================================

CREATE TABLE IF NOT EXISTS `users` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(60) NOT NULL UNIQUE,
  `full_name` VARCHAR(120) NOT NULL,
  `email` VARCHAR(160) NOT NULL UNIQUE,
  `whatsapp_number` VARCHAR(30) NOT NULL,
  `password_hash` VARCHAR(255) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `admin_users` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(60) NOT NULL UNIQUE,
  `password_hash` VARCHAR(255) NOT NULL,
  `role` VARCHAR(20) DEFAULT 'superadmin',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `categories` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(80) NOT NULL,
  `slug` VARCHAR(80) NOT NULL UNIQUE,
  `display_order` INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `products` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(191) NOT NULL,
  `category_id` INT DEFAULT NULL,
  `description` TEXT,
  `retail_price_pkr` DECIMAL(10,2) NOT NULL,     -- What customer pays in PKR
  `supplier_cost_pkr` DECIMAL(10,2) DEFAULT 0.00, -- Cost from MakerCC (0 if own product)
  `stock_count` INT DEFAULT 100,
  `product_source` ENUM('makercc_api', 'my_own_product') NOT NULL DEFAULT 'makercc_api',
  `supplier_service_id` VARCHAR(100) DEFAULT NULL,
  `delivery_method` ENUM('automatic', 'manual') NOT NULL DEFAULT 'automatic',
  `validity_period` VARCHAR(60) DEFAULT '1 Month',
  `warranty_days` INT DEFAULT 30,
  `is_active` TINYINT(1) DEFAULT 1,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `custom_digital_inventory` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `product_id` INT NOT NULL,
  `credential_payload` TEXT NOT NULL,           -- License key or email:password
  `is_allocated` TINYINT(1) DEFAULT 0,
  `allocated_order_id` INT DEFAULT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `orders` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `order_number` VARCHAR(40) NOT NULL UNIQUE,
  `customer_name` VARCHAR(120) NOT NULL,
  `customer_email` VARCHAR(160) NOT NULL,
  `customer_whatsapp` VARCHAR(30) NOT NULL,
  `product_id` INT NOT NULL,
  `product_name` VARCHAR(191) NOT NULL,
  `product_source` ENUM('makercc_api', 'my_own_product') NOT NULL,
  `quantity` INT DEFAULT 1,
  -- Store Customer Sales Accounting (Account 2)
  `customer_paid_pkr` DECIMAL(10,2) NOT NULL,
  `payment_gateway` VARCHAR(50) NOT NULL,       -- JazzCash, Easypaisa, Raast, Card
  `payment_transaction_id` VARCHAR(100) NOT NULL,
  `gateway_fee_pkr` DECIMAL(10,2) DEFAULT 0.00,
  -- Supplier Purchasing Accounting (Account 1 - MakerCC)
  `supplier_cost_pkr` DECIMAL(10,2) DEFAULT 0.00,
  `supplier_order_id` VARCHAR(80) DEFAULT NULL,
  -- Real-time Profit Calculation
  `gross_profit_pkr` DECIMAL(10,2) NOT NULL,
  `net_profit_pkr` DECIMAL(10,2) NOT NULL,
  -- Delivery status
  `status` ENUM('completed', 'processing', 'failed', 'pending_manual') DEFAULT 'completed',
  `delivery_credentials` TEXT,
  `failure_reason` TEXT,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `supplier_wallet_transactions` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `supplier_name` VARCHAR(50) DEFAULT 'MakerCC',
  `order_id` INT DEFAULT NULL,
  `supplier_order_id` VARCHAR(80) DEFAULT NULL,
  `deducted_amount_pkr` DECIMAL(10,2) NOT NULL,
  `remaining_balance_pkr` DECIMAL(10,2) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `customer_reviews` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `customer_name` VARCHAR(100) NOT NULL,
  `rating` INT DEFAULT 5,
  `comment` TEXT NOT NULL,
  `purchased_product` VARCHAR(150),
  `is_approved` TINYINT(1) DEFAULT 1,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `whatsapp_feedback` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `customer_name` VARCHAR(80) NOT NULL,
  `caption` VARCHAR(255) NOT NULL,
  `image_path` VARCHAR(255) NOT NULL,
  `preview_snippet` TEXT,
  `display_order` INT DEFAULT 1,
  `is_published` TINYINT(1) DEFAULT 1,
  `is_obscured_for_privacy` TINYINT(1) DEFAULT 1,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `social_media_links` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `platform` VARCHAR(50) NOT NULL UNIQUE,
  `url` VARCHAR(255) NOT NULL,
  `is_enabled` TINYINT(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `store_settings` (
  `setting_key` VARCHAR(60) PRIMARY KEY,
  `setting_value` TEXT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
