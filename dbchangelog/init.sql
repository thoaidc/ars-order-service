CREATE DATABASE IF NOT EXISTS `ars_order` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `ars_order`;

SET FOREIGN_KEY_CHECKS = 0;

-- ============================
-- TABLE: cart
-- ============================
DROP TABLE IF EXISTS cart;
CREATE TABLE cart (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    quantity INT DEFAULT 0,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================
-- TABLE: cart_product
-- ============================
DROP TABLE IF EXISTS cart_product;
CREATE TABLE cart_product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT NOT NULL,
    product_id INT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    thumbnail VARCHAR(255) NOT NULL,
    price DECIMAL(21,6) DEFAULT 0.00 NOT NULL,
    data VARCHAR(1000),
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================
-- TABLE: orders
-- ============================
DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    customer_id INT NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    amount DECIMAL(21,6) DEFAULT 0.00 NOT NULL,
    discount DECIMAL(21,6) DEFAULT 0.00 NOT NULL,
    total_amount DECIMAL(21,6) DEFAULT 0.00 NOT NULL,
    payment_method VARCHAR(100),
    payment_status VARCHAR(50) NOT NULL,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================
-- TABLE: sub_order (Đơn hàng con của từng shop)
-- ============================
DROP TABLE IF EXISTS sub_order;
CREATE TABLE sub_order (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    order_id INT NOT NULL,
    shop_id INT NOT NULL,
    customer_id INT NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    amount DECIMAL(21,6) DEFAULT 0.00 NOT NULL,
    discount DECIMAL(21,6) DEFAULT 0.00 NOT NULL,
    total_amount DECIMAL(21,6) DEFAULT 0.00 NOT NULL,
    payment_method VARCHAR(100),
    payment_status VARCHAR(50) NOT NULL,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================
-- TABLE: order_product
-- ============================
DROP TABLE IF EXISTS order_product;
CREATE TABLE order_product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    sub_order_id INT NOT NULL,
    shop_id INT NOT NULL,
    product_id INT NOT NULL,
    product_code VARCHAR(100) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_thumbnail VARCHAR(255) NOT NULL,
    note VARCHAR(500),
    data VARCHAR(1000),
    total_amount DECIMAL(21,6) DEFAULT 0.00 NOT NULL,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================
-- TABLE: outbox
-- ============================
DROP TABLE IF EXISTS outbox;
CREATE TABLE outbox (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ref_id INT NOT NULL,
    type VARCHAR(100) NOT NULL,
    value VARCHAR(1000) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_outbox_type_status_id ON outbox (type, status, id DESC);
CREATE INDEX idx_outbox_status_id ON outbox (status, id DESC);


SET FOREIGN_KEY_CHECKS = 1;
