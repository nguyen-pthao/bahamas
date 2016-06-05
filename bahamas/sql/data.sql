#Dummy data to be loaded to bahamas tables

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
('Other');

#LANGUAGE_LIST
insert into LANGUAGE_LIST(LANGUAGE_NAME) value
('Bengali'),
('Burmese'),
('Cambodian'),
('Chinese'),
('Filipino'),
('Hindi'),
('Indonesian'),
('Malay'),
('Punjabi'),
('Sinhala'),
('Tamil'),
('Telugu'),
('Vietnamese'),
('Other');

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
('Other');

#MEMBERSHIP_CLASS_LIST
insert into MEMBERSHIP_CLASS_LIST(MEMBERSHIP_CLASS_NAME) value
('Ordinary'),
('Discounted'),
('Associate'),
('Honorary'),
('Other');

#PAYMENT_MODE_LIST
insert into PAYMENT_MODE_LIST(PAYMENT_MODE_NAME) value
('Cash'),
('Bank direct'),
('Cheque'),
('Paypal'),
('Giving Sg'),
('Other');

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
('Other');

#EVENT_CLASS_LIST
insert into EVENT_CLASS_LIST(EVENT_CLASS_NAME) value
('Closed'),
('Open'),
('Training'),
('Cancelled'),
('Other');

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
('Other','This is Other team');


#CONTACT
insert into CONTACT(CONTACT_ID, CONTACT_TYPE,USERNAME,PASSWORD,ISADMIN,DEACTIVATED,DATE_CREATED,CREATED_BY,NAME,ALT_NAME,EXPLAIN_IF_OTHER,PROFESSION,JOB_TITLE,NRIC_FIN,GENDER,NATIONALITY,DATE_OF_BIRTH,PROFILE_PIC,REMARKS,NOTIFICATION) value
(1, 'TWC2', 'admin1', '7c222fb2927d828af22f592134e8932480637c0d', 1, 0, '2016-05-24', 'admin', 'Thao1', 'Cool Thao1', '', 'IT', 'TSO', 'S1234567A', 'M', 'Singapore', '1977-02-22', '', '', 'Y'),
(2, 'TWC2', 'admin2', '7c222fb2927d828af22f592134e8932480637c0d', 1, 0, '2016-05-24', 'admin', 'Thao2', 'Cool Thao2', '', 'IT', 'TSO', 'S1234567A', 'F', 'Singapore', '1977-02-22', '', '', 'N'),
(3, 'Religious', 'teammanager1', '7c222fb2927d828af22f592134e8932480637c0d', 0, 0, '2016-05-24', 'admin', 'tan1', 'Cool tan1', '', 'IT', 'TSO', 'S1234567A', 'M', 'Singapore', '1977-02-22', '', '', 'Y'),
(4, 'Media local', 'teammanager2', '7c222fb2927d828af22f592134e8932480637c0d', 0, 0, '2016-05-24', 'admin', 'tan2', 'Cool tan2', '', 'IT', 'TSO', 'S1234567A', 'F', 'Singapore', '1977-02-22', '', '', 'N'),
(5, 'Educational', 'eventleader1', '7c222fb2927d828af22f592134e8932480637c0d', 0, 0, '2016-05-24', 'admin', 'Goh1', 'Cool Goh1', '', 'IT', 'TSO', 'S1234567A', 'F', 'Singapore', '1977-02-22', '', '', 'Y'),
(6, 'Employer', 'eventleader2', '7c222fb2927d828af22f592134e8932480637c0d', 0, 0, '2016-05-24', 'admin', 'Goh2', 'Cool Goh2', '', 'IT', 'TSO', 'S1234567A', 'F', 'Singapore', '1977-02-22', '', '', 'N'),
(7, 'Event services', 'associate1', '7c222fb2927d828af22f592134e8932480637c0d', 0, 0, '2016-05-24', 'admin', 'Mok1', 'Cool Mok1', '', 'IT', 'TSO', 'S1234567A', 'M', 'Singapore', '1977-02-22', '', '', 'Y'),
(8, 'Government-linked', 'associate2', '7c222fb2927d828af22f592134e8932480637c0d', 0, 0, '2016-05-24', 'admin', 'Mok2', 'Cool Mok2', '', 'IT', 'TSO', 'S1234567A', 'F', 'Singapore', '1977-02-22', '', '', 'N'),
(9, 'Legal', 'novice1', '7c222fb2927d828af22f592134e8932480637c0d', 0, 0, '2016-05-24', 'admin', 'Ong1', 'Cool Ong1', '', 'IT', 'TSO', 'S1234567A', 'M', 'Singapore', '1977-02-22', '', '', 'Y'),
(10, 'Medical', 'novice2', '7c222fb2927d828af22f592134e8932480637c0d', 0, 0, '2016-05-24', 'admin', 'Ong2', 'Cool Ong2', '', 'IT', 'TSO', 'S1234567A', 'F', 'Singapore', '1977-02-22', '', '', 'N');

#TEAM_JOIN
insert into TEAM_JOIN(CONTACT_ID, TEAM_NAME, DATE_CREATED, CREATED_BY, EXPLAIN_IF_OTHER, SUBTEAM, DATE_OBSOLETE, REMARKS,PERMISSION) value
(3, 'Lifeline', '2016-05-24', 'Thao1' , '', '', '', '', 'Team manager'),
(4, 'Research', '2016-05-24', 'Thao2' , '', '', '', '', 'Team manager'),
(5, 'Lifeline', '2016-05-24', 'Thao1' , '', '', '', '', 'Event leader'),
(6, 'Research', '2016-05-24', 'Thao2' , '', '', '', '', 'Event leader'),
(7, 'Lifeline', '2016-05-24', 'Thao1' , '', '', '', '', 'Associate'),
(8, 'Research', '2016-05-24', 'Thao2' , '', '', '', '', 'Associate');





