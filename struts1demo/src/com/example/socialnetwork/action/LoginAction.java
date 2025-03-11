package com.example.socialnetwork.action;

import com.example.socialnetwork.form.LoginForm;
import com.example.socialnetwork.util.DBConnection;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginAction extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        LoginForm loginForm = (LoginForm) form;
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        // Kiểm tra dữ liệu đầu vào
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Tên đăng nhập và mật khẩu không được để trống!");
            return mapping.findForward("failure");
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Kiểm tra thông tin đăng nhập
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Đăng nhập thành công, lưu username vào session
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                return mapping.findForward("success"); // Chuyển hướng về showPosts.do
            } else {
                request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
                return mapping.findForward("failure");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi đăng nhập: " + e.getMessage());
            e.printStackTrace();
            return mapping.findForward("failure");
        }
    }
}