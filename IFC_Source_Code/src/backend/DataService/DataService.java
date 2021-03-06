package backend.DataService;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

import backend.DataTransferObjects.*;

/**
 * Interface for the data service backend for scheduler database.
 * The implementation of this interface will have a static create method
 * for generating instances of this data service. Methods in this interface
 * are used to query data from the database.
 */
public interface DataService {

    /**
     * Closes the connection. Instance is unusable afterwards.
     */
	public void close();

	/**
	 * Adds a patient to the database.
	 * Adds a new patient with a new PatID is PatID is null in the DTO,
	 * otherwise, it overwrites whatever had the PatID last.
	 *
	 * @param patient
	 * @return true if succeeded, false if failed
	 */
	public boolean addPatient(PatientDto patient);

	/**
	 * Adds a patient given the parameters
	 * Does not add a patient with the same name and phone number
	 * If the patient exists, it will reactivate the patient and return him.
	 */
	public PatientDto addPatient(String first, String last, String phone, String notes);

	/**
	 * Updates the specified field for the specified patient in the database and DTO
	 *
	 * @param name of the field to be updated (use constants in {@link PatientDto})
	 * @return true if succeeded, false if failed
	 */
	public boolean updatePatient(String fieldName, Object value, PatientDto patient);
	
	/**
	 * Removes a patient from the database using the PatID in the PatientDto
	 *
	 * @param patient Contains the PatID to delete
	 * @return True if succeeds false if error is encountered.
	 */
	public boolean removePatient(PatientDto patient);
	
	/**
	 * Retrieves the patientDto by PatID. null if cannot be found.
	 *
	 * @param PatID
	 */
	public PatientDto getPatient(int PatID);

	/**
	 * Retrieves a list of all patients
	 */
	public List<PatientDto> getAllPatients();

	/**
	 * Retrieves a list of patients by the specified name.
	 * 
	 * @return List of results. null if there was an error completing the request.
	 */
	public List<PatientDto> queryPatientByName(String first, String last);
	
	/**
	 * Returns a list no shows within the past six months for a given patient.
	 *
	 * @param PatID
	 * @return List of no shows associated with the patient
	 */
	public List<NoShowDto> getNoShowsByPatient(int PatID);

	/**
	 * @param type name
	 * @return type
	 */
	public TypeDto getType(String type);    

	/**
	 * @param type id
	 * @return type
	 */
	public TypeDto getTypeByID(int typeID);
	
	/**
	 * Adds a new Practitioner type to the database
     * 
     * @param type
     */
    public TypeDto addNewPractitionerType(String serviceType);
    
    /**
     * Removed a Practitioner type from the database
     * should also remove any practitioners registered only under that
     * type.
     *
     * @param type
     */
    public boolean removePractitionerType(String serviceType);
    
    /**
     * Changes the name of the specified type.
     * TypeID remains the same, so that practitioners associated with
     * a type will have their types updated as well
     */
    public boolean updatePractitionerType(String newName, TypeDto type);
    
    /** Retrieves a list of practitioner types.
     * @return List of types in the form of strings. null if there was
     *an error completing the request.
     */
    public List<TypeDto> getAllPractitionerTypes();
    
    /** Retrieves a list of all practitioners
    */
    public List<PractitionerDto> getAllPractitioners();
    
    /** Add a practitioner to the database
    */
    public PractitionerDto addPractitioner(int TypeID, String first, String last, int appLength, String PhoneNumber, String notes);
    
    /**
     * Remove a practitioner from the database
     */
    public boolean removePractitioner(PractitionerDto practitioner);
    
    /**
     * @param patient with the fields to change
     * @return 
     */
    public boolean updatePatient(PatientDto patient);
    
    /** 
     * Change a practitioners info 
     */
    public boolean updatePractitionerInfo(PractitionerDto practitioner);
    
    /**
     * Retrives list of all practitioners in a day
     */
    public List<SchedulePractitionerDto> getAllPractitionersForDay(DayDto day);
    
