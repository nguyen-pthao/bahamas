#Bahamas_create_table

drop schema if exists bahamas;
create schema bahamas;
use bahamas;

#PERMISSION_LEVEL_LIST
CREATE TABLE PERMISSION_LEVEL_LIST(
PERMISSION VARCHAR(50) NOT NULL PRIMARY KEY,
DESCRIPTION VARCHAR(200) 
) CHARACTER SET = utf8;

#TYPE_OF_CONTACT_LIST
CREATE TABLE TYPE_OF_CONTACT_LIST(
CONTACT_TYPE VARCHAR(50) NOT NULL PRIMARY KEY
) CHARACTER SET = utf8;

#CONTACT
CREATE TABLE CONTACT(
CONTACT_ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
CONTACT_TYPE VARCHAR(50),
USERNAME VARCHAR(50),
PASSWORD CHAR(44),
SALT CHAR(12),
ISADMIN	TINYINT(1) DEFAULT 0,
ISNOVICE TINYINT(1) DEFAULT 1,
DEACTIVATED TINYINT(1) DEFAULT 0,
DATE_CREATED DATETIME NOT NULL,
CREATED_BY VARCHAR(50) NOT NULL,
NAME VARCHAR(50),
ALT_NAME VARCHAR(50),
EXPLAIN_IF_OTHER VARCHAR(200),
PROFESSION VARCHAR(200),
JOB_TITLE VARCHAR(50),
NRIC_FIN VARCHAR(9),
GENDER VARCHAR(1),
NATIONALITY VARCHAR(20),
DATE_OF_BIRTH DATE,
PROFILE_PIC VARCHAR(500),
REMARKS VARCHAR(1000),
NOTIFICATION TINYINT(1) DEFAULT 1,
CONSTRAINT CONTACT_FK1 FOREIGN KEY(CONTACT_TYPE) REFERENCES TYPE_OF_CONTACT_LIST(CONTACT_TYPE)
) CHARACTER SET = utf8;

#PHONE
CREATE TABLE PHONE(
CONTACT_ID INTEGER NOT NULL,
DATE_CREATED DATETIME NOT NULL,
CREATED_BY VARCHAR(50) NOT NULL,
COUNTRY_CODE VARCHAR(20) NOT NULL,
PHONE_NUMBER VARCHAR(20) NOT NULL,
REMARKS VARCHAR(1000),
DATE_OBSOLETE DATE,
CONSTRAINT PHONE_PK PRIMARY KEY(CONTACT_ID,PHONE_NUMBER),
CONSTRAINT PHONE_FK1 FOREIGN KEY(CONTACT_ID) REFERENCES CONTACT(CONTACT_ID)
) CHARACTER SET = utf8;

#EMAIL
CREATE TABLE EMAIL(
CONTACT_ID INTEGER NOT NULL,
DATE_CREATED DATETIME NOT NULL,
CREATED_BY VARCHAR(50) NOT NULL,
EMAIL VARCHAR(50) NOT NULL,
REMARKS VARCHAR(1000),
DATE_OBSOLETE DATE,
CONSTRAINT EMAIL_PK PRIMARY KEY(CONTACT_ID,EMAIL),
CONSTRAINT EMAIL_FK1 FOREIGN KEY(CONTACT_ID) REFERENCES CONTACT(CONTACT_ID)
) CHARACTER SET = utf8;

#ADDRESS
CREATE TABLE ADDRESS(
CONTACT_ID INTEGER NOT NULL,
DATE_CREATED DATETIME NOT NULL,
CREATED_BY VARCHAR(50) NOT NULL,
ADDRESS VARCHAR(100) NOT NULL,
COUNTRY VARCHAR(50),
ZIPCODE VARCHAR(20),
REMARKS VARCHAR(1000),
DATE_OBSOLETE DATE,
CONSTRAINT ADDRESS_PK PRIMARY KEY(CONTACT_ID,ADDRESS),
CONSTRAINT ADDRESS_FK1 FOREIGN KEY(CONTACT_ID) REFERENCES CONTACT(CONTACT_ID)
) CHARACTER SET = utf8;

#LANGUAGE
CREATE TABLE LANGUAGE_LIST(
LANGUAGE_NAME VARCHAR(50) NOT NULL PRIMARY KEY
) CHARACTER SET = utf8;

