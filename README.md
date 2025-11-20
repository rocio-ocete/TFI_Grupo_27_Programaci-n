# Integrantes y Roles # 
Yamila Fernández -	COMISIÓN 2 - UML y DAO
Alicia Yasmin Ahumada - COMISIÓN 1 - Entidades
Camila Lihuen Rissi - COMISIÓN 4 - Base de datos y scripts SQL
Rocio Milagros Ocete - COMISIÓN	3 - Service y Menú por consola

Enlace al DRIVE: https://drive.google.com/drive/folders/1pdo4r4Uij1Sjhu1qubwdfO1tGRwpE10u?usp=sharing

Enlace al VIDEO: https://drive.google.com/file/d/1rr7tTpEV7nC7lRISSRTLlP_aWUNawE--/view?usp=sharing

# Sistema de Gestión Usuario → CredencialAcceso

Aplicación Java con arquitectura por capas, JDBC, relación 1→1 y menú por consola.

---

## Descripción del Dominio

El dominio implementado es **Usuario → CredencialAcceso**, con relación **1→1 unidireccional**, donde:

- Cada **Usuario** tiene exactamente **una** CredencialAcceso.  
- La credencial **no referencia** al usuario en el modelo de objetos, manteniendo simplicidad.  
- La relación se implementa mediante una **foreign key única** en MySQL.  

Este dominio permite un escenario real de autenticación con controles de seguridad como:

- Baja lógica  
- Activación/desactivación  
- Reset de contraseña  
- Trazabilidad del último cambio  

Además, habilita una arquitectura limpia con DAO + Service + JDBC transaccional.

---

## Requisitos

### **Software necesario**
| Requisito | Versión recomendada |
|----------|---------------------|
| Java | **JDK 17 o superior** |
| MySQL | **MySQL Server 8.x** |
| Conector JDBC | `mysql-connector-j-8.x.jar` |
| IDE (opcional) | IntelliJ / Eclipse / NetBeans |
| Terminal | Para ejecutar AppMenu |

---

## Instalación de la Base de Datos

El repositorio incluye:


### **Pasos para crear la base**
1. Abrir MySQL Workbench o CLI.  
2. Ejecutar el script:

### Estructura creada ###
Tabla usuario

- id (PK, autoincremental)

- username (único)

- email (único)

- activo, eliminado

- fecha_registro

Tabla credencial_acceso

- id (PK)

- hash_password

- salt

- ultimo_cambio

- requiere_reset

- usuario_id (FK único → usuario.id)

Incluye:

ON DELETE CASCADE (que mantiene la integridad de la relación 1→1.)

ESTRUCTURA DEL PROYECTO
src/
 ├── dao/
 │    ├── GenericDao.java
 │    ├── UsuarioDAOImpl.java
 │    └── CredencialAccesoDAOImpl.java
 └── main/
 │    ├── AppMenu.java
 │    ├── Main.java
 │    ├── TestConexion.java
 │    └── TestUsuario.java
 └── service/
 │     ├── UsuarioService.java
 │     ├── GenericService.java
 │     └── CredencialAccesoService.java
 └── config/
 │    ├── DatabaseConfig.java
 │    └── DatabaseConnection.java
 └── entities/
      ├── Usuario.java
      └── CredencialAcceso.java

Arquitectura

- Entities: modelos (Usuario, CredencialAcceso)
- DAO: acceso directo con JDBC
- Service: transacciones, reglas de negocio
- Main: interfaz de consola (CRUD completo)

### Compilación y Ejecución ###
Compilar

javac -cp ".;lib/mysql-connector-j-8.x.jar" -d out src/**/*.java

(En Linux/macOS reemplazar ; por :.)

Ejecutar
java -cp "out;lib/mysql-connector-j-8.x.jar" main.AppMenu

### Flujo de Uso del Menú ###
## Menú Principal ##
1 - Gestionar Usuarios
2 - Gestionar Credenciales
0 - Salir

# Operaciones de Usuarios #

- Crear usuario + credencial (transacción)

- Leer por ID

- Listar todos

- Actualizar

- Baja lógica

- Buscar por username

# Operaciones de Credenciales #

- Crear

- Leer por ID

- Listar todas

- Actualizar (cambio/reset contraseña)

- Eliminar

# El sistema incluye validaciones: #

- email con formato válido

- campos obligatorios

- IDs inexistentes

- integridad 1→1

- manejo de SQLException
