-- Drop tables if they exist
DROP TABLE IF EXISTS invoice_product;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS family;
DROP TABLE IF EXISTS invoice;

-- Create family table
CREATE TABLE family (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create product table
CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_family INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (id_family) REFERENCES family(id)
);

-- Create invoice table
CREATE TABLE invoice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    price FLOAT NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    paid BOOLEAN NOT NULL DEFAULT FALSE
);

-- Create invoice_product table
CREATE TABLE invoice_detail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    id_invoice INT NOT NULL,
    price FLOAT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (id_invoice) REFERENCES invoice(id)
);

-- Indexes
CREATE INDEX idx_product_family ON product(id_family);
CREATE INDEX idx_invoice_product_product ON invoice_product(product_id);
CREATE INDEX idx_invoice_product_invoice ON invoice_product(id_invoice);

-- Insert family data
INSERT INTO family (name) VALUES
('Electronics'),
('Books'),
('Clothing');

-- Insert product data
INSERT INTO product (id_family, name, price, quantity) VALUES
-- Electronics
(1, 'Smartphone', 599.99, 20),
(1, 'Laptop', 999.99, 15),
(1, 'Bluetooth Speaker', 49.99, 30),
(1, 'Wireless Headphones', 89.99, 25),
(1, 'Smartwatch', 149.99, 18),
(1, 'Tablet', 299.99, 12),
(1, 'Monitor 24"', 129.99, 10),
(1, 'External Hard Drive 1TB', 79.99, 40),

-- Books
(2, 'Novel', 12.99, 100),
(2, 'Cookbook', 24.99, 40),
(2, 'Science Textbook', 39.99, 30),
(2, 'History Book', 18.99, 20),
(2, 'Fantasy Book', 15.50, 60),
(2, 'Self-Help Guide', 13.49, 50),
(2, 'Children Storybook', 9.99, 70),

-- Clothing
(3, 'T-Shirt', 9.99, 50),
(3, 'Jeans', 29.99, 40),
(3, 'Jacket', 59.99, 25),
(3, 'Sneakers', 49.99, 35),
(3, 'Cap', 14.99, 45),
(3, 'Sweater', 34.99, 20),
(3, 'Dress', 44.99, 15),
(3, 'Socks (Pack of 5)', 7.99, 80);
