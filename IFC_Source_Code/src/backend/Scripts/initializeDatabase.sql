CREATE DATABASE IF NOT EXISTS ifc_db;
USE ifc_db;
GRANT ALL ON ifc_db.* TO 'testuser'@'localhost' IDENTIFIED BY 'test623' WITH GRANT OPTION;
GRANT ALL ON ifc_db.* TO 'testuser'@'192.168.1.9' IDENTIFIED BY 'test623' WITH GRANT OPTION;
GRANT ALL ON ifc_db.* TO 'testuser'@'192.168.1.8' IDENTIFIED BY 'test623' WITH GRANT OPTION;
CREATE TABLE IF NOT EXISTS Patient
(
PatID int NOT NULL PRIMARY KEY AUTO_INCREMENT, 
FirstName varchar(255),
LastName varchar(255),
PhoneNumber varchar(255), 
Active int,
Notes text
);

CREATE TABLE IF NOT EXISTS NoShow
(
NoShowID int NOT NULL PRIMARY KEY AUTO_INCREMENT,
PatID int,
NoShowDate date
);

CREATE TABLE IF NOT EXISTS Appointment
(
ApptID int NOT NULL PRIMARY KEY AUTO_INCREMENT,
PractSchedID int,
PatID int,
NoShowID int,
StartTime int,
EndTime int,
Status varchar(255),
ApptDate date,
Confirmation tinyint DEFAULT 0, 
Note text
);

CREATE TABLE IF NOT EXISTS Practitioner
(
PractID int NOT NULL PRIMARY KEY AUTO_INCREMENT,
TypeID int, 
FirstName varchar(255),
LastName varchar(255),
ApptLength int,
PhoneNumber varchar(255),
Active int, 
Notes text
);

CREATE TABLE IF NOT EXISTS ServiceType
(
TypeID int NOT NULL PRIMARY KEY AUTO_INCREMENT,
TypeName varchar(255) UNIQUE
);

CREATE TABLE IF NOT EXISTS PractitionerScheduled
(
PractSchID int NOT NULL PRIMARY KEY AUTO_INCREMENT,
PractID int,
ScheduleDate date,
StartTime int,
EndTime int
);

CREATE TABLE IF NOT EXISTS Day
(
DayDate date NOT NULL PRIMARY KEY,
StartTime int,
EndTime int,
Status tinyint
);

CREATE TABLE IF NOT EXISTS Waitlist
(
WaitlistID int NOT NULL PRIMARY KEY AUTO_INCREMENT, 
PatID int,
TypeID int,
DatetimeEntered datetime,
Comments text
);

CREATE TABLE IF NOT EXISTS DefaultHours(
Day varchar(255),
StartTime int,
EndTime int,
Status tinyint
);

INSERT INTO DefaultHours (Day, StartTime, EndTime, Status) VALUES("Monday", 840, 1080, 1);
INSERT INTO DefaultHours (Day, StartTime, EndTime, Status) VALUES("Tuesday", 900, 1140, 1);
INSERT INTO DefaultHours (Day, StartTime, EndTime, Status) VALUES("Wednesday", 960, 1200, 0);
INSERT INTO DefaultHours (Day, StartTime, EndTime, Status) VALUES("Thursday", 960, 1200, 1);
INSERT INTO DefaultHours (Day, StartTime, EndTime, Status) VALUES("Friday", 960, 1200, 0);
INSERT INTO DefaultHours (Day, StartTime, EndTime, Status) VALUES("Saturday", 960, 1200, 0);
INSERT INTO DefaultHours (Day, StartTime, EndTime, Status) VALUES("Sunday", 960, 1200, 0);
