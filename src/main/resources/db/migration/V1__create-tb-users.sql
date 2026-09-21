CREATE TABLE tb_user (
        id BIGSERIAL PRIMARY KEY,
        name VARCHAR(150) NOT NULL,
        email VARCHAR(100) NOT NULL UNIQUE,
        cpf VARCHAR(14) NOT NULL UNIQUE,
        phone VARCHAR(20) NOT NULL,
        date_of_birth DATE NOT NULL,
        address VARCHAR(150) NOT NULL,
        is_admin BOOLEAN NOT NULL DEFAULT FALSE,
        is_disabled BOOLEAN NOT NULL DEFAULT FALSE,
        password VARCHAR(255) NOT NULL
);