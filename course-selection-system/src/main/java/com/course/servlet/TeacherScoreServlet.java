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

@WebServlet("/teacher/score")
@MultipartConfig
public class TeacherScoreServlet extends HttpServlet {
    private CourseDao courseDao = new CourseDao();
    private CourseSelectionDao selectionDao = new CourseSelectionDao();
    private ScoreDao scoreDao = new ScoreDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null) action = "list";
        Teacher teacher = (Teacher) req.getSession().getAttribute("user");
        if (teacher == null) { resp.sendRedirect("../login.jsp"); return; }
        
        switch (action) {
            case "list": listCourses(req, resp, teacher); break;
            case "manage": manageScores(req, resp); break;
            case "export": exportScores(req, resp); break;
            case "template": downloadTemplate(req, resp); break;
            default: listCourses(req, resp, teacher);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if ("save".equals(action)) { saveScores(req, resp); }
        else if ("import".equals(action)) { importScores(req, resp); }
        else { resp.sendRedirect("score?action=list"); }
    }

    private void listCourses(HttpServletRequest req, HttpServletResponse resp, Teacher teacher) throws ServletException, IOException {
        List<Course> courses = courseDao.findByTeacherId(teacher.getId());
        req.setAttribute("courses", courses);
        req.getRequestDispatcher("score_courses.jsp").forward(req, resp);
    }

    private void manageScores(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int courseId = Integer.parseInt(req.getParameter("courseId"));
        Course course = courseDao.findById(courseId);
        List<CourseSelection> selections = selectionDao.findByCourseId(courseId);
        List<Score> scores = scoreDao.findByCourseId(courseId);
        
        req.setAttribute("course", course);
        req.setAttribute("selections", selections);
        req.setAttribute("scores", scores);
        req.getRequestDispatcher("score_manage.jsp").forward(req, resp);
    }

    private void saveScores(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int courseId = Integer.parseInt(req.getParameter("courseId"));
        String[] studentIds = req.getParameterValues("studentId");
        String[] scoreValues = req.getParameterValues("score");
        
        if (studentIds != null && scoreValues != null) {
            for (int i = 0; i < studentIds.length; i++) {
                if (scoreValues[i] != null && !scoreValues[i].trim().isEmpty()) {
                    Score score = new Score();
                    score.setStudentId(Integer.parseInt(studentIds[i]));
                    score.setCourseId(courseId);
                    score.setScore(Double.parseDouble(scoreValues[i]));
                    scoreDao.insertOrUpdate(score);
                }
            }
        }
        resp.sendRedirect("score?action=manage&courseId=" + courseId + "&msg=success");
    }
    
    // 导出成绩Excel
    private void exportScores(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int courseId = Integer.parseInt(req.getParameter("courseId"));
        Course course = courseDao.findById(courseId);
        List<CourseSelection> selections = selectionDao.findByCourseId(courseId);
        List<Score> scores = scoreDao.findByCourseId(courseId);
        
        Map<Integer, Double> scoreMap = new HashMap<>();
        for (Score s : scores) {
            scoreMap.put(s.getStudentId(), s.getScore());
        }
        
        String[] headers = {"学号", "姓名", "班级", "成绩"};
        List<Object[]> data = new ArrayList<>();
        for (CourseSelection sel : selections) {
            Double score = scoreMap.get(sel.getStudentId());
            data.add(new Object[]{sel.getStudentNo(), sel.getStudentName(), 
                sel.getClassName(), score != null ? score : ""});
        }
        ExcelUtil.exportExcel(resp, course.getName() + "_成绩", headers, data);
    }
    
    // 下载成绩导入模板
    private void downloadTemplate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int courseId = Integer.parseInt(req.getParameter("courseId"));
        Course course = courseDao.findById(courseId);
        List<CourseSelection> selections = selectionDao.findByCourseId(courseId);
        
        String[] headers = {"学号", "姓名", "成绩"};
        List<Object[]> data = new ArrayList<>();
        for (CourseSelection sel : selections) {
            data.add(new Object[]{sel.getStudentNo(), sel.getStudentName(), ""});
        }
        ExcelUtil.exportExcel(resp, course.getName() + "_成绩模板", headers, data);
    }
    
    // 批量导入成绩
    private void importScores(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        int courseId = Integer.parseInt(req.getParameter("courseId"));
        Part filePart = req.getPart("file");
        
        if (filePart == null || filePart.getSize() == 0) {
            resp.sendRedirect("score?action=manage&courseId=" + courseId + "&msg=import_empty");
            return;
        }
        
        String fileName = filePart.getSubmittedFileName();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            resp.sendRedirect("score?action=manage&courseId=" + courseId + "&msg=import_format");
            return;
        }
        
        try {
            List<Map<String, String>> dataList = ExcelUtil.readExcel(filePart.getInputStream(), fileName);
            List<CourseSelection> selections = selectionDao.findByCourseId(courseId);
            Map<String, Integer> studentNoMap = new HashMap<>();
            for (CourseSelection sel : selections) {
                studentNoMap.put(sel.getStudentNo(), sel.getStudentId());
            }
            
            int success = 0, fail = 0;
            for (Map<String, String> row : dataList) {
                try {
                    String studentNo = row.get("学号");
                    String scoreStr = row.get("成绩");
                    if (studentNo != null && scoreStr != null && !scoreStr.isEmpty()) {
                        Integer studentId = studentNoMap.get(studentNo);
                        if (studentId != null) {
                            Score score = new Score();
                            score.setStudentId(studentId);
                            score.setCourseId(courseId);
                            score.setScore(Double.parseDouble(scoreStr));
                            scoreDao.insertOrUpdate(score);
                            success++;
                        } else {
                            fail++;
                        }
                    }
                } catch (Exception e) {
                    fail++;
                }
            }
            resp.sendRedirect("score?action=manage&courseId=" + courseId + "&msg=import_success&success=" + success + "&fail=" + fail);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("score?action=manage&courseId=" + courseId + "&msg=import_error");
        }
    }
}
