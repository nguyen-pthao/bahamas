-- MySQL dump 10.13  Distrib 5.7.12, for osx10.9 (x86_64)
--
-- Host: 127.0.0.1    Database: bahamas
-- ------------------------------------------------------
-- Server version	5.5.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `ADDRESS`
--

/*!40000 ALTER TABLE `ADDRESS` DISABLE KEYS */;
/*!40000 ALTER TABLE `ADDRESS` ENABLE KEYS */;

--
-- Dumping data for table `APPRECIATION`
--

/*!40000 ALTER TABLE `APPRECIATION` DISABLE KEYS */;
/*!40000 ALTER TABLE `APPRECIATION` ENABLE KEYS */;

--
-- Dumping data for table `CONTACT`
--

/*!40000 ALTER TABLE `CONTACT` DISABLE KEYS */;
INSERT INTO `CONTACT` VALUES (1,'TWC2','admin1','GwvbMoaGY9yVMnOiMfujehEdmE7jLDXX8DZAM1uUcTw=','1/rqffBv5u4=',1,0,0,'2016-05-24 23:00:00','admin','System Admin','Admin','','IT','TSO','S1234567A','M','Singapore','1977-02-22','','',1);
/*!40000 ALTER TABLE `CONTACT` ENABLE KEYS */;

--
-- Dumping data for table `DONATION`
--

/*!40000 ALTER TABLE `DONATION` DISABLE KEYS */;
/*!40000 ALTER TABLE `DONATION` ENABLE KEYS */;

--
-- Dumping data for table `EMAIL`
--

/*!40000 ALTER TABLE `EMAIL` DISABLE KEYS */;
/*!40000 ALTER TABLE `EMAIL` ENABLE KEYS */;

--
-- Dumping data for table `EVENT`
--

/*!40000 ALTER TABLE `EVENT` DISABLE KEYS */;
/*!40000 ALTER TABLE `EVENT` ENABLE KEYS */;

--
-- Dumping data for table `EVENT_AFFILIATION`
--

/*!40000 ALTER TABLE `EVENT_AFFILIATION` DISABLE KEYS */;
/*!40000 ALTER TABLE `EVENT_AFFILIATION` ENABLE KEYS */;

--
-- Dumping data for table `EVENT_CLASS_LIST`
--

/*!40000 ALTER TABLE `EVENT_CLASS_LIST` DISABLE KEYS */;
INSERT INTO `EVENT_CLASS_LIST` VALUES ('Basic Training',1),('Advanced Training',2),('Internal',3),('Open to public',4),('Template',5),('Other',6);
/*!40000 ALTER TABLE `EVENT_CLASS_LIST` ENABLE KEYS */;

--
-- Dumping data for table `EVENT_LOCATION_LIST`
--

/*!40000 ALTER TABLE `EVENT_LOCATION_LIST` DISABLE KEYS */;
INSERT INTO `EVENT_LOCATION_LIST` VALUES ('Alankar' ,'98 Dunlop Street' , '20941' ,1),('Isthana' ,'1C Rowell Road #01-01' , '207958' ,2),('Online' ,NULL , NULL ,3),('TWC2 DaySpace' ,NULL , NULL ,4),('TWC2 office' ,'5001 Beach Road #09-86 Golden Mile Complex' , '199588' ,5),('Other' ,NULL , NULL ,6);
/*!40000 ALTER TABLE `EVENT_LOCATION_LIST` ENABLE KEYS */;

--
-- Dumping data for table `EVENT_PARTICIPANT`
--

/*!40000 ALTER TABLE `EVENT_PARTICIPANT` DISABLE KEYS */;
/*!40000 ALTER TABLE `EVENT_PARTICIPANT` ENABLE KEYS */;

--
-- Dumping data for table `EVENT_ROLE_ASSIGNMENT`
--

/*!40000 ALTER TABLE `EVENT_ROLE_ASSIGNMENT` DISABLE KEYS */;
/*!40000 ALTER TABLE `EVENT_ROLE_ASSIGNMENT` ENABLE KEYS */;