#LANGUAGE_ASSIGNMENT
CREATE TABLE LANGUAGE_ASSIGNMENT(
CONTACT_ID INTEGER NOT NULL,
DATE_CREATED DATETIME NOT NULL,
CREATED_BY VARCHAR(50) NOT NULL,
EXPLAIN_IF_OTHER VARCHAR(200),
DATE_OBSOLETE DATE,
PROFICIENCY VARCHAR(50),
REMARKS VARCHAR(1000),
LANGUAGE_NAME VARCHAR(50) NOT NULL,
CONSTRAINT LANGUAGE_ASSIGNMENT_PK PRIMARY KEY(CONTACT_ID,LANGUAGE_NAME),
CONSTRAINT LANGUAGE_ASSIGNMENT_FK1 FOREIGN KEY(CONTACT_ID) REFERENCES CONTACT(CONTACT_ID),
CONSTRAINT LANGUAGE_ASSIGNMENT_FK3 FOREIGN KEY(LANGUAGE_NAME) REFERENCES LANGUAGE_LIST(LANGUAGE_NAME)
) CHARACTER SET = utf8;

#OFFICE_LIST
CREATE TABLE OFFICE_LIST(
OFFICE_HELD_NAME VARCHAR(50) NOT NULL PRIMARY KEY
) CHARACTER SET = utf8;

#OFFICE_HELD
CREATE TABLE OFFICE_HELD(
CONTACT_ID INTEGER NOT NULL,
DATE_CREATED DATETIME NOT NULL,
CREATED_BY VARCHAR(50) NOT NULL,
START_OFFICE DATE NOT NULL,
END_OFFICE DATE NOT NULL,
OFFICE_HELD_NAME VARCHAR(50) NOT NULL,
REMARKS VARCHAR(1000),
CONSTRAINT PERIOD_PK PRIMARY KEY(CONTACT_ID,OFFICE_HELD_NAME),
CONSTRAINT PERIOD_FK1 FOREIGN KEY(CONTACT_ID) REFERENCES CONTACT(CONTACT_ID),
CONSTRAINT PERIOD_FK2 FOREIGN KEY(OFFICE_HELD_NAME) REFERENCES OFFICE_LIST(OFFICE_HELD_NAME)
) CHARACTER SET = utf8;

#PROXY
CREATE TABLE PROXY(
CONTACT_ID INTEGER NOT NULL,
PROXY_ID INTEGER NOT NULL,
CREATED_BY VARCHAR(50) NOT NULL,
PROXY_STANDING VARCHAR(500),
DATE_OBSOLETE DATE,
REMARKS VARCHAR(1000),
CONSTRAINT PROXY_PK PRIMARY KEY(CONTACT_ID,PROXY_ID),
CONSTRAINT PROXY_FK1 FOREIGN KEY(CONTACT_ID) REFERENCES CONTACT(CONTACT_ID),
CONSTRAINT PROXY_FK2 FOREIGN KEY(PROXY_ID) REFERENCES CONTACT(CONTACT_ID)
) CHARACTER SET = utf8;

#MODE_OF_SENDING_RECEIPT_LIST
CREATE TABLE MODE_OF_SENDING_RECEIPT_LIST(
RECEIPT_MODE_NAME VARCHAR(50) NOT NULL PRIMARY KEY
) CHARACTER SET = utf8;

#MEMBERSHIP_CLASS_LIST
CREATE TABLE MEMBERSHIP_CLASS_LIST(
MEMBERSHIP_CLASS_NAME VARCHAR(50) NOT NULL PRIMARY KEY
) CHARACTER SET = utf8;

#PAYMENT_MODE_LIST
CREATE TABLE PAYMENT_MODE_LIST(
PAYMENT_MODE_NAME VARCHAR(50) NOT NULL PRIMARY KEY
) CHARACTER SET = utf8;

