-- H2
CREATE TABLE users (
    id IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    birth_date DATE NOT NULL,
    has_driver_license BOOLEAN NOT NULL
);

INSERT INTO users (name, age, birth_date, has_driver_license) VALUES 
('Claudio Henrique', 30, '1994-05-15', TRUE),
('Elisabeth', 28, '1996-07-22', FALSE),
('Marcos Silva', 35, '1989-03-10', TRUE),
('Ana Paula', 26, '1998-09-05', FALSE),
('Fernando Costa', 40, '1984-12-18', TRUE),
('Juliana Mendes', 29, '1995-06-30', TRUE),
('Ricardo Almeida', 32, '1992-08-14', FALSE),
('Tatiane Souza', 27, '1997-11-03', TRUE),
('Gustavo Lima', 31, '1993-04-25', FALSE),
('Beatriz Rocha', 33, '1991-10-08', TRUE);


-- MYSQL
-- CREATE TABLE users (
--     id SERIAL PRIMARY KEY,
--     name VARCHAR(100) NOT NULL,
--     age INT NOT NULL,
--     birth_date DATE NOT NULL,
--     has_driver_license BOOLEAN NOT NULL
-- );

-- INSERT INTO users (name, age, birth_date, has_driver_license) VALUES 
-- ('Claudio Henrique', 30, '1994-05-15', TRUE),
-- ('Elisabeth', 28, '1996-07-22', FALSE),
-- ('Marcos Silva', 35, '1989-03-10', TRUE),
-- ('Ana Paula', 26, '1998-09-05', FALSE),
-- ('Fernando Costa', 40, '1984-12-18', TRUE),
-- ('Juliana Mendes', 29, '1995-06-30', TRUE),
-- ('Ricardo Almeida', 32, '1992-08-14', FALSE),
-- ('Tatiane Souza', 27, '1997-11-03', TRUE),
-- ('Gustavo Lima', 31, '1993-04-25', FALSE),
-- ('Beatriz Rocha', 33, '1991-10-08', TRUE);


-- POSTGRES
-- CREATE TABLE users (
--     id INT PRIMARY KEY AUTO_INCREMENT,
--     name VARCHAR(100) NOT NULL,
--     age INT NOT NULL,
--     birth_date DATE NOT NULL,
--     has_driver_license BOOLEAN NOT NULL
-- );

-- INSERT INTO users (name, age, birth_date, has_driver_license) VALUES 
-- ('Claudio Henrique', 30, '1994-05-15', TRUE),
-- ('Elisabeth', 28, '1996-07-22', FALSE),
-- ('Marcos Silva', 35, '1989-03-10', TRUE),
-- ('Ana Paula', 26, '1998-09-05', FALSE),
-- ('Fernando Costa', 40, '1984-12-18', TRUE),
-- ('Juliana Mendes', 29, '1995-06-30', TRUE),
-- ('Ricardo Almeida', 32, '1992-08-14', FALSE),
-- ('Tatiane Souza', 27, '1997-11-03', TRUE),
-- ('Gustavo Lima', 31, '1993-04-25', FALSE),
-- ('Beatriz Rocha', 33, '1991-10-08', TRUE);
