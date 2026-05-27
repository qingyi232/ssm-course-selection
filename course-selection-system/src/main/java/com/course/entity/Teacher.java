package com.course.entity;

import java.util.Date;

public class Teacher {
    private Integer id;
    private String teacherNo;
    private String password;
    private String name;
    private String gender;
    private String phone;
    private String email;
    private Integer collegeId;
    private String title;
    private Date createTime;
    
    // 扩展字段
    private String collegeName;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTeacherNo() { return teacherNo; }
    public void setTeacherNo(String teacherNo) { this.teacherNo = teacherNo; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getCollegeId() { return collegeId; }
    public void setCollegeId(Integer collegeId) { this.collegeId = collegeId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getCollegeName() { return collegeName; }
    public void setCollegeName(String collegeName) { this.collegeName = collegeName; }
}
