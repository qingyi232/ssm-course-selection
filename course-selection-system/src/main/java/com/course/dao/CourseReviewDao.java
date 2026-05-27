package com.course.dao;

import com.course.entity.CourseReview;
import com.course.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseReviewDao {
    
    public CourseReview findByStudentAndCourse(Integer studentId, Integer courseId) {
        String sql = "SELECT cr.*, s.name as student_name, c.name as course_name, c.course_no " +
                     "FROM course_review cr " +
                     "JOIN student s ON cr.student_id = s.id " +
                     "JOIN course c ON cr.course_id = c.id " +
                     "WHERE cr.student_id = ? AND cr.course_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            rs = stmt.executeQuery();
            if (rs.next()) return extract(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return null;
    }
    
    public List<CourseReview> findByCourseId(Integer courseId) {
        String sql = "SELECT cr.*, s.name as student_name, c.name as course_name, c.course_no " +
                     "FROM course_review cr " +
                     "JOIN student s ON cr.student_id = s.id " +
                     "JOIN course c ON cr.course_id = c.id " +
                     "WHERE cr.course_id = ? ORDER BY cr.create_time DESC";
        List<CourseReview> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseId);
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
    
    public List<CourseReview> findByStudentId(Integer studentId) {
        String sql = "SELECT cr.*, s.name as student_name, c.name as course_name, c.course_no " +
                     "FROM course_review cr " +
                     "JOIN student s ON cr.student_id = s.id " +
                     "JOIN course c ON cr.course_id = c.id " +
                     "WHERE cr.student_id = ? ORDER BY cr.create_time DESC";
        List<CourseReview> list = new ArrayList<>();
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
    
    public boolean insert(CourseReview review) {
        String sql = "INSERT INTO course_review (student_id, course_id, rating, content) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, review.getStudentId());
            stmt.setInt(2, review.getCourseId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getContent());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
        return false;
    }
    
    public boolean update(CourseReview review) {
        String sql = "UPDATE course_review SET rating = ?, content = ? WHERE student_id = ? AND course_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, review.getRating());
            stmt.setString(2, review.getContent());
            stmt.setInt(3, review.getStudentId());
            stmt.setInt(4, review.getCourseId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
        return false;
    }
    
    public Double getAvgRatingByCourseId(Integer courseId) {
        String sql = "SELECT AVG(rating) FROM course_review WHERE course_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseId);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return 0.0;
    }
    
    public int countByCourseId(Integer courseId) {
        String sql = "SELECT COUNT(*) FROM course_review WHERE course_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseId);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return 0;
    }
    
    private CourseReview extract(ResultSet rs) throws SQLException {
        CourseReview r = new CourseReview();
        r.setId(rs.getInt("id"));
        r.setStudentId(rs.getInt("student_id"));
        r.setCourseId(rs.getInt("course_id"));
        r.setRating(rs.getInt("rating"));
        r.setContent(rs.getString("content"));
        r.setCreateTime(rs.getTimestamp("create_time"));
        try {
            r.setStudentName(rs.getString("student_name"));
            r.setCourseName(rs.getString("course_name"));
            r.setCourseNo(rs.getString("course_no"));
        } catch (SQLException ignored) {}
        return r;
    }
}
