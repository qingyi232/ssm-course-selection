package com.course.servlet;

import com.course.dao.StudentDao;
import com.course.dao.CourseSelectionDao;
import com.course.entity.Student;
import com.course.entity.CourseSelection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/student/profile")
public class StudentProfileServlet extends HttpServlet {
    private StudentDao studentDao = new StudentDao();
    private CourseSelectionDao selectionDao = new CourseSelectionDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "view";
        
        switch (action) {
            case "view":
                viewProfile(request, response);
                break;
            case "schedule":
                viewSchedule(request, response);
                break;
            case "changePwd":
                request.getRequestDispatcher("/student/change_pwd.jsp").forward(request, response);
                break;
            default:
                viewProfile(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        
        switch (action) {
            case "update":
                updateProfile(request, response);
                break;
            case "changePwd":
                changePassword(request, response);
                break;
            default:
                response.sendRedirect("profile?action=view");
        }
    }
    
    private void viewProfile(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Student user = (Student) session.getAttribute("user");
        // 重新获取最新信息
        Student student = studentDao.findById(user.getId());
        request.setAttribute("student", student);
        request.getRequestDispatcher("/student/profile.jsp").forward(request, response);
    }
    
    private void updateProfile(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Student user = (Student) session.getAttribute("user");
        
        user.setPhone(request.getParameter("phone"));
        user.setEmail(request.getParameter("email"));
        
        if (studentDao.update(user)) {
            // 更新session中的用户信息
            Student updated = studentDao.findById(user.getId());
            session.setAttribute("user", updated);
            response.sendRedirect("profile?action=view&msg=success");
        } else {
            response.sendRedirect("profile?action=view&msg=error");
        }
    }
    
    private void changePassword(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Student user = (Student) session.getAttribute("user");
        
        String oldPwd = request.getParameter("oldPassword");
        String newPwd = request.getParameter("newPassword");
        String confirmPwd = request.getParameter("confirmPassword");
        
        // 验证旧密码
        if (!user.getPassword().equals(oldPwd)) {
            request.setAttribute("error", "原密码错误");
            request.getRequestDispatcher("/student/change_pwd.jsp").forward(request, response);
            return;
        }
        
        // 验证新密码
        if (!newPwd.equals(confirmPwd)) {
            request.setAttribute("error", "两次输入的新密码不一致");
            request.getRequestDispatcher("/student/change_pwd.jsp").forward(request, response);
            return;
        }
        
        if (studentDao.updatePassword(user.getId(), newPwd)) {
            user.setPassword(newPwd);
            session.setAttribute("user", user);
            response.sendRedirect("profile?action=view&msg=pwd_success");
        } else {
            request.setAttribute("error", "密码修改失败");
            request.getRequestDispatcher("/student/change_pwd.jsp").forward(request, response);
        }
    }
    
    private void viewSchedule(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Student user = (Student) session.getAttribute("user");
        
        List<CourseSelection> selections = selectionDao.findByStudentId(user.getId());
        request.setAttribute("selections", selections);
        request.getRequestDispatcher("/student/schedule.jsp").forward(request, response);
    }
}
