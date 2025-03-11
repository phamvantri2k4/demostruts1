package com.example.socialnetwork.action;

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

public class DeletePostAction extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null) {
            request.setAttribute("error", "Vui lòng đăng nhập để xóa bài viết!");
            return mapping.findForward("login");
        }

        String postId = request.getParameter("id");
        if (postId == null || postId.trim().isEmpty()) {
            request.setAttribute("error", "Không tìm thấy bài viết để xóa!");
            return mapping.findForward("failure");
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Xóa bài viết
            String sql = "DELETE FROM posts WHERE id = ? AND user_id = (SELECT id FROM users WHERE username = ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(postId));
            stmt.setString(2, username);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                request.setAttribute("error", "Không thể xóa bài viết: Bài viết không tồn tại hoặc không thuộc về bạn!");
                return mapping.findForward("failure");
            }

            return mapping.findForward("success"); // Chuyển hướng về showPosts.do
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi xóa bài viết: " + e.getMessage());
            e.printStackTrace();
            return mapping.findForward("failure");
        }
    }
}