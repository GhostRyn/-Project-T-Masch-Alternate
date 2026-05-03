package helper;

import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * IdGenerator
 * Generate ID unik format XXX-001 untuk setiap tabel.
 *
 * Cara kerja:
 *   1. Query MAX id dari tabel yang dituju
 *   2. Ambil angka di belakang prefix (misal "USR-003" → 3)
 *   3. Tambah 1, lalu format jadi 3 digit → "USR-004"
 *
 * Contoh penggunaan:
 *   String id = IdGenerator.generate("USR", "pengguna", "id_pengguna");
 *   // → "USR-001" jika tabel masih kosong
 */
public class IdGenerator {

    /**
     * @param prefix  Kode tabel, misal "USR", "SSW", "GRU", "JNS", "PGJ", "TKT", "LOG"
     * @param tabel   Nama tabel di database
     * @param kolom   Nama kolom PK di tabel tersebut
     * @return        ID baru dalam format PREFIX-NNN
     */
    public static String generate(String prefix, String tabel, String kolom) {
        Connection conn = DBConnection.getConnection();
        // panjang prefix + tanda "-" = prefix.length() + 2
        // contoh "USR-" = 4 karakter, jadi SUBSTRING mulai dari karakter ke-5
        int startIndex = prefix.length() + 2;

        String sql = "SELECT IFNULL(MAX(CAST(SUBSTRING(" + kolom + ", ?) AS UNSIGNED)), 0) + 1 FROM " + tabel;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, startIndex);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int nextNum = rs.getInt(1);
                return prefix + "-" + String.format("%03d", nextNum);
            }
        } catch (SQLException e) {
            System.err.println("[IdGenerator] Gagal generate ID untuk tabel " + tabel);
            e.printStackTrace();
        }

        // Fallback jika query gagal
        return prefix + "-001";
    }

    // ── Shortcut per tabel ────────────────────────────────────────────────────
    // Supaya DAO tidak perlu hafal nama tabel & kolom

    public static String forPengguna()  { return generate("USR", "pengguna",   "id_pengguna");  }
    public static String forSiswa()     { return generate("SSW", "siswa",      "id_siswa");      }
    public static String forGuru()      { return generate("GRU", "guru",       "id_guru");       }
    public static String forJenisSurat(){ return generate("JNS", "jenis_surat","id_jenis");      }
    public static String forPengajuan() { return generate("PGJ", "pengajuan",  "id_pengajuan");  }
    public static String forTiket()     { return generate("TKT", "tiket",      "id_tiket");      }
    public static String forLog()       { return generate("LOG", "status_log", "id_log");        }
}
