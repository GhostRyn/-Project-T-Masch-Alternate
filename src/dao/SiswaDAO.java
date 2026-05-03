package dao;

import config.DBConnection;
import helper.IdGenerator;
import model.Siswa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SiswaDAO {

    private Connection conn = DBConnection.getConnection();

    public List<Siswa> getAll() {
        List<Siswa> list = new ArrayList<>();
        String sql = "SELECT * FROM siswa ORDER BY nama_lengkap ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Siswa getById(String id) {
        String sql = "SELECT * FROM siswa WHERE id_siswa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // Cari siswa berdasarkan NIS atau nama (untuk search box)
    public List<Siswa> search(String keyword) {
        List<Siswa> list = new ArrayList<>();
        String sql = "SELECT * FROM siswa WHERE nis LIKE ? OR nama_lengkap LIKE ? ORDER BY nama_lengkap";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean isNisTaken(String nis, String excludeId) {
        String sql = "SELECT COUNT(*) FROM siswa WHERE nis = ? AND id_siswa != ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nis);
            ps.setString(2, excludeId == null ? "" : excludeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean insert(Siswa s) {
        s.setIdSiswa(IdGenerator.forSiswa());
        String sql = "INSERT INTO siswa (id_siswa,nis,nama_lengkap,kelas,jurusan,tahun_masuk,no_telp,alamat) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getIdSiswa());
            ps.setString(2, s.getNis());
            ps.setString(3, s.getNamaLengkap());
            ps.setString(4, s.getKelas());
            ps.setString(5, s.getJurusan());
            ps.setInt   (6, s.getTahunMasuk());
            ps.setString(7, s.getNoTelp());
            ps.setString(8, s.getAlamat());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean update(Siswa s) {
        String sql = "UPDATE siswa SET nis=?,nama_lengkap=?,kelas=?,jurusan=?,tahun_masuk=?,no_telp=?,alamat=? WHERE id_siswa=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getNis());
            ps.setString(2, s.getNamaLengkap());
            ps.setString(3, s.getKelas());
            ps.setString(4, s.getJurusan());
            ps.setInt   (5, s.getTahunMasuk());
            ps.setString(6, s.getNoTelp());
            ps.setString(7, s.getAlamat());
            ps.setString(8, s.getIdSiswa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM siswa WHERE id_siswa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Siswa mapRow(ResultSet rs) throws SQLException {
        Siswa s = new Siswa();
        s.setIdSiswa(rs.getString("id_siswa"));
        s.setNis(rs.getString("nis"));
        s.setNamaLengkap(rs.getString("nama_lengkap"));
        s.setKelas(rs.getString("kelas"));
        s.setJurusan(rs.getString("jurusan"));
        s.setTahunMasuk(rs.getInt("tahun_masuk"));
        s.setNoTelp(rs.getString("no_telp"));
        s.setAlamat(rs.getString("alamat"));
        return s;
    }
}
