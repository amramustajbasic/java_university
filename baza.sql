CREATE DATABASE `freedb_Univerzitet_FET`;
USE `freedb_Univerzitet_FET`;

CREATE TABLE `Enrollment` (
  `enrollment_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int DEFAULT NULL,
  `subject_id` int DEFAULT NULL,
  PRIMARY KEY (`enrollment_id`),
  KEY `student_id` (`student_id`),
  KEY `subject_id` (`subject_id`),
  CONSTRAINT `Enrollment_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `Student` (`student_id`),
  CONSTRAINT `Enrollment_ibfk_2` FOREIGN KEY (`subject_id`) REFERENCES `Subject` (`subject_id`)
)

CREATE TABLE `EnrollmentPeriod` (
  `enrollment_period_id` int NOT NULL,
  `begin_period` date DEFAULT NULL,
  `end_period` date DEFAULT NULL,
  PRIMARY KEY (`enrollment_period_id`)
)

CREATE TABLE `Grade` (
  `grade_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int DEFAULT NULL,
  `subject_id` int DEFAULT NULL,
  `grade` int DEFAULT NULL,
  `bodovi` int DEFAULT NULL,
  PRIMARY KEY (`grade_id`),
  KEY `student_id` (`student_id`),
  KEY `subject_id` (`subject_id`),
  CONSTRAINT `Grade_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `Student` (`student_id`),
  CONSTRAINT `Grade_ibfk_2` FOREIGN KEY (`subject_id`) REFERENCES `Subject` (`subject_id`)
)

CREATE TABLE `Locked` (
  `locked_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `locked` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`locked_id`),
  KEY `Locked_Student_student_id_fk` (`student_id`),
  CONSTRAINT `Locked_Student_student_id_fk` FOREIGN KEY (`student_id`) REFERENCES `Student` (`student_id`)
)

CREATE TABLE `Request` (
  `request_id` int NOT NULL,
  `student_id` int DEFAULT NULL,
  `subject_id` int DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `vice_dean_id` int DEFAULT NULL,
  `teacher_id` int DEFAULT NULL,
  PRIMARY KEY (`request_id`),
  KEY `student_id` (`student_id`),
  KEY `subject_id` (`subject_id`),
  KEY `Request_Teacher_teacher_id_fk` (`teacher_id`),
  KEY `Request_User_user_id_fk` (`vice_dean_id`),
  CONSTRAINT `Request_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `Student` (`student_id`),
  CONSTRAINT `Request_ibfk_2` FOREIGN KEY (`subject_id`) REFERENCES `Subject` (`subject_id`),
  CONSTRAINT `Request_Teacher_teacher_id_fk` FOREIGN KEY (`teacher_id`) REFERENCES `Teacher` (`teacher_id`),
  CONSTRAINT `Request_User_user_id_fk` FOREIGN KEY (`vice_dean_id`) REFERENCES `User` (`user_id`)
)

CREATE TABLE `Role` (
  `role_id` int NOT NULL,
  `role_name` varchar(50) NOT NULL,
  PRIMARY KEY (`role_id`)
)

CREATE TABLE `Student` (
  `student_id` int NOT NULL,
  `user_id` int DEFAULT NULL,
  `year_of_study` int DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`student_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `Student_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `User` (`user_id`)
)

CREATE TABLE `Student_Subject` (
  `student_subject_id` int NOT NULL AUTO_INCREMENT,
  `student_id` int DEFAULT NULL,
  `subject_id` int DEFAULT NULL,
  PRIMARY KEY (`student_subject_id`),
  KEY `subject_id` (`subject_id`),
  KEY `Student_Subject_ibfk_1` (`student_id`),
  CONSTRAINT `Student_Subject_ibfk_2` FOREIGN KEY (`subject_id`) REFERENCES `Subject` (`subject_id`)
)

CREATE TABLE `StudyProgram` (
  `program_id` int NOT NULL,
  `subject_id` int DEFAULT NULL,
  `subject_name` varchar(100) NOT NULL,
  `teachers` varchar(100) DEFAULT NULL,
  `responsible_teacher_id` int DEFAULT NULL,
  PRIMARY KEY (`program_id`),
  KEY `StudyProgram_Subject_subject_id_fk` (`subject_id`),
  KEY `StudyProgram_Teacher_teacher_id_fk` (`responsible_teacher_id`),
  CONSTRAINT `StudyProgram_Subject_subject_id_fk` FOREIGN KEY (`subject_id`) REFERENCES `Subject` (`subject_id`),
  CONSTRAINT `StudyProgram_Teacher_teacher_id_fk` FOREIGN KEY (`responsible_teacher_id`) REFERENCES `Teacher` (`teacher_id`)
)

CREATE TABLE `Subject` (
  `subject_id` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `semester` varchar(50) NOT NULL,
  `responsible_teacher_id` int DEFAULT NULL,
  `prerequisite_subject` varchar(100) DEFAULT NULL,
  `ects` int DEFAULT NULL,
  `godina` int NOT NULL,
  PRIMARY KEY (`subject_id`),
  KEY `responsible_teacher_id` (`responsible_teacher_id`),
  CONSTRAINT `Subject_ibfk_1` FOREIGN KEY (`responsible_teacher_id`) REFERENCES `Teacher` (`teacher_id`)
)

CREATE TABLE `Teacher` (
  `teacher_id` int NOT NULL,
  `user_id` int DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`teacher_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `Teacher_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `User` (`user_id`)
)
CREATE TABLE `User` (
  `user_id` int NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `role_id` int DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `User_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `Role` (`role_id`)
)

CREATE TABLE `ZamjenaPredmeta` (
  `zamjenaID` int NOT NULL AUTO_INCREMENT,
  `predmet1` int DEFAULT NULL,
  `predmet2` int DEFAULT NULL,
  `student_id` int NOT NULL,
  PRIMARY KEY (`zamjenaID`),
  KEY `ZamjenaPredmeta_Student_student_id_fk` (`student_id`),
  CONSTRAINT `ZamjenaPredmeta_Student_student_id_fk` FOREIGN KEY (`student_id`) REFERENCES `Student` (`student_id`)
)

