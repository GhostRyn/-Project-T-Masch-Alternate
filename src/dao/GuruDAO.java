package dao;

import config.DBConnection;
import helper.IdGenerator;
import model.Guru;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuruDAO {

    private Connection conn = DBConnection.getConnection();

    public List<Guru> getAll() {
        List<Guru> list = new ArrayList<>();
        String sql = "SELECT * FROM guru ORDER BY nama_lengkap ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Guru getById(String id) {
        String sql = "SELECT * FROM guru WHERE id_guru = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Guru> search(String keyword) {
        List<Guru> list = new ArrayList<>();
        String sql = "SELECT * FROM guru WHERE nip LIKE ? OR nama_lengkap LIKE ? ORDER BY nama_lengkap";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean isNipTaken(String nip, String excludeId) {
        String sql = "SELECT COUNT(*) FROM guru WHERE nip = ? AND id_guru != ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nip);
            ps.setString(2, excludeId == null ? "" : excludeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean insert(Guru g) {
        g.setIdGuru(IdGenerator.forGuru());
        String sql = "INSERT INTO guru (id_guru,nip,nama_lengkap,jabatan,mata_pelajaran,no_telp,alamat) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, g.getIdGuru());
            ps.setString(2, g.getNip());
            ps.setString(3, g.getNamaLengkap());
            ps.setString(4, g.getJabatan());
            ps.setString(5, g.getMataPelajaran());
            ps.setString(6, g.getNoTelp());
            ps.setString(7, g.getAlamat());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean update(Guru g) {
        String sql = "UPDATE guru SET nip=?,nama_lengkap=?,jabatan=?,mata_pelajaran=?,no_telp=?,alamat=? WHERE id_guru=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, g.getNip());
            ps.setString(2, g.getNamaLengkap());
            ps.setString(3, g.getJabatan());
            ps.setString(4, g.getMataPelajaran());
            ps.setString(5, g.getNoTelp());
            ps.setString(6, g.getAlamat());
            ps.setString(7, g.getIdGuru());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM guru WHERE id_guru = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Guru mapRow(ResultSet rs) throws SQLException {
        Guru g = new Guru();
        g.setIdGuru(rs.getString("id_guru"));
        g.setNip(rs.getString("nip"));
        g.setNamaLengkap(rs.getString("nama_lengkap"));
        g.setJabatan(rs.getString("jabatan"));
        g.setMataPelajaran(rs.getString("mata_pelajaran"));
        g.setNoTelp(rs.getString("no_telp"));
        g.setAlamat(rs.getString("alamat"));
        return g;
    }
}
