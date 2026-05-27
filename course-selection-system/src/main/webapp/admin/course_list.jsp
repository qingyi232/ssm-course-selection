<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>课程管理 - 在线课程选课系统</title>
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
        .search-box input{padding:10px 16px;border:1px solid #000;border-right:none;width:280px;font-size:0.875rem}
        .search-box input:focus{outline:none}
        .search-box button{padding:10px 16px;background:#000;color:#fff;border:1px solid #000;font-size:0.75rem;font-weight:600;text-transform:uppercase;letter-spacing:1px;cursor:pointer}
        .btn{display:inline-block;padding:10px 20px;font-size:0.75rem;font-weight:600;text-transform:uppercase;letter-spacing:1px;cursor:pointer;border:1px solid;transition:all 0.2s;text-decoration:none}
        .btn-primary{background:#000;color:#fff;border-color:#000}
        .btn-primary:hover{background:#E53935;border-color:#E53935}
        .btn-warning{background:#000;color:#fff;border-color:#000}
        .btn-danger{color:#E53935;border-color:#E53935;background:transparent}
        .btn-danger:hover{background:#E53935;color:#fff}
        .btn-sm{padding:6px 12px;font-size:0.7rem}
        .course-grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(300px,1fr));gap:24px}
        .course-card{border:1px solid #eee;transition:all 0.2s}
        .course-card:hover{border-color:#000;box-shadow:0 4px 12px rgba(0,0,0,0.1)}
        .course-cover{height:160px;background:#f5f5f5;display:flex;align-items:center;justify-content:center;overflow:hidden}
        .course-cover img{max-width:100%;max-height:100%;object-fit:contain}
        .course-cover .placeholder{font-size:3rem;color:#ddd}
        .course-body{padding:16px}
        .course-no{font-size:0.7rem;color:#999;text-transform:uppercase;letter-spacing:1px;margin-bottom:6px}
        .course-name{font-size:1rem;font-weight:700;margin-bottom:8px}
        .course-info{font-size:0.8rem;color:#666;margin-bottom:4px}
        .course-meta{display:flex;gap:12px;margin-top:12px;padding-top:12px;border-top:1px solid #eee}
        .meta-item{font-size:0.7rem}
        .meta-item .label{color:#999}
        .meta-item .value{font-weight:600}
        .quota-bar{width:100%;height:4px;background:#eee;margin:10px 0 6px}
        .quota-fill{height:100%}
        .quota-fill.green{background:#000}
        .quota-fill.orange{background:#666}
        .quota-fill.red{background:#E53935}
        .quota-text{font-size:0.7rem;color:#666;display:flex;justify-content:space-between}
        .quota-text .urgent{color:#E53935;font-weight:700}
        .status{display:inline-block;padding:3px 10px;font-size:0.65rem;font-weight:600;text-transform:uppercase;letter-spacing:1px}
        .status.open{background:#000;color:#fff}
        .status.closed{background:#eee;color:#666}
        .course-footer{padding:12px 16px;border-top:1px solid #eee;display:flex;justify-content:space-between;align-items:center}
        .action-btns{display:flex;gap:6px}
        .empty{text-align:center;padding:80px;color:#666}
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
            <h2 class="page-title">课程管理</h2>
            <div class="toolbar">
                <form class="search-box" method="get" action="course"><input type="hidden" name="action" value="list"><input type="text" name="keyword" placeholder="搜索课程编号、名称或教师" value="${param.keyword}"><button type="submit">搜索</button></form>
                <a href="course?action=add" class="btn btn-primary">添加课程</a>
            </div>
        </div>
        
        <div class="course-grid">
        <c:forEach items="${courses}" var="course">
            <c:set var="remaining" value="${course.maxStudents - course.currentStudents}"/>
            <c:set var="percent" value="${(course.currentStudents * 100) / course.maxStudents}"/>
            <div class="course-card">
                <div class="course-cover">
                    <c:choose>
                        <c:when test="${not empty course.coverImage and course.coverImage.startsWith('data:')}"><img src="${course.coverImage}" alt="${course.name}"></c:when>
                        <c:when test="${not empty course.coverImage}"><img src="${pageContext.request.contextPath}/${course.coverImage}" alt="${course.name}"></c:when>
                        <c:otherwise><span class="placeholder">📚</span></c:otherwise>
                    </c:choose>
                </div>
                <div class="course-body">
                    <div class="course-no">${course.courseNo}</div>
                    <div class="course-name">${course.name}</div>
                    <div class="course-info">👨‍🏫 ${course.teacherName}</div>
                    <div class="course-info">📍 ${course.location}</div>
                    <div class="course-meta">
                        <div class="meta-item"><span class="label">学分</span> <span class="value">${course.credit}</span></div>
                        <div class="meta-item"><span class="label">学时</span> <span class="value">${course.hours}</span></div>
                        <div class="meta-item"><span class="label">类型</span> <span class="value">${course.typeName}</span></div>
                    </div>
                    <div class="quota-bar"><div class="quota-fill ${percent >= 90 ? 'red' : (percent >= 70 ? 'orange' : 'green')}" style="width:${percent}%"></div></div>
                    <div class="quota-text"><span>已选 ${course.currentStudents} 人</span><span class="${remaining <= 5 ? 'urgent' : ''}">剩余 ${remaining} 人</span></div>
                </div>
                <div class="course-footer">
                    <c:choose><c:when test="${course.status == 1}"><span class="status open">开放</span></c:when><c:otherwise><span class="status closed">关闭</span></c:otherwise></c:choose>
                    <div class="action-btns">
                        <a href="course?action=edit&id=${course.id}" class="btn btn-warning btn-sm">编辑</a>
                        <form action="course" method="post" style="display:inline" onsubmit="return confirm('确定删除？')"><input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="${course.id}"><button type="submit" class="btn btn-danger btn-sm">删除</button></form>
                    </div>
                </div>
            </div>
        </c:forEach>
        </div>
        
        <c:if test="${empty courses}"><div class="empty">暂无课程数据</div></c:if>
    </div>
</body>
</html>
