#Dummy data to be loaded to bahamas tables

use bahamas;

#PERMISSION_LEVEL_LIST
insert into PERMISSION_LEVEL_LIST(PERMISSION, DESCRIPTION) value
('Team manager', 'Manages a team'),
('Event leader', 'Manages an event'),
('Associate', 'Joins event');

#TYPE_OF_CONTACT_LIST
insert into TYPE_OF_CONTACT_LIST(CONTACT_TYPE) value
('Individual'),
('Foundation'),
('Religious'),
('Corporate'),
('Government-linked'),
('Diplomatic'),
('Nonprofit local'),
('Nonprofit abroad'),
('Medical'),
('Legal'),
('Educational'),
('Media local'),
('Media abroad'),
('Financial'),
('Job agent'),
('Employer'),
('Product supplier'),
('Office services'),
('Event services'),
('Financial services'),
('Maintenance & repair'),
('TWC2'),
('Others');

#LANGUAGE_LIST
insert into LANGUAGE_LIST(LANGUAGE_NAME) value
('Bengali'),
('Burmese'),
('Cambodian'),
('Chinese'),
('English'),
('Filipino'),
('Hindi'),
('Indonesian'),
('Malay'),
('Punjabi'),
('Sinhala'),
('Tamil'),
('Telugu'),
('Vietnamese'),
('Others');

#OFFICE_LIST
insert into OFFICE_LIST(OFFICE_HELD_NAME) value
('President'),
('Vice-president'),
('Secretary'),
('Treasurer'),
('Staff');

#MODE_OF_SENDING_RECEIPT_LIST
insert into MODE_OF_SENDING_RECEIPT_LIST(RECEIPT_MODE_NAME) value
('Email'),
('Post'),
('No address'),
('Others');

#MEMBERSHIP_CLASS_LIST
insert into MEMBERSHIP_CLASS_LIST(MEMBERSHIP_CLASS_NAME) value
('Ordinary'),
('Discounted'),
('Associate'),
('Honorary'),
('Others');

#PAYMENT_MODE_LIST
insert into PAYMENT_MODE_LIST(PAYMENT_MODE_NAME) value
('Cash'),
('Bank direct'),
('Cheque'),
('Paypal'),
('Giving Sg'),
('Others');

#LSA_CLASS_LIST
insert into LSA_CLASS_LIST(SKILL_NAME) value
('Medical'),
('Physio'),
('Pharma'),
('Dental'),
('Counselling'),
('Legal'),
('Pro-bono lawyer'),
('Media & Comms'),
('Infotech'),
('Photo, video & design'),
('Accounting'),
('Academia'),
('Event organising'),
('Has event space'),
('Has vehicle'),
('Has shelter space'),
('Others');

#EVENT_CLASS_LIST
insert into EVENT_CLASS_LIST(EVENT_CLASS_NAME) value
('Closed'),
('Open'),
('Training'),
('Cancelled'),
('Others');

#EVENT_LOCATION_LIST
insert into EVENT_LOCATION_LIST(EVENT_LOCATION_NAME) value
('TWC2 office'),
('TWC2 DaySpace'),
('Isthana'),
('Alankar'),
('Online'),
('Others');

#TEAM_AFFILIATION_LIST
insert into TEAM_AFFILIATION_LIST(TEAM_NAME, DESCRIPTION) value
('TCRP','This is TCRP team'),
('R2R','This is R2R team'),
('FareGo','This is FareGo team'),
('Roof','This is Roof team'),
('Lifeline','This is Lifeline team'),
('Casework','This is Casework team'),
('Intern','This is Intern team'),
('Outreach','This is Outreach team'),
('Discover Sg','This is Discover Sg team'),
('Public Engagement','This is Public Engagement team'),
('Communications','This is Communications team'),
('Research','This is Research team'),
('Fundraising','This is Fundraising team'),
('Others','This is Other team');


#CONTACT
insert into CONTACT(CONTACT_ID, CONTACT_TYPE,USERNAME,PASSWORD,SALT,ISADMIN,ISNOVICE,DEACTIVATED,DATE_CREATED,CREATED_BY,NAME,ALT_NAME,EXPLAIN_IF_OTHER,PROFESSION,JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,REMARKS,NOTIFICATION) value
(1, 'TWC2', 'admin1', 'GwvbMoaGY9yVMnOiMfujehEdmE7jLDXX8DZAM1uUcTw=','1/rqffBv5u4=', 1, 0, 0, '2016-05-24 23:00:00', 'admin', 'System Admin', 'Admin', '', 'IT', 'TSO', 'S1234567A', 'M', 'Singapore', '1977-02-22', '', '', 1);





