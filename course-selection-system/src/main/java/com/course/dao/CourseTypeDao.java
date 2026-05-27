package com.course.dao;

import com.course.entity.CourseType;
import com.course.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseTypeDao {
    
    public List<CourseType> findAll() {
        String sql = "SELECT * FROM course_type ORDER BY id";
        List<CourseType> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                CourseType type = new CourseType();
                type.setId(rs.getInt("id"));
                type.setName(rs.getString("name"));
                list.add(type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }
    
    public CourseType findById(Integer id) {
        String sql = "SELECT * FROM course_type WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                CourseType type = new CourseType();
                type.setId(rs.getInt("id"));
                type.setName(rs.getString("name"));
                return type;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return null;
    }
}
