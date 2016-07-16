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
-- Table structure for table `ADDRESS`
--
drop schema if exists bahamas;
create schema bahamas;
use bahamas;

DROP TABLE IF EXISTS `ADDRESS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ADDRESS` (
  `ADDRESS_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONTACT_ID` int(11) NOT NULL,
  `DATE_CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `ADDRESS` varchar(1000) NOT NULL,
  `COUNTRY` varchar(50) DEFAULT NULL,
  `ZIPCODE` varchar(20) DEFAULT NULL,
  `REMARKS` varchar(1000) DEFAULT NULL,
  `DATE_OBSOLETE` date DEFAULT NULL,
  PRIMARY KEY (`ADDRESS_ID`),
  KEY `ADDRESS_FK1` (`CONTACT_ID`),
  CONSTRAINT `ADDRESS_FK1` FOREIGN KEY (`CONTACT_ID`) REFERENCES `CONTACT` (`CONTACT_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `APPRECIATION`
--

DROP TABLE IF EXISTS `APPRECIATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `APPRECIATION` (
  `APPRECIATION_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONTACT_ID` int(11) NOT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `DATE_CREATED` datetime NOT NULL,
  `APPRAISAL_COMMENTS` varchar(500) DEFAULT NULL,
  `APPRAISAL_BY` varchar(50) DEFAULT NULL,
  `APPRAISAL_DATE` date DEFAULT NULL,
  `APPRECIATION_GESTURE` varchar(500) DEFAULT NULL,
  `APPRECIATION_BY` varchar(50) DEFAULT NULL,
  `APPRECIATION_DATE` date DEFAULT NULL,
  `REMARKS` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`APPRECIATION_ID`),
  KEY `APPRECIATION_FK1` (`CONTACT_ID`),
  CONSTRAINT `APPRECIATION_FK1` FOREIGN KEY (`CONTACT_ID`) REFERENCES `CONTACT` (`CONTACT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AUDITLOG`
--

DROP TABLE IF EXISTS `AUDITLOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AUDITLOG` (
  `AUDITLOG_ID` int(11) NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(50) NOT NULL,
  `DATE` datetime NOT NULL,
  `OPERATION` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`AUDITLOG_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CONTACT`
--

DROP TABLE IF EXISTS `CONTACT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CONTACT` (
  `CONTACT_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONTACT_TYPE` varchar(50) NOT NULL,
  `USERNAME` varchar(50) DEFAULT NULL,
  `PASSWORD` char(44) DEFAULT NULL,
  `SALT` char(12) DEFAULT NULL,
  `ISADMIN` tinyint(1) DEFAULT '0',
  `ISNOVICE` tinyint(1) DEFAULT '1',
  `DEACTIVATED` tinyint(1) DEFAULT '0',
  `DATE_CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `ALT_NAME` varchar(50) DEFAULT NULL,
  `EXPLAIN_IF_OTHER` varchar(200) DEFAULT NULL,
  `PROFESSION` varchar(200) DEFAULT NULL,
  `JOB_TITLE` varchar(50) DEFAULT NULL,
  `NRIC_FIN` varchar(9) DEFAULT NULL,
  `GENDER` varchar(1) DEFAULT NULL,
  `NATIONALITY` varchar(20) DEFAULT NULL,
  `DATE_OF_BIRTH` date DEFAULT NULL,
  `PROFILE_PIC` varchar(500) DEFAULT NULL,
  `REMARKS` varchar(1000) DEFAULT NULL,
  `NOTIFICATION` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`CONTACT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DONATION`
--

DROP TABLE IF EXISTS `DONATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DONATION` (
  `DONATION_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONTACT_ID` int(11) NOT NULL,
  `DATE_CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `DATE_RECEIVED` date NOT NULL,
  `DONATION_AMOUNT` float NOT NULL,
  `PAYMENT_MODE_NAME` varchar(50) NOT NULL,
  `EXPLAIN_IF_OTHER_PAYMENT` varchar(200) DEFAULT NULL,
  `EXT_TRANSACTION_REF` varchar(50) DEFAULT NULL,
  `RECEIPT_NUMBER` varchar(50) DEFAULT NULL,
  `RECEIPT_DATE` date DEFAULT NULL,
  `RECEIPT_MODE_NAME` varchar(50) DEFAULT NULL,
  `EXPLAIN_IF_OTHER_RECEIPT` varchar(200) DEFAULT NULL,
  `DONOR_INSTRUCTIONS` varchar(1000) DEFAULT NULL,
  `ALLOCATION_1` varchar(200) DEFAULT NULL,
  `SUBAMOUNT_1` float DEFAULT NULL,
  `ALLOCATION_2` varchar(200) DEFAULT NULL,
  `SUBAMOUNT_2` float DEFAULT NULL,
  `ALLOCATION_3` varchar(200) DEFAULT NULL,
  `SUBAMOUNT_3` float DEFAULT NULL,
  `ASSOCIATED_OCCASION` varchar(500) DEFAULT NULL,
  `REMARKS` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`DONATION_ID`),
  KEY `DONATION_FK1` (`CONTACT_ID`),
  CONSTRAINT `DONATION_FK1` FOREIGN KEY (`CONTACT_ID`) REFERENCES `CONTACT` (`CONTACT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMAIL`
--

DROP TABLE IF EXISTS `EMAIL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EMAIL` (
  `CONTACT_ID` int(11) NOT NULL,
  `DATE_CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `EMAIL` varchar(50) NOT NULL,
  `VERIFIED` tinyint(1) DEFAULT '0',
  `VERIFICATIONID` varchar(64) DEFAULT NULL,
  `REMARKS` varchar(1000) DEFAULT NULL,
  `DATE_OBSOLETE` date DEFAULT NULL,
  PRIMARY KEY (`CONTACT_ID`,`EMAIL`),
  CONSTRAINT `EMAIL_FK1` FOREIGN KEY (`CONTACT_ID`) REFERENCES `CONTACT` (`CONTACT_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EVENT`
--

DROP TABLE IF EXISTS `EVENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EVENT` (
  `EVENT_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CREATED_BY` varchar(50) NOT NULL,
  `DATE_CREATED` datetime NOT NULL,
  `EVENT_TITLE` varchar(200) NOT NULL,
  `EXPLAIN_IF_OTHER` varchar(200) DEFAULT NULL,
  `EVENT_DATE` date NOT NULL,
  `EVENT_TIME_START` datetime NOT NULL,
  `EVENT_TIME_END` datetime NOT NULL,
  `SEND_REMINDER` tinyint(1) DEFAULT '1',
  `EVENT_DESCRIPTION` varchar(2000) DEFAULT NULL,
  `MINIMUM_PARTICIPATIONS` int(11) DEFAULT NULL,
  `EVENT_CLASS_NAME` varchar(50) NOT NULL,
  `EVENT_LOCATION_NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`EVENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EVENT_AFFILIATION`
--

DROP TABLE IF EXISTS `EVENT_AFFILIATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EVENT_AFFILIATION` (
  `EVENT_ID` int(11) NOT NULL,
  `TEAM_NAME` varchar(50) NOT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `DATE_CREATED` datetime NOT NULL,
  `EXPLAIN_IF_OTHER` varchar(200) DEFAULT NULL,
  `DATE_OBSOLETE` date DEFAULT NULL,
  `REMARKS` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`EVENT_ID`,`TEAM_NAME`),
  CONSTRAINT `EVENT_AFFILIATION_FK1` FOREIGN KEY (`EVENT_ID`) REFERENCES `EVENT` (`EVENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EVENT_CLASS_LIST`
--

DROP TABLE IF EXISTS `EVENT_CLASS_LIST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EVENT_CLASS_LIST` (
  `EVENT_CLASS_NAME` varchar(50) NOT NULL,
  `POSITION_NUMBER` int(11) NOT NULL,
  PRIMARY KEY (`EVENT_CLASS_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EVENT_LOCATION_LIST`
--

DROP TABLE IF EXISTS `EVENT_LOCATION_LIST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EVENT_LOCATION_LIST` (
  `EVENT_LOCATION_NAME` varchar(50) NOT NULL,
  `POSITION_NUMBER` int(11) NOT NULL,
  PRIMARY KEY (`EVENT_LOCATION_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EVENT_PARTICIPANT`
--

DROP TABLE IF EXISTS `EVENT_PARTICIPANT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EVENT_PARTICIPANT` (
  `CONTACT_ID` int(11) NOT NULL,
  `AWARDER_ID` int(11) DEFAULT NULL,
  `ROLE_ID` int(11) NOT NULL,
  `EVENT_ID` int(11) NOT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `DATE_CREATED` datetime NOT NULL,
  `PULLOUT` tinyint(1) DEFAULT '0',
  `DATE_PULLOUT` date DEFAULT NULL,
  `REASON` varchar(1000) DEFAULT NULL,
  `HOURS_SERVED` float DEFAULT NULL,
  `SERVICE_COMMENT` varchar(1000) DEFAULT NULL,
  `REMARKS` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`CONTACT_ID`,`EVENT_ID`,`ROLE_ID`),
  KEY `EVENT_PARTICIPANT_FK2` (`AWARDER_ID`),
  KEY `EVENT_PARTICIPANT_FK4` (`EVENT_ID`),
  CONSTRAINT `EVENT_PARTICIPANT_FK1` FOREIGN KEY (`CONTACT_ID`) REFERENCES `CONTACT` (`CONTACT_ID`),
  CONSTRAINT `EVENT_PARTICIPANT_FK2` FOREIGN KEY (`AWARDER_ID`) REFERENCES `CONTACT` (`CONTACT_ID`),
  CONSTRAINT `EVENT_PARTICIPANT_FK4` FOREIGN KEY (`EVENT_ID`) REFERENCES `EVENT` (`EVENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EVENT_ROLE_ASSIGNMENT`
--

DROP TABLE IF EXISTS `EVENT_ROLE_ASSIGNMENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EVENT_ROLE_ASSIGNMENT` (
  `ROLE_ID` int(11) NOT NULL,
  `EVENT_ID` int(11) NOT NULL,
  PRIMARY KEY (`ROLE_ID`,`EVENT_ID`),
  KEY `EVENT_ROLE_ASSIGNMENT_FK2` (`EVENT_ID`),
  CONSTRAINT `EVENT_ROLE_ASSIGNMENT_FK2` FOREIGN KEY (`EVENT_ID`) REFERENCES `EVENT` (`EVENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `LANGUAGE_ASSIGNMENT`
--

DROP TABLE IF EXISTS `LANGUAGE_ASSIGNMENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LANGUAGE_ASSIGNMENT` (
  `CONTACT_ID` int(11) NOT NULL,
  `DATE_CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `EXPLAIN_IF_OTHER` varchar(200) DEFAULT NULL,
  `DATE_OBSOLETE` date DEFAULT NULL,
  `PROFICIENCY` varchar(50) DEFAULT NULL,
  `REMARKS` varchar(1000) DEFAULT NULL,
  `LANGUAGE_NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`CONTACT_ID`,`LANGUAGE_NAME`),
  CONSTRAINT `LANGUAGE_ASSIGNMENT_FK1` FOREIGN KEY (`CONTACT_ID`) REFERENCES `CONTACT` (`CONTACT_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `LANGUAGE_LIST`
--

DROP TABLE IF EXISTS `LANGUAGE_LIST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LANGUAGE_LIST` (
  `LANGUAGE_NAME` varchar(50) NOT NULL,
  `POSITION_NUMBER` int(11) NOT NULL,
  PRIMARY KEY (`LANGUAGE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `LSA_CLASS_LIST`
--

DROP TABLE IF EXISTS `LSA_CLASS_LIST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LSA_CLASS_LIST` (
  `SKILL_NAME` varchar(50) NOT NULL,
  `POSITION_NUMBER` int(11) NOT NULL,
  PRIMARY KEY (`SKILL_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEMBERSHIP`
--

DROP TABLE IF EXISTS `MEMBERSHIP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MEMBERSHIP` (
  `MEMBERSHIP_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONTACT_ID` int(11) NOT NULL,
  `DATE_CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `EXPLAIN_IF_OTHER_CLASS` varchar(200) DEFAULT NULL,
  `START_MEMBERSHIP` date NOT NULL,
  `END_MEMBERSHIP` date NOT NULL,
  `SUBSCRIPTION_AMOUNT` float NOT NULL,
  `EXPLAIN_IF_OTHER_PAYMENT` varchar(200) DEFAULT NULL,
  `EXT_TRANSACTION_REF` varchar(50) DEFAULT NULL,
  `RECEIPT_NUMBER` varchar(50) DEFAULT NULL,
  `RECEIPT_DATE` date DEFAULT NULL,
  `EXPLAIN_IF_OTHER_RECEIPT` varchar(200) DEFAULT NULL,
  `RECEIPT_MODE_NAME` varchar(50) DEFAULT NULL,
  `MEMBERSHIP_CLASS_NAME` varchar(50) NOT NULL,
  `PAYMENT_MODE_NAME` varchar(50) DEFAULT NULL,
  `REMARKS` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`MEMBERSHIP_ID`),
  KEY `MEMBERSHIP_FK1` (`CONTACT_ID`),
  CONSTRAINT `MEMBERSHIP_FK1` FOREIGN KEY (`CONTACT_ID`) REFERENCES `CONTACT` (`CONTACT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEMBERSHIP_CLASS_LIST`
--

DROP TABLE IF EXISTS `MEMBERSHIP_CLASS_LIST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MEMBERSHIP_CLASS_LIST` (
  `MEMBERSHIP_CLASS_NAME` varchar(50) NOT NULL,
  `POSITION_NUMBER` int(11) NOT NULL,
  PRIMARY KEY (`MEMBERSHIP_CLASS_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MODE_OF_SENDING_RECEIPT_LIST`
--

DROP TABLE IF EXISTS `MODE_OF_SENDING_RECEIPT_LIST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MODE_OF_SENDING_RECEIPT_LIST` (
  `RECEIPT_MODE_NAME` varchar(50) NOT NULL,
  `POSITION_NUMBER` int(11) NOT NULL,
  PRIMARY KEY (`RECEIPT_MODE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `NOTICE`
--

DROP TABLE IF EXISTS `NOTICE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `NOTICE` (
  `NOTICE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `EVENT_ID` int(11) NOT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `DATE_CREATED` datetime NOT NULL,
  `ACTIVELY_PUSHOUT` tinyint(1) DEFAULT '1',
  `MESSAGE_TITLE` varchar(200) NOT NULL,
  `MESSAGE_CONTENT` varchar(2000) DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`NOTICE_ID`),
  KEY `NOTICE_FK1` (`EVENT_ID`),
  CONSTRAINT `NOTICE_FK1` FOREIGN KEY (`EVENT_ID`) REFERENCES `EVENT` (`EVENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `OFFICE_HELD`
--

DROP TABLE IF EXISTS `OFFICE_HELD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OFFICE_HELD` (
  `CONTACT_ID` int(11) NOT NULL,
  `DATE_CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `START_OFFICE` date NOT NULL,
  `END_OFFICE` date NOT NULL,
  `OFFICE_HELD_NAME` varchar(50) NOT NULL,
  `REMARKS` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`CONTACT_ID`,`OFFICE_HELD_NAME`,`START_OFFICE`),
  CONSTRAINT `OFFICE_HELD_FK1` FOREIGN KEY (`CONTACT_ID`) REFERENCES `CONTACT` (`CONTACT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `OFFICE_LIST`
--

DROP TABLE IF EXISTS `OFFICE_LIST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OFFICE_LIST` (
  `OFFICE_HELD_NAME` varchar(50) NOT NULL,
  `POSITION_NUMBER` int(11) NOT NULL,
  PRIMARY KEY (`OFFICE_HELD_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PAYMENT_MODE_LIST`
--

DROP TABLE IF EXISTS `PAYMENT_MODE_LIST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PAYMENT_MODE_LIST` (
  `PAYMENT_MODE_NAME` varchar(50) NOT NULL,
  `POSITION_NUMBER` int(11) NOT NULL,
  PRIMARY KEY (`PAYMENT_MODE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PERMISSION_LEVEL_LIST`
--

DROP TABLE IF EXISTS `PERMISSION_LEVEL_LIST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PERMISSION_LEVEL_LIST` (
  `PERMISSION` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(200) DEFAULT NULL,
  `POSITION_NUMBER` int(11) NOT NULL,
  PRIMARY KEY (`PERMISSION`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PHONE`
--

DROP TABLE IF EXISTS `PHONE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PHONE` (
  `CONTACT_ID` int(11) NOT NULL,
  `DATE_CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `COUNTRY_CODE` varchar(20) NOT NULL,
  `PHONE_NUMBER` varchar(20) NOT NULL,
  `REMARKS` varchar(1000) DEFAULT NULL,
  `DATE_OBSOLETE` date DEFAULT NULL,
  PRIMARY KEY (`CONTACT_ID`,`PHONE_NUMBER`),
  CONSTRAINT `PHONE_FK1` FOREIGN KEY (`CONTACT_ID`) REFERENCES `CONTACT` (`CONTACT_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROXY`
--

DROP TABLE IF EXISTS `PROXY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROXY` (
  `PRINCIPAL_ID` int(11) NOT NULL,
  `PROXY_ID` int(11) NOT NULL,
  `DATE_CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `PROXY_STANDING` varchar(500) DEFAULT NULL,
  `DATE_OBSOLETE` date DEFAULT NULL,
  `REMARKS` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`PRINCIPAL_ID`,`PROXY_ID`),
  KEY `PROXY_FK2` (`PROXY_ID`),
  CONSTRAINT `PROXY_FK1` FOREIGN KEY (`PRINCIPAL_ID`) REFERENCES `CONTACT` (`CONTACT_ID`),
  CONSTRAINT `PROXY_FK2` FOREIGN KEY (`PROXY_ID`) REFERENCES `CONTACT` (`CONTACT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ROLE_LIST`
--

DROP TABLE IF EXISTS `ROLE_LIST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ROLE_LIST` (
  `ROLE_ID` int(11) NOT NULL,
  `ROLE_DESCRIPTION` varchar(200) DEFAULT NULL,
  `POSITION_NUMBER` int(11) NOT NULL,
  PRIMARY KEY (`ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SKILL_ASSIGNMENT`
--

DROP TABLE IF EXISTS `SKILL_ASSIGNMENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SKILL_ASSIGNMENT` (
  `CONTACT_ID` int(11) NOT NULL,
  `DATE_CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `EXPLAIN_IF_OTHER` varchar(200) DEFAULT NULL,
  `DATE_OBSOLETE` date DEFAULT NULL,
  `REMARKS` varchar(1000) DEFAULT NULL,
  `SKILL_NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`CONTACT_ID`,`SKILL_NAME`),
  CONSTRAINT `SKILL_ASSIGNMENT_FK1` FOREIGN KEY (`CONTACT_ID`) REFERENCES `CONTACT` (`CONTACT_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TEAM_AFFILIATION_LIST`
--

DROP TABLE IF EXISTS `TEAM_AFFILIATION_LIST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TEAM_AFFILIATION_LIST` (
  `TEAM_NAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(200) DEFAULT NULL,
  `POSITION_NUMBER` int(11) NOT NULL,
  PRIMARY KEY (`TEAM_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TEAM_JOIN`
--

DROP TABLE IF EXISTS `TEAM_JOIN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TEAM_JOIN` (
  `CONTACT_ID` int(11) NOT NULL,
  `TEAM_NAME` varchar(50) NOT NULL,
  `DATE_CREATED` datetime NOT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `EXPLAIN_IF_OTHER` varchar(200) DEFAULT NULL,
  `SUBTEAM` varchar(50) DEFAULT NULL,
  `DATE_OBSOLETE` date DEFAULT NULL,
  `REMARKS` varchar(1000) DEFAULT NULL,
  `PERMISSION` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`CONTACT_ID`,`TEAM_NAME`),
  CONSTRAINT `TEAM_JOIN_FK1` FOREIGN KEY (`CONTACT_ID`) REFERENCES `CONTACT` (`CONTACT_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TRAINING_CERTIFICATED`
--

DROP TABLE IF EXISTS `TRAINING_CERTIFICATED`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TRAINING_CERTIFICATED` (
  `CERTIFICATION_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CERTIFIED_BY` int(11) NOT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `DATE_CREATED` datetime NOT NULL,
  `EXPLAIN_IF_OTHER` varchar(200) DEFAULT NULL,
  `TRAINING_COURSE` int(11) NOT NULL,
  `REMARKS` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`CERTIFICATION_ID`),
  KEY `TRAINING_CERTIFICATED_FK1` (`CERTIFIED_BY`),
  KEY `TRAINING_CERTIFICATED_FK2` (`TRAINING_COURSE`),
  CONSTRAINT `TRAINING_CERTIFICATED_FK1` FOREIGN KEY (`CERTIFIED_BY`) REFERENCES `CONTACT` (`CONTACT_ID`),
  CONSTRAINT `TRAINING_CERTIFICATED_FK2` FOREIGN KEY (`TRAINING_COURSE`) REFERENCES `EVENT` (`EVENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TYPE_OF_CONTACT_LIST`
--

DROP TABLE IF EXISTS `TYPE_OF_CONTACT_LIST`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TYPE_OF_CONTACT_LIST` (
  `CONTACT_TYPE` varchar(50) NOT NULL,
  `POSITION_NUMBER` int(11) NOT NULL,
  PRIMARY KEY (`CONTACT_TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-07-16 17:56:27
