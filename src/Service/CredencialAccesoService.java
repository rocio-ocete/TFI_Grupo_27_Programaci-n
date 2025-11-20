package Service;

import config.DatabaseConfig;
import dao.CredencialAccesoDAOImpl;
import entities.CredencialAcceso;

import java.sql.Connection;
import java.util.List;

public class CredencialAccesoService implements GenericService<CredencialAcceso> {

    private final CredencialAccesoDAOImpl credDao = new CredencialAccesoDAOImpl();

    @Override
    public CredencialAcceso insertar(CredencialAcceso cred) throws Exception {
        if (cred == null) {
            throw new IllegalArgumentException("La credencial no puede ser null.");
        }

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                validar(cred);
                CredencialAcceso creada = credDao.crear(cred, conn);
                conn.commit();
                return creada;
            } catch (Exception e) {
                conn.rollback();
                throw new Exception("Error al insertar la credencial: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public CredencialAcceso actualizar(CredencialAcceso cred) throws Exception {
        if (cred == null) {
            throw new IllegalArgumentException("La credencial no puede ser null.");
        }

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                validar(cred);
                CredencialAcceso updated = credDao.actualizar(cred, conn);
                conn.commit();
                return updated;
            } catch (Exception e) {
                conn.rollback();
                throw new Exception("Error al actualizar la credencial: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public boolean eliminar(long id) throws Exception {
        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                boolean ok = credDao.eliminar(id, conn);
                conn.commit();
                return ok;
            } catch (Exception e) {
                conn.rollback();
                throw new Exception("Error al eliminar la credencial: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public CredencialAcceso getById(long id) throws Exception {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return credDao.leer(id, conn);
        }
    }

    @Override
    public List<CredencialAcceso> getAll() throws Exception {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return credDao.leerTodos(conn);
        }
    }

    private void validar(CredencialAcceso cred) throws Exception {
        if (cred.getHashPassword() == null || cred.getHashPassword().isBlank())
            throw new Exception("La contraseña hash no puede ser vacía.");

        if (cred.getSalt() == null || cred.getSalt().isBlank())
            throw new Exception("El salt es obligatorio.");
    }
}
