package dao;

import entities.Usuario;
import entities.CredencialAcceso;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements GenericDao<Usuario> {

    private final CredencialAccesoDAOImpl credDao = new CredencialAccesoDAOImpl();

    @Override
    public Usuario crear(Usuario entity, Connection conn) throws Exception {
        // Primero crear la credencial si existe
        if (entity.getCredencial() != null) {
            credDao.crear(entity.getCredencial(), conn);
        }

        String sql = "INSERT INTO usuario (username, email, activo, fecha_registro, eliminado, credencial_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getEmail());
            ps.setBoolean(3, entity.isActivo());
            ps.setTimestamp(4, entity.getFechaRegistro() != null ? Timestamp.valueOf(entity.getFechaRegistro()) : null);
            ps.setBoolean(5, entity.isEliminado());
            ps.setObject(6, entity.getCredencial() != null ? entity.getCredencial().getId() : null);

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }
        }
        return entity;
    }

    @Override
    public Usuario leer(long id, Connection conn) throws Exception {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs, conn);
                }
            }
        }
        return null;
    }

    @Override
    public List<Usuario> leerTodos(Connection conn) throws Exception {
        String sql = "SELECT * FROM usuario";
        List<Usuario> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs, conn));
            }
        }
        return lista;
    }

    @Override
    public Usuario actualizar(Usuario entity, Connection conn) throws Exception {
        // Primero actualizar la credencial si existe
        if (entity.getCredencial() != null) {
            credDao.actualizar(entity.getCredencial(), conn);
        }

        String sql = "UPDATE usuario SET username = ?, email = ?, activo = ?, fecha_registro = ?, eliminado = ?, credencial_id = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getEmail());
            ps.setBoolean(3, entity.isActivo());
            ps.setTimestamp(4, entity.getFechaRegistro() != null ? Timestamp.valueOf(entity.getFechaRegistro()) : null);
            ps.setBoolean(5, entity.isEliminado());
            ps.setObject(6, entity.getCredencial() != null ? entity.getCredencial().getId() : null);
            ps.setLong(7, entity.getId());

            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean eliminar(long id, Connection conn) throws Exception {
        Usuario usuario = leer(id, conn);
        if (usuario == null) return false;

        String sql = "DELETE FROM usuario WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            boolean eliminado = ps.executeUpdate() > 0;

            // Eliminar la credencial asociada si existía
            if (eliminado && usuario.getCredencial() != null) {
                credDao.eliminar(usuario.getCredencial().getId(), conn);
            }
            return eliminado;
        }
    }

    /**
     * Convierte un ResultSet en un objeto Usuario
     */
    private Usuario mapear(ResultSet rs, Connection conn) throws Exception {
        LocalDateTime fecha = rs.getTimestamp("fecha_registro") != null
                ? rs.getTimestamp("fecha_registro").toLocalDateTime()
                : null;

        // Obtener la credencial asociada, si existe
        CredencialAcceso cred = null;
        long credId = rs.getLong("credencial_id");
        if (!rs.wasNull()) {
            cred = credDao.leer(credId, conn);
        }

        return new Usuario(
                rs.getLong("id"),
                rs.getBoolean("eliminado"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getBoolean("activo"),
                fecha,
                cred
        );
    }
}
