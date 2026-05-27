<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>录入成绩 - 在线课程选课系统</title>
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
        .container{max-width:800px;margin:0 auto;padding:48px 24px}
        .page-header{margin-bottom:32px;padding-bottom:16px;border-bottom:2px solid #000}
        .page-title{font-size:2rem;font-weight:700;letter-spacing:-1px}
        .alert{padding:16px;margin-bottom:24px;font-size:0.875rem;border-left:3px solid #000;background:#f8f8f8}
        table{width:100%;border-collapse:collapse}
        th{background:#000;color:#fff;padding:16px;font-weight:500;font-size:0.7rem;text-transform:uppercase;letter-spacing:1px;text-align:left}
        td{padding:16px;border-bottom:1px solid #eee;font-size:0.875rem}
        .student-no{font-weight:700}
        .form-control{padding:10px;border:1px solid #ddd;width:100px;font-size:0.875rem;text-align:center}
        .form-control:focus{outline:none;border-color:#000}
        .btn{display:inline-block;padding:12px 24px;font-size:0.75rem;font-weight:600;text-transform:uppercase;letter-spacing:1px;cursor:pointer;border:1px solid;transition:all 0.2s;text-decoration:none;margin-right:12px}
        .btn-primary{background:#000;color:#fff;border-color:#000}
        .btn-primary:hover{background:#E53935;border-color:#E53935}
        .btn-secondary{background:transparent;color:#666;border-color:#ddd}
        .btn-outline{background:transparent;color:#000;border-color:#000}
        .btn-outline:hover{background:#000;color:#fff}
        .alert-danger{border-color:#E53935;color:#E53935;background:#fff}
        .toolbar{display:flex;gap:12px;margin-bottom:24px}
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
        <div class="page-header"><h2 class="page-title">${course.name} · 成绩录入</h2></div>
        <div class="toolbar">
            <a href="score?action=template&courseId=${course.id}" class="btn btn-outline">下载模板</a>
            <button type="button" class="btn btn-outline" onclick="document.getElementById('importFile').click()">导入成绩</button>
            <a href="score?action=export&courseId=${course.id}" class="btn btn-outline">导出成绩</a>
        </div>
        <form id="importForm" action="score" method="post" enctype="multipart/form-data" style="display:none">
            <input type="hidden" name="action" value="import">
            <input type="hidden" name="courseId" value="${course.id}">
            <input type="file" id="importFile" name="file" accept=".xlsx,.xls" onchange="this.form.submit()">
        </form>
        <c:if test="${param.msg == 'success'}"><div class="alert">成绩保存成功</div></c:if>
        <c:if test="${param.msg == 'import_success'}"><div class="alert">导入完成！成功：${param.success} 条，失败：${param.fail} 条</div></c:if>
        <c:if test="${param.msg == 'import_empty'}"><div class="alert alert-danger">请选择要导入的文件</div></c:if>
        <c:if test="${param.msg == 'import_format'}"><div class="alert alert-danger">文件格式错误，请上传Excel文件</div></c:if>
        <c:if test="${param.msg == 'import_error'}"><div class="alert alert-danger">导入失败，请检查文件格式</div></c:if>
        <form action="score" method="post">
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="courseId" value="${course.id}">
            <table>
                <thead><tr><th>学号</th><th>姓名</th><th>成绩</th></tr></thead>
                <tbody>
                <c:forEach items="${selections}" var="sel">
                    <tr>
                        <td class="student-no">${sel.studentNo}</td>
                        <td>${sel.studentName}</td>
                        <td>
                            <input type="hidden" name="studentId" value="${sel.studentId}">
                            <c:set var="existScore" value=""/>
                            <c:forEach items="${scores}" var="sc"><c:if test="${sc.studentId == sel.studentId}"><c:set var="existScore" value="${sc.score}"/></c:if></c:forEach>
                            <input type="number" name="score" class="form-control" min="0" max="100" step="0.5" value="${existScore}" placeholder="0-100">
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div style="margin-top:32px"><button type="submit" class="btn btn-primary">保存成绩</button><a href="score?action=list" class="btn btn-secondary">返回</a></div>
        </form>
    </div>
</body>
</html>