<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty student ? '添加' : '编辑'}学生 - 在线课程选课系统</title>
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
        .container{max-width:500px;margin:0 auto;padding:48px 24px}
        .page-header{margin-bottom:32px;padding-bottom:16px;border-bottom:2px solid #000}
        .page-title{font-size:2rem;font-weight:700;letter-spacing:-1px}
        .form-group{margin-bottom:24px}
        .form-group label{display:block;margin-bottom:8px;font-weight:500;font-size:0.75rem;text-transform:uppercase;letter-spacing:1px}
        .form-control{width:100%;padding:12px 0;background:transparent;border:none;border-bottom:1px solid #ddd;font-size:1rem;transition:border-color 0.2s}
        .form-control:focus{outline:none;border-bottom-color:#000}
        select.form-control{padding:12px 0;background:#fff}
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
        <div class="page-header"><h2 class="page-title">${empty student ? '添加学生' : '编辑学生'}</h2></div>
        <form action="student" method="post">
            <input type="hidden" name="action" value="${empty student ? 'add' : 'edit'}">
            <c:if test="${not empty student}"><input type="hidden" name="id" value="${student.id}"></c:if>
            <c:if test="${empty student}"><div class="form-group"><label>学号</label><input type="text" name="studentNo" class="form-control" required placeholder="请输入学号"></div></c:if>
            <div class="form-group"><label>姓名</label><input type="text" name="name" class="form-control" value="${student.name}" required placeholder="请输入姓名"></div>
            <div class="form-group"><label>性别</label><select name="gender" class="form-control"><option value="男" ${student.gender == '男' ? 'selected' : ''}>男</option><option value="女" ${student.gender == '女' ? 'selected' : ''}>女</option></select></div>
            <div class="form-group"><label>班级</label><select name="classId" class="form-control"><option value="">请选择</option><c:forEach items="${classList}" var="cls"><option value="${cls.id}" ${student.classId == cls.id ? 'selected' : ''}>${cls.name}<c:if test="${not empty cls.majorName}"> - ${cls.majorName}</c:if></option></c:forEach></select></div>
            <div class="form-group"><label>电话</label><input type="text" name="phone" class="form-control" value="${student.phone}" placeholder="请输入联系电话"></div>
            <div class="form-group"><label>邮箱</label><input type="email" name="email" class="form-control" value="${student.email}" placeholder="请输入邮箱"></div>
            <div style="margin-top:32px"><button type="submit" class="btn btn-primary">保存</button><a href="student?action=list" class="btn btn-secondary">返回</a></div>
        </form>
    </div>
</body>
</html>