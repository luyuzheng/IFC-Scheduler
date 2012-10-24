CREATE TABLE IF NOT EXISTS Patient
(
PatID int,
FirstName varchar(255),
LastName varchar(255),
PhoneNumber varchar(255), 
Notes text
);

CREATE TABLE IF NOT EXISTS NoShow
(
NoShowID int,
PatID int
);

CREATE TABLE IF NOT EXISTS Appointment
(
ApptID int,
PractSchedID int,
PatID int,
NowShowID int,
StartTime time,
EndTime time,
Status varchar(255),
ApptDate date,
Note text
);


CREATE TABLE IF NOT EXISTS Practioner
(
PractID int,
TypeID int, 
FirstName varchar(255),
LastName varchar(255),
ApptLength int,
PhoneNumber varchar(255),
Notes text
);

CREATE TABLE IF NOT EXISTS ServiceType
(
TypeID int,
TypeName varchar(255)
);

CREATE TABLE IF NOT EXISTS PractionerScheduled
(
PractSchID int,
PractID int,
StartTime time,
EndTime time
);

CREATE TABLE IF NOT EXISTS Day
(
DayDate date,
StartTime time,
EndTime time
);

CREATE TABLE IF NOT EXISTS Waitlist
(
WaitlistID int, 
PatID int,
TypeID int,
DatetimeEntered datetime
);



