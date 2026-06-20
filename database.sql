-- ============================================================
-- 高校课程作业提交与批改管理系统 - 数据库脚本
-- 数据库: MySQL 8.0
-- 字符集: utf8mb4
-- ============================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS course_assignment_system
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE course_assignment_system;

-- ============================================================
-- 用户表
-- ============================================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id`            BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '用户ID',
    `username`      VARCHAR(50)     NOT NULL                 COMMENT '用户名(登录账号)',
    `password`      VARCHAR(255)    NOT NULL                 COMMENT '密码(加密存储)',
    `real_name`     VARCHAR(50)     NOT NULL                 COMMENT '真实姓名',
    `role`          VARCHAR(20)     NOT NULL DEFAULT 'STUDENT' COMMENT '角色: STUDENT-学生, TEACHER-教师, ADMIN-管理员',
    `student_no`    VARCHAR(30)     DEFAULT NULL             COMMENT '学号(学生专用)',
    `teacher_no`    VARCHAR(30)     DEFAULT NULL             COMMENT '工号(教师专用)',
    `phone`         VARCHAR(20)     DEFAULT NULL             COMMENT '手机号',
    `email`         VARCHAR(100)    DEFAULT NULL             COMMENT '邮箱',
    `avatar`        VARCHAR(255)    DEFAULT NULL             COMMENT '头像路径',
    `status`        TINYINT         NOT NULL DEFAULT 1       COMMENT '状态: 0-禁用, 1-启用',
    `created_at`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_student_no` (`student_no`),
    UNIQUE KEY `uk_teacher_no` (`teacher_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================================
-- 课程表
-- ============================================================
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
    `id`            BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '课程ID',
    `name`          VARCHAR(100)    NOT NULL                 COMMENT '课程名称',
    `code`          VARCHAR(30)     NOT NULL                 COMMENT '课程编码',
    `description`   TEXT            DEFAULT NULL             COMMENT '课程描述',
    `teacher_id`    BIGINT          NOT NULL                 COMMENT '授课教师ID',
    `semester`      VARCHAR(30)     NOT NULL                 COMMENT '学期(如: 2025-2026-2)',
    `status`        TINYINT         NOT NULL DEFAULT 1       COMMENT '状态: 0-结课, 1-进行中',
    `created_at`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`),
    KEY `idx_teacher_id` (`teacher_id`),
    CONSTRAINT `fk_course_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程表';

-- ============================================================
-- 课程-学生关联表(选课记录)
-- ============================================================
DROP TABLE IF EXISTS `course_student`;
CREATE TABLE `course_student` (
    `id`            BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    `course_id`     BIGINT          NOT NULL                 COMMENT '课程ID',
    `student_id`    BIGINT          NOT NULL                 COMMENT '学生ID',
    `created_at`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_course_student` (`course_id`, `student_id`),
    KEY `idx_student_id` (`student_id`),
    CONSTRAINT `fk_cs_course` FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_cs_student` FOREIGN KEY (`student_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程-学生关联表';

