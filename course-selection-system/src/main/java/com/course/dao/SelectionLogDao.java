package com.course.dao;

import com.course.entity.SelectionLog;
import com.course.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SelectionLogDao {
    
    public List<SelectionLog> findByStudentId(Integer studentId) {
        String sql = "SELECT sl.*, c.name as course_name, c.course_no " +
                     "FROM selection_log sl " +
                     "JOIN course c ON sl.course_id = c.id " +
                     "WHERE sl.student_id = ? ORDER BY sl.create_time DESC";
        List<SelectionLog> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extract(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }
    
    public boolean insert(Integer studentId, Integer courseId, String actionType) {
        String sql = "INSERT INTO selection_log (student_id, course_id, action_type) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.setString(3, actionType);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
        return false;
    }
    
    private SelectionLog extract(ResultSet rs) throws SQLException {
        SelectionLog log = new SelectionLog();
        log.setId(rs.getInt("id"));
        log.setStudentId(rs.getInt("student_id"));
        log.setCourseId(rs.getInt("course_id"));
        log.setActionType(rs.getString("action_type"));
        log.setCreateTime(rs.getTimestamp("create_time"));
        try {
            log.setCourseName(rs.getString("course_name"));
            log.setCourseNo(rs.getString("course_no"));
        } catch (SQLException ignored) {}
        return log;
    }
}
