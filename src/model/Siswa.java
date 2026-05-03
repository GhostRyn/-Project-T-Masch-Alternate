package model;

public class Siswa {

    private String idSiswa;
    private String nis;
    private String namaLengkap;
    private String kelas;
    private String jurusan;
    private int    tahunMasuk;
    private String noTelp;
    private String alamat;

    public Siswa() {}

    public Siswa(String idSiswa, String nis, String namaLengkap,
                 String kelas, String jurusan, int tahunMasuk,
                 String noTelp, String alamat) {
        this.idSiswa     = idSiswa;
        this.nis         = nis;
        this.namaLengkap = namaLengkap;
        this.kelas       = kelas;
        this.jurusan     = jurusan;
        this.tahunMasuk  = tahunMasuk;
        this.noTelp      = noTelp;
        this.alamat      = alamat;
    }

    public String getIdSiswa()              { return idSiswa; }
    public void   setIdSiswa(String v)      { this.idSiswa = v; }

    public String getNis()                  { return nis; }
    public void   setNis(String v)          { this.nis = v; }

    public String getNamaLengkap()          { return namaLengkap; }
    public void   setNamaLengkap(String v)  { this.namaLengkap = v; }

    public String getKelas()                { return kelas; }
    public void   setKelas(String v)        { this.kelas = v; }

    public String getJurusan()              { return jurusan; }
    public void   setJurusan(String v)      { this.jurusan = v; }

    public int    getTahunMasuk()           { return tahunMasuk; }
    public void   setTahunMasuk(int v)      { this.tahunMasuk = v; }

    public String getNoTelp()               { return noTelp; }
    public void   setNoTelp(String v)       { this.noTelp = v; }

    public String getAlamat()               { return alamat; }
    public void   setAlamat(String v)       { this.alamat = v; }

    @Override
    public String toString() { return namaLengkap + " - " + nis; }
}
