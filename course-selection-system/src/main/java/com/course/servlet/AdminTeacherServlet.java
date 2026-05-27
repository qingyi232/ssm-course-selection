package com.course.servlet;

import com.course.dao.*;
import com.course.entity.*;
import com.course.util.ExcelUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/admin/teacher")
@MultipartConfig
public class AdminTeacherServlet extends HttpServlet {
    private TeacherDao teacherDao = new TeacherDao();
    private CollegeDao collegeDao = new CollegeDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null) action = "list";
        
        switch (action) {
            case "list": listTeachers(req, resp); break;
            case "add": showAddForm(req, resp); break;
            case "edit": showEditForm(req, resp); break;
            case "export": exportTeachers(req, resp); break;
            case "template": downloadTemplate(req, resp); break;
            default: listTeachers(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        switch (action) {
            case "add": addTeacher(req, resp); break;
            case "edit": updateTeacher(req, resp); break;
            case "delete": deleteTeacher(req, resp); break;
            case "resetPwd": resetPassword(req, resp); break;
            case "import": importTeachers(req, resp); break;
            default: resp.sendRedirect("teacher?action=list");
        }
    }

    private void listTeachers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        List<Teacher> teachers;
        if (keyword != null && !keyword.trim().isEmpty()) {
            teachers = teacherDao.search(keyword);
        } else {
            teachers = teacherDao.findAll();
        }
        req.setAttribute("teachers", teachers);
        req.getRequestDispatcher("teacher_list.jsp").forward(req, resp);
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("colleges", collegeDao.findAll());
        req.getRequestDispatcher("teacher_form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        req.setAttribute("teacher", teacherDao.findById(id));
        req.setAttribute("colleges", collegeDao.findAll());
        req.getRequestDispatcher("teacher_form.jsp").forward(req, resp);
    }

    private void addTeacher(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Teacher teacher = new Teacher();
        teacher.setTeacherNo(req.getParameter("teacherNo"));
        teacher.setPassword("123456");
        teacher.setName(req.getParameter("name"));
        teacher.setGender(req.getParameter("gender"));
        teacher.setPhone(req.getParameter("phone"));
        teacher.setEmail(req.getParameter("email"));
        teacher.setTitle(req.getParameter("title"));
        String collegeId = req.getParameter("collegeId");
        if (collegeId != null && !collegeId.isEmpty()) teacher.setCollegeId(Integer.parseInt(collegeId));
        teacherDao.insert(teacher);
        resp.sendRedirect("teacher?action=list");
    }

    private void updateTeacher(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Teacher teacher = new Teacher();
        teacher.setId(Integer.parseInt(req.getParameter("id")));
        teacher.setName(req.getParameter("name"));
        teacher.setGender(req.getParameter("gender"));
        teacher.setPhone(req.getParameter("phone"));
        teacher.setEmail(req.getParameter("email"));
        teacher.setTitle(req.getParameter("title"));
        String collegeId = req.getParameter("collegeId");
        if (collegeId != null && !collegeId.isEmpty()) teacher.setCollegeId(Integer.parseInt(collegeId));
        teacherDao.update(teacher);
        resp.sendRedirect("teacher?action=list");
    }

    private void deleteTeacher(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        teacherDao.delete(id);
        resp.sendRedirect("teacher?action=list");
    }

    private void resetPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        teacherDao.updatePassword(id, "123456");
        resp.sendRedirect("teacher?action=list&msg=reset_success");
    }
    
    // 导出教师Excel
    private void exportTeachers(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Teacher> teachers = teacherDao.findAll();
        String[] headers = {"工号", "姓名", "性别", "学院", "职称", "电话", "邮箱"};
        List<Object[]> data = new ArrayList<>();
        for (Teacher t : teachers) {
            data.add(new Object[]{t.getTeacherNo(), t.getName(), t.getGender(), 
                t.getCollegeName(), t.getTitle(), t.getPhone(), t.getEmail()});
        }
        ExcelUtil.exportExcel(resp, "教师信息", headers, data);
    }
    
    // 下载导入模板
    private void downloadTemplate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] headers = {"工号", "姓名", "性别", "电话", "邮箱", "职称", "学院ID"};
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{"T001", "李老师", "男", "13800138000", "li@example.com", "副教授", "1"});
        ExcelUtil.exportExcel(resp, "教师导入模板", headers, data);
    }
    
    // 批量导入教师
    private void importTeachers(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Part filePart = req.getPart("file");
        if (filePart == null || filePart.getSize() == 0) {
            resp.sendRedirect("teacher?action=list&msg=import_empty");
            return;
        }
        
        String fileName = filePart.getSubmittedFileName();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            resp.sendRedirect("teacher?action=list&msg=import_format");
            return;
        }
        
        try {
            List<Map<String, String>> dataList = ExcelUtil.readExcel(filePart.getInputStream(), fileName);
            int success = 0, fail = 0;
            for (Map<String, String> row : dataList) {
                try {
                    Teacher teacher = new Teacher();
                    teacher.setTeacherNo(row.get("工号"));
                    teacher.setName(row.get("姓名"));
                    teacher.setGender(row.get("性别"));
                    teacher.setPhone(row.get("电话"));
                    teacher.setEmail(row.get("邮箱"));
                    teacher.setTitle(row.get("职称"));
                    teacher.setPassword("123456");
                    String collegeId = row.get("学院ID");
                    if (collegeId != null && !collegeId.isEmpty()) {
                        teacher.setCollegeId(Integer.parseInt(collegeId));
                    }
                    if (teacher.getTeacherNo() != null && !teacher.getTeacherNo().isEmpty()) {
                        teacherDao.insert(teacher);
                        success++;
                    }
                } catch (Exception e) {
                    fail++;
                }
            }
            resp.sendRedirect("teacher?action=list&msg=import_success&success=" + success + "&fail=" + fail);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("teacher?action=list&msg=import_error");
        }
    }
}
