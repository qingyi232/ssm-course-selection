package com.course.servlet;

import com.course.dao.AdminDao;
import com.course.dao.StudentDao;
import com.course.dao.TeacherDao;
import com.course.entity.Admin;
import com.course.entity.Student;
import com.course.entity.Teacher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private StudentDao studentDao = new StudentDao();
    private TeacherDao teacherDao = new TeacherDao();
    private AdminDao adminDao = new AdminDao();
    
    // 登录失败记录：key = username + "_" + role, value = [失败次数, 首次失败时间]
    private static Map<String, long[]> loginFailures = new ConcurrentHashMap<>();
    private static final int MAX_FAILURES = 5;           // 最大失败次数
    private static final long LOCK_TIME = 30 * 60 * 1000; // 锁定时间30分钟

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String role = req.getParameter("role");
        HttpSession session = req.getSession();
        
        String key = username + "_" + role;
        
        // 检查是否被锁定
        if (isLocked(key)) {
            long remaining = getRemainingLockTime(key);
            int minutes = (int) Math.ceil(remaining / 60000.0);
            req.setAttribute("error", "账号已被锁定，请" + minutes + "分钟后再试");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            return;
        }
        
        boolean loginSuccess = false;
        
        if ("student".equals(role)) {
            Student student = studentDao.login(username, password);
            if (student != null) {
                session.setAttribute("user", student);
                session.setAttribute("role", "student");
                loginSuccess = true;
                clearFailures(key);
                resp.sendRedirect("student/index");
                return;
            }
        } else if ("teacher".equals(role)) {
            Teacher teacher = teacherDao.login(username, password);
            if (teacher != null) {
                session.setAttribute("user", teacher);
                session.setAttribute("role", "teacher");
                loginSuccess = true;
                clearFailures(key);
                resp.sendRedirect("teacher/index");
                return;
            }
        } else if ("admin".equals(role)) {
            Admin admin = adminDao.login(username, password);
            if (admin != null) {
                session.setAttribute("user", admin);
                session.setAttribute("role", "admin");
                loginSuccess = true;
                clearFailures(key);
                resp.sendRedirect("admin/dashboard");
                return;
            }
        }
        
        if (!loginSuccess) {
            int failures = recordFailure(key);
            int remaining = MAX_FAILURES - failures;
            if (remaining > 0) {
                req.setAttribute("error", "用户名或密码错误，还剩" + remaining + "次机会");
            } else {
                req.setAttribute("error", "登录失败次数过多，账号已被锁定30分钟");
            }
        }
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }
    
    // 检查账号是否被锁定
    private boolean isLocked(String key) {
        long[] record = loginFailures.get(key);
        if (record == null) return false;
        if (record[0] >= MAX_FAILURES) {
            // 检查锁定是否过期
            if (System.currentTimeMillis() - record[1] < LOCK_TIME) {
                return true;
            } else {
                // 锁定已过期，清除记录
                loginFailures.remove(key);
                return false;
            }
        }
        return false;
    }
    
    // 获取剩余锁定时间（毫秒）
    private long getRemainingLockTime(String key) {
        long[] record = loginFailures.get(key);
        if (record == null) return 0;
        return LOCK_TIME - (System.currentTimeMillis() - record[1]);
    }
    
    // 记录登录失败，返回当前失败次数
    private int recordFailure(String key) {
        long[] record = loginFailures.get(key);
        long now = System.currentTimeMillis();
        if (record == null) {
            loginFailures.put(key, new long[]{1, now});
            return 1;
        } else {
            // 如果距离首次失败超过30分钟，重新计数
            if (now - record[1] >= LOCK_TIME) {
                loginFailures.put(key, new long[]{1, now});
                return 1;
            }
            record[0]++;
            return (int) record[0];
        }
    }
    
    // 清除失败记录（登录成功时调用）
    private void clearFailures(String key) {
        loginFailures.remove(key);
    }
}
