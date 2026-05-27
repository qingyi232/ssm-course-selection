<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>学生首页 - 在线课程选课系统</title>
    <style>
        *{margin:0;padding:0;box-sizing:border-box}
        body{font-family:'Helvetica Neue',Helvetica,Arial,'PingFang SC','Microsoft YaHei',sans-serif;background:#fff;color:#000;line-height:1.5}
        .navbar{background:#000;padding:0 48px;display:flex;justify-content:space-between;align-items:center;height:64px}
        .navbar h1{color:#fff;font-size:1rem;font-weight:700;letter-spacing:-0.5px}
        .navbar h1 span{color:#E53935}
        .navbar-menu{display:flex;gap:32px;align-items:center}
        .navbar-menu a{color:#999;text-decoration:none;font-size:0.8rem;font-weight:500;text-transform:uppercase;letter-spacing:1px;transition:color 0.2s}
        .navbar-menu a:hover{color:#fff}
        .user-info{color:#fff;font-size:0.8rem;padding-left:32px;border-left:1px solid #333}
        .online-dot{display:inline-block;width:6px;height:6px;background:#E53935;border-radius:50%;margin-right:6px}
        .container{max-width:1200px;margin:0 auto;padding:48px 24px}
        .welcome{margin-bottom:48px}
        .welcome h2{font-size:2.5rem;font-weight:700;letter-spacing:-1px}
        .welcome p{color:#666;margin-top:8px}
        .stats-grid{display:grid;grid-template-columns:repeat(4,1fr);gap:1px;background:#000;margin-bottom:48px}
        .stat-card{background:#fff;padding:32px;text-align:center}
        .stat-card .number{font-size:3rem;font-weight:700;color:#000}
        .stat-card .number.red{color:#E53935}
        .stat-card .label{color:#666;font-size:0.75rem;text-transform:uppercase;letter-spacing:1px;margin-top:8px}
        .section-title{font-size:0.75rem;font-weight:700;text-transform:uppercase;letter-spacing:2px;margin-bottom:24px;padding-bottom:12px;border-bottom:2px solid #000}
        .quick-grid{display:grid;grid-template-columns:repeat(4,1fr);gap:24px;margin-bottom:48px}
        .quick-card{border:1px solid #000;padding:32px 24px;text-decoration:none;color:#000;transition:all 0.2s}
        .quick-card:hover{background:#000;color:#fff}
        .quick-card:hover .arrow{color:#E53935}
        .quick-card .title{font-size:1rem;font-weight:700;margin-bottom:4px}
        .quick-card .desc{font-size:0.8rem;color:#666}
        .quick-card:hover .desc{color:#999}
        .quick-card .arrow{font-size:1.5rem;margin-top:16px;color:#000}
        .grid-2{display:grid;grid-template-columns:1fr 1fr;gap:48px}
        .card{border-top:2px solid #000;padding-top:24px}
        .card-title{font-size:0.75rem;font-weight:700;text-transform:uppercase;letter-spacing:2px;margin-bottom:24px}
        .info-row{display:flex;padding:12px 0;border-bottom:1px solid #eee}
        .info-row:last-child{border-bottom:none}
        .info-row .label{width:80px;color:#666;font-size:0.8rem}
        .info-row .value{font-weight:500;font-size:0.9rem}
        .notice-item{padding:16px 0;border-bottom:1px solid #eee;cursor:pointer;transition:background 0.2s}
        .notice-item:hover{background:#f8f8f8}
        .notice-item:last-child{border-bottom:none}
        .notice-item .title{font-weight:500;font-size:0.9rem;margin-bottom:4px}
        .notice-item .date{color:#999;font-size:0.75rem}
        .empty{color:#999;font-size:0.875rem;padding:24px 0}
        .modal{display:none;position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(0,0,0,0.5);z-index:1000;align-items:center;justify-content:center}
        .modal.show{display:flex}
        .modal-content{background:#fff;max-width:600px;width:90%;max-height:80vh;overflow-y:auto;padding:32px}
        .modal-header{display:flex;justify-content:space-between;align-items:flex-start;margin-bottom:24px;padding-bottom:16px;border-bottom:2px solid #000}
        .modal-title{font-size:1.25rem;font-weight:700}
        .modal-date{font-size:0.75rem;color:#999;margin-top:8px}
        .modal-close{background:none;border:none;font-size:1.5rem;cursor:pointer;color:#999}
        .modal-close:hover{color:#000}
        .modal-body{font-size:0.9rem;line-height:1.8;color:#333}
    </style>
</head>
<body>
    <div class="navbar">
        <h1>在线课程选课<span>系统</span></h1>
        <div class="navbar-menu">
            <a href="index">首页</a>
            <a href="course?action=list">选课中心</a>
            <a href="course?action=selected">我的课程</a>
            <a href="score">成绩查询</a>
            <span class="user-info"><span class="online-dot"></span>${sessionScope.user.name}</span>
            <a href="../logout">退出</a>
        </div>
    </div>
    <div class="container">
        <div class="welcome">
            <h2>你好，${sessionScope.user.name}</h2>
            <p>欢迎使用在线课程选课系统</p>
        </div>
        <div class="stats-grid">
            <div class="stat-card"><div class="number">${selectedCount != null ? selectedCount : 0}</div><div class="label">已选课程</div></div>
            <div class="stat-card"><div class="number">${totalCredit != null ? totalCredit : 0}</div><div class="label">已选学分</div></div>
            <div class="stat-card"><div class="number">${availableCourses != null ? availableCourses : 0}</div><div class="label">可选课程</div></div>
            <div class="stat-card"><div class="number red">${onlineCount != null ? onlineCount : 1}</div><div class="label">在线人数</div></div>
        </div>
        <div class="section-title">快捷入口</div>
        <div class="quick-grid">
            <a href="course?action=list" class="quick-card"><div class="title">选课中心</div><div class="desc">浏览可选课程</div><div class="arrow">→</div></a>
            <a href="course?action=selected" class="quick-card"><div class="title">我的课程</div><div class="desc">查看已选课程</div><div class="arrow">→</div></a>
            <a href="course?action=favorites" class="quick-card"><div class="title">我的收藏</div><div class="desc">收藏的课程</div><div class="arrow">→</div></a>
            <a href="score" class="quick-card"><div class="title">成绩查询</div><div class="desc">查看课程成绩</div><div class="arrow">→</div></a>
            <a href="course?action=logs" class="quick-card"><div class="title">选课记录</div><div class="desc">选课历史日志</div><div class="arrow">→</div></a>
            <a href="profile?action=schedule" class="quick-card"><div class="title">课程表</div><div class="desc">查看上课安排</div><div class="arrow">→</div></a>
        </div>
        <div class="grid-2">
            <div class="card">
                <div class="card-title">个人信息</div>
                <div class="info-row"><span class="label">学号</span><span class="value">${sessionScope.user.studentNo}</span></div>
                <div class="info-row"><span class="label">姓名</span><span class="value">${sessionScope.user.name}</span></div>
                <div class="info-row"><span class="label">性别</span><span class="value">${sessionScope.user.gender}</span></div>
                <div class="info-row"><span class="label">班级</span><span class="value">${sessionScope.user.className}</span></div>
                <div class="info-row"><span class="label">学院</span><span class="value">${sessionScope.user.collegeName}</span></div>
            </div>
            <div class="card">
                <div class="card-title">系统公告</div>
                <c:forEach items="${notices}" var="n" varStatus="s">
                    <c:if test="${s.index < 5}">
                        <div class="notice-item" onclick="showNotice('${n.title}', '${n.publishTime}', '${n.content}')">
                            <div class="title">${n.title}</div>
                            <div class="date">${n.publishTime}</div>
                        </div>
                    </c:if>
                </c:forEach>
                <c:if test="${empty notices}"><div class="empty">暂无公告</div></c:if>
            </div>
        </div>
    </div>
    
    <!-- 公告详情弹窗 -->
    <div class="modal" id="noticeModal">
        <div class="modal-content">
            <div class="modal-header">
                <div>
                    <div class="modal-title" id="modalTitle"></div>
                    <div class="modal-date" id="modalDate"></div>
                </div>
                <button class="modal-close" onclick="closeModal()">&times;</button>
            </div>
            <div class="modal-body" id="modalContent"></div>
        </div>
    </div>
    
    <script>
    function showNotice(title, date, content) {
        document.getElementById('modalTitle').textContent = title;
        document.getElementById('modalDate').textContent = date;
        document.getElementById('modalContent').textContent = content || '暂无详细内容';
        document.getElementById('noticeModal').classList.add('show');
    }
    function closeModal() {
        document.getElementById('noticeModal').classList.remove('show');
    }
    document.getElementById('noticeModal').addEventListener('click', function(e) {
        if (e.target === this) closeModal();
    });
    </script>
</body>
</html>