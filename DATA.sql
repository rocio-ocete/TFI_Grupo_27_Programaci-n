USE tfi_programacion2;

--  Tabla Usuario
INSERT INTO usuario (username, email, activo)
VALUES
('juanp', 'juan.perez@example.com', TRUE),
('mariaa', 'maria.arias@example.com', TRUE);

-- Tabla credencial_acceso
INSERT INTO credencial_acceso (hash_password, salt, ultimo_cambio, requiere_reset, usuario_id)
VALUES
('hash-de-ejemplo-juan', 'salt123', NOW(), FALSE, 
 (SELECT id FROM usuario WHERE username='juanp' LIMIT 1)),
('hash-de-ejemplo-maria', 'salt456', NOW(), FALSE, 
 (SELECT id FROM usuario WHERE username='mariaa' LIMIT 1));

SELECT * FROM usuario;
SELECT * FROM credencial_acceso;


