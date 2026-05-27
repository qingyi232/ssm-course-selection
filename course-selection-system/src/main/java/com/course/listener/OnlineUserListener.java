package com.course.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;

@WebListener
public class OnlineUserListener implements HttpSessionListener, HttpSessionAttributeListener {
    
    private static int onlineCount = 0;
    private static int studentOnline = 0;
    private static int teacherOnline = 0;
    private static int adminOnline = 0;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        onlineCount++;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        onlineCount--;
        if (onlineCount < 0) onlineCount = 0;
        
        HttpSession session = se.getSession();
        String role = (String) session.getAttribute("role");
        if ("student".equals(role)) {
            studentOnline--;
            if (studentOnline < 0) studentOnline = 0;
        } else if ("teacher".equals(role)) {
            teacherOnline--;
            if (teacherOnline < 0) teacherOnline = 0;
        } else if ("admin".equals(role)) {
            adminOnline--;
            if (adminOnline < 0) adminOnline = 0;
        }
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        if ("role".equals(event.getName())) {
            String role = (String) event.getValue();
            if ("student".equals(role)) studentOnline++;
            else if ("teacher".equals(role)) teacherOnline++;
            else if ("admin".equals(role)) adminOnline++;
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        if ("role".equals(event.getName())) {
            String role = (String) event.getValue();
            if ("student".equals(role)) {
                studentOnline--;
                if (studentOnline < 0) studentOnline = 0;
            } else if ("teacher".equals(role)) {
                teacherOnline--;
                if (teacherOnline < 0) teacherOnline = 0;
            } else if ("admin".equals(role)) {
                adminOnline--;
                if (adminOnline < 0) adminOnline = 0;
            }
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {}

    public static int getOnlineCount() { return onlineCount; }
    public static int getStudentOnline() { return studentOnline; }
    public static int getTeacherOnline() { return teacherOnline; }
    public static int getAdminOnline() { return adminOnline; }
}
