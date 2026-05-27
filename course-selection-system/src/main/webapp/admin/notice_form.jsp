<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty notice ? '发布' : '编辑'}公告 - 在线课程选课系统</title>
    <style>
        *{margin:0;padding:0;box-sizing:border-box}
        body{font-family:'Helvetica Neue',Helvetica,Arial,'PingFang SC','Microsoft YaHei',sans-serif;background:#fff;color:#000}
        .navbar{background:#000;padding:0 48px;display:flex;justify-content:space-between;align-items:center;height:64px}
        .navbar h1{color:#fff;font-size:1rem;font-weight:700;letter-spacing:-0.5px}
        .navbar h1 span{color:#E53935}
        .navbar-menu{display:flex;gap:32px;align-items:center}
        .navbar-menu a{color:#999;text-decoration:none;font-size:0.8rem;font-weight:500;text-transform:uppercase;letter-spacing:1px;transition:color 0.2s}
        .navbar-menu a:hover{color:#fff}
        .user-info{color:#fff;font-size:0.8rem;padding-left:32px;border-left:1px solid #333}
        .container{max-width:600px;margin:0 auto;padding:48px 24px}
        .page-header{margin-bottom:32px;padding-bottom:16px;border-bottom:2px solid #000}
        .page-title{font-size:2rem;font-weight:700;letter-spacing:-1px}
        .form-group{margin-bottom:24px}
        .form-group label{display:block;margin-bottom:8px;font-weight:500;font-size:0.75rem;text-transform:uppercase;letter-spacing:1px}
        .form-control{width:100%;padding:12px 0;background:transparent;border:none;border-bottom:1px solid #ddd;font-size:1rem;transition:border-color 0.2s}
        .form-control:focus{outline:none;border-bottom-color:#000}
        select.form-control{padding:12px 0;background:#fff}
        textarea.form-control{min-height:200px;resize:vertical;border:1px solid #ddd;padding:12px}
        textarea.form-control:focus{border-color:#000}
        .btn{display:inline-block;padding:14px 28px;font-size:0.8rem;font-weight:600;text-transform:uppercase;letter-spacing:1px;cursor:pointer;border:1px solid;transition:all 0.2s;text-decoration:none;margin-right:12px}
        .btn-primary{background:#000;color:#fff;border-color:#000}
        .btn-primary:hover{background:#E53935;border-color:#E53935}
        .btn-secondary{background:transparent;color:#666;border-color:#ddd}
    </style>
</head>
<body>
    <div class="navbar">
        <h1>在线课程选课<span>系统</span> · 管理后台</h1>
        <div class="navbar-menu">
            <a href="dashboard">首页</a><a href="student?action=list">学生</a><a href="teacher?action=list">教师</a><a href="course?action=list">课程</a><a href="notice?action=list">公告</a>
            <span class="user-info">${sessionScope.user.name}</span><a href="../logout">退出</a>
        </div>
    </div>
    <div class="container">
        <div class="page-header"><h2 class="page-title">${empty notice ? '发布公告' : '编辑公告'}</h2></div>
        <form action="notice" method="post">
            <input type="hidden" name="action" value="${empty notice ? 'add' : 'edit'}">
            <c:if test="${not empty notice}"><input type="hidden" name="id" value="${notice.id}"></c:if>
            <div class="form-group"><label>标题</label><input type="text" name="title" class="form-control" value="${notice.title}" required placeholder="请输入公告标题"></div>
            <div class="form-group"><label>内容</label><textarea name="content" class="form-control" required placeholder="请输入公告内容">${notice.content}</textarea></div>
            <c:if test="${not empty notice}"><div class="form-group"><label>状态</label><select name="status" class="form-control"><option value="1" ${notice.status == 1 ? 'selected' : ''}>显示</option><option value="0" ${notice.status == 0 ? 'selected' : ''}>隐藏</option></select></div></c:if>
            <div style="margin-top:32px"><button type="submit" class="btn btn-primary">保存</button><a href="notice?action=list" class="btn btn-secondary">返回</a></div>
        </form>
    </div>
</body>
</html>