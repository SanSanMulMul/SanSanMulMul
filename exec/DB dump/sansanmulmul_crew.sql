-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 13.125.228.59    Database: sansanmulmul
-- ------------------------------------------------------
-- Server version	8.0.38

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `crew`
--

DROP TABLE IF EXISTS `crew`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `crew` (
  `crew_id` int NOT NULL AUTO_INCREMENT COMMENT '크루 고유번호',
  `user_id` int NOT NULL COMMENT '방장 고유번호',
  `mountain_id` int NOT NULL COMMENT '산 고유번호',
  `crew_up_course_id` bigint NOT NULL COMMENT '상행 코스 고유번호',
  `crew_down_course_id` bigint NOT NULL COMMENT '하행 코스 고유번호',
  `crew_name` varchar(20) NOT NULL COMMENT '방제목',
  `crew_description` varchar(200) NOT NULL COMMENT '크루 설명',
  `crew_min_age` tinyint NOT NULL DEFAULT '10' COMMENT '최소 나이',
  `crew_max_age` tinyint NOT NULL DEFAULT '90' COMMENT '최대 나이',
  `crew_gender` enum('M','F','A') NOT NULL DEFAULT 'A' COMMENT 'M:남자,F:여자,A:성별무관',
  `crew_max_member` tinyint NOT NULL DEFAULT '10' COMMENT '크루 최대 인원',
  `crew_start_date` datetime NOT NULL COMMENT '크루 산행 시작 일시',
  `crew_end_date` datetime NOT NULL COMMENT '크루 산행 종료 일시',
  `crew_is_done` tinyint(1) NOT NULL DEFAULT '0' COMMENT '방장이 크루 종료했는지 확인',
  `crew_link` varchar(255) DEFAULT 'link is yet',
  `crew_created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '크루 생성일',
  `crew_modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '크루 수정일',
  `crew_is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '크루 삭제 여부',
  PRIMARY KEY (`crew_id`),
  KEY `FK_user_user-crew` (`user_id`),
  KEY `FK_mountain_id_user-crew` (`mountain_id`),
  KEY `FK_crew_up_course_id_user-crew` (`crew_up_course_id`),
  KEY `FK_crew_down_course_id_user-crew` (`crew_down_course_id`),
  CONSTRAINT `FK_crew_down_course_id_user-crew` FOREIGN KEY (`crew_down_course_id`) REFERENCES `course` (`course_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_crew_up_course_id_user-crew` FOREIGN KEY (`crew_up_course_id`) REFERENCES `course` (`course_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_mountain_id_user-crew` FOREIGN KEY (`mountain_id`) REFERENCES `mountain` (`mountain_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_user_user-crew` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=148 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='크루 정보를 저장하는 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-16 12:33:47
