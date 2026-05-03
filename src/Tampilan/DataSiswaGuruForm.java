package Tampilan;

import dao.GuruDAO;
import dao.SiswaDAO;
import model.Guru;
import model.Pengguna;
import model.Siswa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class DataSiswaGuruForm extends JFrame {

    private Pengguna penggunaLogin;
    private SiswaDAO siswaDAO = new SiswaDAO();
    private GuruDAO  guruDAO  = new GuruDAO();

    // ── State pilihan ─────────────────────────────────────────────────────────
    private String idSiswaSelected = null;
    private String idGuruSelected  = null;

    // ── Komponen Tab Siswa 
    private JTable     tblSiswa;
    private JTextField txtNis, txtNamaSiswa, txtKelas, txtJurusan;
    private JTextField txtTahunMasuk, txtNoTelpSiswa;
    private JTextArea  txtAlamatSiswa;
    private JButton    btnSimpanSiswa, btnHapusSiswa, btnBaruSiswa;
    private JLabel     lblModeSiswa;

    // ── Komponen Tab Guru
    private JTable     tblGuru;
    private JTextField txtNip, txtNamaGuru, txtJabatan, txtMapel;
    private JTextField txtNoTelpGuru;
    private JTextArea  txtAlamatGuru;
    private JButton    btnSimpanGuru, btnHapusGuru, btnBaruGuru;
    private JLabel     lblModeGuru;

    public DataSiswaGuruForm(Pengguna pengguna) {
        this.penggunaLogin = pengguna;
        initComponents();
        loadTabelSiswa();
        loadTabelGuru();
    }

    private void initComponents() {
        setTitle("Data Siswa & Guru");
        setSize(920, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel pnlHeader = buatHeader("Data Siswa & Guru");

        // TabbedPane
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 13));
        tabs.addTab("📚  Data Siswa", buatTabSiswa());
        tabs.addTab("👨‍🏫  Data Guru",  buatTabGuru());

        // Reload saat pindah tab
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 0) loadTabelSiswa();
            else loadTabelGuru();
        });

        add(pnlHeader, BorderLayout.NORTH);
        add(tabs,      BorderLayout.CENTER);
    }

   
    //  TAB SISWA
  

    private JPanel buatTabSiswa() {
        JPanel tab = new JPanel(new BorderLayout(10, 0));
        tab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabel
        tblSiswa = new JTable();
        tblSiswa.setRowHeight(26);
        tblSiswa.setFont(new Font("Arial", Font.PLAIN, 12));
        tblSiswa.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblSiswa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblSiswa.setGridColor(new Color(230, 230, 230));

        JScrollPane scroll = new JScrollPane(tblSiswa);
        scroll.setBorder(BorderFactory.createTitledBorder("Daftar Siswa"));

        // Form input
        JPanel pnlForm = new JPanel(null);
        pnlForm.setPreferredSize(new Dimension(290, 0));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Form Siswa"));
        pnlForm.setBackground(Color.WHITE);

        lblModeSiswa = new JLabel("Mode: Tambah Baru");
        lblModeSiswa.setFont(new Font("Arial", Font.ITALIC, 11));
        lblModeSiswa.setForeground(new Color(29, 158, 117));
        lblModeSiswa.setBounds(12, 22, 260, 16);

        txtNis         = buatField(pnlForm, "NIS",          12,  42);
        txtNamaSiswa   = buatField(pnlForm, "Nama Lengkap", 12,  96);
        txtKelas       = buatField(pnlForm, "Kelas",        12, 150);
        txtJurusan     = buatField(pnlForm, "Jurusan",      12, 204);
        txtTahunMasuk  = buatField(pnlForm, "Tahun Masuk",  12, 258);
        txtNoTelpSiswa = buatField(pnlForm, "No. Telp",     12, 312);

        JLabel lAlamat = new JLabel("Alamat");
        lAlamat.setFont(new Font("Arial", Font.PLAIN, 12));
        lAlamat.setBounds(12, 366, 260, 16);
        txtAlamatSiswa = new JTextArea();
        txtAlamatSiswa.setLineWrap(true);
        JScrollPane scrollAlamat = new JScrollPane(txtAlamatSiswa);
        scrollAlamat.setBounds(12, 384, 260, 50);

        btnSimpanSiswa = new JButton("💾  Simpan");
        btnHapusSiswa  = new JButton("🗑  Hapus");
        btnBaruSiswa   = new JButton("➕  Tambah Baru");
        styleBtn(btnSimpanSiswa, new Color(29, 158, 117), Color.WHITE);
        styleBtn(btnHapusSiswa,  new Color(180, 40, 40),  Color.WHITE);
        styleBtn(btnBaruSiswa,   new Color(70, 130, 200), Color.WHITE);
        btnSimpanSiswa.setBounds(12, 444, 260, 32);
        btnHapusSiswa.setBounds (12, 482, 260, 32);
        btnBaruSiswa.setBounds  (12, 520, 260, 32);

        pnlForm.add(lblModeSiswa);
        pnlForm.add(lAlamat);
        pnlForm.add(scrollAlamat);
        pnlForm.add(btnSimpanSiswa);
        pnlForm.add(btnHapusSiswa);
        pnlForm.add(btnBaruSiswa);

        tab.add(scroll,  BorderLayout.CENTER);
        tab.add(pnlForm, BorderLayout.EAST);

        // Listeners tab siswa
        tblSiswa.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = tblSiswa.getSelectedRow();
                if (row < 0) return;
                idSiswaSelected = tblSiswa.getValueAt(row, 0).toString();
                txtNis.setText(tblSiswa.getValueAt(row, 1).toString());
                txtNamaSiswa.setText(tblSiswa.getValueAt(row, 2).toString());
                txtKelas.setText(tblSiswa.getValueAt(row, 3).toString());
                txtJurusan.setText(tblSiswa.getValueAt(row, 4).toString());
                txtTahunMasuk.setText(tblSiswa.getValueAt(row, 5).toString());
                txtNoTelpSiswa.setText(tblSiswa.getValueAt(row, 6) != null ? tblSiswa.getValueAt(row, 6).toString() : "");
                lblModeSiswa.setText("Mode: Edit — " + idSiswaSelected);
                lblModeSiswa.setForeground(new Color(70, 130, 200));
            }
        });

        btnSimpanSiswa.addActionListener(e -> simpanSiswa());
        btnHapusSiswa.addActionListener(e  -> hapusSiswa());
        btnBaruSiswa.addActionListener(e   -> resetFormSiswa());

        return tab;
    }

    private void loadTabelSiswa() {
        List<Siswa> list = siswaDAO.getAll();
        String[] kolom = {"ID", "NIS", "Nama", "Kelas", "Jurusan", "Thn Masuk", "No. Telp"};
        Object[][] data = new Object[list.size()][7];
        for (int i = 0; i < list.size(); i++) {
            Siswa s = list.get(i);
            data[i] = new Object[]{
                s.getIdSiswa(), s.getNis(), s.getNamaLengkap(),
                s.getKelas(), s.getJurusan(), s.getTahunMasuk(), s.getNoTelp()
            };
        }
        tblSiswa.setModel(new DefaultTableModel(data, kolom) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
    }

    private void simpanSiswa() {
        String nis   = txtNis.getText().trim();
        String nama  = txtNamaSiswa.getText().trim();
        String kelas = txtKelas.getText().trim();
        String jur   = txtJurusan.getText().trim();
        String thn   = txtTahunMasuk.getText().trim();

        if (nis.isEmpty() || nama.isEmpty() || kelas.isEmpty() || jur.isEmpty() || thn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIS, Nama, Kelas, Jurusan, dan Tahun Masuk wajib diisi!");
            return;
        }

        try {
            Siswa s = new Siswa();
            s.setNis(nis);
            s.setNamaLengkap(nama);
            s.setKelas(kelas);
            s.setJurusan(jur);
            s.setTahunMasuk(Integer.parseInt(thn));
            s.setNoTelp(txtNoTelpSiswa.getText().trim());
            s.setAlamat(txtAlamatSiswa.getText().trim());

            boolean ok;
            if (idSiswaSelected == null) {
                ok = siswaDAO.insert(s);
            } else {
                s.setIdSiswa(idSiswaSelected);
                ok = siswaDAO.update(s);
            }

            if (ok) {
                JOptionPane.showMessageDialog(this, "Data siswa berhasil disimpan!");
                resetFormSiswa();
                loadTabelSiswa();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tahun masuk harus berupa angka!");
        }
    }

    private void hapusSiswa() {
        if (idSiswaSelected == null) {
            JOptionPane.showMessageDialog(this, "Pilih siswa yang ingin dihapus!"); return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "Yakin hapus siswa ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            if (siswaDAO.delete(idSiswaSelected)) {
                JOptionPane.showMessageDialog(this, "Data siswa berhasil dihapus!");
                resetFormSiswa();
                loadTabelSiswa();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal! Siswa mungkin masih memiliki data pengajuan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void resetFormSiswa() {
        idSiswaSelected = null;
        txtNis.setText(""); txtNamaSiswa.setText(""); txtKelas.setText("");
        txtJurusan.setText(""); txtTahunMasuk.setText("");
        txtNoTelpSiswa.setText(""); txtAlamatSiswa.setText("");
        lblModeSiswa.setText("Mode: Tambah Baru");
        lblModeSiswa.setForeground(new Color(29, 158, 117));
        tblSiswa.clearSelection();
    }

    
    //  TAB GURU 
  

    private JPanel buatTabGuru() {
        JPanel tab = new JPanel(new BorderLayout(10, 0));
        tab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tblGuru = new JTable();
        tblGuru.setRowHeight(26);
        tblGuru.setFont(new Font("Arial", Font.PLAIN, 12));
        tblGuru.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblGuru.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblGuru.setGridColor(new Color(230, 230, 230));

        JScrollPane scroll = new JScrollPane(tblGuru);
        scroll.setBorder(BorderFactory.createTitledBorder("Daftar Guru"));

        JPanel pnlForm = new JPanel(null);
        pnlForm.setPreferredSize(new Dimension(290, 0));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Form Guru"));
        pnlForm.setBackground(Color.WHITE);

        lblModeGuru = new JLabel("Mode: Tambah Baru");
        lblModeGuru.setFont(new Font("Arial", Font.ITALIC, 11));
        lblModeGuru.setForeground(new Color(29, 158, 117));
        lblModeGuru.setBounds(12, 22, 260, 16);

        txtNip       = buatField(pnlForm, "NIP",           12,  42);
        txtNamaGuru  = buatField(pnlForm, "Nama Lengkap",  12,  96);
        txtJabatan   = buatField(pnlForm, "Jabatan",       12, 150);
        txtMapel     = buatField(pnlForm, "Mata Pelajaran",12, 204);
        txtNoTelpGuru= buatField(pnlForm, "No. Telp",      12, 258);

        JLabel lAlamat = new JLabel("Alamat");
        lAlamat.setFont(new Font("Arial", Font.PLAIN, 12));
        lAlamat.setBounds(12, 312, 260, 16);
        txtAlamatGuru = new JTextArea();
        txtAlamatGuru.setLineWrap(true);
        JScrollPane scrollAlamat = new JScrollPane(txtAlamatGuru);
        scrollAlamat.setBounds(12, 330, 260, 50);

        btnSimpanGuru = new JButton("💾  Simpan");
        btnHapusGuru  = new JButton("🗑  Hapus");
        btnBaruGuru   = new JButton("➕  Tambah Baru");
        styleBtn(btnSimpanGuru, new Color(29, 158, 117), Color.WHITE);
        styleBtn(btnHapusGuru,  new Color(180, 40, 40),  Color.WHITE);
        styleBtn(btnBaruGuru,   new Color(70, 130, 200), Color.WHITE);
        btnSimpanGuru.setBounds(12, 392, 260, 32);
        btnHapusGuru.setBounds (12, 432, 260, 32);
        btnBaruGuru.setBounds  (12, 472, 260, 32);

        pnlForm.add(lblModeGuru);
        pnlForm.add(lAlamat);
        pnlForm.add(scrollAlamat);
        pnlForm.add(btnSimpanGuru);
        pnlForm.add(btnHapusGuru);
        pnlForm.add(btnBaruGuru);

        tab.add(scroll,  BorderLayout.CENTER);
        tab.add(pnlForm, BorderLayout.EAST);

        tblGuru.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = tblGuru.getSelectedRow();
                if (row < 0) return;
                idGuruSelected = tblGuru.getValueAt(row, 0).toString();
                txtNip.setText(tblGuru.getValueAt(row, 1).toString());
                txtNamaGuru.setText(tblGuru.getValueAt(row, 2).toString());
                txtJabatan.setText(tblGuru.getValueAt(row, 3).toString());
                txtMapel.setText(tblGuru.getValueAt(row, 4) != null ? tblGuru.getValueAt(row, 4).toString() : "");
                txtNoTelpGuru.setText(tblGuru.getValueAt(row, 5) != null ? tblGuru.getValueAt(row, 5).toString() : "");
                lblModeGuru.setText("Mode: Edit — " + idGuruSelected);
                lblModeGuru.setForeground(new Color(70, 130, 200));
            }
        });

        btnSimpanGuru.addActionListener(e -> simpanGuru());
        btnHapusGuru.addActionListener(e  -> hapusGuru());
        btnBaruGuru.addActionListener(e   -> resetFormGuru());

        return tab;
    }

    private void loadTabelGuru() {
        List<Guru> list = guruDAO.getAll();
        String[] kolom = {"ID", "NIP", "Nama", "Jabatan", "Mapel", "No. Telp"};
        Object[][] data = new Object[list.size()][6];
        for (int i = 0; i < list.size(); i++) {
            Guru g = list.get(i);
            data[i] = new Object[]{
                g.getIdGuru(), g.getNip(), g.getNamaLengkap(),
                g.getJabatan(), g.getMataPelajaran(), g.getNoTelp()
            };
        }
        tblGuru.setModel(new DefaultTableModel(data, kolom) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
    }

    private void simpanGuru() {
        String nip  = txtNip.getText().trim();
        String nama = txtNamaGuru.getText().trim();
        String jab  = txtJabatan.getText().trim();

        if (nip.isEmpty() || nama.isEmpty() || jab.isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIP, Nama, dan Jabatan wajib diisi!"); return;
        }

        Guru g = new Guru();
        g.setNip(nip); g.setNamaLengkap(nama); g.setJabatan(jab);
        g.setMataPelajaran(txtMapel.getText().trim());
        g.setNoTelp(txtNoTelpGuru.getText().trim());
        g.setAlamat(txtAlamatGuru.getText().trim());

        boolean ok;
        if (idGuruSelected == null) {
            ok = guruDAO.insert(g);
        } else {
            g.setIdGuru(idGuruSelected);
            ok = guruDAO.update(g);
        }

        if (ok) {
            JOptionPane.showMessageDialog(this, "Data guru berhasil disimpan!");
            resetFormGuru(); loadTabelGuru();
        }
    }

    private void hapusGuru() {
        if (idGuruSelected == null) {
            JOptionPane.showMessageDialog(this, "Pilih guru yang ingin dihapus!"); return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "Yakin hapus guru ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            if (guruDAO.delete(idGuruSelected)) {
                JOptionPane.showMessageDialog(this, "Data guru berhasil dihapus!");
                resetFormGuru(); loadTabelGuru();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal! Guru mungkin masih memiliki data pengajuan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void resetFormGuru() {
        idGuruSelected = null;
        txtNip.setText(""); txtNamaGuru.setText(""); txtJabatan.setText("");
        txtMapel.setText(""); txtNoTelpGuru.setText(""); txtAlamatGuru.setText("");
        lblModeGuru.setText("Mode: Tambah Baru");
        lblModeGuru.setForeground(new Color(29, 158, 117));
        tblGuru.clearSelection();
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private JTextField buatField(JPanel panel, String label, int x, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setBounds(x, y, 260, 16);
        JTextField field = new JTextField();
        field.setBounds(x, y + 18, 260, 28);
        panel.add(lbl);
        panel.add(field);
        return field;
    }

    private JPanel buatHeader(String judul) {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(new Color(29, 158, 117));
        pnl.setPreferredSize(new Dimension(0, 48));
        pnl.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
        JLabel lbl = new JLabel(judul);
        lbl.setFont(new Font("Arial", Font.BOLD, 15));
        lbl.setForeground(Color.WHITE);
        pnl.add(lbl, BorderLayout.WEST);
        return pnl;
    }

    private void styleBtn(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg); btn.setForeground(fg);
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }


}
