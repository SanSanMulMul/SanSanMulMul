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
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '회원 고유번호',
  `user_provider_id` varchar(50) NOT NULL COMMENT '카카오에서 제공하는 아이디',
  `user_password` varchar(255) NOT NULL DEFAULT '' COMMENT '회원 비밀번호, 설정 X',
  `user_refresh_token` varchar(255) DEFAULT NULL COMMENT '리프레시 토큰 값',
  `user_name` varchar(60) NOT NULL COMMENT '카카오톡 이름 길이 제한: 20',
  `user_nickname` varchar(60) NOT NULL COMMENT '이름 길이 제한과 같이 20',
  `user_gender` enum('M','F') NOT NULL COMMENT 'M:남자, F:여자',
  `user_profile_img` varchar(200) DEFAULT NULL COMMENT '이미지 URL',
  `user_birth` date NOT NULL COMMENT 'YYYY-MM-DD',
  `user_static_badge` tinyint NOT NULL DEFAULT '1' COMMENT '회원의 지정 칭호',
  `user_total_length` double(10,6) NOT NULL DEFAULT '0.000000' COMMENT '단위(m)',
  `user_total_elevation` double(10,6) NOT NULL DEFAULT '0.000000' COMMENT '단위(m)',
  `user_total_steps` int unsigned NOT NULL DEFAULT '0',
  `user_total_kcal` int unsigned NOT NULL DEFAULT '0',
  `user_total_hiking` int unsigned NOT NULL DEFAULT '0' COMMENT '단위(횟수)',
  `user_stone_count` tinyint unsigned NOT NULL DEFAULT '0' COMMENT 'MAX: 100',
  `user_is_admin` tinyint(1) NOT NULL DEFAULT '0' COMMENT '관리자 여부',
  `user_fcm_token` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `unique_user_provider_id` (`user_provider_id`),
  UNIQUE KEY `user_nickname_UNIQUE` (`user_nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='사용자 정보를 저장하는 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-16 12:33:41
