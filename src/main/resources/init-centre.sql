-- Drop tables in correct order (child tables first)
DROP TABLE IF EXISTS invoice_detail;
DROP TABLE IF EXISTS shop_invoice;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS shop;
DROP TABLE IF EXISTS db;

-- Create tables
CREATE TABLE db (
    id INT AUTO_INCREMENT PRIMARY KEY,
    db_name VARCHAR(255) NOT NULL,
    id_shop INT NOT NULL
);

CREATE TABLE shop (
    id INT AUTO_INCREMENT PRIMARY KEY,
    shop_name VARCHAR(255) NOT NULL
);

CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL,
    quantity INT NOT NULL
);

CREATE TABLE shop_invoice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_copy INT NOT NULL,
    id_shop INT NOT NULL,
    price FLOAT NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    paid BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (id_shop) REFERENCES shop(id)
);

CREATE TABLE invoice_detail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_detail_copy INT NOT NULL,
    id_invoice_copy INT NOT NULL,
    id_invoice INT NOT NULL,
    product_id INT NOT NULL,
    price FLOAT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (id_invoice) REFERENCES shop_invoice(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

-- Add foreign key constraint to database table
ALTER TABLE database ADD CONSTRAINT fk_database_shop FOREIGN KEY (id_shop) REFERENCES shop(id);