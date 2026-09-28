CREATE TABLE tb_book (
                         id BIGSERIAL PRIMARY KEY,
                         title VARCHAR(150) NOT NULL,
                         author VARCHAR(100) NOT NULL,
                         release_date DATE,
                         total_quantity INT NOT NULL,
                         in_use_quantity INT NOT NULL DEFAULT 0,
                         publisher_id BIGINT,
                         CONSTRAINT fk_book_publisher FOREIGN KEY (publisher_id) REFERENCES tb_publisher(id)
);