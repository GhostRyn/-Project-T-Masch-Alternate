package Tampilan;

import dao.PengajuanDAO;
import dao.TiketDAO;
import model.Pengguna;
import model.Tiket;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class DashboardForm extends JFrame {

    // ── Data pengguna yang sedang login ───────────────────────────────────────
    private Pengguna penggunaLogin;

    // ── DAO ───────────────────────────────────────────────────────────────────
    private PengajuanDAO pengajuanDAO = new PengajuanDAO();
    private TiketDAO     tiketDAO     = new TiketDAO();

    // ── Komponen UI ───────────────────────────────────────────────────────────
    private JPanel  pnlHeader;
    private JPanel  pnlMenu;
    private JPanel  pnlStat;
    private JPanel  pnlKonten;

    private JLabel  lblNamaUser;
    private JLabel  lblRole;

    // Kartu statistik
    private JLabel  lblTotal;
    private JLabel  lblMenunggu;
    private JLabel  lblProses;
    private JLabel  lblSelesai;

    // Tabel antrian
    private JTable  tblAntrian;
    private JScrollPane scrollAntrian;

    // Tombol navigasi
    private JButton btnManajemenUser;
    private JButton btnDataSiswaGuru;
    private JButton btnPengajuan;
    private JButton btnTracking;
    private JButton btnReportRekap;
    private JButton btnReportHistori;
    private JButton btnLogout;

    // ── Constructor ───────────────────────────────────────────────────────────
    public DashboardForm(Pengguna pengguna) {
        this.penggunaLogin = pengguna;
        initComponents();
        setupListeners();
        loadDashboard();
    }

    // ── Inisialisasi komponen ─────────────────────────────────────────────────
    private void initComponents() {
        setTitle("Dashboard — Sistem Ticketing Administrasi Sekolah");
        setSize(1024, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ── HEADER ────────────────────────────────────────────────────────────
        pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(29, 158, 117));
        pnlHeader.setPreferredSize(new Dimension(0, 60));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel lblApp = new JLabel("Sistem Ticketing Administrasi Sekolah");
        lblApp.setFont(new Font("Arial", Font.BOLD, 16));
        lblApp.setForeground(Color.WHITE);

        JPanel pnlUserInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlUserInfo.setOpaque(false);

        lblNamaUser = new JLabel();
        lblNamaUser.setFont(new Font("Arial", Font.BOLD, 13));
        lblNamaUser.setForeground(Color.WHITE);

        lblRole = new JLabel();
        lblRole.setFont(new Font("Arial", Font.PLAIN, 11));
        lblRole.setForeground(new Color(200, 240, 225));

        btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Arial", Font.PLAIN, 11));
        btnLogout.setBackground(new Color(15, 110, 86));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlUserInfo.add(lblNamaUser);
        pnlUserInfo.add(lblRole);
        pnlUserInfo.add(btnLogout);

        pnlHeader.add(lblApp, BorderLayout.WEST);
        pnlHeader.add(pnlUserInfo, BorderLayout.EAST);

        // ── MENU SIDEBAR ──────────────────────────────────────────────────────
        pnlMenu = new JPanel();
        pnlMenu.setLayout(new BoxLayout(pnlMenu, BoxLayout.Y_AXIS));
        pnlMenu.setBackground(new Color(40, 40, 40));
        pnlMenu.setPreferredSize(new Dimension(190, 0));
        pnlMenu.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel lblMenuJudul = new JLabel("  MENU");
        lblMenuJudul.setFont(new Font("Arial", Font.BOLD, 11));
        lblMenuJudul.setForeground(new Color(150, 150, 150));
        lblMenuJudul.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMenuJudul.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 0));

        btnManajemenUser  = buatTombolMenu("👥  Manajemen Pengguna");
        btnDataSiswaGuru  = buatTombolMenu("📋  Data Siswa & Guru");
        btnPengajuan      = buatTombolMenu("📝  Pengajuan Surat");
        btnTracking       = buatTombolMenu("🎫  Tracking Tiket");
        btnReportRekap    = buatTombolMenu("📊  Report Rekap");
        btnReportHistori  = buatTombolMenu("📜  Report Histori");

        pnlMenu.add(lblMenuJudul);
        pnlMenu.add(btnManajemenUser);
        pnlMenu.add(btnDataSiswaGuru);
        pnlMenu.add(Box.createVerticalStrut(8));
        pnlMenu.add(buatSeparator());
        pnlMenu.add(Box.createVerticalStrut(8));
        pnlMenu.add(btnPengajuan);
        pnlMenu.add(btnTracking);
        pnlMenu.add(Box.createVerticalStrut(8));
        pnlMenu.add(buatSeparator());
        pnlMenu.add(Box.createVerticalStrut(8));
        pnlMenu.add(btnReportRekap);
        pnlMenu.add(btnReportHistori);
        pnlMenu.add(Box.createVerticalGlue());

        // ── KONTEN UTAMA ──────────────────────────────────────────────────────
        pnlKonten = new JPanel(new BorderLayout(10, 10));
        pnlKonten.setBackground(new Color(245, 245, 245));
        pnlKonten.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Kartu statistik (4 kartu sejajar)
        pnlStat = new JPanel(new GridLayout(1, 4, 12, 0));
        pnlStat.setOpaque(false);

        lblTotal    = new JLabel("0", SwingConstants.CENTER);
        lblMenunggu = new JLabel("0", SwingConstants.CENTER);
        lblProses   = new JLabel("0", SwingConstants.CENTER);
        lblSelesai  = new JLabel("0", SwingConstants.CENTER);

        pnlStat.add(buatKartuStat("Total Tiket",  lblTotal,    new Color(70, 70, 70)));
        pnlStat.add(buatKartuStat("Menunggu",      lblMenunggu, new Color(186, 117, 23)));
        pnlStat.add(buatKartuStat("Proses",        lblProses,   new Color(83, 74, 183)));
        pnlStat.add(buatKartuStat("Selesai",       lblSelesai,  new Color(15, 110, 86)));

        // Tabel antrian aktif
        tblAntrian = new JTable();
        tblAntrian.setRowHeight(28);
        tblAntrian.setFont(new Font("Arial", Font.PLAIN, 12));
        tblAntrian.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblAntrian.setSelectionBackground(new Color(220, 240, 235));
        tblAntrian.setGridColor(new Color(230, 230, 230));
        tblAntrian.setShowGrid(true);

        scrollAntrian = new JScrollPane(tblAntrian);
        scrollAntrian.setBorder(BorderFactory.createTitledBorder("Antrian Aktif Hari Ini"));

        pnlKonten.add(pnlStat,       BorderLayout.NORTH);
        pnlKonten.add(scrollAntrian, BorderLayout.CENTER);

        // ── Gabungkan ke JFrame ───────────────────────────────────────────────
        add(pnlHeader,  BorderLayout.NORTH);
        add(pnlMenu,    BorderLayout.WEST);
        add(pnlKonten,  BorderLayout.CENTER);
    }

    // ── Load data ke dashboard ────────────────────────────────────────────────
    private void loadDashboard() {
        // Info user
        lblNamaUser.setText(penggunaLogin.getUsername());
        lblRole.setText("[" + penggunaLogin.getRole() + "]");

        // Statistik — admin lihat semua, siswa/guru hanya milik sendiri
        String idUser = penggunaLogin.getRole().equals("admin")
                        ? null : penggunaLogin.getIdPengguna();

        lblTotal.setText(String.valueOf(pengajuanDAO.countTotal(idUser)));
        lblMenunggu.setText(String.valueOf(pengajuanDAO.countByStatus("menunggu", idUser)));
        lblProses.setText(String.valueOf(pengajuanDAO.countByStatus("proses",    idUser)));
        lblSelesai.setText(String.valueOf(pengajuanDAO.countByStatus("selesai",  idUser)));

        // Tabel antrian
        List<Tiket> antrian = tiketDAO.getAntrianAktif();
        String[] kolom = {"Antrian", "No. Tiket", "Pemohon", "Info", "Jenis Surat", "Status"};
        Object[][] data = new Object[antrian.size()][6];
        for (int i = 0; i < antrian.size(); i++) {
            Tiket t = antrian.get(i);
            data[i] = new Object[]{
                t.getNomorAntrian(), t.getNoTiket(),
                t.getNamaPemohon(), t.getInfoPemohon(),
                t.getNamaJenis(),   t.getStatus()
            };
        }
        tblAntrian.setModel(new DefaultTableModel(data, kolom) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });

        // Visibilitas menu sesuai role
        boolean isAdmin = penggunaLogin.getRole().equals("admin");
        btnManajemenUser.setVisible(isAdmin);
        btnDataSiswaGuru.setVisible(isAdmin);
        btnTracking.setVisible(isAdmin);
        btnReportRekap.setVisible(isAdmin);
        btnReportHistori.setVisible(true); 
    }

    // ── Setup listeners navigasi ──────────────────────────────────────────────
    private void setupListeners() {

      //  btnManajemenUser.addActionListener(e ->
       //    new ManajemenPenggunaForm(penggunaLogin).setVisible(true));

      //  btnDataSiswaGuru.addActionListener(e ->
         //   new DataSiswaGuruForm(penggunaLogin).setVisible(true));

       // btnPengajuan.addActionListener(e ->
           // new PengajuanSuratForm(penggunaLogin).setVisible(true));

       // btnTracking.addActionListener(e ->
         //   new TrackingTiketForm(penggunaLogin).setVisible(true));

       // btnReportRekap.addActionListener(e ->
          //  new ReportRekapForm(penggunaLogin).setVisible(true));

       // btnReportHistori.addActionListener(e ->
          //  new ReportHistoriForm(penggunaLogin).setVisible(true));

      //  btnLogout.addActionListener(e -> {
         //   int ok = JOptionPane.showConfirmDialog(
           //     this, "Yakin ingin logout?", "Logout", JOptionPane.YES_NO_OPTION);
          //  if (ok == JOptionPane.YES_OPTION) {
           //     new LoginForm().setVisible(true);
           //     this.dispose();
        //    }
      //  });
    }

    // ── Helper: buat kartu statistik ─────────────────────────────────────────
    private JPanel buatKartuStat(String judul, JLabel lblAngka, Color warnaAngka) {
        JPanel kartu = new JPanel(new BorderLayout());
        kartu.setBackground(Color.WHITE);
        kartu.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));

        JLabel lblJudul = new JLabel(judul);
        lblJudul.setFont(new Font("Arial", Font.PLAIN, 11));
        lblJudul.setForeground(Color.GRAY);

        lblAngka.setFont(new Font("Arial", Font.BOLD, 28));
        lblAngka.setForeground(warnaAngka);

        kartu.add(lblJudul,  BorderLayout.NORTH);
        kartu.add(lblAngka,  BorderLayout.CENTER);
        return kartu;
    }

    // ── Helper: buat tombol menu sidebar ─────────────────────────────────────
    private JButton buatTombolMenu(String teks) {
        JButton btn = new JButton(teks);
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
        btn.setForeground(new Color(220, 220, 220));
        btn.setBackground(new Color(40, 40, 40));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Efek hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(29, 158, 117));
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(40, 40, 40));
            }
        });
        return btn;
    }

    // ── Helper: garis pemisah di sidebar ─────────────────────────────────────
    private JSeparator buatSeparator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(70, 70, 70));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }
}
