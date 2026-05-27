<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>课程评价 - 在线课程选课系统</title>
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
        .course-info{font-size:0.875rem;color:#666;margin-top:8px}
        .stats-row{display:flex;gap:48px;margin-bottom:48px;padding:32px;background:#f8f8f8}
        .stat-item{text-align:center}
        .stat-item .number{font-size:3rem;font-weight:700}
        .stat-item .number.gold{color:#f59e0b}
        .stat-item .label{font-size:0.75rem;color:#666;text-transform:uppercase;letter-spacing:1px;margin-top:4px}
        .stars{color:#f59e0b;font-size:1.5rem;letter-spacing:2px}
        .review-list{border-top:2px solid #000;padding-top:24px}
        .review-item{padding:24px 0;border-bottom:1px solid #eee}
        .review-item:last-child{border-bottom:none}
        .review-header{display:flex;justify-content:space-between;align-items:center;margin-bottom:12px}
        .review-student{font-weight:600}
        .review-time{font-size:0.75rem;color:#999}
        .review-rating{color:#f59e0b;margin-bottom:8px}
        .review-content{color:#333;line-height:1.8}
        .empty{text-align:center;padding:80px;color:#666}
        .btn{display:inline-block;padding:8px 16px;font-size:0.75rem;font-weight:600;text-transform:uppercase;letter-spacing:1px;cursor:pointer;border:1px solid #000;transition:all 0.2s;text-decoration:none;background:transparent;color:#000}
        .btn:hover{background:#000;color:#fff}
    </style>
</head>
<body>
    <div class="navbar">
        <h1>在线课程选课<span>系统</span></h1>
        <div class="navbar-menu">
            <a href="index">首页</a><a href="course?action=list">我的课程</a><a href="score?action=courses">成绩管理</a>
            <span class="user-info"><span class="online-dot"></span>${sessionScope.user.name}</span><a href="../logout">退出</a>
        </div>
    </div>
    <div class="container">
        <div class="page-header">
            <h2 class="page-title">课程评价</h2>
            <div class="course-info">${course.courseNo} - ${course.name}</div>
        </div>
        
        <div class="stats-row">
            <div class="stat-item">
                <div class="number gold">${avgRatingDisplay}</div>
                <div class="label">平均评分</div>
            </div>
            <div class="stat-item">
                <div class="stars">
                    <c:forEach begin="1" end="5" var="i">
                        <c:choose>
                            <c:when test="${i <= avgRating}">★</c:when>
                            <c:otherwise>☆</c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>
                <div class="label">评分星级</div>
            </div>
            <div class="stat-item">
                <div class="number">${reviewCount}</div>
                <div class="label">评价数量</div>
            </div>
        </div>
        
        <div class="review-list">
            <c:if test="${not empty reviews}">
                <c:forEach items="${reviews}" var="r">
                <div class="review-item">
                    <div class="review-header">
                        <span class="review-student">${r.studentName}</span>
                        <span class="review-time"><fmt:formatDate value="${r.createTime}" pattern="yyyy-MM-dd HH:mm"/></span>
                    </div>
                    <div class="review-rating">
                        <c:forEach begin="1" end="5" var="i">
                            <c:choose>
                                <c:when test="${i <= r.rating}">★</c:when>
                                <c:otherwise>☆</c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
                    <div class="review-content">${not empty r.content ? r.content : '该学生未留下文字评价'}</div>
                </div>
                </c:forEach>
            </c:if>
            <c:if test="${empty reviews}">
                <div class="empty">暂无学生评价</div>
            </c:if>
        </div>
        
        <div style="margin-top:32px"><a href="course?action=list" class="btn">← 返回课程列表</a></div>
    </div>
</body>
</html>
