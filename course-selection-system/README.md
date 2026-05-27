# 基于Java的在线课程选课系统

## 技术栈
- 后端：Java + Servlet + JSP + JDBC
- 前端：HTML5 + CSS3 + JavaScript + JSTL
- 数据库：MySQL 8.0
- 服务器：Tomcat 9.x
- 构建工具：Maven

## 项目结构
```
course-selection-system/
├── pom.xml                          # Maven配置
├── sql/init.sql                     # 数据库初始化脚本
└── src/main/
    ├── java/com/course/
    │   ├── entity/                  # 实体类
    │   ├── dao/                     # 数据访问层
    │   ├── servlet/                 # 控制器
    │   ├── filter/                  # 过滤器
    │   └── util/                    # 工具类
    └── webapp/
        ├── css/style.css            # 样式文件
        ├── login.jsp                # 登录页
        ├── student/                 # 学生端页面
        ├── teacher/                 # 教师端页面
        ├── admin/                   # 管理员端页面
        └── WEB-INF/web.xml
```

## 部署步骤

### 1. 数据库配置
1. 创建MySQL数据库，执行 `sql/init.sql` 脚本
2. 修改 `src/main/java/com/course/util/DBUtil.java` 中的数据库连接信息：
   - URL: jdbc:mysql://localhost:3306/course_selection
   - USERNAME: root
   - PASSWORD: 123456（改成你的密码）

### 2. IDEA配置
1. 用IDEA打开 `course-selection-system` 文件夹
2. 配置Tomcat服务器（推荐9.x版本）
3. 配置项目的Deployment，Application context设为 `/`
4. 运行Tomcat

### 3. 访问系统
- 地址：http://localhost:8080/login.jsp

## 测试账号
| 角色 | 账号 | 密码 |
|------|------|------|
| 管理员 | admin | 123456 |
| 教师 | T001 | 123456 |
| 学生 | 22730401 | 123456 |

## 功能模块

### 学生端
- 个人信息查看
- 选课中心（浏览、搜索、选课）
- 我的课程（查看已选、退课）
- 成绩查询（成绩列表、GPA统计）

### 教师端
- 个人信息查看
- 课程管理（添加、编辑、删除课程）
- 查看选课学生名单
- 成绩录入与管理

### 管理员端
- 数据统计（学生、教师、课程、选课数量）
- 学生管理（增删改查、重置密码）
- 教师管理（增删改查、重置密码）
- 课程管理（增删改查）
- 公告管理（发布、编辑、删除）
