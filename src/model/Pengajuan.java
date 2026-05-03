package model;

import java.sql.Date;

public class Pengajuan {

    private String idPengajuan;
    private String idJenis;
    private String idPengguna;
    private String idSiswa;          // null jika tipe_pemohon = guru
    private String idGuru;           // null jika tipe_pemohon = siswa
    private String tipePemohon;      // "siswa" | "guru"
    private String keperluan;
    private String berkasPendukung;
    private Date   tglPengajuan;

    // Field tambahan dari JOIN (untuk tampil di tabel/report)
    private String namaJenis;
    private String namaPemohon;
    private String infoPemohon;      // kelas (siswa) atau jabatan (guru)
    private String nomorInduk;       // nis (siswa) atau nip (guru)

    public Pengajuan() {}

    // ── Getter & Setter ───────────────────────────────────────────────────────

    public String getIdPengajuan()              { return idPengajuan; }
    public void   setIdPengajuan(String v)      { this.idPengajuan = v; }

    public String getIdJenis()                  { return idJenis; }
    public void   setIdJenis(String v)          { this.idJenis = v; }

    public String getIdPengguna()               { return idPengguna; }
    public void   setIdPengguna(String v)       { this.idPengguna = v; }

    public String getIdSiswa()                  { return idSiswa; }
    public void   setIdSiswa(String v)          { this.idSiswa = v; }

    public String getIdGuru()                   { return idGuru; }
    public void   setIdGuru(String v)           { this.idGuru = v; }

    public String getTipePemohon()              { return tipePemohon; }
    public void   setTipePemohon(String v)      { this.tipePemohon = v; }

    public String getKeperluan()                { return keperluan; }
    public void   setKeperluan(String v)        { this.keperluan = v; }

    public String getBerkasPendukung()              { return berkasPendukung; }
    public void   setBerkasPendukung(String v)      { this.berkasPendukung = v; }

    public Date   getTglPengajuan()             { return tglPengajuan; }
    public void   setTglPengajuan(Date v)       { this.tglPengajuan = v; }

    // Field JOIN
    public String getNamaJenis()                { return namaJenis; }
    public void   setNamaJenis(String v)        { this.namaJenis = v; }

    public String getNamaPemohon()              { return namaPemohon; }
    public void   setNamaPemohon(String v)      { this.namaPemohon = v; }

    public String getInfoPemohon()              { return infoPemohon; }
    public void   setInfoPemohon(String v)      { this.infoPemohon = v; }

    public String getNomorInduk()               { return nomorInduk; }
    public void   setNomorInduk(String v)       { this.nomorInduk = v; }

    @Override
    public String toString() { return idPengajuan + " - " + namaJenis; }
}
