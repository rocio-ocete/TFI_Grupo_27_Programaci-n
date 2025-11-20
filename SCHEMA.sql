-- Crear base de datos
CREATE DATABASE tfi_programacion2
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE tfi_programacion2;

-- TABLA: usuario
CREATE TABLE usuario (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  eliminado BOOLEAN NOT NULL DEFAULT FALSE,
  username VARCHAR(30) NOT NULL UNIQUE,
  email VARCHAR(120) NOT NULL UNIQUE,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  credencial_id BIGINT NULL
);

-- TABLA: credencial_acceso
CREATE TABLE credencial_acceso (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  eliminado BOOLEAN NOT NULL DEFAULT FALSE,
  hash_password VARCHAR(255) NOT NULL,
  salt VARCHAR(64),
  ultimo_cambio DATETIME,
  requiere_reset BOOLEAN NOT NULL DEFAULT FALSE
);

ALTER TABLE usuario
ADD CONSTRAINT fk_usuario_credencial
FOREIGN KEY (credencial_id)
REFERENCES credencial_acceso(id)
ON DELETE SET NULL;


-- Índices opcionales
CREATE INDEX idx_usuario_username ON usuario(username);
CREATE INDEX idx_usuario_email ON usuario(email);
