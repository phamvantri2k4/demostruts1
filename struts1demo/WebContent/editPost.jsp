<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sửa bài viết</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    <script>
        window.onload = function() {
            document.getElementsByName('title')[0].setAttribute('placeholder', 'Tiêu đề...');
            document.getElementsByName('body')[0].setAttribute('placeholder', 'Nội dung...');
        };
    </script>
</head>
<body>
    <div class="container">
        <h2>Sửa bài viết</h2>
        <div class="error-message">
            <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
        </div>
        <div class="form-container">
            <html:form action="/updatePost">
                <html:hidden property="id" value="${postId}"/>
                <html:text property="title" value="${title}"/>
                <html:textarea property="body" value="${body}"/>
                <html:submit value="Cập nhật"/>
            </html:form>
            <a href="index.do" class="back-link">Quay lại danh sách bài viết</a>
        </div>
    </div>
</body>
</html>