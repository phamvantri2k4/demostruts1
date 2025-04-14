<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Trang chủ</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    <script>
        // Hàm toggle dropdown menu
        function toggleMenu(postId) {
            document.getElementById('menu-' + postId).classList.toggle('active');
        }

        // Đóng menu khi click ra ngoài
        document.addEventListener('click', function(event) {
            var menus = document.querySelectorAll('.action-menu');
            menus.forEach(function(menu) {
                if (!menu.contains(event.target)) {
                    menu.classList.remove('active');
                }
            });
        });

        // Gán placeholder sau khi trang tải xong
        window.onload = function() {
            document.getElementsByName('title')[0].setAttribute('placeholder', 'Tiêu đề...');
            document.getElementsByName('body')[0].setAttribute('placeholder', 'Viết gì đó...');
        };
    </script>
</head>
<body>
    <div class="container">
        <!-- Form Đăng bài -->
        <h2>Đăng bài</h2>
        <div class="error-message">
            <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
        </div>
        <div class="form-container">
            <html:form action="/doCreatePost">
                <html:text property="title" value=""/>
                <html:textarea property="body" value=""/>
                <html:submit value="Đăng"/>
            </html:form>
        </div>

        <!-- Danh sách bài viết -->
        <div class="post-list">
            <c:if test="${empty posts}">
                <p class="no-posts">Không có bài viết nào trong cơ sở dữ liệu.</p>
            </c:if>

            <c:if test="${not empty posts}">
                <c:forEach var="post" items="${posts}">
                    <div class="post-card">
                        <div class="post-header">
                            <div class="info">
                                <div class="name">${post.username}</div>
                                <div class="meta">
                                    <fmt:formatDate value="${post.createdAt}" pattern="dd/MM/yyyy HH:mm:ss" />
                                    <span class="scope">Công khai</span>
                                </div>
                            </div>
                      </div>
                        <h3>${post.title}</h3>
                        <p>${post.body}</p>
                        <div class="action-menu" id="menu-${post.id}">
                            <button class="menu-button" onclick="toggleMenu(${post.id})">⋮</button>
                            <div class="dropdown">
                                <a href="editPost.do?id=${post.id}">Sửa</a>
                                <a href="deletePost.do?id=${post.id}" class="delete" onclick="return confirm('Bạn có chắc muốn xóa bài viết này?')">Xóa</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:if>
        </div>

        <!-- Liên kết Đăng xuất và Quay lại đăng nhập -->
        <div style="text-align: center; margin-top: 20px;">
            <a href="login.do" class="login-link">Quay lại đăng nhập</a>
        </div>
    </div>
</body>
</html>