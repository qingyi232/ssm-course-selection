<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>系统设置 - 在线课程选课系统</title>
    <style>
        *{margin:0;padding:0;box-sizing:border-box}
        body{font-family:'Helvetica Neue',Helvetica,Arial,'PingFang SC','Microsoft YaHei',sans-serif;background:#fff;color:#000}
        .navbar{background:#000;padding:0 48px;display:flex;justify-content:space-between;align-items:center;height:64px}
        .navbar h1{color:#fff;font-size:1rem;font-weight:700}
        .navbar h1 span{color:#E53935}
        .navbar-menu{display:flex;gap:32px;align-items:center}
        .navbar-menu a{color:#999;text-decoration:none;font-size:0.8rem;font-weight:500;text-transform:uppercase;letter-spacing:1px}
        .navbar-menu a:hover{color:#fff}
        .user-info{color:#fff;font-size:0.8rem;padding-left:32px;border-left:1px solid #333}
        .container{max-width:800px;margin:0 auto;padding:48px 24px}
        .page-header{margin-bottom:32px;padding-bottom:16px;border-bottom:2px solid #000}
        .page-title{font-size:2rem;font-weight:700;letter-spacing:-1px}
        .alert{padding:16px;margin-bottom:24px;font-size:0.875rem;border-left:3px solid #000;background:#f8f8f8}
        .status-box{padding:24px;margin-bottom:32px;border:2px solid}
        .status-box.open{border-color:#000;background:#f8f8f8}
        .status-box.closed{border-color:#E53935;background:#fff}
        .status-box .label{font-size:0.75rem;text-transform:uppercase;letter-spacing:1px;margin-bottom:8px}
        .status-box .value{font-size:1.5rem;font-weight:700}
        .status-box.closed .value{color:#E53935}
        .form-group{margin-bottom:24px}
        .form-group label{display:block;margin-bottom:8px;font-weight:500;font-size:0.75rem;text-transform:uppercase;letter-spacing:1px}
        .form-control{width:100%;padding:12px;border:1px solid #ddd;font-size:1rem}
        .form-control:focus{outline:none;border-color:#000}
        .checkbox-group{display:flex;align-items:center;gap:12px}
        .checkbox-group input{width:20px;height:20px}
        .btn{display:inline-block;padding:14px 28px;font-size:0.8rem;font-weight:600;text-transform:uppercase;letter-spacing:1px;cursor:pointer;border:1px solid;transition:all 0.2s;text-decoration:none}
        .btn-primary{background:#000;color:#fff;border-color:#000}
        .btn-primary:hover{background:#E53935;border-color:#E53935}
    </style>
</head>
<body>
    <div class="navbar">
        <h1>在线课程选课<span>系统</span> · 管理后台</h1>
        <div class="navbar-menu">
            <a href="dashboard">首页</a><a href="student?action=list">学生</a><a href="teacher?action=list">教师</a><a href="course?action=list">课程</a><a href="notice?action=list">公告</a><a href="setting">设置</a>
            <span class="user-info">${sessionScope.user.name}</span><a href="../logout">退出</a>
        </div>
    </div>
    <div class="container">
        <div class="page-header"><h2 class="page-title">系统设置</h2></div>
        <c:if test="${param.msg == 'success'}"><div class="alert">设置保存成功</div></c:if>
        
        <div class="status-box ${isOpen ? 'open' : 'closed'}">
            <div class="label">当前选课状态</div>
            <div class="value">${isOpen ? '选课已开放' : '选课已关闭'}</div>
        </div>
        
        <form action="setting" method="post">
            <input type="hidden" name="action" value="save">
            <div class="form-group">
                <div class="checkbox-group">
                    <input type="checkbox" name="selectionEnabled" id="selectionEnabled" ${selectionEnabled == '1' ? 'checked' : ''}>
                    <label for="selectionEnabled" style="margin:0">开启选课功能</label>
                </div>
            </div>
            <div class="form-group">
                <label>选课开始时间</label>
                <input type="datetime-local" name="selectionStartTime" class="form-control" value="${selectionStartTime != null ? selectionStartTime.replace(' ', 'T') : ''}">
            </div>
            <div class="form-group">
                <label>选课结束时间</label>
                <input type="datetime-local" name="selectionEndTime" class="form-control" value="${selectionEndTime != null ? selectionEndTime.replace(' ', 'T') : ''}">
            </div>
            <div style="margin-top:32px">
                <button type="submit" class="btn btn-primary">保存设置</button>
                <a href="setting?action=stats" class="btn" style="background:transparent;color:#000;border-color:#000">查看统计</a>
            </div>
        </form>
    </div>
</body>
</html>