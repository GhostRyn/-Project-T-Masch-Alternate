package model;

public class Guru {

    private String idGuru;
    private String nip;
    private String namaLengkap;
    private String jabatan;
    private String mataPelajaran;
    private String noTelp;
    private String alamat;

    public Guru() {}

    public Guru(String idGuru, String nip, String namaLengkap,
                String jabatan, String mataPelajaran,
                String noTelp, String alamat) {
        this.idGuru        = idGuru;
        this.nip           = nip;
        this.namaLengkap   = namaLengkap;
        this.jabatan       = jabatan;
        this.mataPelajaran = mataPelajaran;
        this.noTelp        = noTelp;
        this.alamat        = alamat;
    }

    public String getIdGuru()               { return idGuru; }
    public void   setIdGuru(String v)       { this.idGuru = v; }

    public String getNip()                  { return nip; }
    public void   setNip(String v)          { this.nip = v; }

    public String getNamaLengkap()          { return namaLengkap; }
    public void   setNamaLengkap(String v)  { this.namaLengkap = v; }

    public String getJabatan()              { return jabatan; }
    public void   setJabatan(String v)      { this.jabatan = v; }

    public String getMataPelajaran()            { return mataPelajaran; }
    public void   setMataPelajaran(String v)    { this.mataPelajaran = v; }

    public String getNoTelp()               { return noTelp; }
    public void   setNoTelp(String v)       { this.noTelp = v; }

    public String getAlamat()               { return alamat; }
    public void   setAlamat(String v)       { this.alamat = v; }

    @Override
    public String toString() { return namaLengkap + " - " + nip; }
}
