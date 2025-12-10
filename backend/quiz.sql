-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: quiz
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `quiz`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `quiz` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `quiz`;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `questionText` varchar(255) NOT NULL,
  `answer1Text` varchar(255) NOT NULL,
  `answer1Correct` tinyint(1) NOT NULL DEFAULT '0',
  `answer2Text` varchar(255) NOT NULL,
  `answer2Correct` tinyint(1) NOT NULL DEFAULT '0',
  `answer3Text` varchar(255) NOT NULL,
  `answer3Correct` tinyint(1) NOT NULL DEFAULT '0',
  `answer4Text` varchar(255) NOT NULL,
  `answer4Correct` tinyint(1) NOT NULL DEFAULT '0',
  `isDelete` tinyint DEFAULT NULL,
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
INSERT INTO `questions` VALUES (1,'What is the capital of France?','Paris',1,'London',0,'Berlin',0,'Madrid',0,1,'2025-11-28 10:07:10','2025-11-28 10:07:10'),(2,'which of the following is not a programming language?','Java',0,'Apple',1,'Python',0,'Javascript',0,0,'2025-11-28 10:24:59','2025-11-28 10:24:59'),(3,'以下谁没有Major','donk',0,'ZywOo',0,'NiKo',1,'ropz',0,0,'2025-11-28 10:57:28','2025-11-28 10:57:28'),(4,'以下谁evp数最多','donk',0,'ZywOo',0,'NiKo',1,'ropz',0,0,'2025-11-28 11:39:05','2025-11-28 11:39:05'),(5,'以下谁mvp数最多','donk',0,'ZywOo',1,'NiKo',0,'ropz',0,0,'2025-11-28 11:39:15','2025-11-28 11:39:15'),(6,'谁是2025年top2','donk',0,'ZywOo',0,'NiKo',0,'m0NESY',1,0,'2025-12-01 15:09:03','2025-12-01 15:09:03'),(7,'test','1',1,'2',0,'3',0,'4',0,0,'2025-12-01 15:33:58','2025-12-01 15:33:58'),(8,'2','1',0,'2',1,'3',0,'4',0,0,'2025-12-01 15:51:14','2025-12-01 15:51:14'),(9,'3','1',0,'2',0,'3',1,'4',0,0,'2025-12-01 15:51:40','2025-12-01 15:51:40'),(10,'4','1',0,'2',0,'3',0,'4',1,0,'2025-12-01 16:17:43','2025-12-01 16:17:43'),(12,'test','a',0,'aa',1,'aaa',0,'aaaa',0,0,'2025-12-01 16:29:33','2025-12-01 16:29:33'),(13,'22','1',1,'2',0,'3',0,'4',0,0,'2025-12-01 16:31:33','2025-12-01 16:31:33'),(14,'222','1',0,'2',1,'3',0,'4',0,0,'2025-12-01 16:31:41','2025-12-01 16:31:41'),(15,'2222','11',0,'22',0,'3',0,'4',1,0,'2025-12-01 16:31:50','2025-12-01 16:57:28'),(16,'22222','1',0,'2',0,'3',1,'4',0,0,'2025-12-01 16:31:58','2025-12-01 16:31:58'),(17,'12','11',0,'22',0,'33',1,'44',0,0,'2025-12-01 17:12:18','2025-12-01 17:12:18'),(18,'What is the capital of France?','Paris',1,'London',0,'Berlin',0,'Madrid',0,0,'2025-12-10 16:16:46','2025-12-10 16:16:46');
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `userName` varchar(256) DEFAULT NULL COMMENT '用户名',
  `userPassword` varchar(512) DEFAULT NULL COMMENT '密码',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `isDelete` tinyint DEFAULT NULL,
  `userRole` int DEFAULT '0' COMMENT '表示用户角色， 0 普通用户， 1 管理员',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Hooxi','abb283b0200e38c5676e51f4ee23f28b','2025-12-10 16:16:45','2025-11-21 11:04:48',1,0),(2,'test','186627db971d85322830781dc4400479','2025-12-05 10:37:13','2025-11-21 11:05:23',0,1),(3,'Niko','44c8fda24b6d4b9411f179294341eb6c','2025-12-05 09:59:40','2025-11-21 11:05:40',0,0),(4,'Monesy','abb283b0200e38c5676e51f4ee23f28b','2025-12-01 18:40:56','2025-11-21 11:05:47',0,0),(5,'Hunter','abb283b0200e38c5676e51f4ee23f28b','2025-12-05 09:58:52','2025-11-21 11:05:52',0,0),(6,'donk','186627db971d85322830781dc4400479','2025-12-05 09:59:30','2025-11-21 11:06:12',0,0),(7,'apex','186627db971d85322830781dc4400479','2025-12-05 09:59:01','2025-11-21 11:06:18',0,0),(8,'tom','186627db971d85322830781dc4400479','2025-12-05 09:59:08','2025-11-21 11:06:23',0,0),(9,'joe','186627db971d85322830781dc4400479','2025-12-05 09:59:15','2025-11-21 11:06:27',0,1),(10,'Zywoo','abb283b0200e38c5676e51f4ee23f28b','2025-12-01 18:56:06','2025-11-21 11:06:33',0,0),(11,'FlameZ','abb283b0200e38c5676e51f4ee23f28b','2025-11-21 11:07:01','2025-11-21 11:07:01',0,0),(12,'ropz','abb283b0200e38c5676e51f4ee23f28b','2025-11-21 11:07:07','2025-11-21 11:07:07',0,0),(13,'testuser','password123','2025-11-21 11:18:48','2025-11-21 11:18:48',0,0),(14,'test1','186627db971d85322830781dc4400479','2025-12-01 18:37:12','2025-12-01 18:24:22',0,1),(15,'test2','186627db971d85322830781dc4400479','2025-12-01 18:36:48','2025-12-01 18:25:27',0,0),(16,'123','eb621c6d7a19086b88a60cd0cfee0872','2025-12-01 18:41:10','2025-12-01 18:41:10',0,0),(17,'test11','186627db971d85322830781dc4400479','2025-12-05 09:58:03','2025-12-05 09:58:03',0,0),(18,'testuser','password123','2025-12-10 16:16:46','2025-12-10 16:16:46',0,0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-10 16:17:51
