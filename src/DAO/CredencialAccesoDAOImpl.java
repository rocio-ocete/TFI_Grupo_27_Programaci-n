package dao;

import entities.CredencialAcceso;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CredencialAccesoDAOImpl implements GenericDao<CredencialAcceso> {

    @Override
    public CredencialAcceso crear(CredencialAcceso entity, Connection conn) throws Exception {
        String sql = "INSERT INTO credencial_acceso (hash_password, salt, ultimo_cambio, requiere_reset, eliminado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getHashPassword());
            ps.setString(2, entity.getSalt());
            ps.setTimestamp(3, entity.getUltimoCambio() != null ? Timestamp.valueOf(entity.getUltimoCambio()) : null);
            ps.setBoolean(4, entity.isRequiereReset());
            ps.setBoolean(5, entity.isEliminado());
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
    public CredencialAcceso leer(long id, Connection conn) throws Exception {
        String sql = "SELECT * FROM credencial_acceso WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<CredencialAcceso> leerTodos(Connection conn) throws Exception {
        String sql = "SELECT * FROM credencial_acceso";
        List<CredencialAcceso> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    @Override
    public CredencialAcceso actualizar(CredencialAcceso entity, Connection conn) throws Exception {
        String sql = "UPDATE credencial_acceso SET hash_password = ?, salt = ?, ultimo_cambio = ?, requiere_reset = ?, eliminado = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getHashPassword());
            ps.setString(2, entity.getSalt());
            ps.setTimestamp(3, entity.getUltimoCambio() != null ? Timestamp.valueOf(entity.getUltimoCambio()) : null);
            ps.setBoolean(4, entity.isRequiereReset());
            ps.setBoolean(5, entity.isEliminado());
            ps.setLong(6, entity.getId());
            ps.executeUpdate();
        }
        return entity;
    }

    @Override
    public boolean eliminar(long id, Connection conn) throws Exception {
        String sql = "DELETE FROM credencial_acceso WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public CredencialAcceso getByUsuarioId(long usuarioId, Connection conn) throws Exception {
        String sql = "SELECT * FROM credencial_acceso WHERE usuario_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    private CredencialAcceso mapear(ResultSet rs) throws SQLException {
        LocalDateTime fecha = rs.getTimestamp("ultimo_cambio") != null
                ? rs.getTimestamp("ultimo_cambio").toLocalDateTime()
                : null;

        return new CredencialAcceso(
                rs.getLong("id"),
                rs.getBoolean("eliminado"),
                rs.getString("hash_password"),
                rs.getString("salt"),
                fecha,
                rs.getBoolean("requiere_reset")
        );
    }

    public CredencialAcceso leerPorUsuarioId(long usuarioId, Connection conn) throws Exception {
        return getByUsuarioId(usuarioId, conn);
    }
}
