package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection
 * Singleton pattern — cuma 1 koneksi yang dibuat sepanjang aplikasi berjalan.
 * Semua DAO manggil si DBConnection.getConnection() biar dapet objek Connection.
 */
public class DBConnection {

    // ── Konfigurasi si XAMPP ─────────────────────────────────────────────────────
    private static final String HOST     = "localhost";
    private static final String PORT     = "3306";
    private static final String DATABASE = "db_ticketing_sekolah";
    private static final String USER     = "root";
    private static final String PASSWORD = "";           // default XAMPP kosong

    private static final String URL =
        "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
        + "?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true";

    // ── Singleton instance ────────────────────────────────────────────────────
    private static Connection connection = null;

    // Private constructor — gak boleh di-new dari luar
    private DBConnection() {}

    /**
     * ngembaliin koneksi aktif.
     * Kalo koneksi belom ada / udah ketutup, buat koneksi baru.
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // ngeload driver MySQL 
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[DB] Koneksi berhasil ke " + DATABASE);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("[DB] Driver tidak ditemukan! Pastikan mysql-connector-java.jar sudah ditambahkan.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[DB] Gagal konek ke database: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Tutup koneksi.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Koneksi ditutup.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
