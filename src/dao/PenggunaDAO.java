package dao;

import config.DBConnection;
import helper.IdGenerator;
import model.Pengguna;

import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PenggunaDAO
 * Menangani semua operasi database untuk tabel pengguna.
 * Termasuk login dengan hash SHA-256.
 */
public class PenggunaDAO {

    private Connection conn = DBConnection.getConnection();

    // ── Hash password SHA-256 (sama seperti SHA2 di MySQL) ───────────────────
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ── LOGIN ─────────────────────────────────────────────────────────────────
    /**
     * Validasi login pengguna.
     * @return objek Pengguna jika berhasil, null jika gagal.
     */
    public Pengguna login(String username, String password, String role) {
        String sql = "SELECT * FROM pengguna WHERE username = ? AND password = ? AND role = ? AND is_active = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hashPassword(password));
            ps.setString(3, role);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ── GET ALL ───────────────────────────────────────────────────────────────
    public List<Pengguna> getAll() {
        List<Pengguna> list = new ArrayList<>();
        String sql = "SELECT * FROM pengguna ORDER BY created_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── GET BY ID ─────────────────────────────────────────────────────────────
    public Pengguna getById(String id) {
        String sql = "SELECT * FROM pengguna WHERE id_pengguna = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // ── CEK USERNAME DUPLIKAT ─────────────────────────────────────────────────
    public boolean isUsernameTaken(String username, String excludeId) {
        String sql = "SELECT COUNT(*) FROM pengguna WHERE username = ? AND id_pengguna != ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, excludeId == null ? "" : excludeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ── INSERT ────────────────────────────────────────────────────────────────
    public boolean insert(Pengguna p) {
        p.setIdPengguna(IdGenerator.forPengguna());
        String sql = "INSERT INTO pengguna (id_pengguna, username, password, role, is_active) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getIdPengguna());
            ps.setString(2, p.getUsername());
            ps.setString(3, hashPassword(p.getPassword()));
            ps.setString(4, p.getRole());
            ps.setBoolean(5, p.isActive());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ── UPDATE (tanpa ubah password) ──────────────────────────────────────────
    public boolean update(Pengguna p) {
        String sql = "UPDATE pengguna SET username=?, role=?, is_active=? WHERE id_pengguna=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getUsername());
            ps.setString(2, p.getRole());
            ps.setBoolean(3, p.isActive());
            ps.setString(4, p.getIdPengguna());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ── UPDATE PASSWORD ───────────────────────────────────────────────────────
    public boolean updatePassword(String idPengguna, String passwordBaru) {
        String sql = "UPDATE pengguna SET password=? WHERE id_pengguna=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashPassword(passwordBaru));
            ps.setString(2, idPengguna);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ── DELETE (soft delete — set is_active = 0) ──────────────────────────────
    public boolean delete(String id) {
        String sql = "UPDATE pengguna SET is_active = 0 WHERE id_pengguna = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ── MAP ResultSet → Pengguna ──────────────────────────────────────────────
    private Pengguna mapRow(ResultSet rs) throws SQLException {
        Pengguna p = new Pengguna();
        p.setIdPengguna(rs.getString("id_pengguna"));
        p.setUsername(rs.getString("username"));
        p.setPassword(rs.getString("password"));
        p.setRole(rs.getString("role"));
        p.setActive(rs.getBoolean("is_active"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        return p;
    }
}
