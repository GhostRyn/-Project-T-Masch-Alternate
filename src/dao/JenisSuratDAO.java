package dao;

import config.DBConnection;
import helper.IdGenerator;
import model.JenisSurat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JenisSuratDAO {

    private Connection conn = DBConnection.getConnection();

    // Hanya ambil yang aktif (untuk ComboBox di form pengajuan)
    public List<JenisSurat> getAktif() {
        List<JenisSurat> list = new ArrayList<>();
        String sql = "SELECT * FROM jenis_surat WHERE is_active = 1 ORDER BY nama_jenis";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<JenisSurat> getAll() {
        List<JenisSurat> list = new ArrayList<>();
        String sql = "SELECT * FROM jenis_surat ORDER BY nama_jenis";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean insert(JenisSurat j) {
        j.setIdJenis(IdGenerator.forJenisSurat());
        String sql = "INSERT INTO jenis_surat (id_jenis,nama_jenis,keterangan,is_active) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, j.getIdJenis());
            ps.setString(2, j.getNamaJenis());
            ps.setString(3, j.getKeterangan());
            ps.setBoolean(4, j.isActive());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean update(JenisSurat j) {
        String sql = "UPDATE jenis_surat SET nama_jenis=?,keterangan=?,is_active=? WHERE id_jenis=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, j.getNamaJenis());
            ps.setString(2, j.getKeterangan());
            ps.setBoolean(3, j.isActive());
            ps.setString(4, j.getIdJenis());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private JenisSurat mapRow(ResultSet rs) throws SQLException {
        JenisSurat j = new JenisSurat();
        j.setIdJenis(rs.getString("id_jenis"));
        j.setNamaJenis(rs.getString("nama_jenis"));
        j.setKeterangan(rs.getString("keterangan"));
        j.setActive(rs.getBoolean("is_active"));
        return j;
    }
}
