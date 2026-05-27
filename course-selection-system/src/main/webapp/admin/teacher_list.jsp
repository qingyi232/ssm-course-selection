<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>教师管理 - 在线课程选课系统</title>
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
        .container{max-width:1400px;margin:0 auto;padding:48px 24px}
        .page-header{display:flex;justify-content:space-between;align-items:flex-end;margin-bottom:32px;padding-bottom:16px;border-bottom:2px solid #000}
        .page-title{font-size:2rem;font-weight:700;letter-spacing:-1px}
        .toolbar{display:flex;gap:16px;align-items:center}
        .search-box{display:flex;gap:0}
        .search-box input{padding:10px 16px;border:1px solid #000;border-right:none;width:240px;font-size:0.875rem}
        .search-box input:focus{outline:none}
        .search-box button{padding:10px 16px;background:#000;color:#fff;border:1px solid #000;font-size:0.75rem;font-weight:600;text-transform:uppercase;letter-spacing:1px;cursor:pointer}
        .btn{display:inline-block;padding:10px 20px;font-size:0.75rem;font-weight:600;text-transform:uppercase;letter-spacing:1px;cursor:pointer;border:1px solid;transition:all 0.2s;text-decoration:none}
        .btn-primary{background:#000;color:#fff;border-color:#000}
        .btn-outline{background:transparent;color:#000;border-color:#000}
        .btn-outline:hover{background:#000;color:#fff}
        .btn-warning{background:#000;color:#fff;border-color:#000}
        .btn-danger{color:#E53935;border-color:#E53935;background:transparent}
        .btn-danger:hover{background:#E53935;color:#fff}
        .btn-sm{padding:6px 12px;font-size:0.7rem}
        .alert{padding:16px;margin-bottom:24px;font-size:0.875rem;border-left:3px solid #000;background:#f8f8f8}
        .alert-danger{border-color:#E53935;color:#E53935;background:#fff}
        table{width:100%;border-collapse:collapse}
        th{background:#000;color:#fff;padding:16px;font-weight:500;font-size:0.7rem;text-transform:uppercase;letter-spacing:1px;text-align:left}
        td{padding:16px;border-bottom:1px solid #eee;font-size:0.875rem}
        tr:hover td{background:#f8f8f8}
        .teacher-no{font-weight:700}
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
            <h2 class="page-title">教师管理</h2>
            <div class="toolbar">
                <form class="search-box" method="get" action="teacher"><input type="hidden" name="action" value="list"><input type="text" name="keyword" placeholder="搜索工号或姓名" value="${param.keyword}"><button type="submit">搜索</button></form>
                <a href="teacher?action=template" class="btn btn-outline">下载模板</a>
                <button type="button" class="btn btn-outline" onclick="document.getElementById('importFile').click()">导入Excel</button>
                <a href="teacher?action=export" class="btn btn-outline">导出Excel</a>
                <a href="teacher?action=add" class="btn btn-primary">添加教师</a>
            </div>
        </div>
        <form id="importForm" action="teacher" method="post" enctype="multipart/form-data" style="display:none">
            <input type="hidden" name="action" value="import">
            <input type="file" id="importFile" name="file" accept=".xlsx,.xls" onchange="this.form.submit()">
        </form>
        <c:if test="${param.msg == 'reset_success'}"><div class="alert">密码已重置为123456</div></c:if>
        <c:if test="${param.msg == 'import_success'}"><div class="alert">导入完成！成功：${param.success} 条，失败：${param.fail} 条</div></c:if>
        <c:if test="${param.msg == 'import_empty'}"><div class="alert alert-danger">请选择要导入的文件</div></c:if>
        <c:if test="${param.msg == 'import_format'}"><div class="alert alert-danger">文件格式错误，请上传Excel文件(.xlsx或.xls)</div></c:if>
        <c:if test="${param.msg == 'import_error'}"><div class="alert alert-danger">导入失败，请检查文件格式是否正确</div></c:if>
        <table>
            <thead><tr><th>工号</th><th>姓名</th><th>性别</th><th>职称</th><th>学院</th><th>电话</th><th>操作</th></tr></thead>
            <tbody>
            <c:forEach items="${teachers}" var="t">
                <tr>
                    <td class="teacher-no">${t.teacherNo}</td><td>${t.name}</td><td>${t.gender}</td><td>${t.title}</td><td>${t.collegeName}</td><td>${t.phone}</td>
                    <td>
                        <a href="teacher?action=edit&id=${t.id}" class="btn btn-warning btn-sm">编辑</a>
                        <form action="teacher" method="post" style="display:inline"><input type="hidden" name="action" value="resetPwd"><input type="hidden" name="id" value="${t.id}"><button type="submit" class="btn btn-outline btn-sm" onclick="return confirm('确定重置密码？')">重置</button></form>
                        <form action="teacher" method="post" style="display:inline" onsubmit="return confirm('确定删除？')"><input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="${t.id}"><button type="submit" class="btn btn-danger btn-sm">删除</button></form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>