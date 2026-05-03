package model;

public class JenisSurat {

    private String  idJenis;
    private String  namaJenis;
    private String  keterangan;
    private boolean isActive;

    public JenisSurat() {}

    public JenisSurat(String idJenis, String namaJenis,
                      String keterangan, boolean isActive) {
        this.idJenis    = idJenis;
        this.namaJenis  = namaJenis;
        this.keterangan = keterangan;
        this.isActive   = isActive;
    }

    public String  getIdJenis()             { return idJenis; }
    public void    setIdJenis(String v)     { this.idJenis = v; }

    public String  getNamaJenis()           { return namaJenis; }
    public void    setNamaJenis(String v)   { this.namaJenis = v; }

    public String  getKeterangan()          { return keterangan; }
    public void    setKeterangan(String v)  { this.keterangan = v; }

    public boolean isActive()               { return isActive; }
    public void    setActive(boolean v)     { this.isActive = v; }

    // toString dipakai di JComboBox agar tampil nama jenis bukan alamat object
    @Override
    public String toString() { return namaJenis; }
}
