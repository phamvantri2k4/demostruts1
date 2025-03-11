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

public class UpdatePostAction extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null) {
            request.setAttribute("error", "Vui lòng đăng nhập để cập nhật bài viết!");
            return mapping.findForward("login");
        }

        PostForm postForm = (PostForm) form;
        String postId = postForm.getId();
        String title = postForm.getTitle();
        String body = postForm.getBody();

        // Kiểm tra dữ liệu đầu vào
        if (postId == null || postId.trim().isEmpty()) {
            request.setAttribute("error", "Không tìm thấy bài viết để cập nhật!");
            return mapping.findForward("failure");
        }
        if (title == null || title.trim().isEmpty() || body == null || body.trim().isEmpty()) {
            request.setAttribute("error", "Tiêu đề và nội dung không được để trống!");
            return mapping.findForward("failure");
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Cập nhật bài viết
            String sql = "UPDATE posts SET title = ?, body = ? WHERE id = ? AND user_id = (SELECT id FROM users WHERE username = ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, body);
            stmt.setInt(3, Integer.parseInt(postId));
            stmt.setString(4, username);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                request.setAttribute("error", "Không thể cập nhật bài viết: Bài viết không tồn tại hoặc không thuộc về bạn!");
                return mapping.findForward("failure");
            }

            return mapping.findForward("success"); // Chuyển hướng về showPosts.do
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi cập nhật bài viết: " + e.getMessage());
            e.printStackTrace();
            return mapping.findForward("failure");
        }
    }
}