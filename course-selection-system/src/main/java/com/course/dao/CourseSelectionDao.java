package com.course.dao;

import com.course.entity.CourseSelection;
import com.course.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseSelectionDao {
    
    public boolean exists(Integer studentId, Integer courseId) {
        String sql = "SELECT COUNT(*) FROM course_selection WHERE student_id = ? AND course_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return false;
    }
    
    public List<CourseSelection> findByStudentId(Integer studentId) {
        String sql = "SELECT cs.*, s.student_no, s.name as student_name, c.course_no, c.name as course_name, " +
                     "c.credit, c.cover_image, t.name as teacher_name, c.schedule, c.location " +
                     "FROM course_selection cs " +
                     "JOIN student s ON cs.student_id = s.id " +
                     "JOIN course c ON cs.course_id = c.id " +
                     "LEFT JOIN teacher t ON c.teacher_id = t.id " +
                     "WHERE cs.student_id = ? ORDER BY cs.select_time DESC";
        List<CourseSelection> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractCourseSelection(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }
    
    public List<CourseSelection> findByCourseId(Integer courseId) {
        String sql = "SELECT cs.*, s.student_no, s.name as student_name, ci.name as class_name, " +
                     "c.course_no, c.name as course_name, c.credit, t.name as teacher_name, c.schedule, c.location " +
                     "FROM course_selection cs " +
                     "JOIN student s ON cs.student_id = s.id " +
                     "LEFT JOIN class_info ci ON s.class_id = ci.id " +
                     "JOIN course c ON cs.course_id = c.id " +
                     "LEFT JOIN teacher t ON c.teacher_id = t.id " +
                     "WHERE cs.course_id = ? ORDER BY cs.select_time";
        List<CourseSelection> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractCourseSelection(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }
    
    public boolean insert(Integer studentId, Integer courseId) {
        String sql = "INSERT INTO course_selection (student_id, course_id) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
        return false;
    }
    
    public boolean delete(Integer studentId, Integer courseId) {
        String sql = "DELETE FROM course_selection WHERE student_id = ? AND course_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
        return false;
    }
    
    public boolean deleteByStudentId(Integer studentId) {
        String sql = "DELETE FROM course_selection WHERE student_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            return stmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
        return false;
    }
    
    public boolean deleteByCourseId(Integer courseId) {
        String sql = "DELETE FROM course_selection WHERE course_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseId);
            return stmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
        return false;
    }
    
    public int count() {
        String sql = "SELECT COUNT(*) FROM course_selection";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return 0;
    }
    
    public int countByStudentId(Integer studentId) {
        String sql = "SELECT COUNT(*) FROM course_selection WHERE student_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return 0;
    }
    
    private CourseSelection extractCourseSelection(ResultSet rs) throws SQLException {
        CourseSelection cs = new CourseSelection();
        cs.setId(rs.getInt("id"));
        cs.setStudentId(rs.getInt("student_id"));
        cs.setCourseId(rs.getInt("course_id"));
        cs.setSelectTime(rs.getTimestamp("select_time"));
        cs.setStatus(rs.getInt("status"));
        // 安全地读取扩展字段
        try { cs.setStudentNo(rs.getString("student_no")); } catch (SQLException ignored) {}
        try { cs.setStudentName(rs.getString("student_name")); } catch (SQLException ignored) {}
        try { cs.setClassName(rs.getString("class_name")); } catch (SQLException ignored) {}
        try { cs.setCourseNo(rs.getString("course_no")); } catch (SQLException ignored) {}
        try { cs.setCourseName(rs.getString("course_name")); } catch (SQLException ignored) {}
        try { cs.setCredit(rs.getDouble("credit")); } catch (SQLException ignored) {}
        try { cs.setCoverImage(rs.getString("cover_image")); } catch (SQLException ignored) {}
        try { cs.setTeacherName(rs.getString("teacher_name")); } catch (SQLException ignored) {}
        try { cs.setSchedule(rs.getString("schedule")); } catch (SQLException ignored) {}
        try { cs.setLocation(rs.getString("location")); } catch (SQLException ignored) {}
        return cs;
    }
}
