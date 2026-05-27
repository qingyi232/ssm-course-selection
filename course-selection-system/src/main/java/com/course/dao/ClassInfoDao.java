package com.course.dao;

import com.course.entity.ClassInfo;
import com.course.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassInfoDao {
    
    public List<ClassInfo> findAll() {
        String sql = "SELECT c.id, c.name, c.code, c.major_id, c.grade, " +
                     "m.name as major_name, co.name as college_name FROM class_info c " +
                     "LEFT JOIN major m ON c.major_id = m.id " +
                     "LEFT JOIN college co ON m.college_id = co.id ORDER BY c.id";
        List<ClassInfo> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ClassInfo ci = new ClassInfo();
                ci.setId(rs.getInt("id"));
                ci.setName(rs.getString("name"));
                ci.setCode(rs.getString("code"));
                ci.setMajorId(rs.getInt("major_id"));
                ci.setGrade(rs.getString("grade"));
                ci.setMajorName(rs.getString("major_name"));
                ci.setCollegeName(rs.getString("college_name"));
                list.add(ci);
            }
        } catch (SQLException e) { 
            System.err.println("ClassInfoDao.findAll() error: " + e.getMessage());
            e.printStackTrace(); 
        }
        finally { DBUtil.close(conn, stmt, rs); }
        return list;
    }
    
    public ClassInfo findById(int id) {
        String sql = "SELECT c.id, c.name, c.code, c.major_id, c.grade, " +
                     "m.name as major_name, co.name as college_name FROM class_info c " +
                     "LEFT JOIN major m ON c.major_id = m.id " +
                     "LEFT JOIN college co ON m.college_id = co.id WHERE c.id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                ClassInfo ci = new ClassInfo();
                ci.setId(rs.getInt("id"));
                ci.setName(rs.getString("name"));
                ci.setCode(rs.getString("code"));
                ci.setMajorId(rs.getInt("major_id"));
                ci.setGrade(rs.getString("grade"));
                ci.setMajorName(rs.getString("major_name"));
                ci.setCollegeName(rs.getString("college_name"));
                return ci;
            }
        } catch (SQLException e) { 
            System.err.println("ClassInfoDao.findById() error: " + e.getMessage());
            e.printStackTrace(); 
        }
        finally { DBUtil.close(conn, stmt, rs); }
        return null;
    }
}
