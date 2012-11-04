CREATE TABLE IF NOT EXISTS Patient
(
PatID int NOT NULL PRIMARY KEY AUTO_INCREMENT, 
FirstName varchar(255),
LastName varchar(255),
PhoneNumber varchar(255), 
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
StartTime time,
EndTime time,
Status varchar(255),
ApptDate date,
Note text
);

CREATE TABLE IF NOT EXISTS Practioner
(
PractID int NOT NULL PRIMARY KEY AUTO_INCREMENT,
TypeID int, 
FirstName varchar(255),
LastName varchar(255),
ApptLength int,
PhoneNumber varchar(255),
Notes text
);

CREATE TABLE IF NOT EXISTS ServiceType
(
TypeID int NOT NULL PRIMARY KEY AUTO_INCREMENT,
TypeName varchar(255)
);

CREATE TABLE IF NOT EXISTS PractionerScheduled
(
PractSchID int NOT NULL PRIMARY KEY AUTO_INCREMENT,
PractID int,
StartTime time,
EndTime time
);

CREATE TABLE IF NOT EXISTS Day
(
DayDate date NOT NULL PRIMARY KEY,
StartTime time,
EndTime time
);

CREATE TABLE IF NOT EXISTS Waitlist
(
WaitlistID int NOT NULL PRIMARY KEY AUTO_INCREMENT, 
PatID int,
TypeID int,
DatetimeEntered datetime
);
