package do_an;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class fQuanLyNhanVien extends JFrame {
    private JTable tblNhanVien;
    private DefaultTableModel model;
    private JTextField txtMaNV, txtHoTen, txtGioiTinh, txtNgaySinh, txtDienThoai, txtDiaChi, txtChucVu, txtTimKiem;
    private final JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem, btnThoat;

    public fQuanLyNhanVien() {
        setTitle("Quản Lý Nhân Viên - QLBanHangDienTu");
        setSize(980, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. Panel nhập liệu phía trên dùng GridLayout(4, 4) cực kỳ cân đối (4 hàng, 4 cột: Nhãn1, Ô1, Nhãn2, Ô2)
        JPanel pnlInput = new JPanel(new GridLayout(4, 4, 10, 10));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));

        pnlInput.add(new JLabel("Mã Nhân Viên:"));
        txtMaNV = new JTextField();
        pnlInput.add(txtMaNV);

        pnlInput.add(new JLabel("Họ Tên:"));
        txtHoTen = new JTextField();
        pnlInput.add(txtHoTen);

        pnlInput.add(new JLabel("Giới Tính:"));
        txtGioiTinh = new JTextField();
        pnlInput.add(txtGioiTinh);

        pnlInput.add(new JLabel("Ngày Sinh (YYYY-MM-DD):"));
        txtNgaySinh = new JTextField();
        pnlInput.add(txtNgaySinh);

        pnlInput.add(new JLabel("Số Điện Thoại:"));
        txtDienThoai = new JTextField();
        pnlInput.add(txtDienThoai);

        pnlInput.add(new JLabel("Địa Chỉ:"));
        txtDiaChi = new JTextField();
        pnlInput.add(txtDiaChi);

        pnlInput.add(new JLabel("Chức Vụ:"));
        txtChucVu = new JTextField();
        pnlInput.add(txtChucVu);

        pnlInput.add(new JLabel("Tìm kiếm (Họ Tên):"));
        JPanel pnlSearch = new JPanel(new BorderLayout(5, 0));
        txtTimKiem = new JTextField();
        btnTimKiem = new JButton("Tìm");
        pnlSearch.add(txtTimKiem, BorderLayout.CENTER);
        pnlSearch.add(btnTimKiem, BorderLayout.EAST);
        pnlInput.add(pnlSearch);

        add(pnlInput, BorderLayout.NORTH);

        // 2. Bảng hiển thị ở giữa (khớp chuẩn 7 cột với SQL Server)
        tblNhanVien = new JTable();
        model = new DefaultTableModel(new String[]{
            "Mã NV", "Họ Tên", "Giới Tính", "Ngày Sinh", "Số Điện Thoại", "Địa Chỉ", "Chức Vụ"
        }, 0);
        tblNhanVien.setModel(model);
        add(new JScrollPane(tblNhanVien), BorderLayout.CENTER);

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

        // Load dữ liệu ban đầu từ SQL
        loadDataToTable("");

        // Sự kiện click chọn dòng trên bảng lên các ô nhập liệu
        tblNhanVien.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblNhanVien.getSelectedRow();
                if (row >= 0) {
                    txtMaNV.setText(model.getValueAt(row, 0) != null ? model.getValueAt(row, 0).toString() : "");
                    txtHoTen.setText(model.getValueAt(row, 1) != null ? model.getValueAt(row, 1).toString() : "");
                    txtGioiTinh.setText(model.getValueAt(row, 2) != null ? model.getValueAt(row, 2).toString() : "");
                    txtNgaySinh.setText(model.getValueAt(row, 3) != null ? model.getValueAt(row, 3).toString() : "");
                    txtDienThoai.setText(model.getValueAt(row, 4) != null ? model.getValueAt(row, 4).toString() : "");
                    txtDiaChi.setText(model.getValueAt(row, 5) != null ? model.getValueAt(row, 5).toString() : "");
                    txtChucVu.setText(model.getValueAt(row, 6) != null ? model.getValueAt(row, 6).toString() : "");
                    txtMaNV.setEditable(false);
                }
            }
        });

        // Nút Thêm
        btnThem.addActionListener(e -> {
            try (Connection conn = Do_an.getConnection();
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO NhanVien (MaNV, HoTen, GioiTinh, NgaySinh, SoDienThoai, DiaChi, ChucVu) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                ps.setString(1, txtMaNV.getText());
                ps.setString(2, txtHoTen.getText());
                ps.setString(3, txtGioiTinh.getText());
                ps.setString(4, txtNgaySinh.getText());
                ps.setString(5, txtDienThoai.getText());
                ps.setString(6, txtDiaChi.getText());
                ps.setString(7, txtChucVu.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                loadDataToTable("");
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi thêm: " + ex.getMessage());
            }
        });

        // Nút Sửa
        btnSua.addActionListener(e -> {
            try (Connection conn = Do_an.getConnection();
                 PreparedStatement ps = conn.prepareStatement("UPDATE NhanVien SET HoTen=?, GioiTinh=?, NgaySinh=?, SoDienThoai=?, DiaChi=?, ChucVu=? WHERE MaNV=?")) {
                ps.setString(1, txtHoTen.getText());
                ps.setString(2, txtGioiTinh.getText());
                ps.setString(3, txtNgaySinh.getText());
                ps.setString(4, txtDienThoai.getText());
                ps.setString(5, txtDiaChi.getText());
                ps.setString(6, txtChucVu.getText());
                ps.setString(7, txtMaNV.getText());
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
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = Do_an.getConnection();
                     PreparedStatement ps = conn.prepareStatement("DELETE FROM NhanVien WHERE MaNV=?")) {
                    ps.setString(1, txtMaNV.getText());
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadDataToTable("");
                    clearForm();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi xóa: " + ex.getMessage());
                }
            }
        });

        btnTimKiem.addActionListener(e -> loadDataToTable(txtTimKiem.getText().trim()));
        btnLamMoi.addActionListener(e -> { clearForm(); loadDataToTable(""); });
        btnThoat.addActionListener(e -> dispose());
    }

    private void loadDataToTable(String keyword) {
        model.setRowCount(0);
        String sql = "SELECT * FROM NhanVien" + (keyword.isEmpty() ? "" : " WHERE HoTen LIKE ?");
        try (Connection conn = Do_an.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (!keyword.isEmpty()) {
                ps.setString(1, "%" + keyword + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaNV"),
                    rs.getString("HoTen"),
                    rs.getString("GioiTinh"),
                    rs.getString("NgaySinh"),
                    rs.getString("SoDienThoai"),
                    rs.getString("DiaChi"),
                    rs.getString("ChucVu")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        txtMaNV.setText("");
        txtHoTen.setText("");
        txtGioiTinh.setText("");
        txtNgaySinh.setText("");
        txtDienThoai.setText("");
        txtDiaChi.setText("");
        txtChucVu.setText("");
        txtTimKiem.setText("");
        txtMaNV.setEditable(true);
        tblNhanVien.clearSelection();
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
        java.awt.EventQueue.invokeLater(() -> new fQuanLyNhanVien().setVisible(true));
    }
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
