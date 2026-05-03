package dao;

import config.DBConnection;
import model.Tiket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TiketDAO {

    private Connection conn = DBConnection.getConnection();

    // ── GET dari VIEW antrian aktif hari ini ──────────────────────────────────
    public List<Tiket> getAntrianAktif() {
        return queryView("SELECT * FROM v_antrian_aktif", null);
    }

    // ── GET histori milik satu pengguna (siswa/guru) ──────────────────────────
    public List<Tiket> getHistoriByPengguna(String idPengguna) {
        String sql = "SELECT * FROM v_histori_tiket WHERE id_pengguna = ?";
        return queryView(sql, idPengguna);
    }

    // ── GET semua histori (untuk admin) ──────────────────────────────────────
    public List<Tiket> getAllHistori() {
        return queryView("SELECT * FROM v_histori_tiket", null);
    }

    // ── GET histori filter by tipe_pemohon ────────────────────────────────────
    public List<Tiket> getHistoriByTipe(String tipe) {
        String sql = "SELECT * FROM v_histori_tiket WHERE tipe_pemohon = ?";
        return queryView(sql, tipe);
    }

    // ── UPDATE STATUS tiket ───────────────────────────────────────────────────
    /**
     * Mengubah status tiket. Trigger di MySQL otomatis catat ke status_log.
     * Jika status baru = selesai/ditolak, tgl_selesai diisi sekarang.
     */
    public boolean updateStatus(String idTiket, String statusBaru,
                                 String idPetugas, String catatan) {
        boolean selesai = statusBaru.equals("selesai") || statusBaru.equals("ditolak");
        String sql = selesai
            ? "UPDATE tiket SET status=?, id_petugas=?, catatan=?, tgl_selesai=NOW() WHERE id_tiket=?"
            : "UPDATE tiket SET status=?, id_petugas=?, catatan=? WHERE id_tiket=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statusBaru);
            ps.setString(2, idPetugas);
            ps.setString(3, catatan);
            ps.setString(4, idTiket);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ── GET rekap per jenis surat (untuk Report 1) ────────────────────────────
    public List<String[]> getRekap() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT * FROM v_rekap_pengajuan";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("nama_jenis"),
                    rs.getString("total"),
                    rs.getString("menunggu"),
                    rs.getString("proses"),
                    rs.getString("selesai"),
                    rs.getString("ditolak"),
                    rs.getString("rata_durasi_menit") == null ? "-" : rs.getString("rata_durasi_menit") + " mnt"
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── GET rekap dengan filter tanggal ──────────────────────────────────────
    public List<String[]> getRekapByPeriode(String tglMulai, String tglAkhir) {
        List<String[]> list = new ArrayList<>();
        String sql =
            "SELECT js.nama_jenis, " +
            "COUNT(t.id_tiket) AS total, " +
            "SUM(t.status='menunggu') AS menunggu, " +
            "SUM(t.status='proses') AS proses, " +
            "SUM(t.status='selesai') AS selesai, " +
            "SUM(t.status='ditolak') AS ditolak, " +
            "ROUND(AVG(TIMESTAMPDIFF(MINUTE,t.tgl_dibuat,t.tgl_selesai)),1) AS rata_durasi " +
            "FROM jenis_surat js " +
            "LEFT JOIN pengajuan p ON js.id_jenis=p.id_jenis " +
            "LEFT JOIN tiket t ON p.id_pengajuan=t.id_pengajuan " +
            "WHERE js.is_active=1 AND (p.tgl_pengajuan BETWEEN ? AND ? OR p.tgl_pengajuan IS NULL) " +
            "GROUP BY js.id_jenis, js.nama_jenis ORDER BY total DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tglMulai);
            ps.setString(2, tglAkhir);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("nama_jenis"),
                    rs.getString("total"),
                    rs.getString("menunggu"),
                    rs.getString("proses"),
                    rs.getString("selesai"),
                    rs.getString("ditolak"),
                    rs.getString("rata_durasi") == null ? "-" : rs.getString("rata_durasi") + " mnt"
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── HELPER query view ─────────────────────────────────────────────────────
    private List<Tiket> queryView(String sql, String param) {
        List<Tiket> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (param != null) ps.setString(1, param);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapViewRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── MAP ResultSet dari VIEW → Tiket ──────────────────────────────────────
    private Tiket mapViewRow(ResultSet rs) throws SQLException {
        Tiket t = new Tiket();
        trySet(() -> t.setIdTiket(rs.getString("id_tiket")));
        trySet(() -> t.setNoTiket(rs.getString("no_tiket")));
        trySet(() -> t.setNomorAntrian(rs.getInt("nomor_antrian")));
        trySet(() -> t.setStatus(rs.getString("status")));
        trySet(() -> t.setTglDibuat(rs.getTimestamp("tgl_dibuat")));
        trySet(() -> t.setTglSelesai(rs.getTimestamp("tgl_selesai")));
        trySet(() -> t.setNamaJenis(rs.getString("nama_jenis")));
        trySet(() -> t.setNamaPemohon(rs.getString("nama_pemohon")));
        trySet(() -> t.setInfoPemohon(rs.getString("info_pemohon")));
        trySet(() -> t.setNomorInduk(rs.getString("nomor_induk")));
        trySet(() -> t.setTipePemohon(rs.getString("tipe_pemohon")));
        trySet(() -> t.setNamaPetugas(rs.getString("nama_petugas")));
        trySet(() -> t.setKeperluan(rs.getString("keperluan")));
        trySet(() -> t.setCatatan(rs.getString("catatan")));
        trySet(() -> t.setIdPetugas(rs.getString("id_petugas")));
        trySet(() -> t.setDurasiMenit(rs.getInt("durasi_menit")));
        return t;
    }

    // Supaya tidak crash jika kolom tertentu tidak ada di view yang diquery
    private void trySet(ThrowingRunnable r) {
        try { r.run(); } catch (Exception ignored) {}
    }

    @FunctionalInterface
    interface ThrowingRunnable { void run() throws Exception; }
}
