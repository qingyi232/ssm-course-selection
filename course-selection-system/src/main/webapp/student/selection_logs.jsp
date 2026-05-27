<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>选课记录 - 在线课程选课系统</title>
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
        .online-dot{display:inline-block;width:6px;height:6px;background:#E53935;border-radius:50%;margin-right:6px}
        .container{max-width:1000px;margin:0 auto;padding:48px 24px}
        .page-header{margin-bottom:32px;padding-bottom:16px;border-bottom:2px solid #000}
        .page-title{font-size:2rem;font-weight:700;letter-spacing:-1px}
        .timeline{position:relative;padding-left:30px}
        .timeline::before{content:'';position:absolute;left:8px;top:0;bottom:0;width:2px;background:#eee}
        .log-item{position:relative;padding:20px 0;border-bottom:1px solid #f5f5f5}
        .log-item:last-child{border-bottom:none}
        .log-item::before{content:'';position:absolute;left:-26px;top:24px;width:12px;height:12px;border-radius:50%;background:#000}
        .log-item.select::before{background:#10b981}
        .log-item.drop::before{background:#E53935}
        .log-header{display:flex;justify-content:space-between;align-items:center;margin-bottom:8px}
        .log-action{font-size:0.7rem;font-weight:600;text-transform:uppercase;letter-spacing:1px;padding:4px 12px;border-radius:2px}
        .log-action.select{background:#e6f7f1;color:#10b981}
        .log-action.drop{background:#fde8e8;color:#E53935}
        .log-time{font-size:0.75rem;color:#999}
        .log-course{font-size:1rem;font-weight:600}
        .log-no{font-size:0.8rem;color:#666;margin-top:4px}
        .empty{text-align:center;padding:80px;color:#666}
    </style>
</head>
<body>
    <div class="navbar">
        <h1>在线课程选课<span>系统</span></h1>
        <div class="navbar-menu">
            <a href="index">首页</a><a href="course?action=list">选课中心</a><a href="course?action=selected">我的课程</a><a href="course?action=logs">选课记录</a><a href="score">成绩查询</a>
            <span class="user-info"><span class="online-dot"></span>${sessionScope.user.name}</span><a href="../logout">退出</a>
        </div>
    </div>
    <div class="container">
        <div class="page-header"><h2 class="page-title">选课记录</h2></div>
        
        <c:if test="${not empty logs}">
        <div class="timeline">
            <c:forEach items="${logs}" var="log">
            <div class="log-item ${log.actionType == 'SELECT' ? 'select' : 'drop'}">
                <div class="log-header">
                    <span class="log-action ${log.actionType == 'SELECT' ? 'select' : 'drop'}">${log.actionType == 'SELECT' ? '选课' : '退课'}</span>
                    <span class="log-time"><fmt:formatDate value="${log.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                </div>
                <div class="log-course">${log.courseName}</div>
                <div class="log-no">课程编号：${log.courseNo}</div>
            </div>
            </c:forEach>
        </div>
        </c:if>
        
        <c:if test="${empty logs}">
        <div class="empty"><p>暂无选课记录</p></div>
        </c:if>
    </div>
</body>
</html>
