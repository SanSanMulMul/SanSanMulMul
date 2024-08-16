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
-- Table structure for table `hiking_record`
--

DROP TABLE IF EXISTS `hiking_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hiking_record` (
  `record_id` int NOT NULL AUTO_INCREMENT COMMENT '기록 고유번호',
  `crew_id` int NOT NULL COMMENT '그룹 고유번호',
  `mountain_id` int NOT NULL COMMENT '산 고유번호',
  `user_id` int NOT NULL COMMENT '회원 고유번호',
  `record_start_time` datetime NOT NULL COMMENT 'YYYY-MM-DD 23:22:00',
  `record_end_time` datetime NOT NULL COMMENT 'YYYY-MM-DD 23:22:00',
  `record_distance` double NOT NULL DEFAULT '0' COMMENT '단위(m)',
  `record_duration` int unsigned NOT NULL DEFAULT '0' COMMENT '단위(분)',
  `record_steps` int unsigned NOT NULL DEFAULT '0',
  `record_elevation` double NOT NULL DEFAULT '0',
  `record_kcal` int unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  KEY `fk_crew_id_hiking_record` (`crew_id`),
  KEY `fk_mountain_id_hiking_record` (`mountain_id`),
  CONSTRAINT `fk_crew_id_hiking_record` FOREIGN KEY (`crew_id`) REFERENCES `crew` (`crew_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_mountain_id_hiking_record` FOREIGN KEY (`mountain_id`) REFERENCES `mountain` (`mountain_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-16 12:33:43
