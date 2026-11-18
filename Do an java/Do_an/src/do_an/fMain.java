package do_an;

import javax.swing.*;
import java.awt.*;

public class fMain extends JFrame {

    public fMain() {
        setTitle("HỆ THỐNG QUẢN LÝ BÁN HÀNG ĐIỆN TỬ");
        setSize(550, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(41, 128, 185));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ CỬA HÀNG ĐIỆN TỬ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlMenu = new JPanel(new GridLayout(3, 1, 15, 15));
        pnlMenu.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JButton btnSanPham = new JButton("QUẢN LÝ SẢN PHẨM");
        JButton btnNhanVien = new JButton("QUẢN LÝ NHÂN VIÊN");
        JButton btnKhachHang = new JButton("QUẢN LÝ KHÁCH HÀNG");

        Font btnFont = new Font("Segoe UI", Font.BOLD, 15);
        btnSanPham.setFont(btnFont);
        btnNhanVien.setFont(btnFont);
        btnKhachHang.setFont(btnFont);

        btnSanPham.addActionListener(e -> new fQuanLySanPham().setVisible(true));
        btnNhanVien.addActionListener(e -> new fQuanLyNhanVien().setVisible(true));
        btnKhachHang.addActionListener(e -> new fQuanLyKhachHang().setVisible(true));

        pnlMenu.add(btnSanPham);
        pnlMenu.add(btnNhanVien);
        pnlMenu.add(btnKhachHang);
        add(pnlMenu, BorderLayout.CENTER);

        JPanel pnlFooter = new JPanel();
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        JButton btnThoat = new JButton("Thoát Chương Trình");
        btnThoat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnThoat.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn thoát chương trình?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        pnlFooter.add(btnThoat);
        add(pnlFooter, BorderLayout.SOUTH);
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(() -> new fMain().setVisible(true));
    }
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
