INSERT INTO `usuarios` (username, password, enabled, nombre, apellido, email) VALUES ('juan', '$2a$10$RzIr/B0JYJVMBU/TmD0SaO46..G5U4qdUrDifuL5dBqqNS/4rToJ2', 1, 'Juan', 'Pardinas', 'jpardinas23@gmail.com');
INSERT INTO `usuarios` (username, password, enabled, nombre, apellido, email) VALUES ('admin', '$2a$10$ObdwtG4D1DIufwJJCIGwt.EL5gZK8c4pGoBMzifi3WLNNzZi7BMU2', 1, 'Admin', 'admin', 'test@gmail.com');

INSERT INTO `roles` (nombre) VALUES ('ROLE_USER');
INSERT INTO `roles` (nombre) VALUES ('ROLE_ADMIN');

INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (1,2);
INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (2,2);
INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (2,1);