-- ============================================================
-- 作业表
-- ============================================================
DROP TABLE IF EXISTS `assignment`;
CREATE TABLE `assignment` (
    `id`            BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '作业ID',
    `course_id`     BIGINT          NOT NULL                 COMMENT '所属课程ID',
    `title`         VARCHAR(200)    NOT NULL                 COMMENT '作业标题',
    `description`   TEXT            DEFAULT NULL             COMMENT '作业描述/要求',
    `deadline`      DATETIME        NOT NULL                 COMMENT '截止提交时间',
    `max_score`     DECIMAL(5,1)    NOT NULL DEFAULT 100.0   COMMENT '满分分值',
    `file_types`    VARCHAR(255)    DEFAULT NULL             COMMENT '允许的文件类型(逗号分隔, 如: .pdf,.doc,.docx,.zip)',
    `max_size`      INT             NOT NULL DEFAULT 10      COMMENT '最大文件大小(MB)',
    `status`        TINYINT         NOT NULL DEFAULT 1       COMMENT '状态: 0-已关闭, 1-进行中',
    `created_at`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_course_id` (`course_id`),
    CONSTRAINT `fk_assignment_course` FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='作业表';

-- ============================================================
-- 作业提交表
-- ============================================================
DROP TABLE IF EXISTS `submission`;
CREATE TABLE `submission` (
    `id`                BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '提交ID',
    `assignment_id`     BIGINT          NOT NULL                 COMMENT '作业ID',
    `student_id`        BIGINT          NOT NULL                 COMMENT '提交学生ID',
    `file_name`         VARCHAR(255)    NOT NULL                 COMMENT '原始文件名',
    `file_path`         VARCHAR(500)    NOT NULL                 COMMENT '文件存储路径',
    `file_size`         BIGINT          DEFAULT NULL             COMMENT '文件大小(字节)',
    `submit_time`       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    `is_late`           TINYINT         NOT NULL DEFAULT 0       COMMENT '是否迟交: 0-否, 1-是',
    `status`            VARCHAR(20)     NOT NULL DEFAULT 'SUBMITTED' COMMENT '状态: SUBMITTED-已提交, GRADED-已批改',
    `created_at`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_assignment_student` (`assignment_id`, `student_id`),
    KEY `idx_student_id` (`student_id`),
    CONSTRAINT `fk_submission_assignment` FOREIGN KEY (`assignment_id`) REFERENCES `assignment`(`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_submission_student` FOREIGN KEY (`student_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='作业提交表';

-- ============================================================
-- 批改/成绩表
-- ============================================================
DROP TABLE IF EXISTS `grade`;
CREATE TABLE `grade` (
    `id`                BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '成绩ID',
    `submission_id`     BIGINT          NOT NULL                 COMMENT '提交记录ID',
    `score`             DECIMAL(5,1)    NOT NULL                 COMMENT '得分',
    `comment`           TEXT            DEFAULT NULL             COMMENT '教师评语',
    `graded_by`         BIGINT          NOT NULL                 COMMENT '批改教师ID',
    `graded_time`       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '批改时间',
    `created_at`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_submission` (`submission_id`),
    KEY `idx_graded_by` (`graded_by`),
    CONSTRAINT `fk_grade_submission` FOREIGN KEY (`submission_id`) REFERENCES `submission`(`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_grade_teacher` FOREIGN KEY (`graded_by`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成绩/批改表';

-- ============================================================
-- 系统通知表
-- ============================================================
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
    `id`            BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '通知ID',
    `user_id`       BIGINT          NOT NULL                 COMMENT '接收用户ID',
    `title`         VARCHAR(200)    NOT NULL                 COMMENT '通知标题',
    `content`       TEXT            NOT NULL                 COMMENT '通知内容',
    `type`          VARCHAR(30)     NOT NULL DEFAULT 'SYSTEM' COMMENT '类型: SYSTEM-系统, ASSIGNMENT-作业, GRADE-成绩',
    `is_read`       TINYINT         NOT NULL DEFAULT 0       COMMENT '是否已读: 0-未读, 1-已读',
    `created_at`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    CONSTRAINT `fk_notification_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统通知表';

-- ============================================================
-- 插入初始测试数据
-- ============================================================

-- 密码均为: 123456 (bcrypt加密)
-- 实际开发中使用BCryptPasswordEncoder加密，此处插入的是加密后的值
INSERT INTO `user` (`username`, `password`, `real_name`, `role`, `student_no`, `teacher_no`, `phone`, `email`) VALUES
('admin',    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyY3XVqRe', '系统管理员', 'ADMIN',   NULL,       'T00001', '13800000001', 'admin@university.edu.cn'),
('teacher1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyY3XVqRe', '张教授',     'TEACHER', NULL,       'T00100', '13800000002', 'zhang@university.edu.cn'),
('teacher2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyY3XVqRe', '李副教授',   'TEACHER', NULL,       'T00101', '13800000003', 'li@university.edu.cn'),
('student1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyY3XVqRe', '王同学',     'STUDENT', '2021001001', NULL,     '13800000004', 'wang@student.edu.cn'),
('student2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyY3XVqRe', '陈同学',     'STUDENT', '2021001002', NULL,     '13800000005', 'chen@student.edu.cn'),
('student3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyY3XVqRe', '刘同学',     'STUDENT', '2021001003', NULL,     '13800000006', 'liu@student.edu.cn');

-- 课程数据
INSERT INTO `course` (`name`, `code`, `description`, `teacher_id`, `semester`) VALUES
('Web应用开发',      'CS301', '学习Web前后端开发技术，包括Vue.js、Spring Boot等框架的使用。', 2, '2025-2026-2'),
('数据库原理与应用', 'CS201', '学习关系型数据库设计、SQL语言以及MySQL数据库的使用。',         3, '2025-2026-2'),
('软件工程',         'SE401', '学习软件开发的工程化方法，包括需求分析、设计模式、项目管理等。',  2, '2025-2026-2');

-- 选课数据
INSERT INTO `course_student` (`course_id`, `student_id`) VALUES
(1, 4), (1, 5), (1, 6),
(2, 4), (2, 5),
(3, 4), (3, 5), (3, 6);

-- 作业数据
INSERT INTO `assignment` (`course_id`, `title`, `description`, `deadline`, `max_score`, `file_types`, `max_size`) VALUES
(1, '第一次作业：个人主页设计',      '使用HTML+CSS+JS设计一个个人主页，要求页面美观、结构清晰，包含个人简介、技能展示等模块。',        '2026-07-15 23:59:59', 100, '.zip,.rar,.pdf',              10),
(1, '第二次作业：Vue组件开发',       '使用Vue3框架开发一个待办事项(Todo)应用，实现增删改查功能，要求组件化开发。',                 '2026-07-30 23:59:59', 100, '.zip,.rar',                    20),
(1, '第三次作业：前后端交互',        '结合Spring Boot后端和Vue3前端，实现一个简单的用户管理系统，包含登录注册和CRUD功能。',         '2026-08-20 23:59:59', 100, '.zip,.rar',                    50),
(2, '第一次作业：数据库设计',        '根据给定的业务场景，设计ER图并转化为关系模式，编写建表SQL语句。',                         '2026-07-20 23:59:59', 100, '.pdf,.doc,.docx',              5),
(2, '第二次作业：SQL查询练习',       '完成指定的10道SQL查询题目，包括多表连接、子查询、聚合函数等。',                           '2026-08-10 23:59:59', 100, '.sql,.pdf,.doc,.docx',         5);
