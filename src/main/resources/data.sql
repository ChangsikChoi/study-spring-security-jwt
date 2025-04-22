INSERT INTO `user` (id, user_name, user_password, role, enabled, user_nickname)
VALUES (
    'c5fd1f0e-9b0b-4f77-b9c0-abc123def456',
    'admin',
    '$2a$10$rBd7k9RLxQQCNs42VUZvuuBe8v1FxnlYoTXLUGfGUB7oT5hP0pQpK',
    'ADMIN',
    true,
    '관리자'
);

INSERT INTO `user` (id, user_name, user_password, role, enabled, user_nickname)
VALUES (
    'c5fd1f0e-9b0b-4f77-b9c0-abc123def455',
    'test',
    '$2a$10$rBd7k9RLxQQCNs42VUZvuuBe8v1FxnlYoTXLUGfGUB7oT5hP0pQpK',
    'USER',
    true,
    '테스트유저'
);