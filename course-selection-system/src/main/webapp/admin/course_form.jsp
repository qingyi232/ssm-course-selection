<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty course ? '添加' : '编辑'}课程 - 在线课程选课系统</title>
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
        .container{max-width:700px;margin:0 auto;padding:48px 24px}
        .page-header{margin-bottom:32px;padding-bottom:16px;border-bottom:2px solid #000}
        .page-title{font-size:2rem;font-weight:700;letter-spacing:-1px}
        .form-row{display:grid;grid-template-columns:1fr 1fr;gap:24px}
        .form-group{margin-bottom:24px}
        .form-group.full{grid-column:1/-1}
        .form-group label{display:block;margin-bottom:8px;font-weight:500;font-size:0.75rem;text-transform:uppercase;letter-spacing:1px}
        .form-control{width:100%;padding:12px 0;background:transparent;border:none;border-bottom:1px solid #ddd;font-size:1rem;transition:border-color 0.2s}
        .form-control:focus{outline:none;border-bottom-color:#000}
        select.form-control{padding:12px 0;background:#fff}
        textarea.form-control{min-height:80px;resize:vertical;border:1px solid #ddd;padding:12px}
        textarea.form-control:focus{border-color:#000}
        .cover-upload{border:2px dashed #ddd;padding:24px;text-align:center;cursor:pointer;transition:border-color 0.2s}
        .cover-upload:hover{border-color:#000}
        .cover-upload input{display:none}
        .cover-upload .preview{max-width:200px;max-height:150px;margin:12px auto;display:block}
        .cover-upload .hint{font-size:0.8rem;color:#666}
        .current-cover{margin-top:12px}
        .current-cover img{max-width:200px;max-height:150px;border:1px solid #eee}
        .btn{display:inline-block;padding:14px 28px;font-size:0.8rem;font-weight:600;text-transform:uppercase;letter-spacing:1px;cursor:pointer;border:1px solid;transition:all 0.2s;text-decoration:none;margin-right:12px}
        .btn-primary{background:#000;color:#fff;border-color:#000}
        .btn-primary:hover{background:#E53935;border-color:#E53935}
        .btn-secondary{background:transparent;color:#666;border-color:#ddd}
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
        <div class="page-header"><h2 class="page-title">${empty course ? '添加课程' : '编辑课程'}</h2></div>
        <form action="course" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="${empty course ? 'add' : 'edit'}">
            <c:if test="${not empty course}"><input type="hidden" name="id" value="${course.id}"></c:if>
            
            <div class="form-row">
                <c:if test="${empty course}">
                <div class="form-group"><label>课程编号</label><input type="text" name="courseNo" class="form-control" required placeholder="请输入课程编号"></div>
                </c:if>
                <div class="form-group ${empty course ? '' : 'full'}"><label>课程名称</label><input type="text" name="name" class="form-control" value="${course.name}" required placeholder="请输入课程名称"></div>
            </div>
            
            <div class="form-row">
                <div class="form-group"><label>学分</label><input type="number" name="credit" class="form-control" step="0.5" value="${empty course ? '3.0' : course.credit}" required></div>
                <div class="form-group"><label>学时</label><input type="number" name="hours" class="form-control" value="${empty course ? '48' : course.hours}" required></div>
            </div>
            
            <div class="form-row">
                <div class="form-group"><label>课程类型</label><select name="typeId" class="form-control" required><c:forEach items="${types}" var="t"><option value="${t.id}" ${course.typeId == t.id ? 'selected' : ''}>${t.name}</option></c:forEach></select></div>
                <div class="form-group"><label>授课教师</label><select name="teacherId" class="form-control" required><c:forEach items="${teachers}" var="t"><option value="${t.id}" ${course.teacherId == t.id ? 'selected' : ''}>${t.name} - ${t.title}</option></c:forEach></select></div>
            </div>
            
            <div class="form-group"><label>最大人数</label><input type="number" name="maxStudents" class="form-control" value="${empty course ? '60' : course.maxStudents}" required></div>
            <div class="form-group"><label>上课地点</label><input type="text" name="location" class="form-control" value="${course.location}" placeholder="如：教学楼A301"></div>
            <div class="form-group"><label>上课时间</label><input type="text" name="schedule" class="form-control" value="${course.schedule}" placeholder="如：周一1-2节,周三3-4节"></div>
            <div class="form-group"><label>学期</label><input type="text" name="semester" class="form-control" value="${empty course ? '2024-2025-2' : course.semester}"></div>
            
            <div class="form-group">
                <label>课程封面</label>
                <div class="cover-upload" onclick="document.getElementById('coverInput').click()">
                    <input type="file" name="coverImage" id="coverInput" accept="image/*" onchange="previewImage(this)">
                    <img id="preview" class="preview" style="display:none">
                    <div class="hint" id="uploadHint">点击上传课程封面图片<br>支持 JPG、PNG、GIF 格式，最大 2MB</div>
                </div>
                <c:if test="${not empty course.coverImage}">
                    <div class="current-cover">
                        <p style="font-size:0.75rem;color:#666;margin-bottom:8px">当前封面：</p>
                        <c:choose>
                            <c:when test="${course.coverImage.startsWith('data:')}"><img src="${course.coverImage}" alt="当前封面"></c:when>
                            <c:otherwise><img src="${pageContext.request.contextPath}/${course.coverImage}" alt="当前封面"></c:otherwise>
                        </c:choose>
                    </div>
                </c:if>
            </div>
            
            <div class="form-group"><label>课程简介</label><textarea name="description" class="form-control" placeholder="请输入课程简介">${course.description}</textarea></div>
            
            <c:if test="${not empty course}">
            <div class="form-group"><label>状态</label><select name="status" class="form-control"><option value="1" ${course.status == 1 ? 'selected' : ''}>开放选课</option><option value="0" ${course.status == 0 ? 'selected' : ''}>关闭选课</option></select></div>
            </c:if>
            
            <div style="margin-top:32px"><button type="submit" class="btn btn-primary">保存</button><a href="course?action=list" class="btn btn-secondary">返回</a></div>
        </form>
    </div>
    <script>
    function previewImage(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function(e) {
                document.getElementById('preview').src = e.target.result;
                document.getElementById('preview').style.display = 'block';
                document.getElementById('uploadHint').style.display = 'none';
            }
            reader.readAsDataURL(input.files[0]);
        }
    }
    </script>
</body>
</html>
