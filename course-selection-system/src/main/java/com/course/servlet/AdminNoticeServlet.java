package com.course.servlet;

import com.course.dao.NoticeDao;
import com.course.entity.Admin;
import com.course.entity.Notice;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/notice")
public class AdminNoticeServlet extends HttpServlet {
    private NoticeDao noticeDao = new NoticeDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null) action = "list";
        
        switch (action) {
            case "list": req.setAttribute("notices", noticeDao.findAll()); req.getRequestDispatcher("notice_list.jsp").forward(req, resp); break;
            case "add": req.getRequestDispatcher("notice_form.jsp").forward(req, resp); break;
            case "edit": req.setAttribute("notice", noticeDao.findById(Integer.parseInt(req.getParameter("id")))); req.getRequestDispatcher("notice_form.jsp").forward(req, resp); break;
            default: resp.sendRedirect("notice?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        Admin admin = (Admin) req.getSession().getAttribute("user");
        
        if ("add".equals(action)) {
            Notice notice = new Notice();
            notice.setTitle(req.getParameter("title"));
            notice.setContent(req.getParameter("content"));
            notice.setPublisherId(admin.getId());
            notice.setStatus(1);
            noticeDao.insert(notice);
        } else if ("edit".equals(action)) {
            Notice notice = new Notice();
            notice.setId(Integer.parseInt(req.getParameter("id")));
            notice.setTitle(req.getParameter("title"));
            notice.setContent(req.getParameter("content"));
            notice.setStatus(Integer.parseInt(req.getParameter("status")));
            noticeDao.update(notice);
        } else if ("delete".equals(action)) {
            noticeDao.delete(Integer.parseInt(req.getParameter("id")));
        }
        resp.sendRedirect("notice?action=list");
    }
}
