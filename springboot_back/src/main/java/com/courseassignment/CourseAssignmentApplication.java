package com.courseassignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 高校课程作业提交与批改管理系统 - 启动类
 */
@SpringBootApplication
public class CourseAssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseAssignmentApplication.class, args);
        System.out.println("========================================");
        System.out.println("  高校课程作业提交与批改管理系统启动成功！");
        System.out.println("  访问地址: http://localhost:8080");
        System.out.println("========================================");
    }
}
