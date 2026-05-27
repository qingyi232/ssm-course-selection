<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>选课中心 - 在线课程选课系统</title>
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
        .page-header{display:flex;justify-content:space-between;align-items:flex-end;margin-bottom:32px;padding-bottom:16px;border-bottom:2px solid #000}
        .page-title{font-size:2rem;font-weight:700;letter-spacing:-1px}
        .header-info{display:flex;gap:24px;align-items:center}
        .realtime{font-size:0.75rem;color:#E53935;text-transform:uppercase;letter-spacing:1px}
        .status-closed{background:#E53935;color:#fff;padding:8px 16px;font-size:0.75rem;font-weight:600}
        .filter-bar{background:#f8f8f8;padding:20px;margin-bottom:24px;display:flex;flex-wrap:wrap;gap:16px;align-items:center}
        .filter-group{display:flex;align-items:center;gap:8px}
        .filter-group label{font-size:0.75rem;color:#666;text-transform:uppercase;letter-spacing:1px}
        .filter-group select,.filter-group input{padding:8px 12px;border:1px solid #ddd;font-size:0.875rem;min-width:120px}
        .filter-group select:focus,.filter-group input:focus{outline:none;border-color:#000}
        .filter-btn{padding:8px 16px;background:#000;color:#fff;border:none;font-size:0.75rem;cursor:pointer;text-transform:uppercase}
        .filter-btn:hover{background:#E53935}
        .filter-reset{padding:8px 16px;background:#fff;color:#666;border:1px solid #ddd;font-size:0.75rem;cursor:pointer;text-transform:uppercase;text-decoration:none}
        .toast{position:fixed;top:80px;right:20px;padding:16px 24px;border-radius:4px;font-size:0.875rem;z-index:1000;animation:slideIn 0.3s ease;box-shadow:0 4px 12px rgba(0,0,0,0.15)}
        .toast-success{background:#000;color:#fff}
        .toast-error{background:#E53935;color:#fff}
        .toast-warning{background:#ff9800;color:#fff}
        @keyframes slideIn{from{transform:translateX(100%);opacity:0}to{transform:translateX(0);opacity:1}}
        .course-grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(320px,1fr));gap:24px}
        .course-card{border:1px solid #eee;transition:all 0.2s}
        .course-card:hover{border-color:#000;box-shadow:0 4px 12px rgba(0,0,0,0.1)}
        .course-card.full{opacity:0.7}
        .course-cover{height:180px;background:#f5f5f5;display:flex;align-items:center;justify-content:center;overflow:hidden;position:relative}
        .course-cover img{max-width:100%;max-height:100%;object-fit:contain}
        .course-cover .placeholder{font-size:3rem;color:#ddd}
        .course-cover .full-badge{position:absolute;top:0;left:0;right:0;bottom:0;background:rgba(0,0,0,0.6);display:flex;align-items:center;justify-content:center;color:#fff;font-size:1.5rem;font-weight:700}
        .fav-btn{position:absolute;top:12px;right:12px;background:#fff;border:none;width:36px;height:36px;border-radius:50%;cursor:pointer;font-size:1.2rem;box-shadow:0 2px 8px rgba(0,0,0,0.1);transition:all 0.2s}
        .fav-btn:hover{transform:scale(1.1)}
        .fav-btn.active{color:#E53935}
        .course-body{padding:20px}
        .course-no{font-size:0.7rem;color:#999;text-transform:uppercase;letter-spacing:1px;margin-bottom:8px}
        .course-name{font-size:1.1rem;font-weight:700;margin-bottom:12px}
        .course-info{font-size:0.8rem;color:#666;margin-bottom:8px}
        .course-meta{display:flex;gap:16px;margin-top:16px;padding-top:16px;border-top:1px solid #eee}
        .meta-item{font-size:0.75rem}
        .meta-item .label{color:#999}
        .meta-item .value{font-weight:600}
        .quota-bar{width:100%;height:4px;background:#eee;margin:12px 0 8px}
        .quota-fill{height:100%}
        .quota-fill.green{background:#000}
        .quota-fill.orange{background:#666}
        .quota-fill.red{background:#E53935}
        .quota-text{font-size:0.7rem;color:#666;display:flex;justify-content:space-between}
        .quota-text .urgent{color:#E53935;font-weight:700}
        .course-footer{padding:16px 20px;border-top:1px solid #eee;display:flex;justify-content:space-between;align-items:center}
        .btn{display:inline-block;padding:10px 20px;font-size:0.75rem;font-weight:600;text-transform:uppercase;letter-spacing:1px;cursor:pointer;border:1px solid;transition:all 0.2s;text-decoration:none}
        .btn-primary{background:#000;color:#fff;border-color:#000}
        .btn-primary:hover{background:#E53935;border-color:#E53935}
        .btn-disabled{background:#eee;color:#999;cursor:not-allowed;border-color:#eee}
        .btn-selected{background:transparent;color:#666;border-color:#ddd;cursor:default}
        .empty{text-align:center;padding:80px;color:#666}
        .modal{display:none;position:fixed;top:0;left:0;right:0;bottom:0;background:rgba(0,0,0,0.5);z-index:1000;align-items:center;justify-content:center}
        .modal.show{display:flex}
        .modal-content{background:#fff;padding:32px;max-width:400px;width:90%}
        .modal-title{font-size:1.25rem;font-weight:700;margin-bottom:16px}
        .modal-body{color:#666;margin-bottom:24px;line-height:1.6}
        .modal-footer{display:flex;gap:12px;justify-content:flex-end}
        .btn-outline{background:transparent;color:#000;border-color:#000}
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
            <h2 class="page-title">选课中心</h2>
            <div class="header-info">
                <c:if test="${selectionOpen}"><span class="realtime">● 选课进行中</span></c:if>
                <c:if test="${!selectionOpen}"><span class="status-closed">选课已关闭</span></c:if>
            </div>
        </div>
        <form class="filter-bar" method="get" action="course">
            <input type="hidden" name="action" value="list">
            <div class="filter-group"><label>关键词</label><input type="text" name="keyword" placeholder="课程名/教师" value="${param.keyword}"></div>
            <div class="filter-group"><label>学分</label>
                <select name="credit"><option value="">全部</option>
                    <option value="1" ${param.credit == '1' ? 'selected' : ''}>1学分</option>
                    <option value="2" ${param.credit == '2' ? 'selected' : ''}>2学分</option>
                    <option value="3" ${param.credit == '3' ? 'selected' : ''}>3学分</option>
                    <option value="4" ${param.credit == '4' ? 'selected' : ''}>4学分及以上</option>
                </select>
            </div>
            <div class="filter-group"><label>课程类型</label>
                <select name="typeId"><option value="">全部</option>
                    <c:forEach items="${courseTypes}" var="type"><option value="${type.id}" ${param.typeId == type.id.toString() ? 'selected' : ''}>${type.name}</option></c:forEach>
                </select>
            </div>
            <div class="filter-group"><label>授课教师</label>
                <select name="teacherId"><option value="">全部</option>
                    <c:forEach items="${teachers}" var="t"><option value="${t.id}" ${param.teacherId == t.id.toString() ? 'selected' : ''}>${t.name}</option></c:forEach>
                </select>
            </div>
            <button type="submit" class="filter-btn">筛选</button>
            <a href="course?action=list" class="filter-reset">重置</a>
        </form>
        <div id="toast" class="toast" style="display:none"></div>
        <div class="course-grid">
        <c:forEach items="${courses}" var="course">
            <c:set var="remaining" value="${course.maxStudents - course.currentStudents}"/>
            <c:set var="percent" value="${(course.currentStudents * 100) / course.maxStudents}"/>
            <c:set var="isSelected" value="false"/><c:set var="isFavorite" value="false"/>
            <c:forEach items="${selectedCourses}" var="sel"><c:if test="${sel.courseId == course.id}"><c:set var="isSelected" value="true"/></c:if></c:forEach>
            <c:forEach items="${favoriteCourses}" var="fav"><c:if test="${fav.courseId == course.id}"><c:set var="isFavorite" value="true"/></c:if></c:forEach>
            <div class="course-card ${remaining <= 0 ? 'full' : ''}">
                <div class="course-cover">
                    <c:choose>
                        <c:when test="${not empty course.coverImage and course.coverImage.startsWith('data:')}"><img src="${course.coverImage}" alt="${course.name}"></c:when>
                        <c:when test="${not empty course.coverImage}"><img src="${pageContext.request.contextPath}/${course.coverImage}" alt="${course.name}"></c:when>
                        <c:otherwise><span class="placeholder">📚</span></c:otherwise>
                    </c:choose>
                    <c:if test="${remaining <= 0}"><div class="full-badge">名额已满</div></c:if>
                    <form action="course" method="post" style="display:inline"><input type="hidden" name="action" value="favorite"><input type="hidden" name="courseId" value="${course.id}"><input type="hidden" name="from" value="list">
                        <button type="submit" class="fav-btn ${isFavorite ? 'active' : ''}" title="${isFavorite ? '取消收藏' : '收藏课程'}">${isFavorite ? '❤' : '♡'}</button>
                    </form>
                </div>
                <div class="course-body">
                    <div class="course-no">${course.courseNo}</div>
                    <div class="course-name">${course.name}</div>
                    <div class="course-info">👨‍🏫 ${course.teacherName}</div>
                    <div class="course-info">📍 ${course.location}</div>
                    <div class="course-info">🕐 ${course.schedule}</div>
                    <div class="course-meta">
                        <div class="meta-item"><span class="label">学分</span> <span class="value">${course.credit}</span></div>
                        <div class="meta-item"><span class="label">学时</span> <span class="value">${course.hours}</span></div>
                        <div class="meta-item"><span class="label">类型</span> <span class="value">${course.typeName}</span></div>
                    </div>
                    <div class="quota-bar"><div class="quota-fill ${percent >= 90 ? 'red' : (percent >= 70 ? 'orange' : 'green')}" style="width:${percent}%"></div></div>
                    <div class="quota-text"><span>已选 ${course.currentStudents} 人</span><span class="${remaining <= 5 ? 'urgent' : ''}">剩余 ${remaining} 人</span></div>
                </div>
                <div class="course-footer">
                    <span style="font-size:0.8rem;color:#666">${course.semester}</span>
                    <c:choose>
                        <c:when test="${isSelected}"><span class="btn btn-selected">已选</span></c:when>
                        <c:when test="${!selectionOpen}"><span class="btn btn-disabled" title="选课时间已结束">选课关闭</span></c:when>
                        <c:when test="${remaining <= 0}"><span class="btn btn-disabled" onclick="showToast('该课程名额已满，无法选课','error')" title="该课程名额已满">名额已满</span></c:when>
                        <c:otherwise><button type="button" class="btn btn-primary" onclick="confirmSelect(${course.id},'${course.name}','${course.schedule}')">选课</button></c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:forEach>
        </div>
        <c:if test="${empty courses}"><div class="empty">暂无符合条件的课程</div></c:if>
    </div>
    <div id="selectModal" class="modal">
        <div class="modal-content">
            <div class="modal-title">确认选课</div>
            <div class="modal-body"><p>您确定要选择课程：<strong id="modalCourseName"></strong>？</p><p style="margin-top:8px;font-size:0.85rem">上课时间：<span id="modalSchedule"></span></p></div>
            <div class="modal-footer">
                <button class="btn btn-outline" onclick="closeModal()">取消</button>
                <form id="selectForm" action="course" method="post" style="display:inline"><input type="hidden" name="action" value="select"><input type="hidden" name="courseId" id="modalCourseId"><button type="submit" class="btn btn-primary">确认选课</button></form>
            </div>
        </div>
    </div>
    <script>
    function showToast(message, type) {
        var toast = document.getElementById('toast');
        toast.textContent = message;
        toast.className = 'toast toast-' + type;
        toast.style.display = 'block';
        setTimeout(function() { toast.style.display = 'none'; }, 3000);
    }
    function confirmSelect(courseId, courseName, schedule) {
        document.getElementById('modalCourseId').value = courseId;
        document.getElementById('modalCourseName').textContent = courseName;
        document.getElementById('modalSchedule').textContent = schedule;
        document.getElementById('selectModal').classList.add('show');
    }
    function closeModal() { document.getElementById('selectModal').classList.remove('show'); }
    window.onload = function() {
        var msg = '${param.msg}';
        if (msg === 'success') showToast('🎉 选课成功！', 'success');
        else if (msg === 'full') showToast('❌ 该课程名额已满，请选择其他课程', 'error');
        else if (msg === 'selected') showToast('⚠️ 您已选过该课程', 'warning');
        else if (msg === 'conflict') showToast('❌ 课程时间冲突！该时间段您已有其他课程', 'error');
        else if (msg === 'selection_closed') showToast('⚠️ 选课已关闭，无法进行选课操作', 'warning');
        else if (msg === 'error') showToast('❌ 操作失败，请稍后重试', 'error');
    }
    document.getElementById('selectModal').onclick = function(e) { if (e.target === this) closeModal(); }
    </script>
</body>
</html>