    /**
     * remove a practitioner from a scheduled day
     * should also remove all appointments
     */
    public boolean removePractitionerFromDay(int practSchedId, DayDto day);
    
    /**
     * truncates hours of operation for a practitioner on a day
     * DOES NOT CHANGE THE APPOINTMENTS
     */
    public boolean changePractitionerHoursForDay(SchedulePractitionerDto practitioner, DayDto day,
    		int start, int end);
    
    /**
     * Changes hours of operation for a practitioner on a day while reseting all appointments to
     * that practitioner
     */
    public boolean resetPractitionerHoursForDay(SchedulePractitionerDto practitioner, DayDto day,
    		int start, int end);
    
    /**
     * Adds a patient to an appointment
     */
    public boolean addPatientToAppointment(int patID, AppointmentDto appointment);
    
    /**
     * removes a patient from an appointment
     */
    public boolean removePatientFromAppointment(AppointmentDto appointment);
    
    /**
     * check a patient as a no show
     */
    public boolean checkAsNoShow(AppointmentDto appointment);
    
    /**
     * uncheck a patient as a no show
     */
    public boolean uncheckAsNoShow(AppointmentDto appointment);
    
    /**
     * add patient to waitlist
     */
    public WaitlistDto addPatientToWaitlist(PatientDto patient, TypeDto type, String comments);
    
    /**
     * remove patient from waitlist
     */
    public boolean removePatientFromWaitlist(int waitlistID);
    
    /**
     * Adds comments to waitlist entry
     */
    public boolean commentWaitlist(WaitlistDto entry, String comment);
    
    /**
     * Returns the timestamp of the oldest.
     */
    public Timestamp getOldestWaitlistTime();
    
    /**
     * Updates the time set in time entered. Used to change priorities.
     */
    public boolean updateWaitlistTime(WaitlistDto entry, Timestamp time);
    
    /**
     * get waitlist info
     */
    public List<WaitlistDto> getWaitlist();
    
    /**
     * sets hours of operation for a day
     */
    public boolean setHoursForDay(DayDto day, int start, int end);
    
    /**
     * set open or closed on day
     */
    public boolean setStatus(DayDto day);
    
    /**
     * Schedules a practioner on a day
     */
    public SchedulePractitionerDto addPractitionerToDay(PractitionerDto pract, DayDto day, 
        int start, int end);
    
    /**
     * Gets a day given a date
     */
    public DayDto getOrCreateDay(Date date);
    
    /**
     * Gets a practioner from an ID
     */
    public PractitionerDto getPractitioner(int PractID);
    
    /**
     * Gets all appointments for a scheduledPract 
     */
    public List<AppointmentDto> getAllAppointments(int schedPractId);
    
    /**
     * Gets all future patients by the patient ID
     */
    public List<AppointmentDto> getFutureAppointmentsByPatId(int patID);
    
    /**
     * Marks confirmation to true on both the DTO object and on the database.
     */
    public boolean confirmAppointment(AppointmentDto appointment);

    /** Updates the waitlist when a patient has been added to it. */
    public boolean updateWaitlist(WaitlistDto wp);
    
    /**
     * Finds all appointments of type with typeid.
     * If typeid = -1, then it will search all types.
     */
    public ArrayList<AppointmentDto> searchForAppointments(int typeid);
    
    /** Updates the note field in the database as modified in appointment*/
    public boolean addNotesToAppointment(AppointmentDto appointment);
    
    /** Return all the appointments with scheduled patients on the day*/
    public ArrayList<AppointmentDto> getAllPatientsForDay(Date day);
    
    /** Changes the status of an appointment to unconfirmed*/
    public boolean unConfirmAppointment(AppointmentDto appointment);

    /** The days of the week */
    public enum Day {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
        THURSDAY, FRIDAY, SATURDAY 
    }
    
    /** Returns the default timeslot for that day of the week*/
    public gui.TimeSlot getDayTimeslot(Day day);
    
    /** Sets the default timeslot for the day of the week */
    public boolean setTimeSlot(Day day, gui.TimeSlot newtimes);        
}


