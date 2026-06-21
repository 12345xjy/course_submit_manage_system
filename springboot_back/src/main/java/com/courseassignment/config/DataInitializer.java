package com.courseassignment.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 数据库初始化器 - 应用启动时自动检测并插入测试数据
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // 检查 user 表是否已有数据
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user", Integer.class);
        if (count != null && count > 0) {
            System.out.println("[DataInitializer] 数据库已有 " + count + " 条用户数据，跳过初始化。");
            return;
        }

        System.out.println("[DataInitializer] 开始初始化测试数据...");
        String pwd = passwordEncoder.encode("123456");

        // 插入用户
        jdbcTemplate.update("INSERT INTO user (username, password, real_name, role, student_no, teacher_no, phone, email) VALUES (?,?,?,?,?,?,?,?)",
                "admin", pwd, "系统管理员", "ADMIN", null, "T00001", "13800000001", "admin@university.edu.cn");
        jdbcTemplate.update("INSERT INTO user (username, password, real_name, role, student_no, teacher_no, phone, email) VALUES (?,?,?,?,?,?,?,?)",
                "teacher1", pwd, "张教授", "TEACHER", null, "T00100", "13800000002", "zhang@university.edu.cn");
        jdbcTemplate.update("INSERT INTO user (username, password, real_name, role, student_no, teacher_no, phone, email) VALUES (?,?,?,?,?,?,?,?)",
                "teacher2", pwd, "李副教授", "TEACHER", null, "T00101", "13800000003", "li@university.edu.cn");
        jdbcTemplate.update("INSERT INTO user (username, password, real_name, role, student_no, teacher_no, phone, email) VALUES (?,?,?,?,?,?,?,?)",
                "student1", pwd, "王同学", "STUDENT", "2021001001", null, "13800000004", "wang@student.edu.cn");
        jdbcTemplate.update("INSERT INTO user (username, password, real_name, role, student_no, teacher_no, phone, email) VALUES (?,?,?,?,?,?,?,?)",
                "student2", pwd, "陈同学", "STUDENT", "2021001002", null, "13800000005", "chen@student.edu.cn");
        jdbcTemplate.update("INSERT INTO user (username, password, real_name, role, student_no, teacher_no, phone, email) VALUES (?,?,?,?,?,?,?,?)",
                "student3", pwd, "刘同学", "STUDENT", "2021001003", null, "13800000006", "liu@student.edu.cn");

        // 插入课程
        jdbcTemplate.update("INSERT INTO course (name, code, description, teacher_id, semester) VALUES (?,?,?,?,?)",
                "Web应用开发", "CS301", "学习Web前后端开发技术，包括Vue.js、Spring Boot等框架的使用。", 2L, "2025-2026-2");
        jdbcTemplate.update("INSERT INTO course (name, code, description, teacher_id, semester) VALUES (?,?,?,?,?)",
                "数据库原理与应用", "CS201", "学习关系型数据库设计、SQL语言以及MySQL数据库的使用。", 3L, "2025-2026-2");
        jdbcTemplate.update("INSERT INTO course (name, code, description, teacher_id, semester) VALUES (?,?,?,?,?)",
                "软件工程", "SE401", "学习软件开发的工程化方法，包括需求分析、设计模式、项目管理等。", 2L, "2025-2026-2");

        // 插入选课数据
        jdbcTemplate.update("INSERT INTO course_student (course_id, student_id) VALUES (1,4),(1,5),(1,6),(2,4),(2,5),(3,4),(3,5),(3,6)");

        // 插入作业数据
        jdbcTemplate.update("INSERT INTO assignment (course_id, title, description, deadline, max_score, file_types, max_size) VALUES (?,?,?,?,?,?,?)",
                1L, "第一次作业：个人主页设计", "使用HTML+CSS+JS设计一个个人主页。", "2026-07-15 23:59:59", 100.0, ".zip,.rar,.pdf", 10);
        jdbcTemplate.update("INSERT INTO assignment (course_id, title, description, deadline, max_score, file_types, max_size) VALUES (?,?,?,?,?,?,?)",
                1L, "第二次作业：Vue组件开发", "使用Vue3框架开发Todo应用。", "2026-07-30 23:59:59", 100.0, ".zip,.rar", 20);
        jdbcTemplate.update("INSERT INTO assignment (course_id, title, description, deadline, max_score, file_types, max_size) VALUES (?,?,?,?,?,?,?)",
                2L, "第一次作业：数据库设计", "设计ER图并转化为关系模式。", "2026-07-20 23:59:59", 100.0, ".pdf,.doc,.docx", 5);

        System.out.println("[DataInitializer] 测试数据初始化完成！共创建 6 个用户、3 门课程、3 个作业。");
    }
}
