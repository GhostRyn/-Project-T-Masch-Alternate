package Tampilan;

import dao.PenggunaDAO;
import model.Pengguna;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class ManajemenPenggunaForm extends JFrame {

    private Pengguna     penggunaLogin;
    private PenggunaDAO  dao = new PenggunaDAO();
    private String       idSelected = null; 

    // ── Komponen
    private JTable           tblPengguna;
    private JScrollPane      scrollPane;
    private JTextField       txtUsername;
    private JPasswordField   txtPassword;
    private JComboBox<String> cmbRole;
    private JCheckBox        chkAktif;
    private JButton          btnSimpan;
    private JButton          btnHapus;
    private JButton          btnBaru;
    private JButton          btnRefresh;
    private JLabel           lblMode;

    public ManajemenPenggunaForm(Pengguna pengguna) {
        this.penggunaLogin = pengguna;
        initComponents();
        setupListeners();
        loadTabel();
    }

    private void initComponents() {
        setTitle("Manajemen Pengguna");
        setSize(860, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ── HEADER 
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(29, 158, 117));
        pnlHeader.setPreferredSize(new Dimension(0, 48));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
        JLabel lblJudul = new JLabel("Manajemen Pengguna");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 15));
        lblJudul.setForeground(Color.WHITE);
        pnlHeader.add(lblJudul, BorderLayout.WEST);

        // ── TABEL kiri
        tblPengguna = new JTable();
        tblPengguna.setRowHeight(26);
        tblPengguna.setFont(new Font("Arial", Font.PLAIN, 12));
        tblPengguna.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblPengguna.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPengguna.setGridColor(new Color(230, 230, 230));

        scrollPane = new JScrollPane(tblPengguna);
        scrollPane.setPreferredSize(new Dimension(520, 0));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Pengguna"));

        // ──INPUT (kanan)
        JPanel pnlForm = new JPanel(null);
        pnlForm.setPreferredSize(new Dimension(290, 0));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Form Pengguna"));
        pnlForm.setBackground(Color.WHITE);

        lblMode = new JLabel("Mode: Tambah Baru");
        lblMode.setFont(new Font("Arial", Font.ITALIC, 11));
        lblMode.setForeground(new Color(29, 158, 117));
        lblMode.setBounds(12, 22, 260, 20);

        JLabel l1 = new JLabel("Username");
        l1.setBounds(12, 52, 260, 16);
        l1.setFont(new Font("Arial", Font.PLAIN, 12));

        txtUsername = new JTextField();
        txtUsername.setBounds(12, 70, 260, 30);

        JLabel l2 = new JLabel("Password (kosongkan jika tidak diubah)");
        l2.setBounds(12, 110, 260, 16);
        l2.setFont(new Font("Arial", Font.PLAIN, 12));

        txtPassword = new JPasswordField();
        txtPassword.setBounds(12, 128, 260, 30);

        JLabel l3 = new JLabel("Role");
        l3.setBounds(12, 168, 260, 16);
        l3.setFont(new Font("Arial", Font.PLAIN, 12));

        cmbRole = new JComboBox<>(new String[]{"admin", "siswa", "guru"});
        cmbRole.setBounds(12, 186, 260, 30);

        chkAktif = new JCheckBox("Pengguna Aktif");
        chkAktif.setBounds(12, 228, 260, 24);
        chkAktif.setSelected(true);
        chkAktif.setBackground(Color.WHITE);

        btnSimpan  = new JButton("💾  Simpan");
        btnHapus   = new JButton("🗑  Hapus");
        btnBaru    = new JButton("➕  Tambah Baru");
        btnRefresh = new JButton("🔄  Refresh");

        styleBtn(btnSimpan,  new Color(29, 158, 117), Color.WHITE);
        styleBtn(btnHapus,   new Color(180, 40, 40),  Color.WHITE);
        styleBtn(btnBaru,    new Color(70, 130, 200),  Color.WHITE);
        styleBtn(btnRefresh, new Color(100, 100, 100), Color.WHITE);

        btnSimpan.setBounds(12,  270, 260, 34);
        btnHapus.setBounds (12,  312, 260, 34);
        btnBaru.setBounds  (12,  354, 260, 34);
        btnRefresh.setBounds(12, 396, 260, 34);

        pnlForm.add(lblMode);
        pnlForm.add(l1); pnlForm.add(txtUsername);
        pnlForm.add(l2); pnlForm.add(txtPassword);
        pnlForm.add(l3); pnlForm.add(cmbRole);
        pnlForm.add(chkAktif);
        pnlForm.add(btnSimpan);
        pnlForm.add(btnHapus);
        pnlForm.add(btnBaru);
        pnlForm.add(btnRefresh);

        // ── Konten tengah
        JPanel pnlKonten = new JPanel(new BorderLayout(10, 0));
        pnlKonten.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlKonten.add(scrollPane, BorderLayout.CENTER);
        pnlKonten.add(pnlForm,   BorderLayout.EAST);

        add(pnlHeader,  BorderLayout.NORTH);
        add(pnlKonten,  BorderLayout.CENTER);
    }

    private void loadTabel() {
        List<Pengguna> list = dao.getAll();
        String[] kolom = {"ID", "Username", "Role", "Aktif"};
        Object[][] data = new Object[list.size()][4];
        for (int i = 0; i < list.size(); i++) {
            Pengguna p = list.get(i);
            data[i] = new Object[]{
                p.getIdPengguna(), p.getUsername(),
                p.getRole(), p.isActive() ? "Ya" : "Tidak"
            };
        }
        tblPengguna.setModel(new DefaultTableModel(data, kolom) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
    }

    private void setupListeners() {

        // baris tabel 
        tblPengguna.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = tblPengguna.getSelectedRow();
                if (row < 0) return;
                idSelected = tblPengguna.getValueAt(row, 0).toString();
                txtUsername.setText(tblPengguna.getValueAt(row, 1).toString());
                cmbRole.setSelectedItem(tblPengguna.getValueAt(row, 2).toString());
                chkAktif.setSelected(tblPengguna.getValueAt(row, 3).toString().equals("Ya"));
                txtPassword.setText("");
                lblMode.setText("Mode: Edit — " + idSelected);
                lblMode.setForeground(new Color(70, 130, 200));
            }
        });

        // Simpan
        btnSimpan.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            String role     = cmbRole.getSelectedItem().toString();

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username wajib diisi!");
                return;
            }

            if (idSelected == null && password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password wajib diisi untuk pengguna baru!");
                return;
            }

            boolean ok;
            if (idSelected == null) {
                // Mode tambah
                Pengguna p = new Pengguna();
                p.setUsername(username);
                p.setPassword(password);
                p.setRole(role);
                p.setActive(chkAktif.isSelected());
                ok = dao.insert(p);
            } else {
                // deit pengguna 
                Pengguna p = new Pengguna();
                p.setIdPengguna(idSelected);
                p.setUsername(username);
                p.setRole(role);
                p.setActive(chkAktif.isSelected());
                ok = dao.update(p);

                // Update password kalo diisi
                if (ok && !password.isEmpty()) {
                    dao.updatePassword(idSelected, password);
                }
            }

            if (ok) {
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
                resetForm();
                loadTabel();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Hapus (soft delete)
        btnHapus.addActionListener(e -> {
            if (idSelected == null) {
                JOptionPane.showMessageDialog(this, "Pilih pengguna yang ingin dihapus!");
                return;
            }
            if (idSelected.equals(penggunaLogin.getIdPengguna())) {
                JOptionPane.showMessageDialog(this, "Tidak bisa menghapus akun yang sedang login!");
                return;
            }
            int konfirm = JOptionPane.showConfirmDialog(
                this, "Yakin ingin menonaktifkan pengguna ini?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION
            );
            if (konfirm == JOptionPane.YES_OPTION) {
                if (dao.delete(idSelected)) {
                    JOptionPane.showMessageDialog(this, "Pengguna berhasil dinonaktifkan!");
                    resetForm();
                    loadTabel();
                }
            }
        });

        // Baru
        btnBaru.addActionListener(e -> resetForm());

        // Refresh
        btnRefresh.addActionListener(e -> loadTabel());
    }

    private void resetForm() {
        idSelected = null;
        txtUsername.setText("");
        txtPassword.setText("");
        cmbRole.setSelectedIndex(0);
        chkAktif.setSelected(true);
        lblMode.setText("Mode: Tambah Baru");
        lblMode.setForeground(new Color(29, 158, 117));
        tblPengguna.clearSelection();
    }

    private void styleBtn(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
