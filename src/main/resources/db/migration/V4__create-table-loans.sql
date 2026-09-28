CREATE TABLE tb_loan (
                         id BIGSERIAL PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         book_id BIGINT NOT NULL,
                         loan_date DATE NOT NULL,
                         due_date DATE NOT NULL,
                         return_date DATE,
                         status VARCHAR(20) NOT NULL,

                         CONSTRAINT fk_loan_user FOREIGN KEY (user_id) REFERENCES tb_user (id),
                         CONSTRAINT fk_loan_book FOREIGN KEY (book_id) REFERENCES tb_book (id)
);