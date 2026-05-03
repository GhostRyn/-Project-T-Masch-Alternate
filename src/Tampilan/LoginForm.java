package Tampilan;

import config.DBConnection;
import dao.PenggunaDAO;
import model.Pengguna;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class LoginForm extends JFrame {

    // ── Komponen UI
    private JPanel      pnlKiri;
    private JPanel      pnlKanan;
    private JLabel      lblJudul;
    private JLabel      lblJudul1;
    private JLabel      lblSubjudul;
    private JLabel      lblRole;
    private JLabel      lblUsername;
    private JLabel      lblPassword;
    private JLabel      lblError;
    private JComboBox<String> cmbRole;
    private JTextField  txtUsername;
    private JPasswordField txtPassword;
    private JButton     btnLogin;
    private JButton     btnKeluar;

    // ── DAO
    private PenggunaDAO penggunaDAO = new PenggunaDAO();

    // ── Constructor 
    public LoginForm() {
        initComponents();
        setupListeners();
    }


    private void initComponents() {
        setTitle("Login — Sistem Ticketing Administrasi Sekolah");
        setSize(750, 450);
        setLocationRelativeTo(null);        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new GridLayout(1, 2));    

        // Panel Kiri 
        pnlKiri = new JPanel();
        pnlKiri.setBackground(new Color(29, 158, 117));   // ijo
        pnlKiri.setLayout(new GridBagLayout());

        JPanel pnlKiriInner = new JPanel();
        pnlKiriInner.setOpaque(false);
        pnlKiriInner.setLayout(new BoxLayout(pnlKiriInner, BoxLayout.Y_AXIS));

       // Logo sekolah
    JLabel lblLogo;
    try {
    ImageIcon logoAsli = new ImageIcon(getClass().getResource("/assets/TJ-86680.png"));
    Image logoScaled = logoAsli.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
    lblLogo = new JLabel(new ImageIcon(logoScaled), SwingConstants.CENTER);
    } catch (Exception e) {
    lblLogo = new JLabel("🏫", SwingConstants.CENTER);
    lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
    }
    lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lblJudul1 = new JLabel("SMKS TIRTAJAYA DEPOK", SwingConstants.CENTER);
        lblJudul1.setFont(new Font("Arial", Font.BOLD, 20));
        lblJudul1.setForeground(Color.WHITE);
        lblJudul1.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    
        lblJudul = new JLabel("Sistem Ticketing", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 18));
        lblJudul.setForeground(Color.WHITE);
        lblJudul.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblSubjudul = new JLabel("Administrasi Sekolah", SwingConstants.CENTER);
        lblSubjudul.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSubjudul.setForeground(new Color(200, 240, 225));
        lblSubjudul.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlKiriInner.add(Box.createVerticalGlue());
        pnlKiriInner.add(lblLogo);
        pnlKiriInner.add(Box.createVerticalStrut(12));
        pnlKiriInner.add(Box.createVerticalStrut(12));
        pnlKiriInner.add(lblJudul1);
        pnlKiriInner.add(lblJudul);
        pnlKiriInner.add(Box.createVerticalStrut(4));
        pnlKiriInner.add(lblSubjudul);
        pnlKiriInner.add(Box.createVerticalGlue());

        pnlKiri.add(pnlKiriInner);

        // ── Panel Kanan 
        pnlKanan = new JPanel();
        pnlKanan.setBackground(Color.WHITE);
        pnlKanan.setLayout(null);           // absolute layout 

        JLabel lblWelcome = new JLabel("Selamat Datang");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 20));
        lblWelcome.setBounds(40, 40, 280, 30);

        JLabel lblSub = new JLabel("Masuk untuk melanjutkan ke sistem");
        lblSub.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSub.setForeground(Color.GRAY);
        lblSub.setBounds(40, 70, 280, 20);

        lblRole = new JLabel("Role");
        lblRole.setFont(new Font("Arial", Font.PLAIN, 12));
        lblRole.setBounds(40, 110, 280, 20);

        cmbRole = new JComboBox<>(new String[]{"admin", "siswa", "guru"});
        cmbRole.setBounds(40, 130, 280, 32);

        lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 12));
        lblUsername.setBounds(40, 175, 280, 20);

        txtUsername = new JTextField();
        txtUsername.setBounds(40, 195, 280, 32);

        lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPassword.setBounds(40, 240, 280, 20);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(40, 260, 280, 32);

        lblError = new JLabel("");
        lblError.setFont(new Font("Arial", Font.PLAIN, 11));
        lblError.setForeground(Color.RED);
        lblError.setBounds(40, 300, 280, 20);

        btnLogin = new JButton("Masuk");
        btnLogin.setBounds(40, 325, 280, 38);
        btnLogin.setBackground(new Color(29, 158, 117));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnKeluar = new JButton("Keluar");
        btnKeluar.setBounds(40, 370, 280, 30);
        btnKeluar.setBackground(Color.WHITE);
        btnKeluar.setForeground(Color.GRAY);
        btnKeluar.setFont(new Font("Arial", Font.PLAIN, 11));
        btnKeluar.setFocusPainted(false);
        btnKeluar.setBorderPainted(false);
        btnKeluar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlKanan.add(lblWelcome);
        pnlKanan.add(lblSub);
        pnlKanan.add(lblRole);
        pnlKanan.add(cmbRole);
        pnlKanan.add(lblUsername);
        pnlKanan.add(txtUsername);
        pnlKanan.add(lblPassword);
        pnlKanan.add(txtPassword);
        pnlKanan.add(lblError);
        pnlKanan.add(btnLogin);
        pnlKanan.add(btnKeluar);

        
        add(pnlKiri);
        add(pnlKanan);
    }

    private void setupListeners() {

        
        btnLogin.addActionListener(e -> doLogin());

       
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) doLogin();
            }
        });

    
        btnKeluar.addActionListener(e -> {
            int konfirm = JOptionPane.showConfirmDialog(
                this, "Yakin ingin keluar?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION
            );
            if (konfirm == JOptionPane.YES_OPTION) {
                DBConnection.closeConnection();
                System.exit(0);
            }
        });
    }

    // ── Logic login 
    private void doLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String role     = cmbRole.getSelectedItem().toString();

        // Validasi input kosong
        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Username dan password wajib diisi!");
            return;
        }

        // Cek ke database
        Pengguna pengguna = penggunaDAO.login(username, password, role);

        if (pengguna != null) {
            lblError.setText("");
            // Buka Dashboard dan tutup LoginForm
            new DashboardForm(pengguna).setVisible(true);
            this.dispose();
        } else {
            lblError.setText("Username, password, atau role salah!");
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

  
    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
