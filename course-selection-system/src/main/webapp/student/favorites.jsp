<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的收藏 - 在线课程选课系统</title>
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
        .container{max-width:1400px;margin:0 auto;padding:48px 24px}
        .page-header{margin-bottom:32px;padding-bottom:16px;border-bottom:2px solid #000;display:flex;justify-content:space-between;align-items:center}
        .page-title{font-size:2rem;font-weight:700;letter-spacing:-1px}
        .course-grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(320px,1fr));gap:24px}
        .course-card{border:1px solid #eee;transition:all 0.2s}
        .course-card:hover{border-color:#000;box-shadow:0 4px 12px rgba(0,0,0,0.1)}
        .course-cover{height:180px;background:#f5f5f5;display:flex;align-items:center;justify-content:center;overflow:hidden;position:relative}
        .course-cover img{max-width:100%;max-height:100%;object-fit:contain}
        .course-cover .placeholder{font-size:3rem;color:#ddd}
        .fav-btn{position:absolute;top:12px;right:12px;background:#fff;border:none;width:36px;height:36px;border-radius:50%;cursor:pointer;font-size:1.2rem;box-shadow:0 2px 8px rgba(0,0,0,0.1)}
        .fav-btn.active{color:#E53935}
        .course-body{padding:20px}
        .course-no{font-size:0.7rem;color:#999;text-transform:uppercase;letter-spacing:1px;margin-bottom:8px}
        .course-name{font-size:1.1rem;font-weight:700;margin-bottom:12px}
        .course-info{font-size:0.8rem;color:#666;margin-bottom:6px}
        .course-meta{display:flex;gap:16px;margin-top:16px;padding-top:16px;border-top:1px solid #eee}
        .meta-item{font-size:0.75rem}
        .meta-item .label{color:#999}
        .meta-item .value{font-weight:600}
        .course-footer{padding:16px 20px;border-top:1px solid #eee;display:flex;justify-content:space-between;align-items:center}
        .quota{font-size:0.75rem;color:#666}
        .quota .num{font-weight:700}
        .quota .full{color:#E53935}
        .btn{display:inline-block;padding:8px 16px;font-size:0.75rem;font-weight:600;text-transform:uppercase;letter-spacing:1px;cursor:pointer;border:1px solid;transition:all 0.2s;text-decoration:none;background:transparent}
        .btn-primary{background:#000;color:#fff;border-color:#000}
        .btn-primary:hover{background:#333}
        .btn-disabled{background:#ccc;color:#fff;border-color:#ccc;cursor:not-allowed}
        .empty{text-align:center;padding:80px;color:#666}
        .empty .action{margin-top:24px}
    </style>
</head>
<body>
    <div class="navbar">
        <h1>在线课程选课<span>系统</span></h1>
        <div class="navbar-menu">
            <a href="index">首页</a><a href="course?action=list">选课中心</a><a href="course?action=selected">我的课程</a><a href="course?action=favorites">我的收藏</a><a href="score">成绩查询</a>
            <span class="user-info"><span class="online-dot"></span>${sessionScope.user.name}</span><a href="../logout">退出</a>
        </div>
    </div>
    <div class="container">
        <div class="page-header">
            <h2 class="page-title">我的收藏</h2>
            <span style="color:#666;font-size:0.875rem">共 ${favorites.size()} 门课程</span>
        </div>
        
        <c:if test="${not empty favorites}">
        <div class="course-grid">
            <c:forEach items="${favorites}" var="fav">
            <c:set var="isSelected" value="false"/>
            <c:forEach items="${selectedCourses}" var="sel">
                <c:if test="${sel.courseId == fav.courseId}"><c:set var="isSelected" value="true"/></c:if>
            </c:forEach>
            <div class="course-card">
                <div class="course-cover">
                    <c:choose>
                        <c:when test="${not empty fav.coverImage and fav.coverImage.startsWith('data:')}"><img src="${fav.coverImage}" alt="${fav.courseName}"></c:when>
                        <c:when test="${not empty fav.coverImage}"><img src="${pageContext.request.contextPath}/${fav.coverImage}" alt="${fav.courseName}"></c:when>
                        <c:otherwise><span class="placeholder">📚</span></c:otherwise>
                    </c:choose>
                    <form action="course" method="post" style="display:inline">
                        <input type="hidden" name="action" value="favorite">
                        <input type="hidden" name="courseId" value="${fav.courseId}">
                        <input type="hidden" name="from" value="favorites">
                        <button type="submit" class="fav-btn active" title="取消收藏">❤</button>
                    </form>
                </div>
                <div class="course-body">
                    <div class="course-no">${fav.courseNo}</div>
                    <div class="course-name">${fav.courseName}</div>
                    <div class="course-info">👨‍🏫 ${fav.teacherName}</div>
                    <div class="course-info">📍 ${fav.location}</div>
                    <div class="course-info">🕐 ${fav.schedule}</div>
                    <div class="course-meta">
                        <div class="meta-item"><span class="label">学分</span> <span class="value">${fav.credit}</span></div>
                    </div>
                </div>
                <div class="course-footer">
                    <span class="quota">余量：<span class="num ${fav.currentStudents >= fav.maxStudents ? 'full' : ''}">${fav.maxStudents - fav.currentStudents}</span>/${fav.maxStudents}</span>
                    <c:choose>
                        <c:when test="${isSelected}"><span class="btn btn-disabled">已选</span></c:when>
                        <c:when test="${fav.currentStudents >= fav.maxStudents}"><span class="btn btn-disabled">已满</span></c:when>
                        <c:when test="${!selectionOpen}"><span class="btn btn-disabled">未开放</span></c:when>
                        <c:otherwise>
                            <form action="course" method="post" style="display:inline">
                                <input type="hidden" name="action" value="select">
                                <input type="hidden" name="courseId" value="${fav.courseId}">
                                <button type="submit" class="btn btn-primary">选课</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            </c:forEach>
        </div>
        </c:if>
        
        <c:if test="${empty favorites}">
        <div class="empty">
            <p>暂无收藏课程</p>
            <div class="action"><a href="course?action=list" class="btn btn-primary">去浏览课程</a></div>
        </div>
        </c:if>
    </div>
</body>
</html>
