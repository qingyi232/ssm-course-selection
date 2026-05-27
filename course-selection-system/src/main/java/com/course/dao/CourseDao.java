package com.course.dao;

import com.course.entity.Course;
import com.course.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {
    
    public Course findById(Integer id) {
        String sql = "SELECT c.*, ct.name as type_name, t.name as teacher_name FROM course c " +
                     "LEFT JOIN course_type ct ON c.type_id = ct.id " +
                     "LEFT JOIN teacher t ON c.teacher_id = t.id WHERE c.id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return extractCourse(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return null;
    }
    
    public List<Course> findAll() {
        String sql = "SELECT c.*, ct.name as type_name, t.name as teacher_name FROM course c " +
                     "LEFT JOIN course_type ct ON c.type_id = ct.id " +
                     "LEFT JOIN teacher t ON c.teacher_id = t.id ORDER BY c.course_no";
        List<Course> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractCourse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }
    
    public List<Course> findAvailable() {
        String sql = "SELECT c.*, ct.name as type_name, t.name as teacher_name FROM course c " +
                     "LEFT JOIN course_type ct ON c.type_id = ct.id " +
                     "LEFT JOIN teacher t ON c.teacher_id = t.id " +
                     "WHERE c.status = 1 AND c.current_students < c.max_students ORDER BY c.course_no";
        List<Course> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractCourse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }
    
    public List<Course> findByTeacherId(Integer teacherId) {
        String sql = "SELECT c.*, ct.name as type_name, t.name as teacher_name FROM course c " +
                     "LEFT JOIN course_type ct ON c.type_id = ct.id " +
                     "LEFT JOIN teacher t ON c.teacher_id = t.id " +
                     "WHERE c.teacher_id = ? ORDER BY c.course_no";
        List<Course> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, teacherId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractCourse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }
    
    public List<Course> search(String keyword) {
        String sql = "SELECT c.*, ct.name as type_name, t.name as teacher_name FROM course c " +
                     "LEFT JOIN course_type ct ON c.type_id = ct.id " +
                     "LEFT JOIN teacher t ON c.teacher_id = t.id " +
                     "WHERE c.course_no LIKE ? OR c.name LIKE ? OR t.name LIKE ? ORDER BY c.course_no";
        List<Course> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractCourse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }
    
    // 带筛选条件的查询
    public List<Course> findWithFilters(String keyword, String credit, String typeId, String teacherId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.*, ct.name as type_name, t.name as teacher_name FROM course c ");
        sql.append("LEFT JOIN course_type ct ON c.type_id = ct.id ");
        sql.append("LEFT JOIN teacher t ON c.teacher_id = t.id WHERE c.status = 1 ");
        
        List<Object> params = new ArrayList<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (c.course_no LIKE ? OR c.name LIKE ? OR t.name LIKE ?) ");
            String pattern = "%" + keyword.trim() + "%";
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
        }
        if (credit != null && !credit.isEmpty()) {
            if ("4".equals(credit)) {
                sql.append("AND c.credit >= 4 ");
            } else {
                sql.append("AND c.credit = ? ");
                params.add(Double.parseDouble(credit));
            }
        }
        if (typeId != null && !typeId.isEmpty()) {
            sql.append("AND c.type_id = ? ");
            params.add(Integer.parseInt(typeId));
        }
        if (teacherId != null && !teacherId.isEmpty()) {
            sql.append("AND c.teacher_id = ? ");
            params.add(Integer.parseInt(teacherId));
        }
        sql.append("ORDER BY c.course_no");
        
        List<Course> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractCourse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }
    
    public boolean insert(Course course) {
        String sql = "INSERT INTO course (course_no, name, credit, hours, type_id, teacher_id, max_students, location, schedule, semester, description, cover_image, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, course.getCourseNo());
            stmt.setString(2, course.getName());
            stmt.setDouble(3, course.getCredit());
            stmt.setInt(4, course.getHours());
            stmt.setObject(5, course.getTypeId());
            stmt.setObject(6, course.getTeacherId());
            stmt.setInt(7, course.getMaxStudents() != null ? course.getMaxStudents() : 60);
            stmt.setString(8, course.getLocation());
            stmt.setString(9, course.getSchedule());
            stmt.setString(10, course.getSemester());
            stmt.setString(11, course.getDescription());
            stmt.setString(12, course.getCoverImage());
            stmt.setInt(13, course.getStatus() != null ? course.getStatus() : 1);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
        return false;
    }
    
    public boolean update(Course course) {
        String sql = "UPDATE course SET name = ?, credit = ?, hours = ?, type_id = ?, max_students = ?, location = ?, schedule = ?, semester = ?, description = ?, cover_image = ?, status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, course.getName());
            stmt.setDouble(2, course.getCredit());
            stmt.setInt(3, course.getHours());
            stmt.setObject(4, course.getTypeId());
            stmt.setInt(5, course.getMaxStudents());
            stmt.setString(6, course.getLocation());
            stmt.setString(7, course.getSchedule());
            stmt.setString(8, course.getSemester());
            stmt.setString(9, course.getDescription());
            stmt.setString(10, course.getCoverImage());
            stmt.setInt(11, course.getStatus());
            stmt.setInt(12, course.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
        return false;
    }
    
    public boolean delete(Integer id) {
        String sql = "DELETE FROM course WHERE id = ?";
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
    
    public boolean updateStudentCount(Integer courseId, int delta) {
        String sql = "UPDATE course SET current_students = current_students + ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, delta);
            stmt.setInt(2, courseId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
        return false;
    }
    
    public int count() {
        String sql = "SELECT COUNT(*) FROM course";
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
    
    private Course extractCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setId(rs.getInt("id"));
        course.setCourseNo(rs.getString("course_no"));
        course.setName(rs.getString("name"));
        course.setCredit(rs.getDouble("credit"));
        course.setHours(rs.getInt("hours"));
        course.setTypeId(rs.getInt("type_id"));
        course.setTeacherId(rs.getInt("teacher_id"));
        course.setMaxStudents(rs.getInt("max_students"));
        course.setCurrentStudents(rs.getInt("current_students"));
        course.setLocation(rs.getString("location"));
        course.setSchedule(rs.getString("schedule"));
        course.setSemester(rs.getString("semester"));
        course.setDescription(rs.getString("description"));
        course.setCoverImage(rs.getString("cover_image"));
        course.setStatus(rs.getInt("status"));
        course.setCreateTime(rs.getTimestamp("create_time"));
        try {
            course.setTypeName(rs.getString("type_name"));
            course.setTeacherName(rs.getString("teacher_name"));
        } catch (SQLException ignored) {}
        return course;
    }
    
    // 检查课程时间冲突
    public boolean hasTimeConflict(Integer studentId, String schedule) {
        if (schedule == null || schedule.isEmpty()) return false;
        String sql = "SELECT c.schedule FROM course_selection cs " +
                     "JOIN course c ON cs.course_id = c.id " +
                     "WHERE cs.student_id = ? AND cs.status = 1";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String existSchedule = rs.getString("schedule");
                if (existSchedule != null && checkScheduleConflict(existSchedule, schedule)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return false;
    }
    
    private boolean checkScheduleConflict(String s1, String s2) {
        // 解析时间格式，如 "周一1-2节,周三3-4节"
        // 同时支持英文逗号、中文逗号、分号等分隔符
        String[] parts1 = s1.split("[,，;；、]");
        String[] parts2 = s2.split("[,，;；、]");
        for (String p1 : parts1) {
            for (String p2 : parts2) {
                String t1 = p1.trim();
                String t2 = p2.trim();
                if (t1.isEmpty() || t2.isEmpty()) continue;
                // 完全相同（忽略空格差异）
                if (t1.replaceAll("\\s+", "").equals(t2.replaceAll("\\s+", ""))) return true;
                // 检查是否同一天同一时间段有重叠
                if (isSameDayAndTime(t1, t2)) return true;
            }
        }
        return false;
    }
    
    private boolean isSameDayAndTime(String t1, String t2) {
        // 去除所有空格后比较
        t1 = t1.replaceAll("\\s+", "");
        t2 = t2.replaceAll("\\s+", "");
        
        // 提取星期几
        String day1 = extractDay(t1);
        String day2 = extractDay(t2);
        if (day1 == null || day2 == null || !day1.equals(day2)) return false;
        
        // 提取节次范围
        int[] range1 = extractPeriodRange(t1);
        int[] range2 = extractPeriodRange(t2);
        if (range1 == null || range2 == null) return false;
        
        // 检查节次是否重叠 (range1[0]~range1[1] 与 range2[0]~range2[1] 是否有交集)
        return !(range1[1] < range2[0] || range2[1] < range1[0]);
    }
    
    private String extractDay(String schedule) {
        // 匹配 周一、周二、星期一、星期二 等
        if (schedule.contains("周一") || schedule.contains("星期一") || schedule.contains("Monday")) return "1";
        if (schedule.contains("周二") || schedule.contains("星期二") || schedule.contains("Tuesday")) return "2";
        if (schedule.contains("周三") || schedule.contains("星期三") || schedule.contains("Wednesday")) return "3";
        if (schedule.contains("周四") || schedule.contains("星期四") || schedule.contains("Thursday")) return "4";
        if (schedule.contains("周五") || schedule.contains("星期五") || schedule.contains("Friday")) return "5";
        if (schedule.contains("周六") || schedule.contains("星期六") || schedule.contains("Saturday")) return "6";
        if (schedule.contains("周日") || schedule.contains("星期日") || schedule.contains("Sunday")) return "7";
        return null;
    }
    
    private int[] extractPeriodRange(String schedule) {
        // 匹配 1-2节、3-4节、第1-2节 等格式
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+)[-~](\\d+)");
        java.util.regex.Matcher matcher = pattern.matcher(schedule);
        if (matcher.find()) {
            return new int[]{Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))};
        }
        // 匹配单节 如 第3节
        pattern = java.util.regex.Pattern.compile("第?(\\d+)节");
        matcher = pattern.matcher(schedule);
        if (matcher.find()) {
            int period = Integer.parseInt(matcher.group(1));
            return new int[]{period, period};
        }
        return null;
    }
    
    // 统计各课程选课人数
    public List<Object[]> getSelectionStats() {
        String sql = "SELECT c.course_no, c.name, c.max_students, c.current_students, " +
                     "ROUND(c.current_students * 100.0 / c.max_students, 1) as rate " +
                     "FROM course c ORDER BY rate DESC";
        List<Object[]> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[5];
                row[0] = rs.getString("course_no");
                row[1] = rs.getString("name");
                row[2] = rs.getInt("max_students");
                row[3] = rs.getInt("current_students");
                row[4] = rs.getDouble("rate");
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }
}
