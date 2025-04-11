-- MYSQL
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    birth_date DATE NOT NULL,
    has_driver_license BOOLEAN NOT NULL
);

INSERT INTO users (name, age, birth_date, has_driver_license) VALUES 
('Claudio Henrique', 30, '1994-05-15', TRUE),
('Elisabeth', 30, '1996-07-22', FALSE),
('Marcos Silva', 30, '1989-03-10', TRUE),
('Ana Paula', 30, '1998-09-05', FALSE),
('Fernando Costa', 40, '1984-12-18', TRUE),
('Juliana Mendes', 30, '1995-06-30', TRUE),
('Ricardo Almeida', 32, '1992-08-14', FALSE),
('Tatiane Souza', 27, '1997-11-03', TRUE),
('Gustavo Lima', 29, '1993-04-25', FALSE),
('Beatriz Rocha', 33, '1991-10-08', TRUE);

CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    order_date DATE NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO orders (user_id, order_date, total_amount, status) VALUES 
(1, '2024-03-01', 150.75, 'Pendente'),
(1, '2024-03-02', 200.50, 'Finalizado'),
(1, '2024-03-03', 89.99, 'Cancelado'),
(4, '2024-03-04', 120.00, 'Pendente'),
(4, '2024-03-05', 300.25, 'Finalizado'),
(6, '2024-03-06', 50.00, 'Pendente'),
(7, '2024-03-07', 450.60, 'Finalizado'),
(8, '2024-03-08', 99.99, 'Cancelado'),
(9, '2024-03-09', 210.40, 'Pendente'),
(10, '2024-03-10', 75.30, 'Finalizado');