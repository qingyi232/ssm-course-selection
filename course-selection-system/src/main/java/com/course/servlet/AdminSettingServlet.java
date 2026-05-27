package com.course.servlet;

import com.course.dao.SystemSettingDao;
import com.course.dao.CourseDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/setting")
public class AdminSettingServlet extends HttpServlet {
    private SystemSettingDao settingDao = new SystemSettingDao();
    private CourseDao courseDao = new CourseDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";
        
        switch (action) {
            case "list":
                listSettings(request, response);
                break;
            case "stats":
                showStats(request, response);
                break;
            default:
                listSettings(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        
        if ("save".equals(action)) {
            saveSettings(request, response);
        } else {
            response.sendRedirect("setting?action=list");
        }
    }
    
    private void listSettings(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String enabled = settingDao.getValue("selection_enabled");
        String startTime = settingDao.getValue("selection_start_time");
        String endTime = settingDao.getValue("selection_end_time");
        
        request.setAttribute("selectionEnabled", enabled);
        request.setAttribute("selectionStartTime", startTime);
        request.setAttribute("selectionEndTime", endTime);
        request.setAttribute("isOpen", settingDao.isSelectionOpen());
        
        request.getRequestDispatcher("/admin/setting.jsp").forward(request, response);
    }
    
    private void saveSettings(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String enabled = request.getParameter("selectionEnabled");
        String startTime = request.getParameter("selectionStartTime");
        String endTime = request.getParameter("selectionEndTime");
        
        settingDao.setValue("selection_enabled", enabled != null ? "1" : "0");
        if (startTime != null && !startTime.isEmpty()) {
            // datetime-local格式是 2026-01-03T00:00，转换为 2026-01-03 00:00:00
            settingDao.setValue("selection_start_time", startTime.replace("T", " ") + (startTime.length() == 16 ? ":00" : ""));
        }
        if (endTime != null && !endTime.isEmpty()) {
            settingDao.setValue("selection_end_time", endTime.replace("T", " ") + (endTime.length() == 16 ? ":00" : ""));
        }
        
        response.sendRedirect("setting?action=list&msg=success");
    }
    
    private void showStats(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Object[]> stats = courseDao.getSelectionStats();
        request.setAttribute("stats", stats);
        request.getRequestDispatcher("/admin/stats.jsp").forward(request, response);
    }
}
