package DataService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import DataTransferObjects.NoShowDto;
import DataTransferObjects.PatientDto;

public class DataServiceImpl implements DataService {

	// just for testing
	public static void main(String[] args) {
		//String url = "jdbc:mysql://localhost:3306/test";
		String user = "testuser";
		String password = "test623";

		DataService serv = DataServiceImpl.create("test", "localhost:3306", user, password);

		//        PatientDto patient = new PatientDto();
		//        patient.setFirst("Dead").setLast("Bowie").setPhone(3215552314L).setNotes("ELE member");
		//        serv.addPatient(patient);

		for (PatientDto patient : serv.queryPatientByName("Felicia", "Day")) {
			System.out.println(patient);
		}
		//System.out.println(serv.queryPatientByName("Dead", "Bowie").get(0));
		serv.close();
	}

	private final String url;
	private final String user;
	private final String password;

	private Connection connection;

	private DataServiceImpl(String url, String user, String password) {
		// Instantiate this class with DataService.create(String dbName, String serverAddr);
		this.url = url;
		this.user = user;
		this.password = password;
	}

	/**
	 * Creates an instance of DataService connected to the specified database service.
	 * Returns null if a connection cannot be made.
	 * Don't forget to close the connection after you're done!
	 * 
	 * @param dbName example:test
	 * @param serverAddr example:localhost:3306
	 * @param username example:testuser
	 * @param password example:test623
	 * @return An instance of DataService with an active connection. null if can't connect
	 */
	public static DataService create(
			String dbName, String serverAddr, String username, String password) {

		DataServiceImpl service = new DataServiceImpl(
				"jdbc:mysql://" + serverAddr + "/" + dbName, username, password);

		try {
			service.connection =
				DriverManager.getConnection(service.url, service.user, service.password);
		} catch (SQLException e) {
			Logger lgr = Logger.getLogger(DataServiceImpl.class.getName());
			lgr.log(Level.SEVERE, e.getMessage(), e);
			return null;
		}

		return service;
	}

