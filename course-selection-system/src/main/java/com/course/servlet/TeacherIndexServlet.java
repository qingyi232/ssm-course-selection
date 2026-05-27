package com.course.servlet;

import com.course.dao.*;
import com.course.entity.*;
import com.course.listener.OnlineUserListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/teacher/index")
public class TeacherIndexServlet extends HttpServlet {
    private CourseDao courseDao = new CourseDao();
    private CourseSelectionDao selectionDao = new CourseSelectionDao();
    private NoticeDao noticeDao = new NoticeDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        
        if (teacher == null) {
            resp.sendRedirect("../login.jsp");
            return;
        }
        
        // 获取教师的课程列表
        List<Course> courses = courseDao.findByTeacherId(teacher.getId());
        
        // 统计数据
        int courseCount = courses.size();
        int totalStudents = 0;
        int totalCapacity = 0;
        
        for (Course course : courses) {
            totalStudents += course.getCurrentStudents();
            totalCapacity += course.getMaxStudents();
        }
        
        // 计算平均选课率
        double avgRate = totalCapacity > 0 ? (totalStudents * 100.0 / totalCapacity) : 0;
        
        req.setAttribute("courseCount", courseCount);
        req.setAttribute("totalStudents", totalStudents);
        req.setAttribute("avgRate", String.format("%.1f", avgRate));
        req.setAttribute("onlineCount", OnlineUserListener.getOnlineCount());
        req.setAttribute("courses", courses);
        
        // 获取最新公告
        List<Notice> notices = noticeDao.findAll();
        req.setAttribute("notices", notices);
        
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
