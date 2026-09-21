CREATE TABLE tb_publisher (
                              id BIGSERIAL PRIMARY KEY,
                              name VARCHAR(150) NOT NULL,
                              email VARCHAR(100) UNIQUE,
                              phone VARCHAR(20),
                              website VARCHAR(150)
);