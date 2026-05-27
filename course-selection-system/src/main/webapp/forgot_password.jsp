<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>找回密码 - 在线课程选课系统</title>
    <style>
        *{margin:0;padding:0;box-sizing:border-box}
        body{font-family:'Helvetica Neue',Helvetica,Arial,'PingFang SC','Microsoft YaHei',sans-serif;min-height:100vh;display:flex;background:#fff}
        .left-panel{width:50%;background:#000;display:flex;flex-direction:column;justify-content:center;padding:80px}
        .left-panel h1{color:#fff;font-size:4rem;font-weight:700;line-height:1.1;letter-spacing:-2px}
        .left-panel h1 span{color:#E53935}
        .left-panel p{color:#666;font-size:1rem;margin-top:24px;max-width:400px;line-height:1.6}
        .right-panel{width:50%;display:flex;align-items:center;justify-content:center;padding:40px}
        .form-box{width:100%;max-width:360px}
        .form-box h2{font-size:1.5rem;font-weight:700;color:#000;margin-bottom:8px}
        .form-box .subtitle{color:#666;font-size:0.875rem;margin-bottom:40px}
        .role-tabs{display:flex;gap:0;margin-bottom:32px;border-bottom:2px solid #eee}
        .role-tabs input{display:none}
        .role-tabs label{flex:1;text-align:center;padding:12px 0;cursor:pointer;font-weight:500;font-size:0.875rem;color:#999;border-bottom:2px solid transparent;margin-bottom:-2px;transition:all 0.2s}
        .role-tabs input:checked+label{color:#000;border-bottom-color:#E53935}
        .form-group{margin-bottom:24px}
        .form-group label{display:block;margin-bottom:8px;color:#000;font-weight:500;font-size:0.75rem;text-transform:uppercase;letter-spacing:1px}
        .form-group input{width:100%;padding:14px 0;background:transparent;border:none;border-bottom:1px solid #ddd;font-size:1rem;color:#000;transition:border-color 0.2s}
        .form-group input:focus{outline:none;border-bottom-color:#000}
        .form-group input::placeholder{color:#999}
        .code-group{display:flex;gap:12px}
        .code-group input{flex:1}
        .code-btn{padding:14px 20px;background:#000;color:#fff;border:none;font-size:0.75rem;cursor:pointer;white-space:nowrap}
        .code-btn:hover{background:#E53935}
        .code-btn:disabled{background:#ccc;cursor:not-allowed}
        .submit-btn{width:100%;padding:16px;background:#000;color:#fff;border:none;font-size:0.875rem;font-weight:600;cursor:pointer;text-transform:uppercase;letter-spacing:2px;transition:background 0.2s;margin-top:16px}
        .submit-btn:hover{background:#E53935}
        .back-link{display:block;text-align:center;margin-top:24px;color:#666;font-size:0.875rem;text-decoration:none}
        .back-link:hover{color:#000}
        .toast{position:fixed;top:20px;right:20px;padding:16px 24px;border-radius:4px;font-size:0.875rem;z-index:1000;animation:slideIn 0.3s ease;display:none}
        .toast-success{background:#000;color:#fff}
        .toast-error{background:#E53935;color:#fff}
        @keyframes slideIn{from{transform:translateX(100%);opacity:0}to{transform:translateX(0);opacity:1}}
    </style>
</head>
<body>
    <div class="left-panel">
        <h1>找回<br>密码<span>.</span></h1>
        <p>通过邮箱验证码找回您的账号密码。请确保您的账号已绑定正确的邮箱地址。</p>
    </div>
    <div class="right-panel">
        <div class="form-box">
            <h2>找回密码</h2>
            <p class="subtitle">请选择身份并填写信息</p>
            <div id="toast" class="toast"></div>
            <form id="resetForm">
                <div class="role-tabs">
                    <input type="radio" name="role" value="student" id="r1" checked><label for="r1">学生</label>
                    <input type="radio" name="role" value="teacher" id="r2"><label for="r2">教师</label>
                </div>
                <div class="form-group">
                    <label>账号</label>
                    <input type="text" id="username" placeholder="请输入学号/工号" required>
                </div>
                <div class="form-group">
                    <label>邮箱</label>
                    <input type="email" id="email" placeholder="请输入绑定的邮箱" required>
                </div>
                <div class="form-group">
                    <label>验证码</label>
                    <div class="code-group">
                        <input type="text" id="code" placeholder="请输入验证码" required>
                        <button type="button" class="code-btn" id="sendCodeBtn" onclick="sendCode()">发送验证码</button>
                    </div>
                </div>
                <div class="form-group">
                    <label>新密码</label>
                    <input type="password" id="newPassword" placeholder="请输入新密码" required>
                </div>
                <div class="form-group">
                    <label>确认密码</label>
                    <input type="password" id="confirmPassword" placeholder="请再次输入新密码" required>
                </div>
                <button type="submit" class="submit-btn">重置密码</button>
            </form>
            <a href="login.jsp" class="back-link">← 返回登录</a>
        </div>
    </div>
    <script>
    var countdown = 0;
    
    function showToast(message, type) {
        var toast = document.getElementById('toast');
        toast.textContent = message;
        toast.className = 'toast toast-' + type;
        toast.style.display = 'block';
        setTimeout(function() { toast.style.display = 'none'; }, 3000);
    }
    
    function sendCode() {
        var role = document.querySelector('input[name="role"]:checked').value;
        var username = document.getElementById('username').value;
        var email = document.getElementById('email').value;
        
        if (!username || !email) {
            showToast('请先填写账号和邮箱', 'error');
            return;
        }
        
        var btn = document.getElementById('sendCodeBtn');
        btn.disabled = true;
        btn.textContent = '发送中...';
        
        fetch('password', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'action=sendCode&role=' + role + '&username=' + encodeURIComponent(username) + '&email=' + encodeURIComponent(email)
        })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            if (data.success) {
                showToast('验证码已发送到您的邮箱，请查收', 'success');
                countdown = 60;
                updateCountdown();
            } else {
                showToast(data.message, 'error');
                btn.disabled = false;
                btn.textContent = '发送验证码';
            }
        })
        .catch(function() {
            showToast('发送失败，请稍后重试', 'error');
            btn.disabled = false;
            btn.textContent = '发送验证码';
        });
    }
    
    function updateCountdown() {
        var btn = document.getElementById('sendCodeBtn');
        if (countdown > 0) {
            btn.textContent = countdown + '秒后重发';
            countdown--;
            setTimeout(updateCountdown, 1000);
        } else {
            btn.disabled = false;
            btn.textContent = '发送验证码';
        }
    }
    
    document.getElementById('resetForm').onsubmit = function(e) {
        e.preventDefault();
        
        var role = document.querySelector('input[name="role"]:checked').value;
        var username = document.getElementById('username').value;
        var email = document.getElementById('email').value;
        var code = document.getElementById('code').value;
        var newPassword = document.getElementById('newPassword').value;
        var confirmPassword = document.getElementById('confirmPassword').value;
        
        if (newPassword !== confirmPassword) {
            showToast('两次输入的密码不一致', 'error');
            return;
        }
        if (newPassword.length < 6) {
            showToast('密码长度不能少于6位', 'error');
            return;
        }
        
        fetch('password', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'action=resetPwd&role=' + role + '&username=' + encodeURIComponent(username) + 
                  '&email=' + encodeURIComponent(email) + '&code=' + encodeURIComponent(code) + 
                  '&newPassword=' + encodeURIComponent(newPassword)
        })
        .then(function(r) { return r.json(); })
        .then(function(data) {
            if (data.success) {
                showToast('密码重置成功，即将跳转登录页', 'success');
                setTimeout(function() { window.location.href = 'login.jsp'; }, 2000);
            } else {
                showToast(data.message, 'error');
            }
        })
        .catch(function() {
            showToast('操作失败，请稍后重试', 'error');
        });
    };
    </script>
</body>
</html>
