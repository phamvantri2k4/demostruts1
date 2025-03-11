package com.example.socialnetwork.action;

import com.example.socialnetwork.form.RegistrationForm;
import com.example.socialnetwork.util.DBConnection;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterAction extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        RegistrationForm registrationForm = (RegistrationForm) form;
        String username = registrationForm.getUsername();
        String password = registrationForm.getPassword();

        // Kiểm tra dữ liệu đầu vào
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Tên đăng nhập và mật khẩu không được để trống!");
            return mapping.findForward("failure");
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Kiểm tra xem username đã tồn tại chưa
            String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                request.setAttribute("error", "Tên đăng nhập đã tồn tại!");
                return mapping.findForward("failure");
            }

            // Thêm user mới vào cơ sở dữ liệu
            String sql = "INSERT INTO users (username, password, role, created_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, "user"); // Mặc định role là "user"
            stmt.executeUpdate();

            return mapping.findForward("success"); // Chuyển hướng về login.do
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi đăng ký: " + e.getMessage());
            e.printStackTrace();
            return mapping.findForward("failure");
        }
    }
}