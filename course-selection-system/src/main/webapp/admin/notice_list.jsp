<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>公告管理 - 在线课程选课系统</title>
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
        .container{max-width:1200px;margin:0 auto;padding:48px 24px}
        .page-header{display:flex;justify-content:space-between;align-items:flex-end;margin-bottom:32px;padding-bottom:16px;border-bottom:2px solid #000}
        .page-title{font-size:2rem;font-weight:700;letter-spacing:-1px}
        .btn{display:inline-block;padding:10px 20px;font-size:0.75rem;font-weight:600;text-transform:uppercase;letter-spacing:1px;cursor:pointer;border:1px solid;transition:all 0.2s;text-decoration:none}
        .btn-primary{background:#000;color:#fff;border-color:#000}
        .btn-warning{background:#000;color:#fff;border-color:#000}
        .btn-danger{color:#E53935;border-color:#E53935;background:transparent}
        .btn-danger:hover{background:#E53935;color:#fff}
        .btn-sm{padding:6px 12px;font-size:0.7rem}
        table{width:100%;border-collapse:collapse}
        th{background:#000;color:#fff;padding:16px;font-weight:500;font-size:0.7rem;text-transform:uppercase;letter-spacing:1px;text-align:left}
        td{padding:16px;border-bottom:1px solid #eee;font-size:0.875rem}
        tr:hover td{background:#f8f8f8}
        .status{display:inline-block;padding:4px 12px;font-size:0.7rem;font-weight:600;text-transform:uppercase;letter-spacing:1px}
        .status.show{background:#000;color:#fff}
        .status.hide{background:#eee;color:#666}
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
        <div class="page-header">
            <h2 class="page-title">公告管理</h2>
            <a href="notice?action=add" class="btn btn-primary">发布公告</a>
        </div>
        <table>
            <thead><tr><th>标题</th><th>发布时间</th><th>状态</th><th>操作</th></tr></thead>
            <tbody>
            <c:forEach items="${notices}" var="n">
                <tr>
                    <td style="font-weight:500">${n.title}</td>
                    <td><fmt:formatDate value="${n.publishTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                    <td><c:choose><c:when test="${n.status == 1}"><span class="status show">显示</span></c:when><c:otherwise><span class="status hide">隐藏</span></c:otherwise></c:choose></td>
                    <td>
                        <a href="notice?action=edit&id=${n.id}" class="btn btn-warning btn-sm">编辑</a>
                        <form action="notice" method="post" style="display:inline" onsubmit="return confirm('确定删除？')"><input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="${n.id}"><button type="submit" class="btn btn-danger btn-sm">删除</button></form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>