-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 03, 2026 at 03:44 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_ticketing_sekolah`
--

-- --------------------------------------------------------

--
-- Table structure for table `guru`
--

CREATE TABLE `guru` (
  `id_guru` varchar(10) NOT NULL,
  `nip` varchar(20) NOT NULL,
  `nama_lengkap` varchar(100) NOT NULL,
  `jabatan` varchar(50) NOT NULL,
  `mata_pelajaran` varchar(50) DEFAULT NULL,
  `no_telp` varchar(15) DEFAULT NULL,
  `alamat` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `guru`
--

INSERT INTO `guru` (`id_guru`, `nip`, `nama_lengkap`, `jabatan`, `mata_pelajaran`, `no_telp`, `alamat`) VALUES
('GRU-001', '198501012010011001', 'Budi Santoso', 'Guru Tetap', 'Matematika', '08345678901', NULL),
('GRU-002', '199002022015012002', 'Rina Melati', 'Wali Kelas', 'Bahasa Indonesia', '08456789012', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `jenis_surat`
--

CREATE TABLE `jenis_surat` (
  `id_jenis` varchar(10) NOT NULL,
  `nama_jenis` varchar(100) NOT NULL,
  `keterangan` text DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `jenis_surat`
--

INSERT INTO `jenis_surat` (`id_jenis`, `nama_jenis`, `keterangan`, `is_active`) VALUES
('JNS-001', 'Surat Keterangan Siswa', 'Surat keterangan aktif sebagai siswa', 1),
('JNS-002', 'Surat Izin Siswa', 'Surat izin tidak masuk / kegiatan luar sekolah', 1),
('JNS-003', 'Surat Keterangan Guru', 'Surat keterangan aktif sebagai guru/pegawai', 1),
('JNS-004', 'Surat Pengantar / Rekomendasi', 'Surat pengantar untuk keperluan tertentu', 1);

-- --------------------------------------------------------

--
-- Table structure for table `pengajuan`
--

CREATE TABLE `pengajuan` (
  `id_pengajuan` varchar(10) NOT NULL,
  `id_jenis` varchar(10) NOT NULL,
  `id_pengguna` varchar(10) NOT NULL,
  `id_siswa` varchar(10) DEFAULT NULL,
  `id_guru` varchar(10) DEFAULT NULL,
  `tipe_pemohon` enum('siswa','guru') NOT NULL,
  `keperluan` text NOT NULL,
  `berkas_pendukung` varchar(255) DEFAULT NULL,
  `tgl_pengajuan` date NOT NULL DEFAULT curdate()
) ;

-- --------------------------------------------------------

--
-- Table structure for table `pengguna`
--

CREATE TABLE `pengguna` (
  `id_pengguna` varchar(10) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('admin','siswa','guru') NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pengguna`
--

