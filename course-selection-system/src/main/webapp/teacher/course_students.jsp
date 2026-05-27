<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>选课学生 - 在线课程选课系统</title>
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
        .container{max-width:1000px;margin:0 auto;padding:48px 24px}
        .page-header{margin-bottom:32px;padding-bottom:16px;border-bottom:2px solid #000}
        .page-title{font-size:2rem;font-weight:700;letter-spacing:-1px}
        .page-subtitle{color:#666;font-size:0.875rem;margin-top:8px}
        table{width:100%;border-collapse:collapse}
        th{background:#000;color:#fff;padding:16px;font-weight:500;font-size:0.7rem;text-transform:uppercase;letter-spacing:1px;text-align:left}
        td{padding:16px;border-bottom:1px solid #eee;font-size:0.875rem}
        tr:hover td{background:#f8f8f8}
        .student-no{font-weight:700}
        .btn{display:inline-block;padding:10px 20px;font-size:0.75rem;font-weight:600;text-transform:uppercase;letter-spacing:1px;cursor:pointer;border:1px solid;transition:all 0.2s;text-decoration:none}
        .btn-primary{background:#000;color:#fff;border-color:#000}
        .empty{text-align:center;padding:60px;color:#666}
    </style>
</head>
<body>
    <div class="navbar">
        <h1>在线课程选课<span>系统</span></h1>
        <div class="navbar-menu">
            <a href="index">首页</a><a href="course?action=list">课程管理</a><a href="score?action=list">成绩管理</a><a href="profile">个人信息</a>
            <span class="user-info">${sessionScope.user.name}</span><a href="../logout">退出</a>
        </div>
    </div>
    <div class="container">
        <div class="page-header">
            <h2 class="page-title">${course.name}</h2>
            <p class="page-subtitle">选课学生名单 · 共 ${course.currentStudents} 人</p>
        </div>
        <table>
            <thead><tr><th>学号</th><th>姓名</th><th>选课时间</th></tr></thead>
            <tbody>
            <c:forEach items="${selections}" var="sel">
                <tr><td class="student-no">${sel.studentNo}</td><td>${sel.studentName}</td><td><fmt:formatDate value="${sel.selectTime}" pattern="yyyy-MM-dd HH:mm"/></td></tr>
            </c:forEach>
            <c:if test="${empty selections}"><tr><td colspan="3" class="empty">暂无学生选课</td></tr></c:if>
            </tbody>
        </table>
        <div style="margin-top:32px"><a href="course?action=list" class="btn btn-primary">返回</a></div>
    </div>
</body>
</html>