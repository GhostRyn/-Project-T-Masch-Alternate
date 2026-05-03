package model;

import java.sql.Timestamp;

/**
 * Model untuk tabel pengguna.
 * Setiap field = kolom di tabel, plus getter & setter.
 */
public class Pengguna {

    private String    idPengguna;
    private String    username;
    private String    password;
    private String    role;        // "admin" | "siswa" | "guru"
    private boolean   isActive;
    private Timestamp createdAt;

    // ── Constructor kosong (wajib untuk ResultSet mapping) ────────────────────
    public Pengguna() {}

    // ── Constructor lengkap ───────────────────────────────────────────────────
    public Pengguna(String idPengguna, String username, String password,
                    String role, boolean isActive) {
        this.idPengguna = idPengguna;
        this.username   = username;
        this.password   = password;
        this.role       = role;
        this.isActive   = isActive;
    }

    // ── Getter & Setter ───────────────────────────────────────────────────────
    public String    getIdPengguna()            { return idPengguna; }
    public void      setIdPengguna(String v)    { this.idPengguna = v; }

    public String    getUsername()              { return username; }
    public void      setUsername(String v)      { this.username = v; }

    public String    getPassword()              { return password; }
    public void      setPassword(String v)      { this.password = v; }

    public String    getRole()                  { return role; }
    public void      setRole(String v)          { this.role = v; }

    public boolean   isActive()                 { return isActive; }
    public void      setActive(boolean v)       { this.isActive = v; }

    public Timestamp getCreatedAt()             { return createdAt; }
    public void      setCreatedAt(Timestamp v)  { this.createdAt = v; }

    @Override
    public String toString() {
        return username + " (" + role + ")";
    }
}
