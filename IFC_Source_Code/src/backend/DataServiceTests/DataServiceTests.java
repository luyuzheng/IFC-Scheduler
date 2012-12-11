package backend.DataServiceTests;


import gui.TimeSlot;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

import backend.DataService.DataService;
import backend.DataService.DataService.Day;
import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.AppointmentDto;
import backend.DataTransferObjects.DayDto;
import backend.DataTransferObjects.NoShowDto;
import backend.DataTransferObjects.PatientDto;
import backend.DataTransferObjects.PractitionerDto;
import backend.DataTransferObjects.SchedulePractitionerDto;
import backend.DataTransferObjects.TypeDto;
import backend.DataTransferObjects.WaitlistDto;

public class DataServiceTests extends TestCase {

	DataService dataService;
	
	@Before
	public void setUp() throws Exception {
		Connection Conn = DriverManager.getConnection(
				"jdbc:mysql://localhost/?user=root&password=password"); 
		Statement s = Conn.createStatement();
		
		File testScript = new File("./src/backend/Scripts/initTestDB.sql");
		Scanner scriptScanner = new Scanner(testScript);
		scriptScanner.useDelimiter(";");
		while(scriptScanner.hasNext()) {
			String query = scriptScanner.next().trim();
			if (!query.isEmpty()) {
				s.executeUpdate(query);
			}
		}
		
		dataService = DataServiceImpl.create(
				"ifc_unit_test_db", "localhost:3306", "unit_test_user", "test634");
	}

	@After
	public void tearDown() throws Exception {
		Connection Conn = DriverManager.getConnection(
				"jdbc:mysql://localhost/?user=root&password=password"); 
		Statement s = Conn.createStatement();
		s.executeUpdate("DROP USER 'unit_test_user'@'localhost'");
		s.executeUpdate("DROP DATABASE ifc_unit_test_db");
		dataService.close();
	}
	
	public void testSetup() {
		assertTrue(true);
	}

	/** TEST IS FLAKY (sometimes fails when it should pass) */
	public void test_close() {
		dataService.close();
		try {
			List<PatientDto> patients = dataService.getAllPatients();
			if (patients == null) {
				return;
			}
		} catch (RuntimeException e) {
			return;
		}
		fail();
	}

	public void test_addPatient_getPatient() {
		PatientDto patient = new PatientDto();
		String first = "test";
		String last = "43w9";
		String notes = "notes notes notesnotesu3204reu9we";
		String phone = "(453)125-3782";
		
		patient.setFirst(first);
		patient.setLast(last);
		patient.setNotes(notes);
		patient.setPhone(phone);
		dataService.addPatient(phone, first, last, notes);
		PatientDto pat = dataService.getPatient(1);
		assertEquals(pat, patient);
		
		patient.setField(PatientDto.PATIENT_ID, 123);
		patient.setFirst(first);
		patient.setLast(last);
		patient.setNotes(notes);
		patient.setPhone(phone);
		dataService.addPatient(patient);
		pat = dataService.getPatient(123);
		assertEquals(pat, patient);
	}

	public void test_updatePatient() {
		PatientDto patient = new PatientDto();
		String first = "test";
		String last = "43w9";
		String notes = "notes notes notesnotesu3204reu9we";
		String phone = "(453)125-3782";
		String newNotes = "This is my new note, see how it gleams.";
		
		patient.setFirst(first);
		patient.setLast(last);
		patient.setNotes(notes);
		patient.setPhone(phone);
		dataService.addPatient(patient);
		PatientDto pat = dataService.getPatient(1);
		assertEquals(pat, patient);
		
		dataService.updatePatient(PatientDto.NOTES, newNotes, pat);
		patient.setNotes(newNotes);
		assertEquals(patient, dataService.getPatient(1));
	}

