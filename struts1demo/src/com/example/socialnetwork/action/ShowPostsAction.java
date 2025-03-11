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
import java.util.ArrayList;
import java.util.List;

public class ShowPostsAction extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username"); // Giả định username được lưu khi đăng nhập

        if (username == null) {
            request.setAttribute("error", "Vui lòng đăng nhập để xem bài viết!");
            return mapping.findForward("login");
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Lấy user_id từ username
            String userSql = "SELECT id FROM users WHERE username = ?";
            PreparedStatement userStmt = conn.prepareStatement(userSql);
            userStmt.setString(1, username);
            ResultSet userRs = userStmt.executeQuery();
            int userId = -1;
            if (userRs.next()) {
                userId = userRs.getInt("id");
            }
            if (userId == -1) {
                request.setAttribute("error", "Người dùng không tồn tại!");
                return mapping.findForward("failure");
            }

            // Lấy danh sách bài post của user với status = 'published'
            String postSql = "SELECT id, title, body, created_at FROM posts WHERE user_id = ? AND status = 'published' ORDER BY created_at DESC";
            PreparedStatement postStmt = conn.prepareStatement(postSql);
            postStmt.setInt(1, userId);
            ResultSet postRs = postStmt.executeQuery();

            List<Post> posts = new ArrayList<>();
            while (postRs.next()) {
                Post post = new Post();
                post.setId(postRs.getInt("id"));
                post.setTitle(postRs.getString("title"));
                post.setBody(postRs.getString("body"));
                post.setCreatedAt(postRs.getTimestamp("created_at"));
                posts.add(post);
            }

            request.setAttribute("posts", posts);
            return mapping.findForward("success");
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi lấy danh sách bài viết: " + e.getMessage());
            e.printStackTrace();
            return mapping.findForward("failure");
        }
    }

    public static class Post {
        private int id;
        private String title;
        private String body;
        private java.sql.Timestamp createdAt;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public java.sql.Timestamp getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(java.sql.Timestamp createdAt) {
            this.createdAt = createdAt;
        }
    }
}