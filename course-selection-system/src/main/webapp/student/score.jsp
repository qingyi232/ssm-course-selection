<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>成绩查询 - 在线课程选课系统</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
        .container{max-width:1200px;margin:0 auto;padding:48px 24px}
        .page-header{margin-bottom:32px;padding-bottom:16px;border-bottom:2px solid #000}
        .page-title{font-size:2rem;font-weight:700;letter-spacing:-1px}
        .stats-grid{display:grid;grid-template-columns:repeat(4,1fr);gap:1px;background:#000;margin-bottom:48px}
        .stat-card{background:#fff;padding:32px;text-align:center}
        .stat-card .number{font-size:3rem;font-weight:700;color:#000}
        .stat-card .number.red{color:#E53935}
        .stat-card .number.green{color:#10b981}
        .stat-card .label{color:#666;font-size:0.75rem;text-transform:uppercase;letter-spacing:1px;margin-top:8px}
        .chart-section{display:grid;grid-template-columns:1fr 1fr;gap:32px;margin-bottom:48px}
        .chart-card{border:1px solid #eee;padding:24px}
        .chart-title{font-size:0.875rem;font-weight:600;margin-bottom:16px;text-transform:uppercase;letter-spacing:1px}
        .chart-container{height:200px}
        table{width:100%;border-collapse:collapse}
        th{background:#000;color:#fff;padding:16px;font-weight:500;font-size:0.7rem;text-transform:uppercase;letter-spacing:1px;text-align:left}
        td{padding:16px;border-bottom:1px solid #eee;font-size:0.875rem}
        tr:hover td{background:#f8f8f8}
        .course-no{font-weight:700}
        .credit{display:inline-block;background:#000;color:#fff;padding:2px 8px;font-size:0.75rem;font-weight:500}
        .score{font-size:1.25rem;font-weight:700}
        .score.pass{color:#000}
        .score.fail{color:#E53935}
        .status{display:inline-block;padding:4px 12px;font-size:0.7rem;font-weight:600;text-transform:uppercase;letter-spacing:1px}
        .status.excellent{background:#000;color:#fff}
        .status.good{background:#333;color:#fff}
        .status.pass{background:#666;color:#fff}
        .status.fail{background:#E53935;color:#fff}
        .empty{text-align:center;padding:80px;color:#666}
    </style>
</head>
<body>
    <div class="navbar">
        <h1>在线课程选课<span>系统</span></h1>
        <div class="navbar-menu">
            <a href="index">首页</a><a href="course?action=list">选课中心</a><a href="course?action=selected">我的课程</a><a href="score">成绩查询</a>
            <span class="user-info"><span class="online-dot"></span>${sessionScope.user.name}</span><a href="../logout">退出</a>
        </div>
    </div>
    <div class="container">
        <div class="page-header"><h2 class="page-title">成绩查询</h2></div>
        <div class="stats-grid">
            <div class="stat-card"><div class="number">${gpa != null ? gpa : '0.00'}</div><div class="label">平均绩点 GPA</div></div>
            <div class="stat-card"><div class="number">${totalCredit != null ? totalCredit : '0'}</div><div class="label">已修学分</div></div>
            <div class="stat-card"><div class="number green">${passCount != null ? passCount : '0'}</div><div class="label">及格科目</div></div>
            <div class="stat-card"><div class="number red">${failCount != null ? failCount : '0'}</div><div class="label">不及格科目</div></div>
        </div>
        
        <c:if test="${not empty scores}">
        <div class="chart-section">
            <div class="chart-card">
                <div class="chart-title">成绩分布</div>
                <div class="chart-container"><canvas id="scoreChart"></canvas></div>
            </div>
            <div class="chart-card">
                <div class="chart-title">各科成绩对比</div>
                <div class="chart-container"><canvas id="radarChart"></canvas></div>
            </div>
        </div>
        </c:if>
        
        <table>
            <thead><tr><th>课程编号</th><th>课程名称</th><th>学分</th><th>成绩</th><th>绩点</th><th>状态</th></tr></thead>
            <tbody>
            <c:forEach items="${scores}" var="s">
                <tr>
                    <td class="course-no">${s.courseNo}</td>
                    <td>${s.courseName}</td>
                    <td><span class="credit">${s.credit}</span></td>
                    <td><span class="score ${s.score >= 60 ? 'pass' : 'fail'}">${s.score}</span></td>
                    <td>${s.gradePoint}</td>
                    <td><c:choose><c:when test="${s.score >= 90}"><span class="status excellent">优秀</span></c:when><c:when test="${s.score >= 80}"><span class="status good">良好</span></c:when><c:when test="${s.score >= 60}"><span class="status pass">及格</span></c:when><c:otherwise><span class="status fail">不及格</span></c:otherwise></c:choose></td>
                </tr>
            </c:forEach>
            <c:if test="${empty scores}"><tr><td colspan="6"><div class="empty">暂无成绩记录</div></td></tr></c:if>
            </tbody>
        </table>
    </div>
    
    <c:if test="${not empty scores}">
    <script>
    // 成绩分布饼图
    var scoreData = {excellent: 0, good: 0, pass: 0, fail: 0};
    <c:forEach items="${scores}" var="s">
        <c:choose>
            <c:when test="${s.score >= 90}">scoreData.excellent++;</c:when>
            <c:when test="${s.score >= 80}">scoreData.good++;</c:when>
            <c:when test="${s.score >= 60}">scoreData.pass++;</c:when>
            <c:otherwise>scoreData.fail++;</c:otherwise>
        </c:choose>
    </c:forEach>
    
    new Chart(document.getElementById('scoreChart'), {
        type: 'doughnut',
        data: {
            labels: ['优秀(90+)', '良好(80-89)', '及格(60-79)', '不及格(<60)'],
            datasets: [{
                data: [scoreData.excellent, scoreData.good, scoreData.pass, scoreData.fail],
                backgroundColor: ['#000', '#333', '#666', '#E53935']
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {legend: {position: 'right'}}
        }
    });
    
    // 各科成绩雷达图
    var courseNames = [];
    var courseScores = [];
    <c:forEach items="${scores}" var="s" varStatus="st">
        <c:if test="${st.index < 6}">
        courseNames.push('${s.courseName}'.substring(0, 6));
        courseScores.push(${s.score});
        </c:if>
    </c:forEach>
    
    new Chart(document.getElementById('radarChart'), {
        type: 'radar',
        data: {
            labels: courseNames,
            datasets: [{
                label: '成绩',
                data: courseScores,
                backgroundColor: 'rgba(0, 0, 0, 0.1)',
                borderColor: '#000',
                pointBackgroundColor: '#E53935'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {r: {min: 0, max: 100}},
            plugins: {legend: {display: false}}
        }
    });
    </script>
    </c:if>
</body>
</html>