INSERT INTO tb_user (
    name,
    email,
    cpf,
    phone,
    date_of_birth,
    address,
    password,
    is_admin,
    is_disabled
) VALUES (
             'Administrador',
             'usuario@admin.com',
             '000.000.000-00',
             '88900000000',
             '2000-01-01',
             'Biblioteca Central',
             '$2a$10$E2UPv7arXym3L0.2C3y34.fB8xY34L./8s6W8hN68J6/N2.6k741O',
             TRUE,
             FALSE
         );