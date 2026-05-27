package com.course.servlet;

import com.course.dao.ScoreDao;
import com.course.entity.Score;
import com.course.entity.Student;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/student/score")
public class StudentScoreServlet extends HttpServlet {
    private ScoreDao scoreDao = new ScoreDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Student student = (Student) req.getSession().getAttribute("user");
        if (student == null) { resp.sendRedirect("../login.jsp"); return; }
        
        List<Score> scores = scoreDao.findByStudentId(student.getId());
        double totalCredit = 0, totalGradePoint = 0;
        int passCount = 0, failCount = 0;
        for (Score s : scores) {
            if (s.getScore() != null && s.getCredit() != null) {
                totalCredit += s.getCredit();
                totalGradePoint += s.getGradePoint() * s.getCredit();
                if (s.getScore() >= 60) passCount++;
                else failCount++;
            }
        }
        double gpa = totalCredit > 0 ? totalGradePoint / totalCredit : 0;
        
        req.setAttribute("scores", scores);
        req.setAttribute("gpa", String.format("%.2f", gpa));
        req.setAttribute("totalCredit", totalCredit);
        req.setAttribute("passCount", passCount);
        req.setAttribute("failCount", failCount);
        req.getRequestDispatcher("score.jsp").forward(req, resp);
    }
}