INSERT INTO `pengguna` (`id_pengguna`, `username`, `password`, `role`, `is_active`, `created_at`) VALUES
('USR-001', 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin', 1, '2026-04-29 09:05:57'),
('USR-002', 'andi.s', 'ca82d8a67832679fdc39c9156f087e31236b833ee7371eb3d6e081aeb90016c9', 'siswa', 1, '2026-04-29 09:05:57'),
('USR-003', 'budi.guru', 'ae81343369944399b70de862dbe75536faa8e44c50ad0a312e380303173f4756', 'guru', 1, '2026-04-29 09:05:57');

-- --------------------------------------------------------

--
-- Table structure for table `siswa`
--

CREATE TABLE `siswa` (
  `id_siswa` varchar(10) NOT NULL,
  `nis` varchar(20) NOT NULL,
  `nama_lengkap` varchar(100) NOT NULL,
  `kelas` varchar(10) NOT NULL,
  `jurusan` varchar(50) NOT NULL,
  `tahun_masuk` year(4) NOT NULL,
  `no_telp` varchar(15) DEFAULT NULL,
  `alamat` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `siswa`
--

INSERT INTO `siswa` (`id_siswa`, `nis`, `nama_lengkap`, `kelas`, `jurusan`, `tahun_masuk`, `no_telp`, `alamat`) VALUES
('SSW-001', '2024001', 'Andi Saputra', 'XII RPL 1', 'Rekayasa Perangkat Lunak', '2024', '08123456789', NULL),
('SSW-002', '2024002', 'Siti Rahayu', 'XI TKJ 2', 'Teknik Komputer Jaringan', '2024', '08234567890', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `status_log`
--

CREATE TABLE `status_log` (
  `id_log` varchar(10) NOT NULL,
  `id_tiket` varchar(10) NOT NULL,
  `status_lama` enum('menunggu','proses','selesai','ditolak') NOT NULL,
  `status_baru` enum('menunggu','proses','selesai','ditolak') NOT NULL,
  `id_pengguna` varchar(10) NOT NULL,
  `tgl_update` timestamp NOT NULL DEFAULT current_timestamp(),
  `keterangan` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tiket`
--

CREATE TABLE `tiket` (
  `id_tiket` varchar(10) NOT NULL,
  `id_pengajuan` varchar(10) NOT NULL,
  `no_tiket` varchar(20) NOT NULL,
  `nomor_antrian` int(11) NOT NULL,
  `status` enum('menunggu','proses','selesai','ditolak') NOT NULL DEFAULT 'menunggu',
  `id_petugas` varchar(10) DEFAULT NULL,
  `catatan` text DEFAULT NULL,
  `tgl_dibuat` timestamp NOT NULL DEFAULT current_timestamp(),
  `tgl_selesai` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Triggers `tiket`
--
DELIMITER $$
CREATE TRIGGER `trg_after_update_tiket` AFTER UPDATE ON `tiket` FOR EACH ROW BEGIN
  -- hanya catat jika status benar-benar berubah
  IF OLD.status <> NEW.status THEN

    INSERT INTO status_log (
      id_log,
      id_tiket,
      status_lama,
      status_baru,
      id_pengguna,
      tgl_update,
      keterangan
    ) VALUES (
      CONCAT('LOG-', LPAD(
        (SELECT COUNT(*) + 1 FROM status_log),
        3, '0'
      )),
      NEW.id_tiket,
      OLD.status,
      NEW.status,
      IFNULL(NEW.id_petugas, 'USR-001'),
      NOW(),
      NULL
    );

  END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_antrian_aktif`
-- (See below for the actual view)
--
CREATE TABLE `v_antrian_aktif` (
`id_tiket` varchar(10)
,`no_tiket` varchar(20)
,`nomor_antrian` int(11)
,`status` enum('menunggu','proses','selesai','ditolak')
,`catatan` text
,`tgl_dibuat` timestamp
,`tgl_selesai` timestamp
,`tipe_pemohon` enum('siswa','guru')
,`keperluan` text
,`nama_jenis` varchar(100)
,`nama_pemohon` varchar(100)
,`info_pemohon` varchar(50)
,`nomor_induk` varchar(20)
,`nama_petugas` varchar(50)
,`id_petugas` varchar(10)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_histori_tiket`
-- (See below for the actual view)
--
CREATE TABLE `v_histori_tiket` (
`id_tiket` varchar(10)
,`no_tiket` varchar(20)
,`nomor_antrian` int(11)
,`status` enum('menunggu','proses','selesai','ditolak')
,`tgl_dibuat` timestamp
,`tgl_selesai` timestamp
,`durasi_menit` bigint(21)
,`tipe_pemohon` enum('siswa','guru')
,`keperluan` text
,`tgl_pengajuan` date
,`id_siswa` varchar(10)
,`id_guru` varchar(10)
,`id_pengguna` varchar(10)
,`nama_jenis` varchar(100)
,`nama_pemohon` varchar(100)
,`nomor_induk` varchar(20)
,`info_pemohon` varchar(50)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_rekap_pengajuan`
-- (See below for the actual view)
--
CREATE TABLE `v_rekap_pengajuan` (
`id_jenis` varchar(10)
,`nama_jenis` varchar(100)
,`total` bigint(21)
,`menunggu` decimal(23,0)
,`proses` decimal(23,0)
,`selesai` decimal(23,0)
,`ditolak` decimal(23,0)
,`rata_durasi_menit` decimal(22,1)
);

-- --------------------------------------------------------

--
-- Structure for view `v_antrian_aktif`
--
DROP TABLE IF EXISTS `v_antrian_aktif`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_antrian_aktif`  AS SELECT `t`.`id_tiket` AS `id_tiket`, `t`.`no_tiket` AS `no_tiket`, `t`.`nomor_antrian` AS `nomor_antrian`, `t`.`status` AS `status`, `t`.`catatan` AS `catatan`, `t`.`tgl_dibuat` AS `tgl_dibuat`, `t`.`tgl_selesai` AS `tgl_selesai`, `p`.`tipe_pemohon` AS `tipe_pemohon`, `p`.`keperluan` AS `keperluan`, `js`.`nama_jenis` AS `nama_jenis`, CASE WHEN `p`.`tipe_pemohon` = 'siswa' THEN `s`.`nama_lengkap` ELSE `g`.`nama_lengkap` END AS `nama_pemohon`, CASE WHEN `p`.`tipe_pemohon` = 'siswa' THEN `s`.`kelas` ELSE `g`.`jabatan` END AS `info_pemohon`, CASE WHEN `p`.`tipe_pemohon` = 'siswa' THEN `s`.`nis` ELSE `g`.`nip` END AS `nomor_induk`, `pg`.`username` AS `nama_petugas`, `t`.`id_petugas` AS `id_petugas` FROM (((((`tiket` `t` join `pengajuan` `p` on(`t`.`id_pengajuan` = `p`.`id_pengajuan`)) join `jenis_surat` `js` on(`p`.`id_jenis` = `js`.`id_jenis`)) left join `siswa` `s` on(`p`.`id_siswa` = `s`.`id_siswa`)) left join `guru` `g` on(`p`.`id_guru` = `g`.`id_guru`)) left join `pengguna` `pg` on(`t`.`id_petugas` = `pg`.`id_pengguna`)) WHERE cast(`t`.`tgl_dibuat` as date) = curdate() ORDER BY `t`.`nomor_antrian` ASC ;

-- --------------------------------------------------------

--
-- Structure for view `v_histori_tiket`
--
DROP TABLE IF EXISTS `v_histori_tiket`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_histori_tiket`  AS SELECT `t`.`id_tiket` AS `id_tiket`, `t`.`no_tiket` AS `no_tiket`, `t`.`nomor_antrian` AS `nomor_antrian`, `t`.`status` AS `status`, `t`.`tgl_dibuat` AS `tgl_dibuat`, `t`.`tgl_selesai` AS `tgl_selesai`, timestampdiff(MINUTE,`t`.`tgl_dibuat`,ifnull(`t`.`tgl_selesai`,current_timestamp())) AS `durasi_menit`, `p`.`tipe_pemohon` AS `tipe_pemohon`, `p`.`keperluan` AS `keperluan`, `p`.`tgl_pengajuan` AS `tgl_pengajuan`, `p`.`id_siswa` AS `id_siswa`, `p`.`id_guru` AS `id_guru`, `p`.`id_pengguna` AS `id_pengguna`, `js`.`nama_jenis` AS `nama_jenis`, CASE WHEN `p`.`tipe_pemohon` = 'siswa' THEN `s`.`nama_lengkap` ELSE `g`.`nama_lengkap` END AS `nama_pemohon`, CASE WHEN `p`.`tipe_pemohon` = 'siswa' THEN `s`.`nis` ELSE `g`.`nip` END AS `nomor_induk`, CASE WHEN `p`.`tipe_pemohon` = 'siswa' THEN `s`.`kelas` ELSE `g`.`jabatan` END AS `info_pemohon` FROM ((((`tiket` `t` join `pengajuan` `p` on(`t`.`id_pengajuan` = `p`.`id_pengajuan`)) join `jenis_surat` `js` on(`p`.`id_jenis` = `js`.`id_jenis`)) left join `siswa` `s` on(`p`.`id_siswa` = `s`.`id_siswa`)) left join `guru` `g` on(`p`.`id_guru` = `g`.`id_guru`)) ORDER BY `t`.`tgl_dibuat` DESC ;

-- --------------------------------------------------------

--
-- Structure for view `v_rekap_pengajuan`
--
DROP TABLE IF EXISTS `v_rekap_pengajuan`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_rekap_pengajuan`  AS SELECT `js`.`id_jenis` AS `id_jenis`, `js`.`nama_jenis` AS `nama_jenis`, count(`t`.`id_tiket`) AS `total`, sum(`t`.`status` = 'menunggu') AS `menunggu`, sum(`t`.`status` = 'proses') AS `proses`, sum(`t`.`status` = 'selesai') AS `selesai`, sum(`t`.`status` = 'ditolak') AS `ditolak`, round(avg(timestampdiff(MINUTE,`t`.`tgl_dibuat`,`t`.`tgl_selesai`)),1) AS `rata_durasi_menit` FROM ((`jenis_surat` `js` left join `pengajuan` `p` on(`js`.`id_jenis` = `p`.`id_jenis`)) left join `tiket` `t` on(`p`.`id_pengajuan` = `t`.`id_pengajuan`)) WHERE `js`.`is_active` = 1 GROUP BY `js`.`id_jenis`, `js`.`nama_jenis` ORDER BY count(`t`.`id_tiket`) DESC ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `guru`
--
ALTER TABLE `guru`
  ADD PRIMARY KEY (`id_guru`),
  ADD UNIQUE KEY `uq_nip` (`nip`);

--
-- Indexes for table `jenis_surat`
--
ALTER TABLE `jenis_surat`
  ADD PRIMARY KEY (`id_jenis`);

--
-- Indexes for table `pengajuan`
--
ALTER TABLE `pengajuan`
  ADD PRIMARY KEY (`id_pengajuan`),
  ADD KEY `fk_pgj_jenis` (`id_jenis`),
  ADD KEY `fk_pgj_user` (`id_pengguna`),
  ADD KEY `fk_pgj_siswa` (`id_siswa`),
  ADD KEY `fk_pgj_guru` (`id_guru`);

--
-- Indexes for table `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`id_pengguna`),
  ADD UNIQUE KEY `uq_username` (`username`);

--
-- Indexes for table `siswa`
--
ALTER TABLE `siswa`
  ADD PRIMARY KEY (`id_siswa`),
  ADD UNIQUE KEY `uq_nis` (`nis`);

--
-- Indexes for table `status_log`
--
ALTER TABLE `status_log`
  ADD PRIMARY KEY (`id_log`),
  ADD KEY `fk_log_tiket` (`id_tiket`),
  ADD KEY `fk_log_user` (`id_pengguna`);

--
-- Indexes for table `tiket`
--
ALTER TABLE `tiket`
  ADD PRIMARY KEY (`id_tiket`),
  ADD UNIQUE KEY `uq_no_tiket` (`no_tiket`),
  ADD KEY `fk_tkt_pengajuan` (`id_pengajuan`),
  ADD KEY `fk_tkt_petugas` (`id_petugas`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `pengajuan`
--
ALTER TABLE `pengajuan`
  ADD CONSTRAINT `fk_pgj_guru` FOREIGN KEY (`id_guru`) REFERENCES `guru` (`id_guru`),
  ADD CONSTRAINT `fk_pgj_jenis` FOREIGN KEY (`id_jenis`) REFERENCES `jenis_surat` (`id_jenis`),
  ADD CONSTRAINT `fk_pgj_siswa` FOREIGN KEY (`id_siswa`) REFERENCES `siswa` (`id_siswa`),
  ADD CONSTRAINT `fk_pgj_user` FOREIGN KEY (`id_pengguna`) REFERENCES `pengguna` (`id_pengguna`);

--
-- Constraints for table `status_log`
--
ALTER TABLE `status_log`
  ADD CONSTRAINT `fk_log_tiket` FOREIGN KEY (`id_tiket`) REFERENCES `tiket` (`id_tiket`),
  ADD CONSTRAINT `fk_log_user` FOREIGN KEY (`id_pengguna`) REFERENCES `pengguna` (`id_pengguna`);

--
-- Constraints for table `tiket`
--
ALTER TABLE `tiket`
  ADD CONSTRAINT `fk_tkt_pengajuan` FOREIGN KEY (`id_pengajuan`) REFERENCES `pengajuan` (`id_pengajuan`),
  ADD CONSTRAINT `fk_tkt_petugas` FOREIGN KEY (`id_petugas`) REFERENCES `pengguna` (`id_pengguna`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
