package DataService;

import java.util.List;

import DataTransferObjects.PatientDto;

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
	 * Retrieves the patientDto by PatID. null if cannot be found.
	 *
	 * @param PatID
	 */
	public PatientDto getPatient(int PatID);
	
	/**
	 * Retrieves a list of patients by the specified name.
	 * 
	 * @return List of results. null if there was an error completing the request.
	 */
	public List<PatientDto> queryPatientByName(String first, String last);
}