#MEMBERSHIP
CREATE TABLE MEMBERSHIP(
CONTACT_ID INTEGER NOT NULL,
DATE_CREATED DATETIME NOT NULL,
CREATED_BY VARCHAR(50) NOT NULL,
EXPLAIN_IF_OTHER_CLASS VARCHAR(50),
START_MEMBERSHIP DATE,
END_MEMBERSHIP DATE,
SUBSCRIPTION_AMOUNT FLOAT,
EXPLAIN_IF_OTHER_PAYMENT VARCHAR(50),
EXT_TRANSACTION_REF VARCHAR(50),
RECEIPT_NUMBER VARCHAR(50),
RECEIPT_DATE DATE,
EXPLAIN_IF_OTHER_RECEIPT VARCHAR(200),
RECEIPT_MODE_NAME VARCHAR(50),
MEMBERSHIP_CLASS_NAME VARCHAR(50),
PAYMENT_MODE_NAME VARCHAR(50), 
REMARKS VARCHAR(1000),
CONSTRAINT MEMBERSHIP_PK PRIMARY KEY(CONTACT_ID,DATE_CREATED),
CONSTRAINT MEMBERSHIP_FK1 FOREIGN KEY(CONTACT_ID) REFERENCES CONTACT(CONTACT_ID),
CONSTRAINT MEMBERSHIP_FK3 FOREIGN KEY(RECEIPT_MODE_NAME) REFERENCES MODE_OF_SENDING_RECEIPT_LIST(RECEIPT_MODE_NAME),
CONSTRAINT MEMBERSHIP_FK4 FOREIGN KEY(MEMBERSHIP_CLASS_NAME) REFERENCES MEMBERSHIP_CLASS_LIST(MEMBERSHIP_CLASS_NAME),
CONSTRAINT MEMBERSHIP_FK5 FOREIGN KEY(PAYMENT_MODE_NAME) REFERENCES PAYMENT_MODE_LIST(PAYMENT_MODE_NAME)
) CHARACTER SET = utf8;

#LSA_CLASS_LIST
CREATE TABLE LSA_CLASS_LIST(
SKILL_NAME VARCHAR(50) NOT NULL PRIMARY KEY
) CHARACTER SET = utf8;

#SKILL_ASSIGNMENT
CREATE TABLE SKILL_ASSIGNMENT(
CONTACT_ID INTEGER NOT NULL,
DATE_CREATED DATETIME NOT NULL,
CREATED_BY VARCHAR(50) NOT NULL,
EXPLAIN_IF_OTHER VARCHAR(200),
DATE_OBSOLETE DATE,
REMARKS VARCHAR(1000),
SKILL_NAME VARCHAR(50) NOT NULL,
CONSTRAINT SKILL_PK PRIMARY KEY(CONTACT_ID,SKILL_NAME),
CONSTRAINT SKILL_ASSIGNMENT_FK1 FOREIGN KEY(CONTACT_ID) REFERENCES CONTACT(CONTACT_ID),
CONSTRAINT SKILL_ASSIGNMENT_FK3 FOREIGN KEY(SKILL_NAME) REFERENCES LSA_CLASS_LIST(SKILL_NAME)
) CHARACTER SET = utf8;

#TEAM_AFFILIATION
CREATE TABLE TEAM_AFFILIATION_LIST(
TEAM_NAME VARCHAR(50) NOT NULL PRIMARY KEY,
DESCRIPTION VARCHAR(200)
) CHARACTER SET = utf8;

#TEAM_JOIN
CREATE TABLE TEAM_JOIN(
CONTACT_ID INTEGER NOT NULL,
TEAM_NAME VARCHAR(50) NOT NULL,
DATE_CREATED DATETIME NOT NULL,
CREATED_BY VARCHAR(50) NOT NULL,
EXPLAIN_IF_OTHER VARCHAR(200),
SUBTEAM VARCHAR(50),
DATE_OBSOLETE DATE,
REMARKS VARCHAR(1000),
PERMISSION VARCHAR(50) NOT NULL,
CONSTRAINT TEAM_JOIN_PK PRIMARY KEY(CONTACT_ID,TEAM_NAME,PERMISSION),
CONSTRAINT TEAM_JOIN_FK1 FOREIGN KEY(CONTACT_ID) REFERENCES CONTACT(CONTACT_ID),
CONSTRAINT TEAM_JOIN_FK2 FOREIGN KEY(TEAM_NAME) REFERENCES TEAM_AFFILIATION_LIST(TEAM_NAME),
CONSTRAINT TEAM_JOIN_FK3 FOREIGN KEY(PERMISSION) REFERENCES PERMISSION_LEVEL_LIST(PERMISSION)
) CHARACTER SET = utf8;

