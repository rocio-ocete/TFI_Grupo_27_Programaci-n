package Service;

import config.DatabaseConfig;
import dao.UsuarioDAOImpl;
import dao.CredencialAccesoDAOImpl;
import entities.Usuario;

import java.sql.Connection;
import java.util.List;

public class UsuarioService implements GenericService<Usuario> {

    private final UsuarioDAOImpl usuarioDao = new UsuarioDAOImpl();
    private final CredencialAccesoDAOImpl credDao = new CredencialAccesoDAOImpl();

    @Override
    public Usuario insertar(Usuario usuario) throws Exception {

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);

            validar(usuario);

            // 1 → Primero crear credencial
            if (usuario.getCredencial() != null) {
                credDao.crear(usuario.getCredencial(), conn);
            }

            // 2 → Después crear usuario
            Usuario creado = usuarioDao.crear(usuario, conn);

            conn.commit(); 
            return creado;

        } catch (Exception e) {
            throw new Exception("Error al insertar usuario: " + e.getMessage());
        }
    }

    @Override
    public Usuario actualizar(Usuario usuario) throws Exception {
        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);

            validar(usuario);

            if (usuario.getCredencial() != null) {
                credDao.actualizar(usuario.getCredencial(), conn);
            }

            Usuario actualizado = usuarioDao.actualizar(usuario, conn);

            conn.commit();
            return actualizado;
        }
    }

    @Override
    public boolean eliminar(long id) throws Exception {
        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);

            boolean ok = usuarioDao.eliminar(id, conn);

            conn.commit();
            return ok;

        } catch (Exception e) {
            throw new Exception("Error eliminando usuario: " + e.getMessage());
        }
    }

    @Override
    public Usuario getById(long id) throws Exception {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return usuarioDao.leer(id, conn);
        }
    }

    @Override
    public List<Usuario> getAll() throws Exception {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return usuarioDao.leerTodos(conn);
        }
    }

    private void validar(Usuario u) throws Exception {
        if (u.getUsername() == null || u.getUsername().isBlank())
            throw new Exception("El username no puede estar vacío.");

        if (u.getEmail() == null || u.getEmail().isBlank())
            throw new Exception("El email es obligatorio.");

        if (!u.getEmail().contains("@"))
            throw new Exception("Formato de email inválido.");
    }
}
