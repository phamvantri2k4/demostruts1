package com.example.socialnetwork.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.example.socialnetwork.dao.PostDAO;
import com.example.socialnetwork.dao.UserDAO;
import com.example.socialnetwork.form.PostForm;
import com.example.socialnetwork.model.User;

public class CreatePostAction extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return mapping.findForward("login");
        }

        UserDAO userDAO = new UserDAO();
        User currentUser = userDAO.getUserByUsername(username);
        if (currentUser == null) {
            return mapping.findForward("login");
        }

        PostForm postForm = (PostForm) form;
        String title = postForm.getTitle();
        String body = postForm.getBody();

        // Kiểm tra dữ liệu đầu vào
        if (title == null || title.trim().isEmpty() || body == null || body.trim().isEmpty()) {
            request.setAttribute("error", "Tiêu đề và nội dung không được để trống!");
            return mapping.findForward("failure");
        }

        // Tạo bài viết
        PostDAO postDAO = new PostDAO();
        try {
            postDAO.createPost(title, body, currentUser.getId());
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi xảy ra khi đăng bài: " + e.getMessage());
            return mapping.findForward("failure");
        }

        // Redirect về profile.do để tránh POST lại khi refresh
        response.sendRedirect(request.getContextPath() + "/profile.do");
        return null;
    }
}