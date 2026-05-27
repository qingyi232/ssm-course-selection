package com.course.dao;

import com.course.entity.Notice;
import com.course.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoticeDao {
    
    public Notice findById(Integer id) {
        String sql = "SELECT * FROM notice WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) { return extractNotice(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { DBUtil.close(conn, stmt, rs); }
        return null;
    }
    
    public List<Notice> findAll() {
        String sql = "SELECT * FROM notice WHERE status = 1 ORDER BY publish_time DESC";
        List<Notice> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) { list.add(extractNotice(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { DBUtil.close(conn, stmt, rs); }
        return list;
    }

    public boolean insert(Notice notice) {
        String sql = "INSERT INTO notice (title, content, publisher_id, status) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, notice.getTitle());
            stmt.setString(2, notice.getContent());
            stmt.setObject(3, notice.getPublisherId());
            stmt.setInt(4, notice.getStatus() != null ? notice.getStatus() : 1);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        finally { DBUtil.close(conn, stmt); }
        return false;
    }
    
    public boolean update(Notice notice) {
        String sql = "UPDATE notice SET title = ?, content = ?, status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, notice.getTitle());
            stmt.setString(2, notice.getContent());
            stmt.setInt(3, notice.getStatus());
            stmt.setInt(4, notice.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        finally { DBUtil.close(conn, stmt); }
        return false;
    }
    
    public boolean delete(Integer id) {
        String sql = "DELETE FROM notice WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        finally { DBUtil.close(conn, stmt); }
        return false;
    }
    
    private Notice extractNotice(ResultSet rs) throws SQLException {
        Notice notice = new Notice();
        notice.setId(rs.getInt("id"));
        notice.setTitle(rs.getString("title"));
        notice.setContent(rs.getString("content"));
        notice.setPublisherId(rs.getInt("publisher_id"));
        notice.setPublishTime(rs.getTimestamp("publish_time"));
        notice.setStatus(rs.getInt("status"));
        return notice;
    }
}
