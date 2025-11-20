package entities;

import java.time.LocalDateTime;

public class CredencialAcceso {

    private Long id;
    private boolean eliminado;
    private String hashPassword;
    private String salt;
    private LocalDateTime ultimoCambio;
    private boolean requiereReset;
    private Long usuarioId; // Nueva propiedad para relacionar con Usuario

    public CredencialAcceso() {}

    public CredencialAcceso(Long id, boolean eliminado, String hashPassword, String salt,
                            LocalDateTime ultimoCambio, boolean requiereReset) {
        this.id = id;
        this.eliminado = eliminado;
        this.hashPassword = hashPassword;
        this.salt = salt;
        this.ultimoCambio = ultimoCambio;
        this.requiereReset = requiereReset;
    }

    // -----------------------------
    // GETTERS Y SETTERS
    // -----------------------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }

    public String getHashPassword() { return hashPassword; }
    public void setHashPassword(String hashPassword) { this.hashPassword = hashPassword; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }

    public LocalDateTime getUltimoCambio() { return ultimoCambio; }
    public void setUltimoCambio(LocalDateTime ultimoCambio) { this.ultimoCambio = ultimoCambio; }

    public boolean isRequiereReset() { return requiereReset; }
    public void setRequiereReset(boolean requiereReset) { this.requiereReset = requiereReset; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    @Override
    public String toString() {
        return "CredencialAcceso{" +
                "id=" + id +
                ", eliminado=" + eliminado +
                ", hashPassword='" + hashPassword + '\'' +
                ", salt='" + salt + '\'' +
                ", ultimoCambio=" + ultimoCambio +
                ", requiereReset=" + requiereReset +
                ", usuarioId=" + usuarioId +
                '}';
    }
}
