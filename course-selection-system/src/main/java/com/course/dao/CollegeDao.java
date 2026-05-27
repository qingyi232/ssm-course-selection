package com.course.dao;

import com.course.entity.College;
import com.course.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CollegeDao {
    
    public List<College> findAll() {
        String sql = "SELECT * FROM college ORDER BY id";
        List<College> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                College c = new College();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setCode(rs.getString("code"));
                list.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { DBUtil.close(conn, stmt, rs); }
        return list;
    }
}
