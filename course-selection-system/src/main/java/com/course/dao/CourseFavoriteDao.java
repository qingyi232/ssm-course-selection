package com.course.dao;

import com.course.entity.CourseFavorite;
import com.course.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseFavoriteDao {
    
    public boolean exists(Integer studentId, Integer courseId) {
        String sql = "SELECT COUNT(*) FROM course_favorite WHERE student_id = ? AND course_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return false;
    }
    
    public List<CourseFavorite> findByStudentId(Integer studentId) {
        String sql = "SELECT cf.*, c.name as course_name, c.course_no, c.credit, c.cover_image, " +
                     "c.schedule, c.location, c.max_students, c.current_students, t.name as teacher_name " +
                     "FROM course_favorite cf " +
                     "JOIN course c ON cf.course_id = c.id " +
                     "LEFT JOIN teacher t ON c.teacher_id = t.id " +
                     "WHERE cf.student_id = ? ORDER BY cf.create_time DESC";
        List<CourseFavorite> list = new ArrayList<>();
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
    
    public boolean insert(Integer studentId, Integer courseId) {
        String sql = "INSERT INTO course_favorite (student_id, course_id) VALUES (?, ?)";
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
        String sql = "DELETE FROM course_favorite WHERE student_id = ? AND course_id = ?";
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
    
    public int countByStudentId(Integer studentId) {
        String sql = "SELECT COUNT(*) FROM course_favorite WHERE student_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return 0;
    }
    
    private CourseFavorite extract(ResultSet rs) throws SQLException {
        CourseFavorite f = new CourseFavorite();
        f.setId(rs.getInt("id"));
        f.setStudentId(rs.getInt("student_id"));
        f.setCourseId(rs.getInt("course_id"));
        f.setCreateTime(rs.getTimestamp("create_time"));
        try {
            f.setCourseName(rs.getString("course_name"));
            f.setCourseNo(rs.getString("course_no"));
            f.setCredit(rs.getDouble("credit"));
            f.setTeacherName(rs.getString("teacher_name"));
            f.setCoverImage(rs.getString("cover_image"));
            f.setSchedule(rs.getString("schedule"));
            f.setLocation(rs.getString("location"));
            f.setMaxStudents(rs.getInt("max_students"));
            f.setCurrentStudents(rs.getInt("current_students"));
        } catch (SQLException ignored) {}
        return f;
    }
}
