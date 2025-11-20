package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String URL = "jdbc:mysql://localhost:3306/tfi_programacion2?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";   // CAMBIAR SI TU MYSQL TIENE OTRO USUARIO
    private static final String PASS = "";       // CAMBIAR SI TENÉS CONTRASEÑA

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
