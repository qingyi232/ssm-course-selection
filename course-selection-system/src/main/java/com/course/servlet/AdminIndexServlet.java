package com.course.servlet;

import com.course.dao.*;
import com.course.listener.OnlineUserListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/dashboard")
public class AdminIndexServlet extends HttpServlet {
    private StudentDao studentDao = new StudentDao();
    private TeacherDao teacherDao = new TeacherDao();
    private CourseDao courseDao = new CourseDao();
    private CourseSelectionDao selectionDao = new CourseSelectionDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("studentCount", studentDao.count());
        req.setAttribute("teacherCount", teacherDao.count());
        req.setAttribute("courseCount", courseDao.count());
        req.setAttribute("selectionCount", selectionDao.count());
        
        // 在线用户统计
        req.setAttribute("onlineCount", OnlineUserListener.getOnlineCount());
        req.setAttribute("studentOnline", OnlineUserListener.getStudentOnline());
        req.setAttribute("teacherOnline", OnlineUserListener.getTeacherOnline());
        req.setAttribute("adminOnline", OnlineUserListener.getAdminOnline());
        
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
