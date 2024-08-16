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
-- Table structure for table `chk_badge`
--

DROP TABLE IF EXISTS `chk_badge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chk_badge` (
  `chk_id` int NOT NULL AUTO_INCREMENT COMMENT '고유번호',
  `user_id` int NOT NULL COMMENT '회원 고유번호',
  `chk_hanra` tinyint(1) NOT NULL DEFAULT '0' COMMENT '한라산 등산 여부 확인',
  `chk_jiri` tinyint(1) NOT NULL DEFAULT '0' COMMENT '지리산 등산 여부 확인',
  `chk_seolak` tinyint(1) NOT NULL DEFAULT '0' COMMENT '설악산 등산 여부 확인',
  `chk_mudeung` tinyint(1) NOT NULL DEFAULT '0' COMMENT '무등산 등산 여부 확인',
  `chk_gyeryong` tinyint(1) NOT NULL DEFAULT '0' COMMENT '계룡산 등산 여부 확인',
  `chk_unmun` tinyint(1) NOT NULL DEFAULT '0' COMMENT '운문산 등산 여부 확인',
  `chk_gazi` tinyint(1) NOT NULL DEFAULT '0' COMMENT '가지산 등산 여부 확인',
  `chk_chunhwang` tinyint(1) NOT NULL DEFAULT '0' COMMENT '천황산 등산 여부 확인',
  `chk_jaeyak` tinyint(1) NOT NULL DEFAULT '0' COMMENT '재약산 등산 여부 확인',
  `chk_youngchuck` tinyint(1) NOT NULL DEFAULT '0' COMMENT '영축산 등산 여부 확인',
  `chk_sinbool` tinyint(1) NOT NULL DEFAULT '0' COMMENT '신불산 등산 여부 확인',
  `chk_ganweol` tinyint(1) NOT NULL DEFAULT '0' COMMENT '간월산 등산 여부 확인',
  `chk_gohun` tinyint(1) NOT NULL DEFAULT '0' COMMENT '고헌산 등산 여부 확인',
  PRIMARY KEY (`chk_id`),
  KEY `FK_user_id_chk-badge` (`user_id`),
  CONSTRAINT `FK_user_id_chk-badge` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-16 12:33:44