	@Override
	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			Logger lgr = Logger.getLogger(DataServiceImpl.class.getName());
			lgr.log(Level.SEVERE, "DataService.close() failed.\n" + e.getMessage(), e);
		}
	}

	@Override
	public boolean addPatient(PatientDto patient) {
		PreparedStatement st = null;

		try {
			if (patient.getPatID() == null) {
				st = connection.prepareStatement(
						"INSERT INTO Patients (First, Last, Phone, Notes) VALUES (?, ?, ?, ?)");
			} else {
				st = connection.prepareStatement(
						"INSERT INTO Patients (First, Last, Phone, Notes, PatID) " +
				"VALUES (?, ?, ?, ?, ?)");
				st.setInt(5, patient.getPatID());
			}
			st.setString(1, patient.getFirst());
			st.setString(2, patient.getLast());
			st.setLong(3, patient.getPhone()); //TODO: npe when getPhone returns null
			st.setString(4, patient.getNotes());
			st.executeUpdate();
			return true;
		} catch (SQLException e) {
			Logger lgr = Logger.getLogger(DataServiceImpl.class.getName());
			lgr.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(DataServiceImpl.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		return false;
	}
	
	@Override
	public boolean removePatient(PatientDto patient) {
		PreparedStatement st = null;

		try {
			if (patient.getPatID() == null) {
				Logger lgr = Logger.getLogger(DataServiceImpl.class.getName());
				lgr.log(Level.WARNING, "Tried to delete patient without ID");
				return false;
			} else {
				st = connection.prepareStatement(
				"DELETE FROM Patients WHERE PatID=\'?\'");
				st.setInt(1, patient.getPatID());
			}
			st.executeUpdate();
			return true;
		} catch (SQLException e) {
			Logger lgr = Logger.getLogger(DataServiceImpl.class.getName());
			lgr.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(DataServiceImpl.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		return false;
	}

	@Override
	public PatientDto getPatient(int PatID) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = connection.prepareStatement("SELECT * FROM Patients WHERE PatID=(?)");
			st.setInt(1, PatID);
			rs = st.executeQuery();
			PatientDto patient = new PatientDto();
			if (rs.next()) {
				patient.setField(PatientDto.PATIENT_ID, rs.getInt(1));
				patient.setField(PatientDto.FIRST, rs.getString(2));
				patient.setField(PatientDto.LAST, rs.getString(3));
				patient.setField(PatientDto.PHONE, rs.getLong(4));
				patient.setField(PatientDto.NOTES, rs.getString(5));
				return patient;
			}

			return null;
		} catch (SQLException e) {
			Logger lgr = Logger.getLogger(DataServiceImpl.class.getName());
			lgr.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(DataServiceImpl.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		return null;
	}

	@Override
	public List<PatientDto> getAllPatients() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = connection.prepareStatement("SELECT * FROM Patients");
			rs = st.executeQuery();
			List<PatientDto> results = new ArrayList<PatientDto>();
			PatientDto patient = new PatientDto();
			while (rs.next()) {
				patient.setField(PatientDto.PATIENT_ID, rs.getInt(PatientDto.PATIENT_ID));
				patient.setField(PatientDto.FIRST, rs.getString(PatientDto.FIRST));
				patient.setField(PatientDto.LAST, rs.getString(PatientDto.LAST));
				patient.setField(PatientDto.PHONE, rs.getLong(PatientDto.PHONE));
				patient.setField(PatientDto.NOTES, rs.getString(PatientDto.NOTES));
				results.add(patient);
				patient = new PatientDto();
			}
			return results;
		} catch (SQLException e) {
			Logger lgr = Logger.getLogger(DataServiceImpl.class.getName());
			lgr.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(DataServiceImpl.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		return null;
	}

	@Override
	public List<PatientDto> queryPatientByName(String first, String last) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = connection.prepareStatement("SELECT * FROM Patients WHERE First=? AND Last=?");
			st.setString(1, first);
			st.setString(2, last);
			rs = st.executeQuery();
			List<PatientDto> results = new ArrayList<PatientDto>();
			PatientDto patient = new PatientDto();
			while (rs.next()) {
				// TODO: Will the columns always be the same order?
				patient.setField(PatientDto.PATIENT_ID, rs.getInt(PatientDto.PATIENT_ID));
				patient.setField(PatientDto.FIRST, rs.getString(PatientDto.FIRST));
				patient.setField(PatientDto.LAST, rs.getString(PatientDto.LAST));
				patient.setField(PatientDto.PHONE, rs.getLong(PatientDto.PHONE));
				patient.setField(PatientDto.NOTES, rs.getString(PatientDto.NOTES));
				results.add(patient);
				patient = new PatientDto();
			}
			return results;
		} catch (SQLException e) {
			Logger lgr = Logger.getLogger(DataServiceImpl.class.getName());
			lgr.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(DataServiceImpl.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		return null;
	}

	@Override
	public List<NoShowDto> getNoShowsByPatient(int patID) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = connection.prepareStatement("SELECT * FROM NoShows WHERE PatID=?");
			st.setInt(1, patID);
			rs = st.executeQuery();
			List<NoShowDto> results = new ArrayList<NoShowDto>();
			NoShowDto noShow = new NoShowDto();
			while (rs.next()) {
				// TODO: Will the columns always be the same order?
				noShow.setField(NoShowDto.NOSHOW_ID, rs.getInt(NoShowDto.NOSHOW_ID));
				noShow.setField(NoShowDto.PATIENT_ID, rs.getString(NoShowDto.PATIENT_ID));
				noShow.setField(NoShowDto.DATE, rs.getString(NoShowDto.DATE));
				results.add(noShow);
				noShow = new NoShowDto();
			}
			return results;
		} catch (SQLException e) {
			Logger lgr = Logger.getLogger(DataServiceImpl.class.getName());
			lgr.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(DataServiceImpl.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		return null;
	}
}