--
-- Dumping data for table `EVENT_STATUS_LIST`
--

/*!40000 ALTER TABLE `EVENT_STATUS_LIST` DISABLE KEYS */;
INSERT INTO `EVENT_STATUS_LIST` VALUES ('Cancelled',4),('Filled',2),('Open',1),('Postponed',3);
/*!40000 ALTER TABLE `EVENT_STATUS_LIST` ENABLE KEYS */;

--
-- Dumping data for table `LANGUAGE_ASSIGNMENT`
--

/*!40000 ALTER TABLE `LANGUAGE_ASSIGNMENT` DISABLE KEYS */;
/*!40000 ALTER TABLE `LANGUAGE_ASSIGNMENT` ENABLE KEYS */;

--
-- Dumping data for table `LANGUAGE_LIST`
--

/*!40000 ALTER TABLE `LANGUAGE_LIST` DISABLE KEYS */;
INSERT INTO `LANGUAGE_LIST` VALUES ('Bengali',1),('Burmese',2),('Cambodian',3),('Chinese',4),('English',5),('Filipino',6),('Hindi',7),('Indonesian',8),('Malay',9),('Punjabi',10),('Sinhala',11),('Tamil',12),('Telugu',13),('Vietnamese',14),('Other',15);
/*!40000 ALTER TABLE `LANGUAGE_LIST` ENABLE KEYS */;

--
-- Dumping data for table `LSA_CLASS_LIST`
--

/*!40000 ALTER TABLE `LSA_CLASS_LIST` DISABLE KEYS */;
INSERT INTO `LSA_CLASS_LIST` VALUES ('Academia',1),('Accounting',2),('Counselling',3),('Dental',4),('Event organising',5),('Has event space',6),('Has shelter space',7),('Has vehicle',8),('Infotech',9),('Legal',10),('Media & Comms',11),('Medical',12),('Pharma',13),('Photo, video & design',14),('Physio',15),('Pro-bono lawyer',16),('Other',17);
/*!40000 ALTER TABLE `LSA_CLASS_LIST` ENABLE KEYS */;

--
-- Dumping data for table `MEMBERSHIP`
--

/*!40000 ALTER TABLE `MEMBERSHIP` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEMBERSHIP` ENABLE KEYS */;

--
-- Dumping data for table `MEMBERSHIP_CLASS_LIST`
--

/*!40000 ALTER TABLE `MEMBERSHIP_CLASS_LIST` DISABLE KEYS */;
INSERT INTO `MEMBERSHIP_CLASS_LIST` VALUES ('Associate',1),('Discounted',2),('Honorary',3),('Ordinary',4),('Other',5);
/*!40000 ALTER TABLE `MEMBERSHIP_CLASS_LIST` ENABLE KEYS */;

--
-- Dumping data for table `MODE_OF_SENDING_RECEIPT_LIST`
--

/*!40000 ALTER TABLE `MODE_OF_SENDING_RECEIPT_LIST` DISABLE KEYS */;
INSERT INTO `MODE_OF_SENDING_RECEIPT_LIST` VALUES ('Email',1),('No address',2),('Post',3),('Other',4);
/*!40000 ALTER TABLE `MODE_OF_SENDING_RECEIPT_LIST` ENABLE KEYS */;

--
-- Dumping data for table `NOTICE`
--

/*!40000 ALTER TABLE `NOTICE` DISABLE KEYS */;
/*!40000 ALTER TABLE `NOTICE` ENABLE KEYS */;

--
-- Dumping data for table `OFFICE_HELD`
--

/*!40000 ALTER TABLE `OFFICE_HELD` DISABLE KEYS */;
/*!40000 ALTER TABLE `OFFICE_HELD` ENABLE KEYS */;

--
-- Dumping data for table `OFFICE_LIST`
--

/*!40000 ALTER TABLE `OFFICE_LIST` DISABLE KEYS */;
INSERT INTO `OFFICE_LIST` VALUES ('President',1),('Secretary',2),('Staff',3),('Treasurer',4),('Vice-president',5);
/*!40000 ALTER TABLE `OFFICE_LIST` ENABLE KEYS */;