#DONATION
CREATE TABLE DONATION(
DONATION_ID INTEGER NOT NULL PRIMARY KEY,
CONTACT_ID INTEGER NOT NULL,
DATE_CREATED DATETIME NOT NULL,
CREATED_BY VARCHAR(50) NOT NULL,
DATE_RECEIVED DATE NOT NULL,
DONATION_AMOUNT FLOAT NOT NULL,
PAYMENT_MODE_NAME VARCHAR(50) NOT NULL,
EXPLAIN_IF_OTHER_PAYMENT VARCHAR(200),
EXT_TRANSACTION_REF VARCHAR(50),
RECEIPT_NUMBER VARCHAR(50),
RECEIPT_DATE DATE,
EXPLAIN_IF_OTHER_RECEIPT VARCHAR(200),
DONOR_INSTRUCTIONS VARCHAR(1000),
ALLOCATION_1 VARCHAR(200),
SUBAMOUNT_1 FLOAT,
ALLOCATION_2 VARCHAR(200),
SUBAMOUNT_2 FLOAT,
ALLOCATION_3 VARCHAR(200),
SUBAMOUNT_3 FLOAT,
ASSOCIATED_OCCASION VARCHAR(500),
REMARKS VARCHAR(1000),
CONSTRAINT DONATION_FK1 FOREIGN KEY(CONTACT_ID) REFERENCES CONTACT(CONTACT_ID),
CONSTRAINT DONATION_FK2 FOREIGN KEY(PAYMENT_MODE_NAME) REFERENCES PAYMENT_MODE_LIST(PAYMENT_MODE_NAME)
) CHARACTER SET = utf8;

#APPRECIATION
CREATE TABLE APPRECIATION(
APPRECIATION_ID INTEGER NOT NULL PRIMARY KEY,
CONTACT_ID INTEGER NOT NULL,
CREATED_BY VARCHAR(50) NOT NULL,
DATE_CREATED DATETIME NOT NULL,
APPRAISAL_COMMENTS VARCHAR(500),
APPRAISED_BY VARCHAR(50),
APPRAISAL_DATE DATE,
APPRECIATION_GESTURE VARCHAR(500),
APPRECIATION_DATE DATE,
REMARKS VARCHAR(1000),
CONSTRAINT APPRECIATION_FK1 FOREIGN KEY(CONTACT_ID) REFERENCES CONTACT(CONTACT_ID)
) CHARACTER SET = utf8;

#EVENT_CLASS_LIST
CREATE TABLE EVENT_CLASS_LIST(
EVENT_CLASS_NAME VARCHAR(50) NOT NULL PRIMARY KEY
) CHARACTER SET = utf8;

#EVENT_LOCATION_LIST
CREATE TABLE EVENT_LOCATION_LIST(
EVENT_LOCATION_NAME VARCHAR(50) NOT NULL PRIMARY KEY
) CHARACTER SET = utf8;

#EVENT
CREATE TABLE EVENT(
EVENT_ID INTEGER NOT NULL PRIMARY KEY,
CREATED_BY VARCHAR(50) NOT NULL,
DATE_CREATED DATETIME NOT NULL,
EVENT_TITLE VARCHAR(200) NOT NULL,
EXPLAIN_IF_OTHER VARCHAR(200),
EVENT_DATE DATE NOT NULL,
EVENT_TIME_START DATETIME NOT NULL,
EVENT_TIME_END DATETIME NOT NULL,
SEND_REMINDER TINYINT(1) DEFAULT 1,
EVENT_DESCRIPTION VARCHAR(2000),
MINIMUM_PARTICIPATIONS INTEGER,
EVENT_CLASS_NAME VARCHAR(50) NOT NULL,
EVENT_LOCATION_NAME VARCHAR(50) NOT NULL,
CONSTRAINT EVENT_FK1 FOREIGN KEY(EVENT_CLASS_NAME) REFERENCES EVENT_CLASS_LIST(EVENT_CLASS_NAME),
CONSTRAINT EVENT_FK2 FOREIGN KEY(EVENT_LOCATION_NAME) REFERENCES EVENT_LOCATION_LIST(EVENT_LOCATION_NAME)
) CHARACTER SET = utf8;

#TRAINING_CERTIFICATED
CREATE TABLE TRAINING_CERTIFICATED(
CERTIFICATION_ID INTEGER NOT NULL PRIMARY KEY,
CERTIFIED_BY INTEGER NOT NULL,
CREATED_BY VARCHAR(50) NOT NULL,
DATE_CREATED DATETIME NOT NULL,
EXPLAIN_IF_OTHER VARCHAR(200),
TRAINING_COURSE INTEGER NOT NULL,
REMARKS VARCHAR(1000),
CONSTRAINT TRAINING_CERTIFICATED_FK1 FOREIGN KEY(CERTIFIED_BY) REFERENCES CONTACT(CONTACT_ID),
CONSTRAINT TRAINING_CERTIFICATED_FK2 FOREIGN KEY(TRAINING_COURSE) REFERENCES EVENT(EVENT_ID)
) CHARACTER SET = utf8;

