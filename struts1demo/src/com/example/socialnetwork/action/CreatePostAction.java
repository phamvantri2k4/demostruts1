package com.example.socialnetwork.action;

import com.example.socialnetwork.form.PostForm;
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

public class CreatePostAction extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null) {
            request.setAttribute("error", "Vui lòng đăng nhập để tạo bài viết!");
            return mapping.findForward("login");
        }

        PostForm postForm = (PostForm) form;
        String title = postForm.getTitle();
        String body = postForm.getBody();

        if (title == null || title.trim().isEmpty() || body == null || body.trim().isEmpty()) {
            request.setAttribute("error", "Tiêu đề và nội dung không được để trống!");
            return mapping.findForward("failure");
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Lấy user_id từ username
            String userSql = "SELECT id FROM users WHERE username = ?";
            PreparedStatement userStmt = conn.prepareStatement(userSql);
            userStmt.setString(1, username);
            int userId = userStmt.executeQuery().next() ? userStmt.getResultSet().getInt("id") : -1;

            if (userId == -1) {
                request.setAttribute("error", "Người dùng không tồn tại!");
                return mapping.findForward("failure");
            }

            // Thêm bài post mới
            String sql = "INSERT INTO posts (title, body, user_id, status, created_at) VALUES (?, ?, ?, 'published', CURRENT_TIMESTAMP)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, body);
            stmt.setInt(3, userId);
            stmt.executeUpdate();

            return mapping.findForward("success"); // Chuyển hướng về showPosts.do
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi tạo bài viết: " + e.getMessage());
            e.printStackTrace();
            return mapping.findForward("failure");
        }
    }
}