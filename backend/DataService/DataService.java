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
     */
    public void addNewPractitionerType(String type);
    
    /**
     * Removed a Practitioner type from the database
     * should also remove any practitioners registered only under that
     * type.
     */
    public void removePractitionerType(String type);
    
    /** Retrieves a list of practitioner types.
     * @return List of types in the form of strings. null if there was
     *an error completing the request.
     */
    public List<String> getAllPractitionerTypers();
    
    
        
    
}
