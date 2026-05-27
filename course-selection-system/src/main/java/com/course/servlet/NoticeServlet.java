package com.course.servlet;

import com.course.dao.NoticeDao;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/notice")
public class NoticeServlet extends HttpServlet {
    private NoticeDao noticeDao = new NoticeDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("detail".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("notice", noticeDao.findById(id));
            req.getRequestDispatcher("notice_detail.jsp").forward(req, resp);
        } else {
            req.setAttribute("notices", noticeDao.findAll());
            req.getRequestDispatcher("notice_list.jsp").forward(req, resp);
        }
    }
}
