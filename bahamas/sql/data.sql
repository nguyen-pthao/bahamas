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
INSERT INTO `EVENT_CLASS_LIST` VALUES ('Basic Training',0),('Advanced Training',0),('Internal',0),('Open to public',0),('Template',0),('Other',0);
/*!40000 ALTER TABLE `EVENT_CLASS_LIST` ENABLE KEYS */;

--
-- Dumping data for table `EVENT_LOCATION_LIST`
--

/*!40000 ALTER TABLE `EVENT_LOCATION_LIST` DISABLE KEYS */;
INSERT INTO `EVENT_LOCATION_LIST` VALUES ('Alankar' ,NULL , NULL ,0),('Isthana' ,NULL , NULL ,0),('Online' ,NULL , NULL ,0),('Other' ,NULL , NULL ,0),('TWC2 DaySpace' ,NULL , NULL ,0),('TWC2 office' ,NULL , NULL ,0);
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
INSERT INTO `EVENT_STATUS_LIST` VALUES ('Cancelled',0),('Filled',0),('Open',0),('Postponed',0);
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
INSERT INTO `LANGUAGE_LIST` VALUES ('Bengali',0),('Burmese',0),('Cambodian',0),('Chinese',0),('English',0),('Filipino',0),('Hindi',0),('Indonesian',0),('Malay',0),('Other',0),('Punjabi',0),('Sinhala',0),('Tamil',0),('Telugu',0),('Vietnamese',0);
/*!40000 ALTER TABLE `LANGUAGE_LIST` ENABLE KEYS */;

--
-- Dumping data for table `LSA_CLASS_LIST`
--

/*!40000 ALTER TABLE `LSA_CLASS_LIST` DISABLE KEYS */;
INSERT INTO `LSA_CLASS_LIST` VALUES ('Academia',0),('Accounting',0),('Counselling',0),('Dental',0),('Event organising',0),('Has event space',0),('Has shelter space',0),('Has vehicle',0),('Infotech',0),('Legal',0),('Media & Comms',0),('Medical',0),('Other',0),('Pharma',0),('Photo, video & design',0),('Physio',0),('Pro-bono lawyer',0);
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
INSERT INTO `MEMBERSHIP_CLASS_LIST` VALUES ('Associate',0),('Discounted',0),('Honorary',0),('Ordinary',0),('Other',0);
/*!40000 ALTER TABLE `MEMBERSHIP_CLASS_LIST` ENABLE KEYS */;

--
-- Dumping data for table `MODE_OF_SENDING_RECEIPT_LIST`
--

/*!40000 ALTER TABLE `MODE_OF_SENDING_RECEIPT_LIST` DISABLE KEYS */;
INSERT INTO `MODE_OF_SENDING_RECEIPT_LIST` VALUES ('Email',0),('No address',0),('Other',0),('Post',0);
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
INSERT INTO `OFFICE_LIST` VALUES ('President',0),('Secretary',0),('Staff',0),('Treasurer',0),('Vice-president',0);
/*!40000 ALTER TABLE `OFFICE_LIST` ENABLE KEYS */;

--
-- Dumping data for table `PAYMENT_MODE_LIST`
--

/*!40000 ALTER TABLE `PAYMENT_MODE_LIST` DISABLE KEYS */;
INSERT INTO `PAYMENT_MODE_LIST` VALUES ('Bank direct',0),('Cash',0),('Cheque',0),('Giving Sg',0),('Other',0),('Paypal',0);
/*!40000 ALTER TABLE `PAYMENT_MODE_LIST` ENABLE KEYS */;

--
-- Dumping data for table `PERMISSION_LEVEL_LIST`
--

/*!40000 ALTER TABLE `PERMISSION_LEVEL_LIST` DISABLE KEYS */;
INSERT INTO `PERMISSION_LEVEL_LIST` VALUES ('Associate','Joins event',0),('Event leader','Manages an event',0),('Team manager','Manages a team',0);
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
INSERT INTO `TEAM_AFFILIATION_LIST` VALUES ('Casework','This is Casework team',0),('Communications','This is Communications team',0),('Discover Sg','This is Discover Sg team',0),('FareGo','This is FareGo team',0),('Fundraising','This is Fundraising team',0),('Intern','This is Intern team',0),('Lifeline','This is Lifeline team',0),('Outreach','This is Outreach team',0),('Public Engagement','This is Public Engagement team',0),('R2R','This is R2R team',0),('Research','This is Research team',0),('Roof','This is Roof team',0),('TCRP','This is TCRP team',0);
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
INSERT INTO `TYPE_OF_CONTACT_LIST` VALUES ('Corporate',0),('Diplomatic',0),('Educational',0),('Employer',0),('Event services',0),('Financial',0),('Financial services',0),('Foundation',0),('Government-linked',0),('Individual',0),('Job agent',0),('Legal',0),('Maintenance & repair',0),('Media abroad',0),('Media local',0),('Medical',0),('Nonprofit abroad',0),('Nonprofit local',0),('Office services',0),('Other',0),('Product supplier',0),('Religious',0),('TWC2',0);
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
