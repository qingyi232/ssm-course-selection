package com.course.dao;

import com.course.entity.Teacher;
import com.course.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDao {
    
    public Teacher login(String teacherNo, String password) {
        String sql = "SELECT t.*, c.name as college_name FROM teacher t " +
                     "LEFT JOIN college c ON t.college_id = c.id " +
                     "WHERE t.teacher_no = ? AND t.password = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, teacherNo);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return extractTeacher(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return null;
    }
    
    public Teacher findById(Integer id) {
        String sql = "SELECT t.*, c.name as college_name FROM teacher t " +
                     "LEFT JOIN college c ON t.college_id = c.id WHERE t.id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return extractTeacher(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return null;
    }
    
    public List<Teacher> findAll() {
        String sql = "SELECT t.*, c.name as college_name FROM teacher t " +
                     "LEFT JOIN college c ON t.college_id = c.id ORDER BY t.teacher_no";
        List<Teacher> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractTeacher(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }
    
    public List<Teacher> search(String keyword) {
        String sql = "SELECT t.*, c.name as college_name FROM teacher t " +
                     "LEFT JOIN college c ON t.college_id = c.id " +
                     "WHERE t.teacher_no LIKE ? OR t.name LIKE ? ORDER BY t.teacher_no";
        List<Teacher> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractTeacher(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }
    
    public boolean insert(Teacher teacher) {
        String sql = "INSERT INTO teacher (teacher_no, password, name, gender, phone, email, college_id, title) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, teacher.getTeacherNo());
            stmt.setString(2, teacher.getPassword());
            stmt.setString(3, teacher.getName());
            stmt.setString(4, teacher.getGender());
            stmt.setString(5, teacher.getPhone());
            stmt.setString(6, teacher.getEmail());
            stmt.setObject(7, teacher.getCollegeId());
            stmt.setString(8, teacher.getTitle());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
        return false;
    }
    
    public boolean update(Teacher teacher) {
        String sql = "UPDATE teacher SET name = ?, gender = ?, phone = ?, email = ?, college_id = ?, title = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, teacher.getName());
            stmt.setString(2, teacher.getGender());
            stmt.setString(3, teacher.getPhone());
            stmt.setString(4, teacher.getEmail());
            stmt.setObject(5, teacher.getCollegeId());
            stmt.setString(6, teacher.getTitle());
            stmt.setInt(7, teacher.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
        return false;
    }
    
    public boolean delete(Integer id) {
        String sql = "DELETE FROM teacher WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
        return false;
    }
    
    public boolean updatePassword(Integer id, String newPassword) {
        String sql = "UPDATE teacher SET password = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, newPassword);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
        return false;
    }
    
    public int count() {
        String sql = "SELECT COUNT(*) FROM teacher";
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
    
    public Teacher findByTeacherNo(String teacherNo) {
        String sql = "SELECT t.*, c.name as college_name FROM teacher t " +
                     "LEFT JOIN college c ON t.college_id = c.id WHERE t.teacher_no = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, teacherNo);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return extractTeacher(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return null;
    }
    
    private Teacher extractTeacher(ResultSet rs) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(rs.getInt("id"));
        teacher.setTeacherNo(rs.getString("teacher_no"));
        teacher.setPassword(rs.getString("password"));
        teacher.setName(rs.getString("name"));
        teacher.setGender(rs.getString("gender"));
        teacher.setPhone(rs.getString("phone"));
        teacher.setEmail(rs.getString("email"));
        teacher.setCollegeId(rs.getInt("college_id"));
        teacher.setTitle(rs.getString("title"));
        teacher.setCreateTime(rs.getTimestamp("create_time"));
        try {
            teacher.setCollegeName(rs.getString("college_name"));
        } catch (SQLException ignored) {}
        return teacher;
    }
}
