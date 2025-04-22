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
        function toggleMenu(postId) {
            document.getElementById('menu-' + postId).classList.toggle('active');
        }

        document.addEventListener('click', function(event) {
            var menus = document.querySelectorAll('.action-menu');
            menus.forEach(function(menu) {
                if (!menu.contains(event.target)) {
                    menu.classList.remove('active');
                }
            });
        });

        window.onload = function() {
            document.getElementsByName('title')[0].setAttribute('placeholder', 'Tiêu đề...');
            document.getElementsByName('body')[0].setAttribute('placeholder', 'Viết gì đó...');
        };

        function toggleFollow(button, followedUsername, currentAction) {
            var xhr = new XMLHttpRequest();
            xhr.open("GET", "follow.do?followedUsername=" + followedUsername + "&action=" + currentAction, true);
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    if (currentAction === "follow") {
                        button.textContent = "Hủy theo dõi";
                        button.classList.add("unfollow");
                        button.onclick = function() {
                            toggleFollow(button, followedUsername, "unfollow");
                        };
                    } else {
                        button.textContent = "Theo dõi";
                        button.classList.remove("unfollow");
                        button.onclick = function() {
                            toggleFollow(button, followedUsername, "follow");
                        };
                    }
                } else if (xhr.readyState === 4) {
                    alert("Có lỗi xảy ra khi thực hiện hành động!");
                }
            };
            xhr.send();
        }
    </script>
</head>
<body>
    <div class="header">
        <div class="header-left">
            <a href="index.do" class="login-link">Trang Chủ</a>
        </div>
        <div class="header-right">
            <a href="profile.do" class="profile-link username-link" style="color: black; text-decoration: none; margin-right: 15px;">
                ${sessionScope.username}
            </a>
            <a href="logout.do" class="logout-link" style="padding: 5px 10px; background: #dc3545; color: white; text-decoration: none; border-radius: 4px;">
                Đăng xuất
            </a>
        </div>
    </div>

    <div class="container index-container">
        <div class="sidebar">
    <h3>Danh sách người dùng</h3>
    <c:if test="${empty users}">
        <p>Không có người dùng nào.</p>
    </c:if>
    <c:if test="${not empty users}">
        <ul>
            <c:forEach var="user" items="${users}">
                <li>
                    <a href="profile.do?username=${user.username}">${user.username}</a>
                    <c:set var="isFollowing" value="false"/>
                    <c:forEach var="followedUser" items="${sessionScope.followedUsers}">
                        <c:if test="${followedUser.id == user.id}">
                            <c:set var="isFollowing" value="true"/>
                        </c:if>
                    </c:forEach>
                    <button class="follow-button ${isFollowing ? 'unfollow' : ''}" 
                            onclick="toggleFollow(this, '${user.username}', '${isFollowing ? 'unfollow' : 'follow'}')">
                        ${isFollowing ? 'Hủy theo dõi' : 'Theo dõi'}
                    </button>
                </li>
            </c:forEach>
        </ul>
    </c:if>
</div>
        <div class="main-content">
            <div style="text-align: center; margin-bottom: 20px;">
                <h2>Đăng bài</h2>
            </div>

            <div class="error-message">
                <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
            </div>
           <div class="form-container">
    		<html:form action="/doCreatePost">
        	<html:hidden property="source" value="index"/>
        	<html:text property="title" value=""/>
        	<html:textarea property="body" value=""/>
        	<html:submit value="Đăng"/>
    		</html:form>
		</div>

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
                                    </div>
                                </div>
                            </div>
                            <h3>${post.title}</h3>
                            <p>${post.body}</p>
                            <c:if test="${post.username == sessionScope.username}">
                                <div class="action-menu" id="menu-${post.id}">
                                    <button class="menu-button" onclick="toggleMenu(${post.id})">⋮</button>
                                    <div class="dropdown">
    									<a href="editPost.do?id=${post.id}&source=index">Sửa</a>
    									<a href="deletePost.do?id=${post.id}&source=index" class="delete" onclick="return confirm('Bạn có chắc muốn xóa bài viết này?')">Xóa</a>
									</div>
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>
                </c:if>
            </div>
        </div>
    </div>
</body>
</html>