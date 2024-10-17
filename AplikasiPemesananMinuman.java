import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Minuman {
    private String nama;
    private double harga;

    public Minuman(String nama, double harga) {
        this.nama = nama;
        this.harga = harga;
    }

    public String getNama() {
        return nama;
    }

    public double getHarga() {
        return harga;
    }

    @Override
    public String toString() {
        return nama + " - Rp" + harga;
    }
}

class Pelanggan {
    private String username;
    private String password;
    private ArrayList<Minuman> riwayatPesanan;

    public Pelanggan(String username, String password) {
        this.username = username;
        this.password = password;
        this.riwayatPesanan = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void tambahPesanan(Minuman minuman) {
        riwayatPesanan.add(minuman);
    }

    public ArrayList<Minuman> getRiwayatPesanan() {
        return riwayatPesanan;
    }

    public double totalPembayaran() {
        return riwayatPesanan.stream().mapToDouble(Minuman::getHarga).sum();
    }

    public void kosongkanKeranjang() {
        riwayatPesanan.clear();
    }
}

public class AplikasiPemesananMinuman {
    private ArrayList<Pelanggan> pelangganList;
    private ArrayList<Minuman> menuMinuman;
    private final String FILE_AKUN = "userakun.txt";

    public AplikasiPemesananMinuman() {
        pelangganList = new ArrayList<>();
        menuMinuman = new ArrayList<>();
        menuMinuman.add(new Minuman("Teh Manis", 5000));
        menuMinuman.add(new Minuman("Kopi", 10000));
        menuMinuman.add(new Minuman("Jus Jeruk", 7000));
        menuMinuman.add(new Minuman("Cola", 8000));
        menuMinuman.add(new Minuman("Sprite", 8000));
        menuMinuman.add(new Minuman("Jus stroberi", 10000));

        loadAkunDariFile();
    }

    private void loadAkunDariFile() {
        try {
            File file = new File(FILE_AKUN);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 2) {
                        Pelanggan pelanggan = new Pelanggan(data[0], data[1]);
                        pelangganList.add(pelanggan);
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            System.out.println("Gagal memuat akun.");
        }
    }

    public void menuUtama() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("=== Aplikasi Pemesanan Minuman ===");
            System.out.println("1. Registrasi");
            System.out.println("2. Login");
            System.out.println("0. Keluar");
            System.out.print("Pilih opsi: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine();

            switch (pilihan) {
                case 1:
                    registrasi(scanner);
                    break;
                case 2:
                    login(scanner);
                    break;
                case 0:
                    System.out.println("Terimakasih telah menggunakan aplikasi!");
                    System.exit(0);
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private void registrasi(Scanner scanner) {
        System.out.print("Masukkan username: ");
        String username = scanner.nextLine();
        System.out.print("Masukkan password: ");
        String password = scanner.nextLine();

        pelangganList.add(new Pelanggan(username, password));
        simpanAkunKeFile(username, password);

        System.out.println("Registrasi berhasil!");
    }

    private void simpanAkunKeFile(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_AKUN, true))) {
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Gagal menyimpan akun.");
        }
    }

    private void login(Scanner scanner) {
        System.out.print("Masukkan username: ");
        String username = scanner.nextLine();
        System.out.print("Masukkan password: ");
        String password = scanner.nextLine();

        for (Pelanggan pelanggan : pelangganList) {
            if (pelanggan.getUsername().equals(username) && pelanggan.getPassword().equals(password)) {
                System.out.println("Login berhasil!");
                menuLogin(pelanggan, scanner);
                return;
            }
        }
        System.out.println("Username atau password salah!");
    }

