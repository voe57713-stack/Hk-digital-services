# Hamad Hassan Digital Store — cPanel & Server Deployment Guide

## 1. Requirements
- PHP 8.2+ with `cURL`, `OpenSSL`, `JSON`, `PDO_MySQL` extensions enabled.
- MySQL 5.7+ or MariaDB 10.4+.
- SSL Certificate (HTTPS mandatory for automated callbacks).

## 2. Database Installation
1. Log in to your cPanel.
2. Go to **MySQL Databases** and create a new database: `hamadhassan_store`.
3. Create a new user with strong password and assign **ALL PRIVILEGES** to `hamadhassan_store`.
4. Open **phpMyAdmin**, select the created database, click **Import**, and upload `backend/schema.sql`.

## 3. MakerCC Supplier API Integration
1. Obtain your API key from [https://makercc.shop/](https://makercc.shop/).
2. Keep your MakerCC wallet topped up in advance (Account 1).
3. The backend uses the official endpoint: `https://makercc.shop/api/v1/index.php`.
4. Enter your key inside Admin Panel > MakerCC API settings or in protected server environment variables:
   ```
   MAKERCC_API_KEY=your_secret_key_here
   ```

## 4. Payment Gateway Webhook URLs
Configure your Merchant Portals with these server callback URLs:
- **JazzCash Instant IPN / Webhook:** `https://yourdomain.pk/backend/payment_callback.php?gateway=jazzcash`
- **Easypaisa Payment Notification:** `https://yourdomain.pk/backend/payment_callback.php?gateway=easypaisa`

## 5. Store Support Contact
- **Store Name:** Hamad Hassan Digital Store
- **WhatsApp Support:** `+92 335 4313211`
- **Direct WhatsApp Link:** `https://wa.me/923354313211`
- **Currency:** Pakistani Rupees (PKR)
