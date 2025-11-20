package main;

import config.DatabaseConfig;
import dao.UsuarioDAOImpl;
import dao.CredencialAccesoDAOImpl;
import entities.Usuario;
import entities.CredencialAcceso;

import java.sql.Connection;
import java.time.LocalDateTime;

public class TestUsuarioDAO {

    public static void main(String[] args) {
        // Paso 1: Declaramos la conexión
        Connection conn = null;

        try {
            // Paso 2: Obtenemos la conexión desde DatabaseConfig
            conn = DatabaseConfig.getConnection();
            // Paso 3: Iniciamos una transacción
            conn.setAutoCommit(false);

            // Paso 4: Instanciamos los DAOs
            CredencialAccesoDAOImpl credDao = new CredencialAccesoDAOImpl();
            UsuarioDAOImpl usuarioDao = new UsuarioDAOImpl();

            // -----------------------------
            // Paso 5: Creamos una CredencialAcceso
            CredencialAcceso cred = new CredencialAcceso();
            cred.setHashPassword("miHash123");
            cred.setSalt("saltsito");
            cred.setUltimoCambio(LocalDateTime.now());
            cred.setRequiereReset(false);
            cred.setEliminado(false);

            // Paso 6: Creamos un Usuario usando esa credencial
            Usuario user = new Usuario();
            user.setUsername("yamila123");
            user.setEmail("yamila@example.com");
            user.setActivo(true);
            user.setFechaRegistro(LocalDateTime.now());
            user.setEliminado(false);
            user.setCredencial(cred);

            // -----------------------------
            // Paso 7: Guardamos el usuario (esto también guardará la credencial)
            usuarioDao.crear(user, conn);

            // Paso 8: Commit de la transacción
            conn.commit();
            System.out.println("✔ Usuario y Credencial creados correctamente:");
            System.out.println(user);

            // -----------------------------
            // Paso 9: Leer usuario desde la DB para verificar
            Usuario userLeido = usuarioDao.leer(user.getId(), conn);
            System.out.println("✔ Usuario leído desde la base:");
            System.out.println(userLeido);

        } catch (Exception e) {
            // Paso 10: Si algo falla, hacemos rollback
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            // Paso 11: Cerramos la conexión
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}