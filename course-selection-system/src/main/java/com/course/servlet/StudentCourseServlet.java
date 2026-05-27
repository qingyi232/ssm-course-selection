package com.course.servlet;

import com.course.dao.*;
import com.course.entity.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/student/course")
public class StudentCourseServlet extends HttpServlet {
    private CourseDao courseDao = new CourseDao();
    private CourseSelectionDao selectionDao = new CourseSelectionDao();
    private SystemSettingDao settingDao = new SystemSettingDao();
    private CourseFavoriteDao favoriteDao = new CourseFavoriteDao();
    private SelectionLogDao logDao = new SelectionLogDao();
    private CourseReviewDao reviewDao = new CourseReviewDao();
    private CourseTypeDao typeDao = new CourseTypeDao();
    private TeacherDao teacherDao = new TeacherDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null) action = "list";
        
        Student student = (Student) req.getSession().getAttribute("user");
        if (student == null) { resp.sendRedirect("../login.jsp"); return; }
        
        switch (action) {
            case "list": listAvailableCourses(req, resp, student); break;
            case "selected": listSelectedCourses(req, resp, student); break;
            case "favorites": listFavorites(req, resp, student); break;
            case "logs": listLogs(req, resp, student); break;
            default: listAvailableCourses(req, resp, student);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        Student student = (Student) req.getSession().getAttribute("user");
        if (student == null) { resp.sendRedirect("../login.jsp"); return; }
        
        switch (action) {
            case "select": selectCourse(req, resp, student); break;
            case "drop": dropCourse(req, resp, student); break;
            case "favorite": toggleFavorite(req, resp, student); break;
            case "review": submitReview(req, resp, student); break;
            default: resp.sendRedirect("course?action=list");
        }
    }

    private void listAvailableCourses(HttpServletRequest req, HttpServletResponse resp, Student student) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        String credit = req.getParameter("credit");
        String typeId = req.getParameter("typeId");
        String teacherId = req.getParameter("teacherId");
        
        List<Course> courses = courseDao.findWithFilters(keyword, credit, typeId, teacherId);
        List<CourseSelection> selected = selectionDao.findByStudentId(student.getId());
        List<CourseFavorite> favorites = favoriteDao.findByStudentId(student.getId());
        List<CourseType> courseTypes = typeDao.findAll();
        List<Teacher> teachers = teacherDao.findAll();
        
        req.setAttribute("courses", courses);
        req.setAttribute("selectedCourses", selected);
        req.setAttribute("favoriteCourses", favorites);
        req.setAttribute("courseTypes", courseTypes);
        req.setAttribute("teachers", teachers);
        req.setAttribute("selectionOpen", settingDao.isSelectionOpen());
        req.getRequestDispatcher("course_list.jsp").forward(req, resp);
    }

    private void listSelectedCourses(HttpServletRequest req, HttpServletResponse resp, Student student) throws ServletException, IOException {
        List<CourseSelection> selections = selectionDao.findByStudentId(student.getId());
        req.setAttribute("selections", selections);
        req.setAttribute("selectionOpen", settingDao.isSelectionOpen());
        req.getRequestDispatcher("my_courses.jsp").forward(req, resp);
    }

    private void selectCourse(HttpServletRequest req, HttpServletResponse resp, Student student) throws IOException {
        // 检查选课是否开放
        if (!settingDao.isSelectionOpen()) {
            resp.sendRedirect("course?action=list&msg=selection_closed");
            return;
        }
        
        int courseId = Integer.parseInt(req.getParameter("courseId"));
        Course course = courseDao.findById(courseId);
        
        if (course == null || course.getStatus() != 1) {
            resp.sendRedirect("course?action=list&msg=course_unavailable");
            return;
        }
        if (course.getCurrentStudents() >= course.getMaxStudents()) {
            resp.sendRedirect("course?action=list&msg=full");
            return;
        }
        if (selectionDao.exists(student.getId(), courseId)) {
            resp.sendRedirect("course?action=list&msg=selected");
            return;
        }
        // 检查时间冲突
        if (courseDao.hasTimeConflict(student.getId(), course.getSchedule())) {
            resp.sendRedirect("course?action=list&msg=conflict");
            return;
        }
        
        if (selectionDao.insert(student.getId(), courseId)) {
            courseDao.updateStudentCount(courseId, 1);
            logDao.insert(student.getId(), courseId, "SELECT");
            resp.sendRedirect("course?action=list&msg=success");
        } else {
            resp.sendRedirect("course?action=list&msg=error");
        }
    }

    private void dropCourse(HttpServletRequest req, HttpServletResponse resp, Student student) throws IOException {
        // 检查选课是否开放
        if (!settingDao.isSelectionOpen()) {
            resp.sendRedirect("course?action=selected&msg=selection_closed");
            return;
        }
        
        int courseId = Integer.parseInt(req.getParameter("courseId"));
        if (selectionDao.delete(student.getId(), courseId)) {
            courseDao.updateStudentCount(courseId, -1);
            logDao.insert(student.getId(), courseId, "DROP");
            resp.sendRedirect("course?action=selected&msg=drop_success");
        } else {
            resp.sendRedirect("course?action=selected&msg=error");
        }
    }
    
    private void listFavorites(HttpServletRequest req, HttpServletResponse resp, Student student) throws ServletException, IOException {
        List<CourseFavorite> favorites = favoriteDao.findByStudentId(student.getId());
        List<CourseSelection> selected = selectionDao.findByStudentId(student.getId());
        req.setAttribute("favorites", favorites);
        req.setAttribute("selectedCourses", selected);
        req.setAttribute("selectionOpen", settingDao.isSelectionOpen());
        req.getRequestDispatcher("favorites.jsp").forward(req, resp);
    }
    
    private void listLogs(HttpServletRequest req, HttpServletResponse resp, Student student) throws ServletException, IOException {
        List<SelectionLog> logs = logDao.findByStudentId(student.getId());
        req.setAttribute("logs", logs);
        req.getRequestDispatcher("selection_logs.jsp").forward(req, resp);
    }
    
    private void toggleFavorite(HttpServletRequest req, HttpServletResponse resp, Student student) throws IOException {
        int courseId = Integer.parseInt(req.getParameter("courseId"));
        String from = req.getParameter("from");
        if (from == null) from = "list";
        
        if (favoriteDao.exists(student.getId(), courseId)) {
            favoriteDao.delete(student.getId(), courseId);
        } else {
            favoriteDao.insert(student.getId(), courseId);
        }
        resp.sendRedirect("course?action=" + from);
    }
    
    private void submitReview(HttpServletRequest req, HttpServletResponse resp, Student student) throws IOException {
        int courseId = Integer.parseInt(req.getParameter("courseId"));
        int rating = Integer.parseInt(req.getParameter("rating"));
        String content = req.getParameter("content");
        
        CourseReview review = new CourseReview();
        review.setStudentId(student.getId());
        review.setCourseId(courseId);
        review.setRating(rating);
        review.setContent(content);
        
        CourseReview existing = reviewDao.findByStudentAndCourse(student.getId(), courseId);
        if (existing != null) {
            reviewDao.update(review);
        } else {
            reviewDao.insert(review);
        }
        resp.sendRedirect("course?action=selected&msg=review_success");
    }
}
