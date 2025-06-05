-- Drop tables in correct order (child tables first)
DROP TABLE IF EXISTS invoice_detail;
DROP TABLE IF EXISTS shop_invoice;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS shop;

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


-- Insert shop data
INSERT INTO shop (shop_name) VALUES
                                 ('TechWorld Store');

-- Insert product data (matching the first database)
INSERT INTO product (name, price, quantity) VALUES
-- Electronics products
('Smartphone', 599.99, 20),
('Laptop', 999.99, 15),
('Bluetooth Speaker', 49.99, 30),
('Wireless Headphones', 89.99, 25),
('Smartwatch', 149.99, 18),
('Tablet', 299.99, 12),
('Monitor 24"', 129.99, 10),
('External Hard Drive 1TB', 79.99, 40),

-- Books products
('Novel', 12.99, 100),
('Cookbook', 24.99, 40),
('Science Textbook', 39.99, 30),
('History Book', 18.99, 20),
('Fantasy Book', 15.50, 60),
('Self-Help Guide', 13.49, 50),
('Children Storybook', 9.99, 70),

-- Clothing products
('T-Shirt', 9.99, 50),
('Jeans', 29.99, 40),
('Jacket', 59.99, 25),
('Sneakers', 49.99, 35),
('Cap', 14.99, 45),
('Sweater', 34.99, 20),
('Dress', 44.99, 15),
('Socks (Pack of 5)', 7.99, 80);

-- Add some indexes for better performance
CREATE INDEX idx_shop_invoice_shop ON shop_invoice(id_shop);
CREATE INDEX idx_shop_invoice_date ON shop_invoice(date);
CREATE INDEX idx_invoice_detail_invoice ON invoice_detail(id_invoice);
CREATE INDEX idx_invoice_detail_product ON invoice_detail(product_id);