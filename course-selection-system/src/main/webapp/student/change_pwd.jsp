<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>修改密码 - 在线课程选课系统</title>
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
        .container{max-width:400px;margin:0 auto;padding:48px 24px}
        .page-header{margin-bottom:32px;padding-bottom:16px;border-bottom:2px solid #000}
        .page-title{font-size:2rem;font-weight:700;letter-spacing:-1px}
        .error{padding:16px;margin-bottom:24px;font-size:0.875rem;border-left:3px solid #E53935;background:#fff;color:#E53935}
        .form-group{margin-bottom:24px}
        .form-group label{display:block;margin-bottom:8px;font-weight:500;font-size:0.75rem;text-transform:uppercase;letter-spacing:1px}
        .form-control{width:100%;padding:12px 0;background:transparent;border:none;border-bottom:1px solid #ddd;font-size:1rem}
        .form-control:focus{outline:none;border-bottom-color:#000}
        .btn{display:inline-block;padding:14px 28px;font-size:0.8rem;font-weight:600;text-transform:uppercase;letter-spacing:1px;cursor:pointer;border:1px solid;transition:all 0.2s;text-decoration:none;margin-right:12px}
        .btn-primary{background:#000;color:#fff;border-color:#000}
        .btn-primary:hover{background:#E53935;border-color:#E53935}
        .btn-secondary{background:transparent;color:#666;border-color:#ddd}
    </style>
</head>
<body>
    <div class="navbar">
        <h1>在线课程选课<span>系统</span></h1>
        <div class="navbar-menu">
            <a href="index">首页</a><a href="course?action=list">选课中心</a><a href="profile">个人信息</a>
            <span class="user-info">${sessionScope.user.name}</span><a href="../logout">退出</a>
        </div>
    </div>
    <div class="container">
        <div class="page-header"><h2 class="page-title">修改密码</h2></div>
        <% if(request.getAttribute("error") != null) { %><div class="error"><%= request.getAttribute("error") %></div><% } %>
        <form action="profile" method="post">
            <input type="hidden" name="action" value="changePwd">
            <div class="form-group"><label>原密码</label><input type="password" name="oldPassword" class="form-control" required placeholder="请输入原密码"></div>
            <div class="form-group"><label>新密码</label><input type="password" name="newPassword" class="form-control" required placeholder="请输入新密码"></div>
            <div class="form-group"><label>确认新密码</label><input type="password" name="confirmPassword" class="form-control" required placeholder="请再次输入新密码"></div>
            <div style="margin-top:32px"><button type="submit" class="btn btn-primary">确认修改</button><a href="profile" class="btn btn-secondary">返回</a></div>
        </form>
    </div>
</body>
</html>