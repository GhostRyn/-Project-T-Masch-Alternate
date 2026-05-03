package model;

import java.sql.Timestamp;

public class Tiket {

    private String    idTiket;
    private String    idPengajuan;
    private String    noTiket;
    private int       nomorAntrian;
    private String    status;        // menunggu | proses | selesai | ditolak
    private String    idPetugas;
    private String    catatan;
    private Timestamp tglDibuat;
    private Timestamp tglSelesai;

    // Field tambahan dari JOIN (untuk tampil di tabel/report)
    private String namaJenis;
    private String namaPemohon;
    private String infoPemohon;
    private String nomorInduk;
    private String tipePemohon;
    private String namaPetugas;
    private String keperluan;
    private int    durasiMenit;

    public Tiket() {}

    // ── Getter & Setter ───────────────────────────────────────────────────────

    public String    getIdTiket()               { return idTiket; }
    public void      setIdTiket(String v)       { this.idTiket = v; }

    public String    getIdPengajuan()           { return idPengajuan; }
    public void      setIdPengajuan(String v)   { this.idPengajuan = v; }

    public String    getNoTiket()               { return noTiket; }
    public void      setNoTiket(String v)       { this.noTiket = v; }

    public int       getNomorAntrian()          { return nomorAntrian; }
    public void      setNomorAntrian(int v)     { this.nomorAntrian = v; }

    public String    getStatus()                { return status; }
    public void      setStatus(String v)        { this.status = v; }

    public String    getIdPetugas()             { return idPetugas; }
    public void      setIdPetugas(String v)     { this.idPetugas = v; }

    public String    getCatatan()               { return catatan; }
    public void      setCatatan(String v)       { this.catatan = v; }

    public Timestamp getTglDibuat()             { return tglDibuat; }
    public void      setTglDibuat(Timestamp v)  { this.tglDibuat = v; }

    public Timestamp getTglSelesai()            { return tglSelesai; }
    public void      setTglSelesai(Timestamp v) { this.tglSelesai = v; }

    // Field JOIN
    public String getNamaJenis()                { return namaJenis; }
    public void   setNamaJenis(String v)        { this.namaJenis = v; }

    public String getNamaPemohon()              { return namaPemohon; }
    public void   setNamaPemohon(String v)      { this.namaPemohon = v; }

    public String getInfoPemohon()              { return infoPemohon; }
    public void   setInfoPemohon(String v)      { this.infoPemohon = v; }

    public String getNomorInduk()               { return nomorInduk; }
    public void   setNomorInduk(String v)       { this.nomorInduk = v; }

    public String getTipePemohon()              { return tipePemohon; }
    public void   setTipePemohon(String v)      { this.tipePemohon = v; }

    public String getNamaPetugas()              { return namaPetugas; }
    public void   setNamaPetugas(String v)      { this.namaPetugas = v; }

    public String getKeperluan()                { return keperluan; }
    public void   setKeperluan(String v)        { this.keperluan = v; }

    public int    getDurasiMenit()              { return durasiMenit; }
    public void   setDurasiMenit(int v)         { this.durasiMenit = v; }

    @Override
    public String toString() { return noTiket + " [" + status + "]"; }
}