--
-- Dumping data for table `PAYMENT_MODE_LIST`
--

/*!40000 ALTER TABLE `PAYMENT_MODE_LIST` DISABLE KEYS */;
INSERT INTO `PAYMENT_MODE_LIST` VALUES ('Bank direct',1),('Cash',2),('Cheque',3),('Giving Sg',4),('Paypal',5),('Other',6);
/*!40000 ALTER TABLE `PAYMENT_MODE_LIST` ENABLE KEYS */;

--
-- Dumping data for table `PERMISSION_LEVEL_LIST`
--

/*!40000 ALTER TABLE `PERMISSION_LEVEL_LIST` DISABLE KEYS */;
INSERT INTO `PERMISSION_LEVEL_LIST` VALUES ('Associate','Joins event',1),('Event leader','Manages an event',2),('Team manager','Manages a team',3);
/*!40000 ALTER TABLE `PERMISSION_LEVEL_LIST` ENABLE KEYS */;

--
-- Dumping data for table `PHONE`
--

/*!40000 ALTER TABLE `PHONE` DISABLE KEYS */;
/*!40000 ALTER TABLE `PHONE` ENABLE KEYS */;

--
-- Dumping data for table `PROXY`
--

/*!40000 ALTER TABLE `PROXY` DISABLE KEYS */;
/*!40000 ALTER TABLE `PROXY` ENABLE KEYS */;

--
-- Dumping data for table `SKILL_ASSIGNMENT`
--

/*!40000 ALTER TABLE `SKILL_ASSIGNMENT` DISABLE KEYS */;
/*!40000 ALTER TABLE `SKILL_ASSIGNMENT` ENABLE KEYS */;

--
-- Dumping data for table `TEAM_AFFILIATION_LIST`
--

/*!40000 ALTER TABLE `TEAM_AFFILIATION_LIST` DISABLE KEYS */;
INSERT INTO `TEAM_AFFILIATION_LIST` VALUES ('Casework','This is Casework team',1),('Communications','This is Communications team',2),('Discover Sg','This is Discover Sg team',3),('FareGo','This is FareGo team',4),('Fundraising','This is Fundraising team',5),('Intern','This is Intern team',6),('Lifeline','This is Lifeline team',7),('Outreach','This is Outreach team',8),('Public Engagement','This is Public Engagement team',9),('R2R','This is R2R team',10),('Research','This is Research team',11),('Roof','This is Roof team',12),('TCRP','This is TCRP team',13),('Other','This is Other',14);
/*!40000 ALTER TABLE `TEAM_AFFILIATION_LIST` ENABLE KEYS */;

--
-- Dumping data for table `TEAM_JOIN`
--

/*!40000 ALTER TABLE `TEAM_JOIN` DISABLE KEYS */;
/*!40000 ALTER TABLE `TEAM_JOIN` ENABLE KEYS */;

--
-- Dumping data for table `TRAINING_CERTIFICATED`
--

/*!40000 ALTER TABLE `TRAINING_CERTIFICATED` DISABLE KEYS */;
/*!40000 ALTER TABLE `TRAINING_CERTIFICATED` ENABLE KEYS */;

--
-- Dumping data for table `TYPE_OF_CONTACT_LIST`
--

/*!40000 ALTER TABLE `TYPE_OF_CONTACT_LIST` DISABLE KEYS */;
INSERT INTO `TYPE_OF_CONTACT_LIST` VALUES ('Corporate',1),('Diplomatic',2),('Educational',3),('Employer',4),('Event services',5),('Financial',6),('Financial services',7),('Foundation',8),('Government-linked',9),('Individual',10),('Job agent',11),('Legal',12),('Maintenance & repair',13),('Media abroad',14),('Media local',15),('Medical',16),('Nonprofit abroad',17),('Nonprofit local',18),('Office services',19),('Product supplier',20),('Religious',21),('TWC2',22),('Other',23);
/*!40000 ALTER TABLE `TYPE_OF_CONTACT_LIST` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-07-29 15:23:50
