<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录 - 在线课程选课系统</title>
    <style>
        *{margin:0;padding:0;box-sizing:border-box}
        body{font-family:'Helvetica Neue',Helvetica,Arial,'PingFang SC','Microsoft YaHei',sans-serif;min-height:100vh;display:flex;background:#fff}
        .left-panel{width:50%;background:#000;display:flex;flex-direction:column;justify-content:center;padding:80px}
        .left-panel h1{color:#fff;font-size:4rem;font-weight:700;line-height:1.1;letter-spacing:-2px}
        .left-panel h1 span{color:#E53935}
        .left-panel p{color:#666;font-size:1rem;margin-top:24px;max-width:400px;line-height:1.6}
        .right-panel{width:50%;display:flex;align-items:center;justify-content:center;padding:40px}
        .login-box{width:100%;max-width:360px}
        .login-box h2{font-size:1.5rem;font-weight:700;color:#000;margin-bottom:8px}
        .login-box .subtitle{color:#666;font-size:0.875rem;margin-bottom:40px}
        .role-tabs{display:flex;gap:0;margin-bottom:32px;border-bottom:2px solid #eee}
        .role-tabs input{display:none}
        .role-tabs label{flex:1;text-align:center;padding:12px 0;cursor:pointer;font-weight:500;font-size:0.875rem;color:#999;border-bottom:2px solid transparent;margin-bottom:-2px;transition:all 0.2s}
        .role-tabs input:checked+label{color:#000;border-bottom-color:#E53935}
        .form-group{margin-bottom:24px}
        .form-group label{display:block;margin-bottom:8px;color:#000;font-weight:500;font-size:0.75rem;text-transform:uppercase;letter-spacing:1px}
        .form-group input{width:100%;padding:14px 0;background:transparent;border:none;border-bottom:1px solid #ddd;font-size:1rem;color:#000;transition:border-color 0.2s}
        .form-group input:focus{outline:none;border-bottom-color:#000}
        .form-group input::placeholder{color:#999}
        .login-btn{width:100%;padding:16px;background:#000;color:#fff;border:none;font-size:0.875rem;font-weight:600;cursor:pointer;text-transform:uppercase;letter-spacing:2px;transition:background 0.2s;margin-top:16px}
        .login-btn:hover{background:#E53935}
        .error-msg{background:#fff;color:#E53935;padding:12px 0;margin-bottom:20px;font-size:0.875rem;border-left:3px solid #E53935;padding-left:12px}
        .login-footer{text-align:center;margin-top:48px;color:#999;font-size:0.75rem;letter-spacing:1px}
    </style>
</head>
<body>
    <div class="left-panel">
        <h1>在线课程<br>选课<span>系统</span></h1>
        <p>Course Selection System — 基于严格的信息架构，为学生、教师、管理员提供高效的选课管理服务。</p>
    </div>
    <div class="right-panel">
        <div class="login-box">
            <h2>登录</h2>
            <p class="subtitle">请选择身份并输入账号密码</p>
            <% if(request.getAttribute("error") != null) { %>
                <div class="error-msg"><%= request.getAttribute("error") %></div>
            <% } %>
            <form action="login" method="post">
                <div class="role-tabs">
                    <input type="radio" name="role" value="student" id="r1" checked><label for="r1">学生</label>
                    <input type="radio" name="role" value="teacher" id="r2"><label for="r2">教师</label>
                    <input type="radio" name="role" value="admin" id="r3"><label for="r3">管理员</label>
                </div>
                <div class="form-group">
                    <label>账号</label>
                    <input type="text" name="username" placeholder="请输入账号" required>
                </div>
                <div class="form-group">
                    <label>密码</label>
                    <input type="password" name="password" placeholder="请输入密码" required>
                </div>
                <button type="submit" class="login-btn">登录</button>
                <a href="password" style="display:block;text-align:center;margin-top:16px;color:#666;font-size:0.8rem;text-decoration:none">忘记密码？</a>
            </form>
            <div class="login-footer">© 2025 COURSE SELECTION SYSTEM</div>
        </div>
    </div>
</body>
</html>