package com.course.entity;

import java.util.Date;

public class Course {
    private Integer id;
    private String courseNo;
    private String name;
    private Double credit;
    private Integer hours;
    private Integer typeId;
    private Integer teacherId;
    private Integer maxStudents;
    private Integer currentStudents;
    private String location;
    private String schedule;
    private String semester;
    private String description;
    private Integer status;
    private String coverImage;  // 课程封面图片
    private Date createTime;
    
    // 扩展字段
    private String typeName;
    private String teacherName;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCourseNo() { return courseNo; }
    public void setCourseNo(String courseNo) { this.courseNo = courseNo; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getCredit() { return credit; }
    public void setCredit(Double credit) { this.credit = credit; }
    public Integer getHours() { return hours; }
    public void setHours(Integer hours) { this.hours = hours; }
    public Integer getTypeId() { return typeId; }
    public void setTypeId(Integer typeId) { this.typeId = typeId; }
    public Integer getTeacherId() { return teacherId; }
    public void setTeacherId(Integer teacherId) { this.teacherId = teacherId; }
    public Integer getMaxStudents() { return maxStudents; }
    public void setMaxStudents(Integer maxStudents) { this.maxStudents = maxStudents; }
    public Integer getCurrentStudents() { return currentStudents; }
    public void setCurrentStudents(Integer currentStudents) { this.currentStudents = currentStudents; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }
}
