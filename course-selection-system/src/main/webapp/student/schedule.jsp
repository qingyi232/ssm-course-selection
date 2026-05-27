<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的课程表 - 在线课程选课系统</title>
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
        .container{max-width:1200px;margin:0 auto;padding:48px 24px}
        .page-header{margin-bottom:32px;padding-bottom:16px;border-bottom:2px solid #000}
        .page-title{font-size:2rem;font-weight:700;letter-spacing:-1px}
        .schedule-grid{display:grid;grid-template-columns:80px repeat(7,1fr);gap:1px;background:#000;margin-top:24px}
        .schedule-cell{background:#fff;padding:12px 8px;min-height:80px;font-size:0.8rem}
        .schedule-header{background:#000;color:#fff;text-align:center;font-weight:600;font-size:0.75rem;text-transform:uppercase;padding:16px 8px}
        .time-slot{background:#f8f8f8;font-weight:600;text-align:center;display:flex;align-items:center;justify-content:center}
        .course-item{background:#f0f0f0;padding:8px;margin-bottom:4px;border-left:3px solid #E53935}
        .course-item .name{font-weight:600;font-size:0.75rem;margin-bottom:4px}
        .course-item .info{font-size:0.7rem;color:#666}
        .empty{text-align:center;padding:60px;color:#666}
    </style>
</head>
<body>
    <div class="navbar">
        <h1>在线课程选课<span>系统</span></h1>
        <div class="navbar-menu">
            <a href="index">首页</a><a href="course?action=list">选课中心</a><a href="course?action=selected">我的课程</a><a href="profile?action=schedule">课程表</a>
            <span class="user-info">${sessionScope.user.name}</span><a href="../logout">退出</a>
        </div>
    </div>
    <div class="container">
        <div class="page-header"><h2 class="page-title">我的课程表</h2></div>
        <c:if test="${empty selections}">
            <div class="empty">暂无已选课程，请先去选课</div>
        </c:if>
        <c:if test="${not empty selections}">
            <div class="schedule-grid">
                <div class="schedule-header">时间</div>
                <div class="schedule-header">周一</div>
                <div class="schedule-header">周二</div>
                <div class="schedule-header">周三</div>
                <div class="schedule-header">周四</div>
                <div class="schedule-header">周五</div>
                <div class="schedule-header">周六</div>
                <div class="schedule-header">周日</div>
                
                <%-- 1-2节 --%>
                <div class="schedule-cell time-slot">1-2节<br>08:00-09:40</div>
                <c:forEach begin="1" end="7" var="day">
                    <c:set var="dayName" value="${day == 1 ? '一' : (day == 2 ? '二' : (day == 3 ? '三' : (day == 4 ? '四' : (day == 5 ? '五' : (day == 6 ? '六' : '日')))))}"/>
                    <div class="schedule-cell">
                        <c:forEach items="${selections}" var="sel">
                            <c:if test="${fn:contains(sel.schedule, '周'.concat(dayName).concat('1-2')) || fn:contains(sel.schedule, '周'.concat(dayName).concat('1-4'))}">
                                <div class="course-item"><div class="name">${sel.courseName}</div><div class="info">${sel.location}</div></div>
                            </c:if>
                        </c:forEach>
                    </div>
                </c:forEach>
                
                <%-- 3-4节 --%>
                <div class="schedule-cell time-slot">3-4节<br>10:00-11:40</div>
                <c:forEach begin="1" end="7" var="day">
                    <c:set var="dayName" value="${day == 1 ? '一' : (day == 2 ? '二' : (day == 3 ? '三' : (day == 4 ? '四' : (day == 5 ? '五' : (day == 6 ? '六' : '日')))))}"/>
                    <div class="schedule-cell">
                        <c:forEach items="${selections}" var="sel">
                            <c:if test="${fn:contains(sel.schedule, '周'.concat(dayName).concat('3-4')) || fn:contains(sel.schedule, '周'.concat(dayName).concat('1-4'))}">
                                <div class="course-item"><div class="name">${sel.courseName}</div><div class="info">${sel.location}</div></div>
                            </c:if>
                        </c:forEach>
                    </div>
                </c:forEach>
                
                <%-- 5-6节 --%>
                <div class="schedule-cell time-slot">5-6节<br>14:00-15:40</div>
                <c:forEach begin="1" end="7" var="day">
                    <c:set var="dayName" value="${day == 1 ? '一' : (day == 2 ? '二' : (day == 3 ? '三' : (day == 4 ? '四' : (day == 5 ? '五' : (day == 6 ? '六' : '日')))))}"/>
                    <div class="schedule-cell">
                        <c:forEach items="${selections}" var="sel">
                            <c:if test="${fn:contains(sel.schedule, '周'.concat(dayName).concat('5-6')) || fn:contains(sel.schedule, '周'.concat(dayName).concat('5-8'))}">
                                <div class="course-item"><div class="name">${sel.courseName}</div><div class="info">${sel.location}</div></div>
                            </c:if>
                        </c:forEach>
                    </div>
                </c:forEach>
                
                <%-- 7-8节 --%>
                <div class="schedule-cell time-slot">7-8节<br>16:00-17:40</div>
                <c:forEach begin="1" end="7" var="day">
                    <c:set var="dayName" value="${day == 1 ? '一' : (day == 2 ? '二' : (day == 3 ? '三' : (day == 4 ? '四' : (day == 5 ? '五' : (day == 6 ? '六' : '日')))))}"/>
                    <div class="schedule-cell">
                        <c:forEach items="${selections}" var="sel">
                            <c:if test="${fn:contains(sel.schedule, '周'.concat(dayName).concat('7-8')) || fn:contains(sel.schedule, '周'.concat(dayName).concat('5-8'))}">
                                <div class="course-item"><div class="name">${sel.courseName}</div><div class="info">${sel.location}</div></div>
                            </c:if>
                        </c:forEach>
                    </div>
                </c:forEach>
            </div>
            
            <h3 style="margin-top:48px;font-size:0.75rem;text-transform:uppercase;letter-spacing:2px;margin-bottom:16px">课程列表</h3>
            <table style="width:100%;border-collapse:collapse">
                <thead><tr style="background:#000;color:#fff"><th style="padding:12px;text-align:left;font-size:0.7rem;text-transform:uppercase">课程名称</th><th style="padding:12px;text-align:left;font-size:0.7rem">上课时间</th><th style="padding:12px;text-align:left;font-size:0.7rem">上课地点</th><th style="padding:12px;text-align:left;font-size:0.7rem">授课教师</th></tr></thead>
                <tbody>
                <c:forEach items="${selections}" var="sel">
                    <tr style="border-bottom:1px solid #eee"><td style="padding:12px;font-weight:600">${sel.courseName}</td><td style="padding:12px">${sel.schedule}</td><td style="padding:12px">${sel.location}</td><td style="padding:12px">${sel.teacherName}</td></tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
</body>
</html>