    private void simpanRiwayatPesanan(Pelanggan pelanggan) {
        try {
            String fileRiwayat = "riwayat_" + pelanggan.getUsername() + ".txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileRiwayat, true));
            for (Minuman minuman : pelanggan.getRiwayatPesanan()) {
                writer.write(minuman.getNama() + "," + minuman.getHarga());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Gagal menyimpan riwayat pesanan.");
        }
    }

    private void menuLogin(Pelanggan pelanggan, Scanner scanner) {
        while (true) {
            System.out.println("===== Menu =====");
            System.out.println("1. Lihat Menu Minuman");
            System.out.println("2. Lihat Keranjang");
            System.out.println("3. Pembayaran");
            System.out.println("4. Hapus pesanan");
            System.out.println("5. Lihat Riwayat Pesanan");
            System.out.println("0. LOG OUT");
            System.out.print("Pilih opsi: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine();

            switch (pilihan) {
                case 1:
                    menuPemesanan(pelanggan, scanner);
                    break;
                case 2:
                    lihatKeranjang(pelanggan);
                    System.out.print("Press ENTER to continue...");
                    scanner.nextLine();
                    break;
                case 3:
                    pembayaran(pelanggan);
                    break;
                case 4:
                    hapusPesananDariKeranjang(pelanggan, scanner);
                    break;
                case 5:
                    lihatRiwayatPesanan(pelanggan);
                    System.out.print("Press ENTER to continue...");
                    scanner.nextLine();
                    break;
                case 0:
                    System.out.println("Keluar dari akun...");
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private void menuPemesanan(Pelanggan pelanggan, Scanner scanner) {
        System.out.println("=== Menu Minuman ===");
        int no = 0;
        for (int i = 0; i < menuMinuman.size(); i++) {
            System.out.println((i + 1) + ". " + menuMinuman.get(i));
            no = i + 1;
        }
        System.out.println("0. Kembali");
        System.out.println("==========================");
        System.out.print("Tambahkan menu ke keranjang? (y/n): ");
        String konfirmasi = scanner.nextLine();
        while (true) {
            if (konfirmasi.equalsIgnoreCase("y")) {
                System.out.print("Pilih minuman (1-" + no + ") : ");
                int pilihan = scanner.nextInt();
                scanner.nextLine();
                System.out.print("");
                if (pilihan == 0) {
                    break;
                } else if (pilihan > 0 && pilihan <= menuMinuman.size()) {
                    Minuman minuman = menuMinuman.get(pilihan - 1);
                    pelanggan.tambahPesanan(minuman);
                    System.out.println("Pesanan ditambahkan: " + minuman.getNama());
                } else {
                    System.out.println("Pilihan tidak valid!");
                }
            } else if(konfirmasi.equalsIgnoreCase("n")) {
                System.out.print("Press ENTER for back to main menu...");
                scanner.nextLine();
                break;
            } else {
                System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private void lihatKeranjang(Pelanggan pelanggan) {
        System.out.println("=== Keranjang ===");
        ArrayList<Minuman> keranjang = pelanggan.getRiwayatPesanan();
        if (keranjang.isEmpty()) {
            System.out.println("Keranjang kosong.");
        } else {
            for (Minuman minuman : keranjang) {
                System.out.println(minuman);
            }
        }
    }

    private void hapusPesananDariKeranjang(Pelanggan pelanggan, Scanner scanner) {
        ArrayList<Minuman> keranjang = pelanggan.getRiwayatPesanan();

        if (keranjang.isEmpty()) {
            System.out.println("Keranjang kosong, tidak ada yang bisa dihapus.");
            return;
        }

        System.out.println("=== Keranjang ===");
        for (int i = 0; i < keranjang.size(); i++) {
            System.out.println((i + 1) + ". " + keranjang.get(i));
        }

        System.out.print("Pilih nomor pesanan yang ingin dihapus (0 untuk batal): ");
        int pilihan = scanner.nextInt();
        scanner.nextLine();

        if (pilihan == 0) {
            System.out.println("Penghapusan dibatalkan.");
        } else if (pilihan > 0 && pilihan <= keranjang.size()) {
            Minuman minumanTerhapus = keranjang.remove(pilihan - 1);
            System.out.println("Pesanan \"" + minumanTerhapus.getNama() + "\" berhasil dihapus dari keranjang.");

        } else {
            System.out.println("Pilihan tidak valid!");
        }
    }


    private void pembayaran(Pelanggan pelanggan) {
        double total = pelanggan.totalPembayaran();
        if (total == 0) {
            System.out.println("Keranjang kosong, tidak ada yang perlu dibayar.");
            return;
        }

        System.out.println("Total yang harus dibayar: Rp" + total);
        System.out.print("Konfirmasi pembayaran? (y/n): ");
        Scanner scanner = new Scanner(System.in);
        String konfirmasi = scanner.nextLine();

        if (konfirmasi.equalsIgnoreCase("y")) {
            simpanRiwayatPesanan(pelanggan);
            pelanggan.kosongkanKeranjang();
            System.out.println("Pembayaran berhasil! Terima kasih.");
            System.out.print("Apakah anda mau LOG OUT? (y/n): ");
            String out = scanner.nextLine();
            if (out.equals("y")) {
                System.out.println("Terimakasih telah menggunakan aplikasi!");
                System.exit(0);
            }
        } else {
            System.out.println("Pembayaran dibatalkan.");
        }
    }

    private void lihatRiwayatPesanan(Pelanggan pelanggan) {
        try {
            String fileRiwayat = "riwayat_" + pelanggan.getUsername() + ".txt";
            BufferedReader reader = new BufferedReader(new FileReader(fileRiwayat));
            String line;
            System.out.println("=== Riwayat Pesanan ===");
            boolean isEmpty = true;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                isEmpty = false;  // Ubah menjadi false jika ada data
            }

            if (isEmpty) {
                System.out.println("Tidak ada riwayat pesanan.");
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Gagal memuat riwayat pesanan.");
        }
    }
    public static void main(String[] args) {
        AplikasiPemesananMinuman app = new AplikasiPemesananMinuman();
        app.menuUtama();
    }
}