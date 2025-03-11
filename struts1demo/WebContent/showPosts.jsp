<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Danh sách bài viết</title>
    <style type="text/css">
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f5f5f5;
        }

        h2 {
            color: #333;
            border-bottom: 2px solid #007bff;
            padding-bottom: 10px;
        }

        .error-message {
            color: red;
            margin-bottom: 15px;
            font-weight: bold;
        }

        .no-posts {
            color: #555;
            font-style: italic;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            margin: 20px 0;
        }

        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #007bff;
            color: white;
        }

        tr:hover {
            background-color: #f9f9f9;
        }

        .action-buttons a {
            margin-right: 15px;
            text-decoration: none;
            color: #007bff;
            font-weight: bold;
        }

        .action-buttons a.delete {
            color: #dc3545; /* Màu đỏ cho nút xóa */
        }

        .action-buttons a:hover {
            text-decoration: underline;
        }

        .create-link, .login-link {
            display: inline-block;
            margin-top: 15px;
            color: #007bff;
            text-decoration: none;
            font-weight: bold;
        }

        .create-link:hover, .login-link:hover {
            text-decoration: underline;
        }

        .create-link {
            margin-right: 20px;
        }
    </style>
</head>
<body>
    <h2>Danh sách bài viết</h2>
    <div class="error-message">
        <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
    </div>

    <c:if test="${empty posts}">
        <p class="no-posts">Không có bài viết nào trong cơ sở dữ liệu.</p>
    </c:if>

    <c:if test="${not empty posts}">
        <table>
            <tr>
                <th>ID</th>
                <th>Tiêu đề</th>
                <th>Nội dung</th>
                <th>Ngày tạo</th>
                <th>Hành động</th>
            </tr>
            <c:forEach var="post" items="${posts}">
                <tr>
                    <td>${post.id}</td>
                    <td>${post.title}</td>
                    <td>${post.body}</td>
                    <td><fmt:formatDate value="${post.createdAt}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                    <td class="action-buttons">
                        <a href="editPost.do?id=${post.id}">Sửa</a>
                        <a href="deletePost.do?id=${post.id}" class="delete" onclick="return confirm('Bạn có chắc muốn xóa bài viết này?')">Xóa</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

    <p>
        <a href="createPost.do" class="create-link">Tạo bài viết mới</a>
        <a href="login.do" class="login-link">Quay lại đăng nhập</a>
    </p>
</body>
</html>