package com.course.entity;

import java.util.Date;

public class CourseSelection {
    private Integer id;
    private Integer studentId;
    private Integer courseId;
    private Date selectTime;
    private Integer status;
    
    // 扩展字段
    private String studentNo;
    private String studentName;
    private String className;
    private String courseName;
    private String courseNo;
    private Double credit;
    private String coverImage;
    private String teacherName;
    private String schedule;
    private String location;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }
    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }
    public Date getSelectTime() { return selectTime; }
    public void setSelectTime(Date selectTime) { this.selectTime = selectTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getCourseNo() { return courseNo; }
    public void setCourseNo(String courseNo) { this.courseNo = courseNo; }
    public Double getCredit() { return credit; }
    public void setCredit(Double credit) { this.credit = credit; }
    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
