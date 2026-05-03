package dao;

import config.DBConnection;
import helper.IdGenerator;
import model.Pengajuan;
import model.Tiket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PengajuanDAO {

    private Connection conn = DBConnection.getConnection();

    // ── GET ALL dengan JOIN (untuk tabel di form Admin) ───────────────────────
    public List<Pengajuan> getAll() {
        List<Pengajuan> list = new ArrayList<>();
        String sql =
            "SELECT p.*, js.nama_jenis, " +
            "CASE WHEN p.tipe_pemohon='siswa' THEN s.nama_lengkap ELSE g.nama_lengkap END AS nama_pemohon, " +
            "CASE WHEN p.tipe_pemohon='siswa' THEN s.kelas ELSE g.jabatan END AS info_pemohon, " +
            "CASE WHEN p.tipe_pemohon='siswa' THEN s.nis ELSE g.nip END AS nomor_induk " +
            "FROM pengajuan p " +
            "JOIN jenis_surat js ON p.id_jenis = js.id_jenis " +
            "LEFT JOIN siswa s ON p.id_siswa = s.id_siswa " +
            "LEFT JOIN guru  g ON p.id_guru  = g.id_guru " +
            "ORDER BY p.tgl_pengajuan DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── GET by PENGGUNA (untuk siswa/guru lihat milik sendiri) ────────────────
    public List<Pengajuan> getByPengguna(String idPengguna) {
        List<Pengajuan> list = new ArrayList<>();
        String sql =
            "SELECT p.*, js.nama_jenis, " +
            "CASE WHEN p.tipe_pemohon='siswa' THEN s.nama_lengkap ELSE g.nama_lengkap END AS nama_pemohon, " +
            "CASE WHEN p.tipe_pemohon='siswa' THEN s.kelas ELSE g.jabatan END AS info_pemohon, " +
            "CASE WHEN p.tipe_pemohon='siswa' THEN s.nis ELSE g.nip END AS nomor_induk " +
            "FROM pengajuan p " +
            "JOIN jenis_surat js ON p.id_jenis = js.id_jenis " +
            "LEFT JOIN siswa s ON p.id_siswa = s.id_siswa " +
            "LEFT JOIN guru  g ON p.id_guru  = g.id_guru " +
            "WHERE p.id_pengguna = ? " +
            "ORDER BY p.tgl_pengajuan DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idPengguna);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── INSERT + otomatis generate TIKET ──────────────────────────────────────
    /**
     * Menyimpan pengajuan baru dan langsung generate tiket + nomor antrian.
     * Semua dalam satu transaksi — jika salah satu gagal, keduanya dibatalkan.
     */
    public boolean insertDenganTiket(Pengajuan p, String idPetugas) {
        try {
            conn.setAutoCommit(false);  // mulai transaksi

            // 1. Simpan pengajuan
            p.setIdPengajuan(IdGenerator.forPengajuan());
            String sqlPgj =
                "INSERT INTO pengajuan (id_pengajuan,id_jenis,id_pengguna,id_siswa,id_guru," +
                "tipe_pemohon,keperluan,berkas_pendukung,tgl_pengajuan) VALUES (?,?,?,?,?,?,?,?,CURDATE())";
            try (PreparedStatement ps = conn.prepareStatement(sqlPgj)) {
                ps.setString(1, p.getIdPengajuan());
                ps.setString(2, p.getIdJenis());
                ps.setString(3, p.getIdPengguna());
                ps.setString(4, p.getIdSiswa());
                ps.setString(5, p.getIdGuru());
                ps.setString(6, p.getTipePemohon());
                ps.setString(7, p.getKeperluan());
                ps.setString(8, p.getBerkasPendukung());
                ps.executeUpdate();
            }

            // 2. Generate nomor antrian harian (reset tiap hari)
            int antrian = 1;
            String sqlAntrian = "SELECT IFNULL(MAX(nomor_antrian),0)+1 FROM tiket WHERE DATE(tgl_dibuat) = CURDATE()";
            try (PreparedStatement ps = conn.prepareStatement(sqlAntrian);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) antrian = rs.getInt(1);
            }

            // 3. Simpan tiket
            String idTiket  = IdGenerator.forTiket();
            String noTiket  = "TKT-" + String.format("%03d", antrian);
            String sqlTiket =
                "INSERT INTO tiket (id_tiket,id_pengajuan,no_tiket,nomor_antrian,status,id_petugas) " +
                "VALUES (?,?,?,?,'menunggu',?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlTiket)) {
                ps.setString(1, idTiket);
                ps.setString(2, p.getIdPengajuan());
                ps.setString(3, noTiket);
                ps.setInt   (4, antrian);
                ps.setString(5, idPetugas);
                ps.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            try { conn.rollback(); conn.setAutoCommit(true); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    public boolean delete(String id) {
        String sql = "DELETE FROM pengajuan WHERE id_pengajuan = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ── STATISTIK untuk Dashboard ─────────────────────────────────────────────
    public int countByStatus(String status, String idPengguna) {
        String sql = idPengguna == null
            ? "SELECT COUNT(*) FROM tiket WHERE status = ?"
            : "SELECT COUNT(*) FROM tiket t JOIN pengajuan p ON t.id_pengajuan=p.id_pengajuan WHERE t.status=? AND p.id_pengguna=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            if (idPengguna != null) ps.setString(2, idPengguna);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public int countTotal(String idPengguna) {
        String sql = idPengguna == null
            ? "SELECT COUNT(*) FROM tiket"
            : "SELECT COUNT(*) FROM tiket t JOIN pengajuan p ON t.id_pengajuan=p.id_pengajuan WHERE p.id_pengguna=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (idPengguna != null) ps.setString(1, idPengguna);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    // ── MAP ResultSet → Pengajuan ─────────────────────────────────────────────
    private Pengajuan mapRow(ResultSet rs) throws SQLException {
        Pengajuan p = new Pengajuan();
        p.setIdPengajuan(rs.getString("id_pengajuan"));
        p.setIdJenis(rs.getString("id_jenis"));
        p.setIdPengguna(rs.getString("id_pengguna"));
        p.setIdSiswa(rs.getString("id_siswa"));
        p.setIdGuru(rs.getString("id_guru"));
        p.setTipePemohon(rs.getString("tipe_pemohon"));
        p.setKeperluan(rs.getString("keperluan"));
        p.setBerkasPendukung(rs.getString("berkas_pendukung"));
        p.setTglPengajuan(rs.getDate("tgl_pengajuan"));
        // Field JOIN
        try { p.setNamaJenis(rs.getString("nama_jenis")); }     catch (SQLException ignored) {}
        try { p.setNamaPemohon(rs.getString("nama_pemohon")); }  catch (SQLException ignored) {}
        try { p.setInfoPemohon(rs.getString("info_pemohon")); }  catch (SQLException ignored) {}
        try { p.setNomorInduk(rs.getString("nomor_induk")); }    catch (SQLException ignored) {}
        return p;
    }
}