	public void test_removePatient() {
		PatientDto patient = new PatientDto();
		String first = "test";
		String last = "43w9";
		String notes = "notes notes notesnotesu3204reu9we";
		String phone = "(453)125-3782";
		String newNotes = "This is my new note, see how it gleams.";
		
		patient.setFirst(first);
		patient.setLast(last);
		patient.setNotes(notes);
		patient.setPhone(phone);
		dataService.addPatient(patient);
		PatientDto pat = dataService.getPatient(1);
		assertEquals(pat, patient);
		
		dataService.removePatient(pat);
		List<PatientDto> patients = dataService.getAllPatients();
		assertFalse(patients.contains(pat));
	}

	public List<PatientDto> test_getAllPatients() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<PatientDto> test_queryPatientByName(String first, String last) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<NoShowDto> test_getNoShowsByPatient(int PatID) {
		// TODO Auto-generated method stub
		return null;
	}

	public TypeDto test_getType(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	public TypeDto test_addNewPractitionerType(String serviceType) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean test_removePractitionerType(String serviceType) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<TypeDto> test_getAllPractitionerTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<PractitionerDto> test_getAllPractitioners() {
		// TODO Auto-generated method stub
		return null;
	}

	public PractitionerDto test_addPractitioner(int TypeID, String first,
			String last, int appLength, String PhoneNumber, String notes) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean test_removePractitioner(PractitionerDto practitioner) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean test_updatePatient(PatientDto patient) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean test_updatePractitionerInfo(PractitionerDto practitioner) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<SchedulePractitionerDto> test_getAllPractitionersForDay(DayDto day) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean test_removePractitionerFromDay(int practSchedId, DayDto day) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean test_changePractitionerHoursForDay(
			SchedulePractitionerDto practitioner, DayDto day, int start, int end) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean test_resetPractitionerHoursForDay(
			SchedulePractitionerDto practitioner, DayDto day, int start, int end) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean test_addPatientToAppointment(int patID, AppointmentDto appointment) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean test_removePatientFromAppointment(AppointmentDto appointment) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean test_checkAsNoShow(AppointmentDto appointment) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean test_uncheckAsNoShow(AppointmentDto appointment) {
		// TODO Auto-generated method stub
		return false;
	}

	public WaitlistDto test_addPatientToWaitlist(PatientDto patient, TypeDto type,
			String comments) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean test_removePatientFromWaitlist(PatientDto patient, TypeDto type) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean test_commentWaitlist(WaitlistDto entry, String comment) {
		// TODO Auto-generated method stub
		return false;
	}

	public Timestamp test_getOldestWaitlistTime() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean test_updateWaitlistTime(WaitlistDto entry, Timestamp time) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<WaitlistDto> test_getWaitlist() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean test_setHoursForDay(DayDto day, int start, int end) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean test_setStatus(DayDto day) {
		// TODO Auto-generated method stub
		return false;
	}

	public SchedulePractitionerDto test_addPractitionerToDay(PractitionerDto pract,
			DayDto day, int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}

	public DayDto test_getOrCreateDay(Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	public PractitionerDto test_getPractitioner(int PractID) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<AppointmentDto> test_getAllAppointments(int schedPractId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<AppointmentDto> test_getFutureAppointmentsByPatId(int patID) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean test_confirmAppointment(AppointmentDto appointment) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean test_updateWaitlist(WaitlistDto wp) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean test_removePatientFromWaitlist(WaitlistDto patient) {
		// TODO Auto-generated method stub
		return false;
	}

	public ArrayList<AppointmentDto> test_searchForAppointments(int typeid) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean test_addNotesToAppointment(AppointmentDto appointment) {
		// TODO Auto-generated method stub
		return false;
	}

	public ArrayList<AppointmentDto> test_getAllPatientsForDay(Date day) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean test_unConfirmAppointment(AppointmentDto appointment) {
		// TODO Auto-generated method stub
		return false;
	}

	public TimeSlot test_getDayTimeslot(Day day) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean test_setTimeSlot(Day day, TimeSlot newtimes) {
		// TODO Auto-generated method stub
		return false;
	}
}
