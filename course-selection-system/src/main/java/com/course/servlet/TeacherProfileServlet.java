package com.course.servlet;

import com.course.dao.TeacherDao;
import com.course.entity.Teacher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/teacher/profile")
public class TeacherProfileServlet extends HttpServlet {
    private TeacherDao teacherDao = new TeacherDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "view";
        
        switch (action) {
            case "view":
                viewProfile(request, response);
                break;
            case "changePwd":
                request.getRequestDispatcher("/teacher/change_pwd.jsp").forward(request, response);
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
        Teacher user = (Teacher) session.getAttribute("user");
        Teacher teacher = teacherDao.findById(user.getId());
        request.setAttribute("teacher", teacher);
        request.getRequestDispatcher("/teacher/profile.jsp").forward(request, response);
    }
    
    private void updateProfile(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Teacher user = (Teacher) session.getAttribute("user");
        
        user.setPhone(request.getParameter("phone"));
        user.setEmail(request.getParameter("email"));
        
        if (teacherDao.update(user)) {
            Teacher updated = teacherDao.findById(user.getId());
            session.setAttribute("user", updated);
            response.sendRedirect("profile?action=view&msg=success");
        } else {
            response.sendRedirect("profile?action=view&msg=error");
        }
    }
    
    private void changePassword(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Teacher user = (Teacher) session.getAttribute("user");
        
        String oldPwd = request.getParameter("oldPassword");
        String newPwd = request.getParameter("newPassword");
        String confirmPwd = request.getParameter("confirmPassword");
        
        if (!user.getPassword().equals(oldPwd)) {
            request.setAttribute("error", "原密码错误");
            request.getRequestDispatcher("/teacher/change_pwd.jsp").forward(request, response);
            return;
        }
        
        if (!newPwd.equals(confirmPwd)) {
            request.setAttribute("error", "两次输入的新密码不一致");
            request.getRequestDispatcher("/teacher/change_pwd.jsp").forward(request, response);
            return;
        }
        
        if (teacherDao.updatePassword(user.getId(), newPwd)) {
            user.setPassword(newPwd);
            session.setAttribute("user", user);
            response.sendRedirect("profile?action=view&msg=pwd_success");
        } else {
            request.setAttribute("error", "密码修改失败");
            request.getRequestDispatcher("/teacher/change_pwd.jsp").forward(request, response);
        }
    }
}
