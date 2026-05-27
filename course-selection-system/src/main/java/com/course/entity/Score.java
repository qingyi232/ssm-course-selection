package com.course.entity;

import java.util.Date;

public class Score {
    private Integer id;
    private Integer studentId;
    private Integer courseId;
    private Double score;
    private Double gradePoint;
    private Date updateTime;
    
    // 扩展字段
    private String studentNo;
    private String studentName;
    private String courseName;
    private String courseNo;
    private Double credit;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }
    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
    public Double getGradePoint() { return gradePoint; }
    public void setGradePoint(Double gradePoint) { this.gradePoint = gradePoint; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getCourseNo() { return courseNo; }
    public void setCourseNo(String courseNo) { this.courseNo = courseNo; }
    public Double getCredit() { return credit; }
    public void setCredit(Double credit) { this.credit = credit; }
}