#ROLE_LIST
CREATE TABLE ROLE_LIST(
ROLE_ID INTEGER NOT NULL PRIMARY KEY,
ROLE_DESCRIPTION VARCHAR(200)
) CHARACTER SET = utf8;

#EVENT_ROLE_ASSIGNMENT
CREATE TABLE EVENT_ROLE_ASSIGNMENT(
ROLE_ID INTEGER NOT NULL,
EVENT_ID INTEGER NOT NULL,
CONSTRAINT TEAM_JOIN_PK PRIMARY KEY(ROLE_ID,EVENT_ID),
CONSTRAINT EVENT_ROLE_ASSIGNMENT_FK1 FOREIGN KEY(ROLE_ID) REFERENCES ROLE_LIST(ROLE_ID),
CONSTRAINT EVENT_ROLE_ASSIGNMENT_FK2 FOREIGN KEY(EVENT_ID) REFERENCES EVENT(EVENT_ID)
) CHARACTER SET = utf8;

#EVENT_PARTICIPANT
CREATE TABLE EVENT_PARTICIPANT(
CONTACT_ID INTEGER NOT NULL,
AWARDER_ID INTEGER NULL,
ROLE_ID INTEGER NOT NULL,
EVENT_ID INTEGER NOT NULL,
CREATED_BY VARCHAR(50) NOT NULL,
DATE_CREATED DATETIME NOT NULL,
PULLOUT TINYINT(1) DEFAULT 0,
DATE_PULLOUT DATE,
REASON VARCHAR(1000),
HOURS_SERVED FLOAT,
SERVICE_COMMENT VARCHAR(1000),
REMARKS VARCHAR(1000),
CONSTRAINT EVENT_PARTICIPANT_PK PRIMARY KEY(CONTACT_ID,EVENT_ID,ROLE_ID),
CONSTRAINT EVENT_PARTICIPANT_FK1 FOREIGN KEY(CONTACT_ID) REFERENCES CONTACT(CONTACT_ID),
CONSTRAINT EVENT_PARTICIPANT_FK2 FOREIGN KEY(AWARDER_ID) REFERENCES CONTACT(CONTACT_ID),
CONSTRAINT EVENT_PARTICIPANT_FK3 FOREIGN KEY(ROLE_ID) REFERENCES ROLE_LIST(ROLE_ID),
CONSTRAINT EVENT_PARTICIPANT_FK4 FOREIGN KEY(EVENT_ID) REFERENCES EVENT(EVENT_ID)
) CHARACTER SET = utf8;

#EVENT_AFFILIATION
CREATE TABLE EVENT_AFFILIATION(
EVENT_ID INTEGER NOT NULL,
TEAM_NAME VARCHAR(50) NOT NULL,
CREATED_BY VARCHAR(50) NOT NULL,
DATE_CREATED DATETIME NOT NULL,
EXPLAIN_IF_OTHER VARCHAR(200),
DATE_OBSOLETE DATE,
REMARKS VARCHAR(1000),
CONSTRAINT EVENT_AFFILIATION_PK PRIMARY KEY(EVENT_ID,TEAM_NAME),
CONSTRAINT EVENT_AFFILIATION_FK1 FOREIGN KEY(EVENT_ID) REFERENCES EVENT(EVENT_ID),
CONSTRAINT EVENT_AFFILIATION_FK2 FOREIGN KEY(TEAM_NAME) REFERENCES TEAM_AFFILIATION_LIST(TEAM_NAME)
) CHARACTER SET = utf8;

#NOTICE
CREATE TABLE NOTICE(
NOTICE_ID INTEGER NOT NULL PRIMARY KEY,
EVENT_ID INTEGER NOT NULL,
CREATED_BY VARCHAR(50) NOT NULL,
DATE_CREATED DATETIME NOT NULL,
ACTIVELY_PUSHOUT TINYINT(1) DEFAULT 1,
MESSAGE_TITLE VARCHAR(200) NOT NULL,
MESSAGE_CONTENT VARCHAR(2000),
STATUS VARCHAR(10),
CONSTRAINT NOTICE_FK1 FOREIGN KEY(EVENT_ID) REFERENCES EVENT(EVENT_ID)
) CHARACTER SET = utf8;

#AUDIT
CREATE TABLE AUDITLOG(
AUDITLOG_ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
USERNAME VARCHAR(50) NOT NULL,
`DATE` DATETIME NOT NULL,
OPERATION VARCHAR(100)
) CHARACTER SET = utf8;
