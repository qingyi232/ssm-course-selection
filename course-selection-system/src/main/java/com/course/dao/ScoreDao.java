package com.course.dao;

import com.course.entity.Score;
import com.course.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDao {
    
    public Score findByStudentAndCourse(Integer studentId, Integer courseId) {
        String sql = "SELECT sc.*, s.student_no, s.name as student_name, c.course_no, c.name as course_name, c.credit " +
                     "FROM score sc " +
                     "JOIN student s ON sc.student_id = s.id " +
                     "JOIN course c ON sc.course_id = c.id " +
                     "WHERE sc.student_id = ? AND sc.course_id = ?";
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
                return extractScore(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return null;
    }
    
    public List<Score> findByStudentId(Integer studentId) {
        String sql = "SELECT sc.*, s.student_no, s.name as student_name, c.course_no, c.name as course_name, c.credit " +
                     "FROM score sc " +
                     "JOIN student s ON sc.student_id = s.id " +
                     "JOIN course c ON sc.course_id = c.id " +
                     "WHERE sc.student_id = ? ORDER BY sc.update_time DESC";
        List<Score> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractScore(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }
    
    public List<Score> findByCourseId(Integer courseId) {
        String sql = "SELECT sc.*, s.student_no, s.name as student_name, c.course_no, c.name as course_name, c.credit " +
                     "FROM score sc " +
                     "JOIN student s ON sc.student_id = s.id " +
                     "JOIN course c ON sc.course_id = c.id " +
                     "WHERE sc.course_id = ? ORDER BY s.student_no";
        List<Score> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractScore(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }
    
    public boolean insertOrUpdate(Score score) {
        Score existing = findByStudentAndCourse(score.getStudentId(), score.getCourseId());
        if (existing != null) {
            return update(score);
        } else {
            return insert(score);
        }
    }
    
    public boolean insert(Score score) {
        String sql = "INSERT INTO score (student_id, course_id, score, grade_point) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, score.getStudentId());
            stmt.setInt(2, score.getCourseId());
            stmt.setObject(3, score.getScore());
            stmt.setObject(4, calculateGradePoint(score.getScore()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
        return false;
    }
    
    public boolean update(Score score) {
        String sql = "UPDATE score SET score = ?, grade_point = ? WHERE student_id = ? AND course_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setObject(1, score.getScore());
            stmt.setObject(2, calculateGradePoint(score.getScore()));
            stmt.setInt(3, score.getStudentId());
            stmt.setInt(4, score.getCourseId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
        return false;
    }
    
    public boolean delete(Integer studentId, Integer courseId) {
        String sql = "DELETE FROM score WHERE student_id = ? AND course_id = ?";
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
    
    private Double calculateGradePoint(Double score) {
        if (score == null) return null;
        if (score >= 90) return 4.0;
        if (score >= 85) return 3.7;
        if (score >= 82) return 3.3;
        if (score >= 78) return 3.0;
        if (score >= 75) return 2.7;
        if (score >= 72) return 2.3;
        if (score >= 68) return 2.0;
        if (score >= 64) return 1.5;
        if (score >= 60) return 1.0;
        return 0.0;
    }
    
    private Score extractScore(ResultSet rs) throws SQLException {
        Score score = new Score();
        score.setId(rs.getInt("id"));
        score.setStudentId(rs.getInt("student_id"));
        score.setCourseId(rs.getInt("course_id"));
        score.setScore(rs.getDouble("score"));
        score.setGradePoint(rs.getDouble("grade_point"));
        score.setUpdateTime(rs.getTimestamp("update_time"));
        try {
            score.setStudentNo(rs.getString("student_no"));
            score.setStudentName(rs.getString("student_name"));
            score.setCourseNo(rs.getString("course_no"));
            score.setCourseName(rs.getString("course_name"));
            score.setCredit(rs.getDouble("credit"));
        } catch (SQLException ignored) {}
        return score;
    }
}
