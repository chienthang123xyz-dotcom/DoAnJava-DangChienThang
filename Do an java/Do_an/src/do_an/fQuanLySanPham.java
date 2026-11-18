package do_an;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class fQuanLySanPham extends JFrame {
    private JTable tblSanPham;
    private DefaultTableModel model;
    private JTextField txtMaSP, txtTenSP, txtMaDM, txtDonViTinh, txtSoLuong, txtGiaBan, txtBaoHanh, txtTimKiem;
    private final JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem, btnThoat;

    public fQuanLySanPham() {
        setTitle("Quản Lý Sản Phẩm - Đồ Án Java Swing");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. Panel nhập liệu phía trên (Dùng GridLayout(5, 2) cho các trường thông tin)
        JPanel pnlInput = new JPanel(new GridLayout(5, 2, 10, 8));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));

        pnlInput.add(new JLabel("  Mã Sản Phẩm:"));
        txtMaSP = new JTextField();
        pnlInput.add(txtMaSP);

        pnlInput.add(new JLabel("  Tên Sản Phẩm:"));
        txtTenSP = new JTextField();
        pnlInput.add(txtTenSP);

        pnlInput.add(new JLabel("  Mã Danh Mục:"));
        txtMaDM = new JTextField();
        pnlInput.add(txtMaDM);

        pnlInput.add(new JLabel("  Đơn Vị Tính (ĐVT):"));
        txtDonViTinh = new JTextField();
        pnlInput.add(txtDonViTinh);

        pnlInput.add(new JLabel("  Số Lượng Tồn:"));
        txtSoLuong = new JTextField();
        pnlInput.add(txtSoLuong);

        pnlInput.add(new JLabel("  Đơn Giá:"));
        txtGiaBan = new JTextField();
        pnlInput.add(txtGiaBan);

        pnlInput.add(new JLabel("  Bảo Hành (Tháng):"));
        txtBaoHanh = new JTextField();
        pnlInput.add(txtBaoHanh);

        // Hàng tìm kiếm
        JPanel pnlSearch = new JPanel(new BorderLayout(5, 0));
        txtTimKiem = new JTextField();
        btnTimKiem = new JButton("Tìm Kiếm");
        pnlSearch.add(txtTimKiem, BorderLayout.CENTER);
        pnlSearch.add(btnTimKiem, BorderLayout.EAST);

        pnlInput.add(new JLabel("  Tìm kiếm (Tên SP):"));
        pnlInput.add(pnlSearch);

        add(pnlInput, BorderLayout.NORTH);

        // 2. Bảng hiển thị ở giữa
        tblSanPham = new JTable();
        model = new DefaultTableModel(new String[]{"Mã SP", "Tên SP", "Mã DM", "ĐVT", "Số Lượng Tồn", "Đơn Giá", "Bảo Hành (Tháng)"}, 0);
        tblSanPham.setModel(model);
        add(new JScrollPane(tblSanPham), BorderLayout.CENTER);

        // 3. Panel nút bấm phía dưới
        JPanel pnlButtons = new JPanel();
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm Mới");
        btnThoat = new JButton("Thoát");

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);
        pnlButtons.add(btnThoat);
        add(pnlButtons, BorderLayout.SOUTH);

        // Load dữ liệu ban đầu
        loadDataToTable("");

        // Sự kiện click chọn dòng trên bảng lên các ô nhập liệu
        tblSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblSanPham.getSelectedRow();
                if (row >= 0) {
                    txtMaSP.setText(model.getValueAt(row, 0).toString());
                    txtTenSP.setText(model.getValueAt(row, 1).toString());
                    txtMaDM.setText(model.getValueAt(row, 2).toString());
                    txtDonViTinh.setText(model.getValueAt(row, 3).toString());
                    txtSoLuong.setText(model.getValueAt(row, 4).toString());
                    txtGiaBan.setText(model.getValueAt(row, 5).toString());
                    txtBaoHanh.setText(model.getValueAt(row, 6).toString());
                    txtMaSP.setEditable(false);
                }
            }
        });

        // Nút Thêm (đầy đủ các trường)
        btnThem.addActionListener(e -> {
            try (Connection conn = Do_an.getConnection();
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO SanPham (MaSP, TenSP, MaDM, DonViTinh, SoLuongTon, DonGia, BaoHanhThang) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                ps.setString(1, txtMaSP.getText());
                ps.setString(2, txtTenSP.getText());
                ps.setString(3, txtMaDM.getText());
                ps.setString(4, txtDonViTinh.getText());
                ps.setInt(5, Integer.parseInt(txtSoLuong.getText()));
                ps.setDouble(6, Double.parseDouble(txtGiaBan.getText()));
                ps.setInt(7, Integer.parseInt(txtBaoHanh.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
                loadDataToTable("");
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi thêm: " + ex.getMessage());
            }
        });

        // Nút Sửa
        btnSua.addActionListener(e -> {
            try (Connection conn = Do_an.getConnection();
                 PreparedStatement ps = conn.prepareStatement("UPDATE SanPham SET TenSP=?, MaDM=?, DonViTinh=?, SoLuongTon=?, DonGia=?, BaoHanhThang=? WHERE MaSP=?")) {
                ps.setString(1, txtTenSP.getText());
                ps.setString(2, txtMaDM.getText());
                ps.setString(3, txtDonViTinh.getText());
                ps.setInt(4, Integer.parseInt(txtSoLuong.getText()));
                ps.setDouble(5, Double.parseDouble(txtGiaBan.getText()));
                ps.setInt(6, Integer.parseInt(txtBaoHanh.getText()));
                ps.setString(7, txtMaSP.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadDataToTable("");
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi sửa: " + ex.getMessage());
            }
        });

        // Nút Xóa
        btnXoa.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = Do_an.getConnection();
                     PreparedStatement ps = conn.prepareStatement("DELETE FROM SanPham WHERE MaSP=?")) {
                    ps.setString(1, txtMaSP.getText());
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadDataToTable("");
                    clearForm();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi xóa: " + ex.getMessage());
                }
            }
        });

        // Nút Tìm kiếm, Làm mới và Thoát
        btnTimKiem.addActionListener(e -> loadDataToTable(txtTimKiem.getText().trim()));
        btnLamMoi.addActionListener(e -> { clearForm(); loadDataToTable(""); });
        btnThoat.addActionListener(e -> dispose());
    }

    private void loadDataToTable(String keyword) {
        model.setRowCount(0);
        String sql = "SELECT * FROM SanPham" + (keyword.isEmpty() ? "" : " WHERE TenSP LIKE ?");
        try (Connection conn = Do_an.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (!keyword.isEmpty()) {
                ps.setString(1, "%" + keyword + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaSP"),
                    rs.getString("TenSP"),
                    rs.getString("MaDM"),
                    rs.getString("DonViTinh"),
                    rs.getInt("SoLuongTon"),
                    rs.getDouble("DonGia"),
                    rs.getInt("BaoHanhThang")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtMaDM.setText("");
        txtDonViTinh.setText("");
        txtSoLuong.setText("");
        txtGiaBan.setText("");
        txtBaoHanh.setText("");
        txtTimKiem.setText("");
        txtMaSP.setEditable(true);
        tblSanPham.clearSelection();
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
        java.awt.EventQueue.invokeLater(() -> new fQuanLySanPham().setVisible(true));
    }
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

