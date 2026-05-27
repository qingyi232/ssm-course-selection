package com.course.dao;

import com.course.entity.Student;
import com.course.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {
    
    public Student login(String studentNo, String password) {
        String sql = "SELECT s.*, c.name as class_name, m.name as major_name, co.name as college_name " +
                     "FROM student s " +
                     "LEFT JOIN class_info c ON s.class_id = c.id " +
                     "LEFT JOIN major m ON c.major_id = m.id " +
                     "LEFT JOIN college co ON m.college_id = co.id " +
                     "WHERE s.student_no = ? AND s.password = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, studentNo);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return extractStudent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return null;
    }
    
    public Student findById(Integer id) {
        String sql = "SELECT s.*, c.name as class_name, m.name as major_name, co.name as college_name " +
                     "FROM student s " +
                     "LEFT JOIN class_info c ON s.class_id = c.id " +
                     "LEFT JOIN major m ON c.major_id = m.id " +
                     "LEFT JOIN college co ON m.college_id = co.id " +
                     "WHERE s.id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return extractStudent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return null;
    }
    
    public List<Student> findAll() {
        String sql = "SELECT s.*, c.name as class_name, m.name as major_name, co.name as college_name " +
                     "FROM student s " +
                     "LEFT JOIN class_info c ON s.class_id = c.id " +
                     "LEFT JOIN major m ON c.major_id = m.id " +
                     "LEFT JOIN college co ON m.college_id = co.id " +
                     "ORDER BY s.student_no";
        List<Student> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }
    
    public List<Student> search(String keyword) {
        String sql = "SELECT s.*, c.name as class_name, m.name as major_name, co.name as college_name " +
                     "FROM student s " +
                     "LEFT JOIN class_info c ON s.class_id = c.id " +
                     "LEFT JOIN major m ON c.major_id = m.id " +
                     "LEFT JOIN college co ON m.college_id = co.id " +
                     "WHERE s.student_no LIKE ? OR s.name LIKE ? " +
                     "ORDER BY s.student_no";
        List<Student> list = new ArrayList<>();
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
                list.add(extractStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }
    
    public boolean insert(Student student) {
        String sql = "INSERT INTO student (student_no, password, name, gender, phone, email, class_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, student.getStudentNo());
            stmt.setString(2, student.getPassword());
            stmt.setString(3, student.getName());
            stmt.setString(4, student.getGender());
            stmt.setString(5, student.getPhone());
            stmt.setString(6, student.getEmail());
            stmt.setObject(7, student.getClassId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
        return false;
    }
    
    public boolean update(Student student) {
        String sql = "UPDATE student SET name = ?, gender = ?, phone = ?, email = ?, class_id = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getGender());
            stmt.setString(3, student.getPhone());
            stmt.setString(4, student.getEmail());
            stmt.setObject(5, student.getClassId());
            stmt.setInt(6, student.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
        return false;
    }
    
    public boolean delete(Integer id) {
        String sql = "DELETE FROM student WHERE id = ?";
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
        String sql = "UPDATE student SET password = ? WHERE id = ?";
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
        String sql = "SELECT COUNT(*) FROM student";
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
    
    public Student findByStudentNo(String studentNo) {
        String sql = "SELECT s.*, c.name as class_name FROM student s " +
                     "LEFT JOIN class_info c ON s.class_id = c.id WHERE s.student_no = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, studentNo);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return extractStudent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return null;
    }
    
    private Student extractStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setStudentNo(rs.getString("student_no"));
        student.setPassword(rs.getString("password"));
        student.setName(rs.getString("name"));
        student.setGender(rs.getString("gender"));
        student.setPhone(rs.getString("phone"));
        student.setEmail(rs.getString("email"));
        student.setClassId(rs.getInt("class_id"));
        student.setCreateTime(rs.getTimestamp("create_time"));
        try {
            student.setClassName(rs.getString("class_name"));
            student.setMajorName(rs.getString("major_name"));
            student.setCollegeName(rs.getString("college_name"));
        } catch (SQLException ignored) {}
        return student;
    }
}
