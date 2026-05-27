<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>管理后台 - 在线课程选课系统</title>
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
        .section-title{font-size:0.75rem;font-weight:700;text-transform:uppercase;letter-spacing:2px;margin-bottom:24px}
        .online-panel{margin-bottom:48px;padding:32px;border:2px solid #000}
        .online-header{display:flex;align-items:center;gap:12px;margin-bottom:24px}
        .online-dot{width:8px;height:8px;background:#E53935;border-radius:50%;animation:pulse 2s infinite}
        @keyframes pulse{0%,100%{opacity:1}50%{opacity:0.5}}
        .online-header h3{font-size:0.75rem;font-weight:700;text-transform:uppercase;letter-spacing:2px}
        .realtime{font-size:0.7rem;color:#E53935;text-transform:uppercase;letter-spacing:1px;margin-left:auto}
        .online-stats{display:grid;grid-template-columns:repeat(4,1fr);gap:1px;background:#000}
        .online-item{background:#fff;padding:24px;text-align:center}
        .online-item .num{font-size:2.5rem;font-weight:700}
        .online-item .num.red{color:#E53935}
        .online-item .label{color:#666;font-size:0.7rem;text-transform:uppercase;letter-spacing:1px;margin-top:4px}
        .stats-grid{display:grid;grid-template-columns:repeat(4,1fr);gap:1px;background:#000;margin-bottom:48px}
        .stat-card{background:#fff;padding:32px;text-align:center}
        .stat-card .number{font-size:3rem;font-weight:700}
        .stat-card .label{color:#666;font-size:0.7rem;text-transform:uppercase;letter-spacing:1px;margin-top:8px}
        .quick-grid{display:grid;grid-template-columns:repeat(4,1fr);gap:24px}
        .quick-card{border:1px solid #000;padding:24px;text-decoration:none;color:#000;transition:all 0.2s;text-align:center}
        .quick-card:hover{background:#000;color:#fff}
        .quick-card .title{font-size:0.875rem;font-weight:700;margin-bottom:4px}
        .quick-card .arrow{font-size:1.25rem;margin-top:12px}
        .quick-card:hover .arrow{color:#E53935}
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
        <div class="online-panel">
            <div class="online-header"><span class="online-dot"></span><h3>实时在线监控</h3><span class="realtime">● 实时更新</span></div>
            <div class="online-stats">
                <div class="online-item"><div class="num red">${onlineCount != null ? onlineCount : 0}</div><div class="label">在线总人数</div></div>
                <div class="online-item"><div class="num">${studentOnline != null ? studentOnline : 0}</div><div class="label">在线学生</div></div>
                <div class="online-item"><div class="num">${teacherOnline != null ? teacherOnline : 0}</div><div class="label">在线教师</div></div>
                <div class="online-item"><div class="num">${adminOnline != null ? adminOnline : 0}</div><div class="label">在线管理员</div></div>
            </div>
        </div>
        <div class="section-title">系统概览</div>
        <div class="stats-grid">
            <div class="stat-card"><div class="number">${studentCount != null ? studentCount : 0}</div><div class="label">学生总数</div></div>
            <div class="stat-card"><div class="number">${teacherCount != null ? teacherCount : 0}</div><div class="label">教师总数</div></div>
            <div class="stat-card"><div class="number">${courseCount != null ? courseCount : 0}</div><div class="label">课程总数</div></div>
            <div class="stat-card"><div class="number">${selectionCount != null ? selectionCount : 0}</div><div class="label">选课记录</div></div>
        </div>
        <div class="section-title">快捷操作</div>
        <div class="quick-grid">
            <a href="student?action=add" class="quick-card"><div class="title">添加学生</div><div class="arrow">→</div></a>
            <a href="teacher?action=add" class="quick-card"><div class="title">添加教师</div><div class="arrow">→</div></a>
            <a href="course?action=add" class="quick-card"><div class="title">添加课程</div><div class="arrow">→</div></a>
            <a href="notice?action=add" class="quick-card"><div class="title">发布公告</div><div class="arrow">→</div></a>
        </div>
    </div>
    <script>setTimeout(function(){location.reload();},30000);</script>
</body>
</html>