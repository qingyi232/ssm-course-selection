package com.course.servlet;

import com.course.dao.*;
import com.course.entity.*;
import com.course.listener.OnlineUserListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/student/index")
public class StudentIndexServlet extends HttpServlet {
    private NoticeDao noticeDao = new NoticeDao();
    private CourseDao courseDao = new CourseDao();
    private CourseSelectionDao selectionDao = new CourseSelectionDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Student student = (Student) req.getSession().getAttribute("user");
        if (student == null) {
            resp.sendRedirect("../login.jsp");
            return;
        }
        
        // 获取已选课程数量
        int selectedCount = selectionDao.countByStudentId(student.getId());
        req.setAttribute("selectedCount", selectedCount);
        
        // 获取已选学分
        List<CourseSelection> selections = selectionDao.findByStudentId(student.getId());
        double totalCredit = 0;
        for (CourseSelection sel : selections) {
            if (sel.getCredit() != null) {
                totalCredit += sel.getCredit();
            }
        }
        req.setAttribute("totalCredit", (int) totalCredit);
        
        // 获取可选课程数量
        List<Course> availableCourses = courseDao.findAvailable();
        req.setAttribute("availableCourses", availableCourses.size());
        
        // 获取在线人数
        req.setAttribute("onlineCount", OnlineUserListener.getOnlineCount());
        
        // 获取公告
        req.setAttribute("notices", noticeDao.findAll());
        
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
