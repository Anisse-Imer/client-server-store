-- Drop tables if they exist to start fresh
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

-- Create invoice_product junction table
CREATE TABLE invoice_product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    id_invoice INT NOT NULL,
    price FLOAT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (id_invoice) REFERENCES invoice(id)
);

-- Add indexes for better performance
CREATE INDEX idx_product_family ON product(id_family);
CREATE INDEX idx_invoice_product_product ON invoice_product(product_id);
CREATE INDEX idx_invoice_product_invoice ON invoice_product(id_invoice);
