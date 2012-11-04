package DataService;

import java.util.List;

import DataTransferObjects.NoShowDto;
import DataTransferObjects.PatientDto;

/**
 * Interface for the data service backend for scheduler database.
 * The implementation of this interface will have a static create method
 * for generating instances of this data service. Methods in this interface
 * are used to query data from the database.
 * TODO: add more methods as needed and we will try to implement them.
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
	 * Returns a list no shows according patient.
	 *
	 * @param PatID
	 * @return List of no shows associated with the patient
	 */
	public List<NoShowDto> getNoShowsByPatient(int PatID);
	
	/**
	 * Returns a list of no shows in the last six months for the specified patient.
	 */
	public int getNoShowCountInLastSixMonths(PatientDto patient);
    
    /**
     * Adds a new Practitioner type to the database
     * 
     * @param type
     */
    public boolean addNewPractitionerType(String type);
    
    /**
     * Removed a Practitioner type from the database
     * should also remove any practitioners registered only under that
     * type.
     *
     * @param type
     */
    public boolean removePractitionerType(String type);
    
    /** Retrieves a list of practitioner types.
     * @return List of types in the form of strings. null if there was
     *an error completing the request.
     */
    public List<String> getAllPractitionerTypers();
    
    /** Retrieves a list of all practitioners
    */
    public List<PractitionerDto> getAllPractioners();
    
    /** Add a practitioner to the database
    */
    public boolean addPractitioner(PractitionerDto practitioner);
    
    /**
     * Remove a practitioner from the database
     */
    public boolean removePractitioner(PractitionerDto practitioner);
    
    /** 
     * Change a practitioners info 
     */
    public boolean updatePractitionerInfo(PractitionerDto practitioner);
    
    /**
     * Retrives list of all practitioners in a day
     */
    public List<PractitionerDto> getAllPractitionersForDay(DayDto day);
    
    /**
     * get list of each practitioner's appointment on a day
     */
    public List<AppointmentDto> getPractitionersAppointments(int practID, DayDto day);
    
    /**
     * remove a practitioner from a scheduled day
     * should also remove all appointments
     */
    public boolean removePractitionerFromDay(int practId, DayDto day);
    
    /**
     * change hours of operation for a practitioner on a day
     * should also remove any affected appointments
     */
    public boolean changePractitionerHoursForDay(PractitionerDto practitioner, DayDto day);
    
    /**
     * Create appointment when adding a practitioner to day
     * this appointment is not yet assigned to a patient
     */
    public boolean addAppointmentsToDay(DayDto day, int patID);
    
    /**
     *remove appointments when removing a practitioner from a day
     * need to alert
     */
    public boolean removeAppointmentsFromDay(DayDto day, int patId);
    
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
    public boolean uncheckAsNoShow(AppointmentDto);
    
    /**
     * add patient to waitlist
     */
    public boolean addPatientToWaitlist(PatientDto patient, String type);
    
    /**
     * remove patient from waitlist
     */
    public boolean removePatientFromWaitlist(PatientDto, String type);
    
    /**
     * get waitlist info
     */
    public WaitlistDto getWaitlist();
    
    /**
     * sets hours of operation for a day
     */
    public boolean setHoursForDay(DayDto day);
    
    /**
     * set open or closed on day
     */
    public boolean setStatus(DayDto day);
    
    /**
     * get a list of all practitioners for the day
     */
    public List<AppointmentDto> getAppointmentsForPractitioner(DayDto, int practID);
    
    
}
