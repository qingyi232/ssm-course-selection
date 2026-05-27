<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的课程 - 在线课程选课系统</title>
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
        .page-header{margin-bottom:32px;padding-bottom:16px;border-bottom:2px solid #000}
        .page-title{font-size:2rem;font-weight:700;letter-spacing:-1px}
        .alert{padding:16px;margin-bottom:24px;font-size:0.875rem;border-left:3px solid #000;background:#f8f8f8}
        .alert-success{border-left-color:#10b981;background:#e6f7f1}
        .course-grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(320px,1fr));gap:24px}
        .course-card{border:1px solid #eee;transition:all 0.2s}
        .course-card:hover{border-color:#000;box-shadow:0 4px 12px rgba(0,0,0,0.1)}
        .course-cover{height:180px;background:#f5f5f5;display:flex;align-items:center;justify-content:center;overflow:hidden}
        .course-cover img{max-width:100%;max-height:100%;object-fit:contain}
        .course-cover .placeholder{font-size:3rem;color:#ddd}
        .course-body{padding:20px}
        .course-no{font-size:0.7rem;color:#999;text-transform:uppercase;letter-spacing:1px;margin-bottom:8px}
        .course-name{font-size:1.1rem;font-weight:700;margin-bottom:12px}
        .course-info{font-size:0.8rem;color:#666;margin-bottom:6px}
        .course-meta{display:flex;gap:16px;margin-top:16px;padding-top:16px;border-top:1px solid #eee}
        .meta-item{font-size:0.75rem}
        .meta-item .label{color:#999}
        .meta-item .value{font-weight:600}
        .course-footer{padding:16px 20px;border-top:1px solid #eee;display:flex;justify-content:space-between;align-items:center;gap:8px}
        .select-time{font-size:0.75rem;color:#999}
        .btn{display:inline-block;padding:8px 16px;font-size:0.75rem;font-weight:600;text-transform:uppercase;letter-spacing:1px;cursor:pointer;border:1px solid;transition:all 0.2s;text-decoration:none;background:transparent}
        .btn-danger{color:#E53935;border-color:#E53935}
        .btn-danger:hover{background:#E53935;color:#fff}
        .btn-primary{background:#000;color:#fff;border-color:#000}
        .btn-secondary{color:#666;border-color:#ccc}
        .btn-secondary:hover{background:#f5f5f5}
        .empty{text-align:center;padding:80px;color:#666}
        .empty .action{margin-top:24px}
        .modal{display:none;position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(0,0,0,0.5);z-index:1000;align-items:center;justify-content:center}
        .modal.show{display:flex}
        .modal-content{background:#fff;max-width:500px;width:90%;padding:32px}
        .modal-header{display:flex;justify-content:space-between;align-items:center;margin-bottom:24px;padding-bottom:16px;border-bottom:2px solid #000}
        .modal-title{font-size:1.25rem;font-weight:700}
        .modal-close{background:none;border:none;font-size:1.5rem;cursor:pointer;color:#999}
        .modal-close:hover{color:#000}
        .rating-stars{display:flex;gap:8px;margin-bottom:20px}
        .rating-stars input{display:none}
        .rating-stars label{font-size:2rem;color:#ddd;cursor:pointer;transition:color 0.2s}
        .rating-stars label:hover,.rating-stars label:hover~label,.rating-stars input:checked~label{color:#f59e0b}
        .rating-stars{flex-direction:row-reverse;justify-content:flex-end}
        textarea{width:100%;padding:12px;border:1px solid #ddd;font-size:0.9rem;resize:vertical;min-height:100px;margin-bottom:20px}
        textarea:focus{outline:none;border-color:#000}
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
        <div class="page-header"><h2 class="page-title">我的课程</h2></div>
        <c:if test="${param.msg == 'drop_success'}"><div class="alert alert-success">退课成功</div></c:if>
        <c:if test="${param.msg == 'review_success'}"><div class="alert alert-success">评价提交成功</div></c:if>
        <c:if test="${param.msg == 'selection_closed'}"><div class="alert" style="border-left-color:#E53935">选课/退课通道已关闭，无法操作</div></c:if>
        <c:if test="${param.msg == 'error'}"><div class="alert" style="border-left-color:#E53935">操作失败，请重试</div></c:if>
        
        <c:if test="${not empty selections}">
        <div class="course-grid">
            <c:forEach items="${selections}" var="sel">
            <div class="course-card">
                <div class="course-cover">
                    <c:choose>
                        <c:when test="${not empty sel.coverImage and sel.coverImage.startsWith('data:')}"><img src="${sel.coverImage}" alt="${sel.courseName}"></c:when>
                        <c:when test="${not empty sel.coverImage}"><img src="${pageContext.request.contextPath}/${sel.coverImage}" alt="${sel.courseName}"></c:when>
                        <c:otherwise><span class="placeholder">📚</span></c:otherwise>
                    </c:choose>
                </div>
                <div class="course-body">
                    <div class="course-no">${sel.courseNo}</div>
                    <div class="course-name">${sel.courseName}</div>
                    <div class="course-info">👨‍🏫 ${sel.teacherName}</div>
                    <div class="course-info">📍 ${sel.location}</div>
                    <div class="course-info">🕐 ${sel.schedule}</div>
                    <div class="course-meta">
                        <div class="meta-item"><span class="label">学分</span> <span class="value">${sel.credit}</span></div>
                    </div>
                </div>
                <div class="course-footer">
                    <span class="select-time">选课：<fmt:formatDate value="${sel.selectTime}" pattern="MM-dd HH:mm"/></span>
                    <div>
                        <button type="button" class="btn btn-secondary" onclick="openReview(${sel.courseId}, '${sel.courseName}')">评价</button>
                        <form action="course" method="post" style="display:inline" onsubmit="return confirm('确定要退选该课程吗？')">
                            <input type="hidden" name="action" value="drop">
                            <input type="hidden" name="courseId" value="${sel.courseId}">
                            <button type="submit" class="btn btn-danger">退课</button>
                        </form>
                    </div>
                </div>
            </div>
            </c:forEach>
        </div>
        </c:if>
        
        <c:if test="${empty selections}">
        <div class="empty">
            <p>暂无已选课程</p>
            <div class="action"><a href="course?action=list" class="btn btn-primary">去选课</a></div>
        </div>
        </c:if>
    </div>
    
    <!-- 评价弹窗 -->
    <div class="modal" id="reviewModal">
        <div class="modal-content">
            <div class="modal-header">
                <div class="modal-title">课程评价</div>
                <button class="modal-close" onclick="closeReview()">&times;</button>
            </div>
            <form action="course" method="post">
                <input type="hidden" name="action" value="review">
                <input type="hidden" name="courseId" id="reviewCourseId">
                <p style="margin-bottom:16px;color:#666" id="reviewCourseName"></p>
                <div class="rating-stars">
                    <input type="radio" name="rating" value="5" id="star5" checked><label for="star5">★</label>
                    <input type="radio" name="rating" value="4" id="star4"><label for="star4">★</label>
                    <input type="radio" name="rating" value="3" id="star3"><label for="star3">★</label>
                    <input type="radio" name="rating" value="2" id="star2"><label for="star2">★</label>
                    <input type="radio" name="rating" value="1" id="star1"><label for="star1">★</label>
                </div>
                <textarea name="content" placeholder="分享你对这门课程的看法..."></textarea>
                <button type="submit" class="btn btn-primary" style="width:100%">提交评价</button>
            </form>
        </div>
    </div>
    
    <script>
    function openReview(courseId, courseName) {
        document.getElementById('reviewCourseId').value = courseId;
        document.getElementById('reviewCourseName').textContent = courseName;
        document.getElementById('reviewModal').classList.add('show');
    }
    function closeReview() {
        document.getElementById('reviewModal').classList.remove('show');
    }
    document.getElementById('reviewModal').addEventListener('click', function(e) {
        if (e.target === this) closeReview();
    });
    </script>
</body>
</html>
