package do_an;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class fQuanLyKhachHang extends JFrame {
    private JTable tblKhachHang;
    private DefaultTableModel model;
    private JTextField txtMaKH, txtHoTen, txtDienThoai, txtDiaChi, txtEmail, txtTimKiem;
    private final JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem, btnThoat;

    public fQuanLyKhachHang() {
        setTitle("Quản Lý Khách Hàng - QLBanHangDienTu");
        setSize(850, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. Panel nhập liệu phía trên dùng GridLayout(6, 2) chuẩn chỉnh, không bị lệch
        JPanel pnlInput = new JPanel(new GridLayout(6, 2, 10, 8));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));

        pnlInput.add(new JLabel("  Mã Khách Hàng:"));
        txtMaKH = new JTextField();
        pnlInput.add(txtMaKH);

        pnlInput.add(new JLabel("  Họ Tên:"));
        txtHoTen = new JTextField();
        pnlInput.add(txtHoTen);

        pnlInput.add(new JLabel("  Số Điện Thoại:"));
        txtDienThoai = new JTextField();
        pnlInput.add(txtDienThoai);

        pnlInput.add(new JLabel("  Địa Chỉ:"));
        txtDiaChi = new JTextField();
        pnlInput.add(txtDiaChi);

        pnlInput.add(new JLabel("  Email:"));
        txtEmail = new JTextField();
        pnlInput.add(txtEmail);

        // Hàng tìm kiếm
        JPanel pnlSearch = new JPanel(new BorderLayout(5, 0));
        txtTimKiem = new JTextField();
        btnTimKiem = new JButton("Tìm Kiếm");
        pnlSearch.add(txtTimKiem, BorderLayout.CENTER);
        pnlSearch.add(btnTimKiem, BorderLayout.EAST);

        pnlInput.add(new JLabel("  Tìm kiếm (Họ Tên):"));
        pnlInput.add(pnlSearch);

        add(pnlInput, BorderLayout.NORTH);

        // 2. Bảng hiển thị ở giữa
        tblKhachHang = new JTable();
        model = new DefaultTableModel(new String[]{"Mã KH", "Họ Tên", "Số Điện Thoại", "Địa Chỉ", "Email"}, 0);
        tblKhachHang.setModel(model);
        add(new JScrollPane(tblKhachHang), BorderLayout.CENTER);

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

        // Sự kiện click chọn dòng trên bảng
        tblKhachHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblKhachHang.getSelectedRow();
                if (row >= 0) {
                    txtMaKH.setText(model.getValueAt(row, 0).toString());
                    txtHoTen.setText(model.getValueAt(row, 1).toString());
                    txtDienThoai.setText(model.getValueAt(row, 2).toString());
                    txtDiaChi.setText(model.getValueAt(row, 3).toString());
                    txtEmail.setText(model.getValueAt(row, 4).toString());
                    txtMaKH.setEditable(false);
                }
            }
        });

        // Nút Thêm
        btnThem.addActionListener(e -> {
            try (Connection conn = Do_an.getConnection();
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO KhachHang (MaKH, HoTen, SoDienThoai, DiaChi, Email) VALUES (?, ?, ?, ?, ?)")) {
                ps.setString(1, txtMaKH.getText());
                ps.setString(2, txtHoTen.getText());
                ps.setString(3, txtDienThoai.getText());
                ps.setString(4, txtDiaChi.getText());
                ps.setString(5, txtEmail.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                loadDataToTable("");
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi thêm: " + ex.getMessage());
            }
        });

        // Nút Sửa
        btnSua.addActionListener(e -> {
            try (Connection conn = Do_an.getConnection();
                 PreparedStatement ps = conn.prepareStatement("UPDATE KhachHang SET HoTen=?, SoDienThoai=?, DiaChi=?, Email=? WHERE MaKH=?")) {
                ps.setString(1, txtHoTen.getText());
                ps.setString(2, txtDienThoai.getText());
                ps.setString(3, txtDiaChi.getText());
                ps.setString(4, txtEmail.getText());
                ps.setString(5, txtMaKH.getText());
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
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khách hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = Do_an.getConnection();
                     PreparedStatement ps = conn.prepareStatement("DELETE FROM KhachHang WHERE MaKH=?")) {
                    ps.setString(1, txtMaKH.getText());
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
        String sql = "SELECT * FROM KhachHang" + (keyword.isEmpty() ? "" : " WHERE HoTen LIKE ?");
        try (Connection conn = Do_an.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (!keyword.isEmpty()) {
                ps.setString(1, "%" + keyword + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaKH"),
                    rs.getString("HoTen"),
                    rs.getString("SoDienThoai"),
                    rs.getString("DiaChi"),
                    rs.getString("Email")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        txtMaKH.setText("");
        txtHoTen.setText("");
        txtDienThoai.setText("");
        txtDiaChi.setText("");
        txtEmail.setText("");
        txtTimKiem.setText("");
        txtMaKH.setEditable(true);
        tblKhachHang.clearSelection();
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
        java.awt.EventQueue.invokeLater(() -> new fQuanLyKhachHang().setVisible(true));
    }
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
