package com.course.servlet;

import com.course.dao.*;
import com.course.entity.*;
import com.course.util.EmailUtil;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/password")
public class PasswordServlet extends HttpServlet {
    private StudentDao studentDao = new StudentDao();
    private TeacherDao teacherDao = new TeacherDao();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        req.getRequestDispatcher("forgot_password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        
        String action = req.getParameter("action");
        Map<String, Object> result = new HashMap<>();
        
        try {
            switch (action) {
                case "sendCode": sendVerifyCode(req, result); break;
                case "resetPwd": resetPassword(req, result); break;
                default: result.put("success", false); result.put("message", "未知操作");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "操作失败：" + e.getMessage());
        }
        
        resp.getWriter().write(gson.toJson(result));
    }
    
    // 发送验证码
    private void sendVerifyCode(HttpServletRequest req, Map<String, Object> result) {
        String role = req.getParameter("role");
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        
        if (username == null || email == null || username.isEmpty() || email.isEmpty()) {
            result.put("success", false);
            result.put("message", "请填写完整信息");
            return;
        }
        
        // 验证用户和邮箱是否匹配
        String dbEmail = null;
        if ("student".equals(role)) {
            Student student = studentDao.findByStudentNo(username);
            if (student != null) dbEmail = student.getEmail();
        } else if ("teacher".equals(role)) {
            Teacher teacher = teacherDao.findByTeacherNo(username);
            if (teacher != null) dbEmail = teacher.getEmail();
        }
        
        if (dbEmail == null || !dbEmail.equals(email)) {
            result.put("success", false);
            result.put("message", "账号与邮箱不匹配");
            return;
        }
        
        // 生成并发送验证码
        String code = EmailUtil.generateCode();
        boolean sent = EmailUtil.sendVerifyCode(email, code);
        
        if (sent) {
            result.put("success", true);
            result.put("message", "验证码已发送到您的邮箱");
        } else {
            result.put("success", false);
            result.put("message", "验证码发送失败，请稍后重试");
        }
    }
    
    // 重置密码
    private void resetPassword(HttpServletRequest req, Map<String, Object> result) {
        String role = req.getParameter("role");
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String code = req.getParameter("code");
        String newPassword = req.getParameter("newPassword");
        
        if (username == null || email == null || code == null || newPassword == null) {
            result.put("success", false);
            result.put("message", "请填写完整信息");
            return;
        }
        
        // 验证验证码
        if (!EmailUtil.verifyCode(email, code)) {
            result.put("success", false);
            result.put("message", "验证码错误或已过期");
            return;
        }
        
        // 重置密码
        boolean success = false;
        if ("student".equals(role)) {
            Student student = studentDao.findByStudentNo(username);
            if (student != null) {
                success = studentDao.updatePassword(student.getId(), newPassword);
            }
        } else if ("teacher".equals(role)) {
            Teacher teacher = teacherDao.findByTeacherNo(username);
            if (teacher != null) {
                success = teacherDao.updatePassword(teacher.getId(), newPassword);
            }
        }
        
        if (success) {
            result.put("success", true);
            result.put("message", "密码重置成功");
        } else {
            result.put("success", false);
            result.put("message", "密码重置失败");
        }
    }
}
