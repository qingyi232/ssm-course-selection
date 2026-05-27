package com.course.dao;

import com.course.entity.SystemSetting;
import com.course.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SystemSettingDao {
    
    public String getValue(String key) {
        String sql = "SELECT setting_value FROM system_setting WHERE setting_key = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, key);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("setting_value");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return null;
    }
    
    public boolean setValue(String key, String value) {
        String sql = "INSERT INTO system_setting (setting_key, setting_value) VALUES (?, ?) " +
                     "ON DUPLICATE KEY UPDATE setting_value = ?, update_time = NOW()";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, key);
            stmt.setString(2, value);
            stmt.setString(3, value);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
        return false;
    }
    
    public List<SystemSetting> findAll() {
        String sql = "SELECT * FROM system_setting ORDER BY setting_key";
        List<SystemSetting> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                SystemSetting s = new SystemSetting();
                s.setId(rs.getInt("id"));
                s.setSettingKey(rs.getString("setting_key"));
                s.setSettingValue(rs.getString("setting_value"));
                s.setDescription(rs.getString("description"));
                s.setUpdateTime(rs.getTimestamp("update_time"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }
    
    // 检查选课是否开放
    public boolean isSelectionOpen() {
        String enabled = getValue("selection_enabled");
        if (!"1".equals(enabled)) return false;
        
        String startTime = getValue("selection_start_time");
        String endTime = getValue("selection_end_time");
        if (startTime == null || endTime == null) return true;
        
        try {
            java.util.Date now = new java.util.Date();
            java.util.Date start = parseDateTime(startTime);
            java.util.Date end = parseDateTime(endTime);
            if (start == null || end == null) return true;
            return now.after(start) && now.before(end);
        } catch (Exception e) {
            return true;
        }
    }
    
    // 解析多种日期格式
    private java.util.Date parseDateTime(String dateStr) {
        String[] patterns = {
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd",
            "yyyy/MM/dd"
        };
        for (String pattern : patterns) {
            try {
                return new java.text.SimpleDateFormat(pattern).parse(dateStr);
            } catch (Exception ignored) {}
        }
        return null;
    }
}
