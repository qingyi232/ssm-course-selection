package com.course.entity;

import java.util.Date;

public class CourseFavorite {
    private Integer id;
    private Integer studentId;
    private Integer courseId;
    private Date createTime;
    
    // 扩展字段
    private String courseName;
    private String courseNo;
    private Double credit;
    private String teacherName;
    private String coverImage;
    private String schedule;
    private String location;
    private Integer maxStudents;
    private Integer currentStudents;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }
    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getCourseNo() { return courseNo; }
    public void setCourseNo(String courseNo) { this.courseNo = courseNo; }
    public Double getCredit() { return credit; }
    public void setCredit(Double credit) { this.credit = credit; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }
    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Integer getMaxStudents() { return maxStudents; }
    public void setMaxStudents(Integer maxStudents) { this.maxStudents = maxStudents; }
    public Integer getCurrentStudents() { return currentStudents; }
    public void setCurrentStudents(Integer currentStudents) { this.currentStudents = currentStudents; }
}
