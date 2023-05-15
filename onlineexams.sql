/*
 Navicat Premium Data Transfer

 Source Server         : quake
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : onlineexams

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 15/05/2023 17:35:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for class
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class`  (
  `ClassId` char(7) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `ClassName` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ClassId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for groupvolumes
-- ----------------------------
DROP TABLE IF EXISTS `groupvolumes`;
CREATE TABLE `groupvolumes`  (
  `PageId` char(5) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `Tno` char(12) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `ClassId` char(7) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `SubjectId` char(5) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `state` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`PageId`, `Tno`, `ClassId`, `SubjectId`) USING BTREE,
  INDEX `Tno_foreign_key2`(`Tno`) USING BTREE,
  INDEX `ClassId_foreign_key3`(`ClassId`) USING BTREE,
  INDEX `SubjectId_foreign3`(`SubjectId`) USING BTREE,
  CONSTRAINT `ClassId_foreign_key3` FOREIGN KEY (`ClassId`) REFERENCES `class` (`ClassId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PageId_foreign_key` FOREIGN KEY (`PageId`) REFERENCES `page` (`PageId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `SubjectId_foreign3` FOREIGN KEY (`SubjectId`) REFERENCES `subject` (`SubjectId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Tno_foreign_key2` FOREIGN KEY (`Tno`) REFERENCES `teacher` (`Tno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for objectivequestions
-- ----------------------------
DROP TABLE IF EXISTS `objectivequestions`;
CREATE TABLE `objectivequestions`  (
  `SubjectId` char(5) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `ProblemId` char(3) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `Answer` char(4) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `Selection1` text CHARACTER SET gbk COLLATE gbk_chinese_ci NULL,
  `Selection2` text CHARACTER SET gbk COLLATE gbk_chinese_ci NULL,
  `Selection3` text CHARACTER SET gbk COLLATE gbk_chinese_ci NULL,
  `Selection4` text CHARACTER SET gbk COLLATE gbk_chinese_ci NULL,
  PRIMARY KEY (`SubjectId`, `ProblemId`) USING BTREE,
  INDEX `ProblemId_foreign_key2`(`ProblemId`) USING BTREE,
  CONSTRAINT `ProblemId_foreign_key2` FOREIGN KEY (`ProblemId`) REFERENCES `questionbank` (`ProblemId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `SubjectId_foreign_key4` FOREIGN KEY (`SubjectId`) REFERENCES `subject` (`SubjectId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for page
-- ----------------------------
DROP TABLE IF EXISTS `page`;
CREATE TABLE `page`  (
  `PageId` char(5) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `SubjectId` char(5) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `ProblemId` char(3) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `ProblemType` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`PageId`, `SubjectId`, `ProblemId`) USING BTREE,
  INDEX `PageId`(`PageId`) USING BTREE,
  INDEX `ProblemId_foreign_key`(`ProblemId`) USING BTREE,
  INDEX `SubjectId_foreign_key3`(`SubjectId`) USING BTREE,
  CONSTRAINT `ProblemId_foreign_key` FOREIGN KEY (`ProblemId`) REFERENCES `questionbank` (`ProblemId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `SubjectId_foreign_key3` FOREIGN KEY (`SubjectId`) REFERENCES `subject` (`SubjectId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for questionbank
-- ----------------------------
DROP TABLE IF EXISTS `questionbank`;
CREATE TABLE `questionbank`  (
  `SubjectId` char(5) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `ProblemId` char(3) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `ProblemType` int(0) NULL DEFAULT NULL,
  `Problem` text CHARACTER SET gbk COLLATE gbk_chinese_ci NULL,
  `grade` decimal(3, 1) NULL DEFAULT NULL,
  PRIMARY KEY (`SubjectId`, `ProblemId`) USING BTREE,
  INDEX `ProblemId`(`ProblemId`) USING BTREE,
  CONSTRAINT `SubjectId_foreign_key2` FOREIGN KEY (`SubjectId`) REFERENCES `subject` (`SubjectId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for score
-- ----------------------------
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score`  (
  `Sno` char(12) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `PageId` char(5) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `PageScore` decimal(4, 1) NULL DEFAULT NULL,
  `ObjectiveScore` decimal(4, 1) NULL DEFAULT NULL,
  `SubjectiveScore` decimal(4, 1) NULL DEFAULT NULL,
  PRIMARY KEY (`Sno`, `PageId`) USING BTREE,
  INDEX `PageId_foreign_key3`(`PageId`) USING BTREE,
  CONSTRAINT `PageId_foreign_key3` FOREIGN KEY (`PageId`) REFERENCES `page` (`PageId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Sno_foreign_key3` FOREIGN KEY (`Sno`) REFERENCES `student` (`Sno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `Sno` char(12) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `Sname` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `Password` char(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '12345',
  `ClassId` char(7) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  PRIMARY KEY (`Sno`) USING BTREE,
  INDEX `ClassId_foreign_key2`(`ClassId`) USING BTREE,
  CONSTRAINT `ClassId_foreign_key2` FOREIGN KEY (`ClassId`) REFERENCES `class` (`ClassId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for studentanswer
-- ----------------------------
DROP TABLE IF EXISTS `studentanswer`;
CREATE TABLE `studentanswer`  (
  `Sno` char(12) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `PageId` char(5) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `ProblemId` char(3) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `Answer` text CHARACTER SET gbk COLLATE gbk_chinese_ci NULL,
  `state` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`Sno`, `PageId`, `ProblemId`) USING BTREE,
  INDEX `PageId_foreign_key2`(`PageId`) USING BTREE,
  INDEX `ProblemId_foreign_key3`(`ProblemId`) USING BTREE,
  CONSTRAINT `PageId_foreign_key2` FOREIGN KEY (`PageId`) REFERENCES `page` (`PageId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ProblemId_foreign_key3` FOREIGN KEY (`ProblemId`) REFERENCES `questionbank` (`ProblemId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Sno_foreign_key1` FOREIGN KEY (`Sno`) REFERENCES `student` (`Sno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for subject
-- ----------------------------
DROP TABLE IF EXISTS `subject`;
CREATE TABLE `subject`  (
  `SubjectId` char(5) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `SubjectName` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SubjectId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for taught
-- ----------------------------
DROP TABLE IF EXISTS `taught`;
CREATE TABLE `taught`  (
  `Tno` char(12) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `ClassId` char(7) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `SubjectId` char(5) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  PRIMARY KEY (`ClassId`, `SubjectId`) USING BTREE,
  INDEX `Tno_foreign_key`(`Tno`) USING BTREE,
  INDEX `SubjectId_foreign_key`(`SubjectId`) USING BTREE,
  CONSTRAINT `ClassId_foreign_key` FOREIGN KEY (`ClassId`) REFERENCES `class` (`ClassId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `SubjectId_foreign_key` FOREIGN KEY (`SubjectId`) REFERENCES `subject` (`SubjectId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Tno_foreign_key` FOREIGN KEY (`Tno`) REFERENCES `teacher` (`Tno`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `Tno` char(12) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `Tname` varchar(4) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `Password` char(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT '12345',
  PRIMARY KEY (`Tno`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Procedure structure for importExamProblem
-- ----------------------------
DROP PROCEDURE IF EXISTS `importExamProblem`;
delimiter ;;
CREATE PROCEDURE `importExamProblem`(in pid char(5), in sid char(5))
begin
    select page.SubjectId, page.ProblemId, page.ProblemType, Problem, grade
    from page join questionbank on page.SubjectId = questionbank.SubjectId and page.ProblemId = questionbank.ProblemId
    where PageId = pid and page.SubjectId = sid;
end
;;
delimiter ;

-- ----------------------------
-- Procedure structure for queryProblemAccordingProblemId
-- ----------------------------
DROP PROCEDURE IF EXISTS `queryProblemAccordingProblemId`;
delimiter ;;
CREATE PROCEDURE `queryProblemAccordingProblemId`(in paid char(5), in prid char(3))
begin
    select Problem,grade
    from questionbank
    where ProblemId = prid and SubjectId in
    (
        select distinct SubjectId
        from groupvolumes
        where PageId = paid
    );
end
;;
delimiter ;

-- ----------------------------
-- Procedure structure for selectSubjectProblemCount
-- ----------------------------
DROP PROCEDURE IF EXISTS `selectSubjectProblemCount`;
delimiter ;;
CREATE PROCEDURE `selectSubjectProblemCount`(in name varchar(20), out problemCount bigint)
begin
    select count(ProblemId) into problemCount
    from questionbank
    where SubjectId in
    (
        select SubjectId
        from subject
        where SubjectName = name
    );
end
;;
delimiter ;

-- ----------------------------
-- Procedure structure for selectTeacherSubjectClass
-- ----------------------------
DROP PROCEDURE IF EXISTS `selectTeacherSubjectClass`;
delimiter ;;
CREATE PROCEDURE `selectTeacherSubjectClass`(in id char(12), in name varchar(20))
begin
    select ClassName
    from class
    where ClassId in
    (
        select ClassId
        from taught join subject on taught.SubjectId = subject.SubjectId
        where Tno = id and SubjectName = name
    );
end
;;
delimiter ;

-- ----------------------------
-- Procedure structure for selectTeacherSubjects
-- ----------------------------
DROP PROCEDURE IF EXISTS `selectTeacherSubjects`;
delimiter ;;
CREATE PROCEDURE `selectTeacherSubjects`(in id char(12))
begin
    select SubjectName
    from subject
    where SubjectId in
    (
        select distinct SubjectId
        from taught
        where Tno = id
    );
end
;;
delimiter ;

-- ----------------------------
-- Procedure structure for studentQueryScore
-- ----------------------------
DROP PROCEDURE IF EXISTS `studentQueryScore`;
delimiter ;;
CREATE PROCEDURE `studentQueryScore`(in sid char(12))
begin
    select distinct subject.SubjectId,SubjectName,score.PageId,PageScore,ObjectiveScore,SubjectiveScore
    from score join groupvolumes on groupvolumes.PageId = score.PageId join subject on groupvolumes.SubjectId = subject.SubjectId
    where Sno = sid and state = 1;
end
;;
delimiter ;

-- ----------------------------
-- Procedure structure for studnetselectExamPage
-- ----------------------------
DROP PROCEDURE IF EXISTS `studnetselectExamPage`;
delimiter ;;
CREATE PROCEDURE `studnetselectExamPage`(in id char(7))
begin
    select PageId,SubjectName
    from groupvolumes join subject on groupvolumes.SubjectId = subject.SubjectId
    where ClassId = id and state = 0;
end
;;
delimiter ;

-- ----------------------------
-- Procedure structure for teacherQueryScore
-- ----------------------------
DROP PROCEDURE IF EXISTS `teacherQueryScore`;
delimiter ;;
CREATE PROCEDURE `teacherQueryScore`(in pid char(5))
begin
    select class.ClassId,ClassName,student.Sno,Sname,ObjectiveScore,SubjectiveScore
    from score join student on score.Sno = student.Sno join class on student.ClassId = class.ClassId
    where PageId = pid;
end
;;
delimiter ;

-- ----------------------------
-- Procedure structure for teacherselectExamPage
-- ----------------------------
DROP PROCEDURE IF EXISTS `teacherselectExamPage`;
delimiter ;;
CREATE PROCEDURE `teacherselectExamPage`(in id char(12), in st int)
begin
    select distinct PageId, SubjectName
    from groupvolumes join subject on groupvolumes.SubjectId = subject.SubjectId
    where Tno = id and state = st;
end
;;
delimiter ;

-- ----------------------------
-- Procedure structure for verifyPageId
-- ----------------------------
DROP PROCEDURE IF EXISTS `verifyPageId`;
delimiter ;;
CREATE PROCEDURE `verifyPageId`(in id char(5))
begin
    select *
    from page
    where PageId = id;
end
;;
delimiter ;

-- ----------------------------
-- Procedure structure for verifyStudentLogin
-- ----------------------------
DROP PROCEDURE IF EXISTS `verifyStudentLogin`;
delimiter ;;
CREATE PROCEDURE `verifyStudentLogin`(in id char(12), in pword char(20))
begin
    select *
    from student
    where Sno=id and Password=password;
end
;;
delimiter ;

-- ----------------------------
-- Procedure structure for verifyTeacherLogin
-- ----------------------------
DROP PROCEDURE IF EXISTS `verifyTeacherLogin`;
delimiter ;;
CREATE PROCEDURE `verifyTeacherLogin`(in id char(12), in pword char(20))
begin
    select *
    from teacher
    where Tno=id and Password=pword;
end
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
