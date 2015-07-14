package backend.DataServiceTests;


import gui.TimeSlot;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

import backend.DataService.DataService;
import backend.DataService.DataService.Day;
import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.AppointmentDto;
import backend.DataTransferObjects.PatientDto;

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
		patient.setField(PatientDto.PATIENT_ID, 1);
		patient.setField(PatientDto.NO_SHOW, 0);
		dataService.addPatient(phone, first, last, notes);
		PatientDto pat = dataService.getPatient(1);
		assertEquals(pat, patient);
		
		patient.setField(PatientDto.PATIENT_ID, 123);
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
		patient.setField(PatientDto.PATIENT_ID, 1);
		patient.setField(PatientDto.NO_SHOW, 0);
		dataService.addPatient(phone, first, last, notes);
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
		
		patient.setFirst(first);
		patient.setLast(last);
		patient.setNotes(notes);
		patient.setPhone(phone);
		patient.setField(PatientDto.PATIENT_ID, 1);
		patient.setField(PatientDto.NO_SHOW, 0);
		dataService.addPatient(phone, first, last, notes);
		PatientDto pat = dataService.getPatient(1);
		assertEquals(pat, patient);
		
		dataService.removePatient(pat);
		List<PatientDto> patients = dataService.getAllPatients();
		assertFalse(patients.contains(pat));
	}

	public void test_getAllPatients() {
		PatientDto patient = new PatientDto();
		PatientDto patient2 = new PatientDto();
		String first = "test";
		String last = "43w9";
		String notes = "notes notes notesnotesu3204reu9we";
		String phone = "(453)125-3782";

		patient.setField(PatientDto.PATIENT_ID, 1);
		patient.setField(PatientDto.NO_SHOW, 0);
		patient.setFirst(first);
		patient.setLast(last);
		patient.setNotes(notes);
		patient.setPhone(phone);
		patient2.setField(PatientDto.PATIENT_ID, 2);
		patient2.setField(PatientDto.NO_SHOW, 0);
		patient2.setFirst(first + "2");
		patient2.setLast(last + "2");
		patient2.setNotes(notes + "2");
		patient2.setPhone(phone + "2");
		dataService.addPatient(patient);
		dataService.addPatient(patient2);
		PatientDto pat = dataService.getPatient(1);
		assertEquals(pat, patient);
		pat = dataService.getPatient(2);
		assertEquals(pat, patient2);
		List<PatientDto> pats = dataService.getAllPatients();
		System.out.println(pats);
		assertTrue(pats.contains(patient));
		assertTrue(pats.contains(patient2));
	}

	public void test_addNotesToAppointment() {
		AppointmentDto appointment = new AppointmentDto();
		appointment.setField(AppointmentDto.APPT_ID, 1);
		appointment.setField(AppointmentDto.PRACT_SCHED_ID,1);
		appointment.setField(AppointmentDto.PAT_ID,1);
		appointment.setField(AppointmentDto.NO_SHOW_ID,null);
		appointment.setField(AppointmentDto.START, 0);
		appointment.setField(AppointmentDto.END,30);
		appointment.setField(AppointmentDto.APPT_DATE, new Date(10000000));
		appointment.setField(AppointmentDto.NOTE, null);
		appointment.setField(AppointmentDto.CONFIRMATION, 0);
		appointment.setField(AppointmentDto.PRACTITIONER_NAME, "Test One");
		
		assertNull(appointment.getNote());
		appointment.setField(AppointmentDto.NOTE, "Here is a note.");
		
		dataService.addNotesToAppointment(appointment);
		assertEquals(appointment.getNote(), "Here is a note");
	}
	
	public void test_confirmAppointment() {
		AppointmentDto appointment = new AppointmentDto();
		appointment.setField(AppointmentDto.APPT_ID, 1);
		appointment.setField(AppointmentDto.PRACT_SCHED_ID,1);
		appointment.setField(AppointmentDto.PAT_ID,1);
		appointment.setField(AppointmentDto.NO_SHOW_ID,null);
		appointment.setField(AppointmentDto.START, 0);
		appointment.setField(AppointmentDto.END,30);
		appointment.setField(AppointmentDto.APPT_DATE, new Date(10000000));
		appointment.setField(AppointmentDto.NOTE, null);
		appointment.setField(AppointmentDto.CONFIRMATION, 0);
		appointment.setField(AppointmentDto.PRACTITIONER_NAME, "Test One");
		
		dataService.confirmAppointment(appointment);
		assertTrue(appointment.getConfirmation());
		dataService.unConfirmAppointment(appointment);
		assertFalse(appointment.getConfirmation());
	}

	public boolean test_setTimeSlot() {
		TimeSlot ts = new TimeSlot(0,30);
		dataService.setTimeSlot(Day.SUNDAY,ts);
		TimeSlot newTS = dataService.getDayTimeslot(Day.SUNDAY);
		assertEquals(ts,newTS);
		
		// TODO Auto-generated method stub
		return false;
	}
}
