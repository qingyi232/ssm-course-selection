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

@WebServlet("/admin/student")
@MultipartConfig
public class AdminStudentServlet extends HttpServlet {
    private StudentDao studentDao = new StudentDao();
    private ClassInfoDao classDao = new ClassInfoDao();
    private CourseSelectionDao selectionDao = new CourseSelectionDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null) action = "list";
        
        switch (action) {
            case "list": listStudents(req, resp); break;
            case "add": showAddForm(req, resp); break;
            case "edit": showEditForm(req, resp); break;
            case "export": exportStudents(req, resp); break;
            case "template": downloadTemplate(req, resp); break;
            default: listStudents(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        switch (action) {
            case "add": addStudent(req, resp); break;
            case "edit": updateStudent(req, resp); break;
            case "delete": deleteStudent(req, resp); break;
            case "resetPwd": resetPassword(req, resp); break;
            case "import": importStudents(req, resp); break;
            default: resp.sendRedirect("student?action=list");
        }
    }

    private void listStudents(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        List<Student> students;
        if (keyword != null && !keyword.trim().isEmpty()) {
            students = studentDao.search(keyword);
        } else {
            students = studentDao.findAll();
        }
        req.setAttribute("students", students);
        req.getRequestDispatcher("student_list.jsp").forward(req, resp);
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        java.util.List<com.course.entity.ClassInfo> classList = classDao.findAll();
        System.out.println("AdminStudentServlet.showAddForm - classList count: " + (classList != null ? classList.size() : "null"));
        req.setAttribute("classList", classList);
        req.getRequestDispatcher("student_form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        java.util.List<com.course.entity.ClassInfo> classList = classDao.findAll();
        System.out.println("AdminStudentServlet.showEditForm - classList count: " + (classList != null ? classList.size() : "null"));
        req.setAttribute("student", studentDao.findById(id));
        req.setAttribute("classList", classList);
        req.getRequestDispatcher("student_form.jsp").forward(req, resp);
    }

    private void addStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Student student = new Student();
        student.setStudentNo(req.getParameter("studentNo"));
        student.setPassword("123456");
        student.setName(req.getParameter("name"));
        student.setGender(req.getParameter("gender"));
        student.setPhone(req.getParameter("phone"));
        student.setEmail(req.getParameter("email"));
        String classId = req.getParameter("classId");
        if (classId != null && !classId.isEmpty()) student.setClassId(Integer.parseInt(classId));
        studentDao.insert(student);
        resp.sendRedirect("student?action=list");
    }

    private void updateStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Student student = new Student();
        student.setId(Integer.parseInt(req.getParameter("id")));
        student.setName(req.getParameter("name"));
        student.setGender(req.getParameter("gender"));
        student.setPhone(req.getParameter("phone"));
        student.setEmail(req.getParameter("email"));
        String classId = req.getParameter("classId");
        if (classId != null && !classId.isEmpty()) student.setClassId(Integer.parseInt(classId));
        studentDao.update(student);
        resp.sendRedirect("student?action=list");
    }

    private void deleteStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        selectionDao.deleteByStudentId(id);
        studentDao.delete(id);
        resp.sendRedirect("student?action=list");
    }

    private void resetPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        studentDao.updatePassword(id, "123456");
        resp.sendRedirect("student?action=list&msg=reset_success");
    }
    
    // 导出学生Excel
    private void exportStudents(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Student> students = studentDao.findAll();
        String[] headers = {"学号", "姓名", "性别", "班级", "电话", "邮箱"};
        List<Object[]> data = new ArrayList<>();
        for (Student s : students) {
            data.add(new Object[]{s.getStudentNo(), s.getName(), s.getGender(), 
                s.getClassName(), s.getPhone(), s.getEmail()});
        }
        ExcelUtil.exportExcel(resp, "学生信息", headers, data);
    }
    
    // 下载导入模板
    private void downloadTemplate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] headers = {"学号", "姓名", "性别", "电话", "邮箱", "班级ID"};
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{"22730101", "张三", "男", "13800138000", "zhangsan@example.com", "1"});
        ExcelUtil.exportExcel(resp, "学生导入模板", headers, data);
    }
    
    // 批量导入学生
    private void importStudents(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Part filePart = req.getPart("file");
        if (filePart == null || filePart.getSize() == 0) {
            resp.sendRedirect("student?action=list&msg=import_empty");
            return;
        }
        
        String fileName = filePart.getSubmittedFileName();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            resp.sendRedirect("student?action=list&msg=import_format");
            return;
        }
        
        try {
            List<Map<String, String>> dataList = ExcelUtil.readExcel(filePart.getInputStream(), fileName);
            int success = 0, fail = 0;
            for (Map<String, String> row : dataList) {
                try {
                    Student student = new Student();
                    student.setStudentNo(row.get("学号"));
                    student.setName(row.get("姓名"));
                    student.setGender(row.get("性别"));
                    student.setPhone(row.get("电话"));
                    student.setEmail(row.get("邮箱"));
                    student.setPassword("123456");
                    String classId = row.get("班级ID");
                    if (classId != null && !classId.isEmpty()) {
                        student.setClassId(Integer.parseInt(classId));
                    }
                    if (student.getStudentNo() != null && !student.getStudentNo().isEmpty()) {
                        studentDao.insert(student);
                        success++;
                    }
                } catch (Exception e) {
                    fail++;
                }
            }
            resp.sendRedirect("student?action=list&msg=import_success&success=" + success + "&fail=" + fail);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("student?action=list&msg=import_error");
        }
    }
}
