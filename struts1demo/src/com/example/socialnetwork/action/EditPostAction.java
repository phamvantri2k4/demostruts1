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
import java.sql.ResultSet;

public class EditPostAction extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null) {
            request.setAttribute("error", "Vui lòng đăng nhập để sửa bài viết!");
            return mapping.findForward("login");
        }

        String postId = request.getParameter("id");
        if (postId == null || postId.trim().isEmpty()) {
            request.setAttribute("error", "Không tìm thấy bài viết để sửa!");
            return mapping.findForward("failure");
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Lấy thông tin bài viết
            String sql = "SELECT id, title, body FROM posts WHERE id = ? AND user_id = (SELECT id FROM users WHERE username = ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(postId));
            stmt.setString(2, username);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                request.setAttribute("error", "Bài viết không tồn tại hoặc không thuộc về bạn!");
                return mapping.findForward("failure");
            }

            // Lưu thông tin bài viết vào request để hiển thị trên form sửa
            request.setAttribute("postId", rs.getInt("id"));
            request.setAttribute("title", rs.getString("title"));
            request.setAttribute("body", rs.getString("body"));

            return mapping.findForward("edit");
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi lấy thông tin bài viết: " + e.getMessage());
            e.printStackTrace();
            return mapping.findForward("failure");
        }
    }
}