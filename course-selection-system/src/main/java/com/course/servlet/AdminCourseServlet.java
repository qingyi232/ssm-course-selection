package com.course.servlet;

import com.course.dao.*;
import com.course.entity.*;
import com.course.util.FileUploadUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/course")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class AdminCourseServlet extends HttpServlet {
    private CourseDao courseDao = new CourseDao();
    private CourseTypeDao typeDao = new CourseTypeDao();
    private TeacherDao teacherDao = new TeacherDao();
    private CourseSelectionDao selectionDao = new CourseSelectionDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null) action = "list";
        
        switch (action) {
            case "list": listCourses(req, resp); break;
            case "add": showAddForm(req, resp); break;
            case "edit": showEditForm(req, resp); break;
            default: listCourses(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        switch (action) {
            case "add": addCourse(req, resp); break;
            case "edit": updateCourse(req, resp); break;
            case "delete": deleteCourse(req, resp); break;
            default: resp.sendRedirect("course?action=list");
        }
    }

    private void listCourses(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        List<Course> courses;
        if (keyword != null && !keyword.trim().isEmpty()) {
            courses = courseDao.search(keyword);
        } else {
            courses = courseDao.findAll();
        }
        req.setAttribute("courses", courses);
        req.getRequestDispatcher("course_list.jsp").forward(req, resp);
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("types", typeDao.findAll());
        req.setAttribute("teachers", teacherDao.findAll());
        req.getRequestDispatcher("course_form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        req.setAttribute("course", courseDao.findById(id));
        req.setAttribute("types", typeDao.findAll());
        req.setAttribute("teachers", teacherDao.findAll());
        req.getRequestDispatcher("course_form.jsp").forward(req, resp);
    }

    private void addCourse(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Course course = new Course();
        course.setCourseNo(req.getParameter("courseNo"));
        course.setName(req.getParameter("name"));
        course.setCredit(Double.parseDouble(req.getParameter("credit")));
        course.setHours(Integer.parseInt(req.getParameter("hours")));
        course.setTypeId(Integer.parseInt(req.getParameter("typeId")));
        course.setTeacherId(Integer.parseInt(req.getParameter("teacherId")));
        course.setMaxStudents(Integer.parseInt(req.getParameter("maxStudents")));
        course.setLocation(req.getParameter("location"));
        course.setSchedule(req.getParameter("schedule"));
        course.setSemester(req.getParameter("semester"));
        course.setDescription(req.getParameter("description"));
        course.setStatus(1);
        
        // 处理封面图片上传
        Part filePart = req.getPart("coverImage");
        if (filePart != null && filePart.getSize() > 0) {
            String realPath = getServletContext().getRealPath("/");
            String coverPath = FileUploadUtil.uploadFile(filePart, realPath);
            course.setCoverImage(coverPath);
        }
        
        courseDao.insert(course);
        resp.sendRedirect("course?action=list");
    }

    private void updateCourse(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        int id = Integer.parseInt(req.getParameter("id"));
        Course oldCourse = courseDao.findById(id);
        
        Course course = new Course();
        course.setId(id);
        course.setName(req.getParameter("name"));
        course.setCredit(Double.parseDouble(req.getParameter("credit")));
        course.setHours(Integer.parseInt(req.getParameter("hours")));
        course.setTypeId(Integer.parseInt(req.getParameter("typeId")));
        course.setMaxStudents(Integer.parseInt(req.getParameter("maxStudents")));
        course.setLocation(req.getParameter("location"));
        course.setSchedule(req.getParameter("schedule"));
        course.setSemester(req.getParameter("semester"));
        course.setDescription(req.getParameter("description"));
        course.setStatus(Integer.parseInt(req.getParameter("status")));
        
        // 处理封面图片上传
        Part filePart = req.getPart("coverImage");
        if (filePart != null && filePart.getSize() > 0) {
            String realPath = getServletContext().getRealPath("/");
            // 删除旧图片
            if (oldCourse.getCoverImage() != null) {
                FileUploadUtil.deleteFile(oldCourse.getCoverImage(), realPath);
            }
            String coverPath = FileUploadUtil.uploadFile(filePart, realPath);
            course.setCoverImage(coverPath);
        } else {
            course.setCoverImage(oldCourse.getCoverImage());
        }
        
        courseDao.update(course);
        resp.sendRedirect("course?action=list");
    }

    private void deleteCourse(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Course course = courseDao.findById(id);
        
        // 删除封面图片
        if (course != null && course.getCoverImage() != null) {
            String realPath = getServletContext().getRealPath("/");
            FileUploadUtil.deleteFile(course.getCoverImage(), realPath);
        }
        
        selectionDao.deleteByCourseId(id);
        courseDao.delete(id);
        resp.sendRedirect("course?action=list");
    }
}
