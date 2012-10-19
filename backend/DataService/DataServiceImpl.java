package DataService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import DataTransferObjects.PatientDto;

public class DataServiceImpl implements DataService {

    // just for testing
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "testuser";
        String password = "test623";
        
        DataService serv = DataServiceImpl.create("test", "localhost:3306", user, password);
        PatientDto patient = new PatientDto();
        patient.setFirst("Dead").setLast("Bowie").setPhone(3215552314L).setNotes("ELE member");
        serv.addPatient(patient);
        
        System.out.println(serv.getPatient(2));
        //System.out.println(serv.queryPatientByName("Dead", "Bowie").get(0));
        
        /*
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(url, user, password);
            
            st = con.prepareStatement("INSERT INTO Patients (First, Last, Phone, Notes) VALUES (?, ?, ?, ?)");
            st.setString(1, "Nathan");
            st.setString(2, "Fillion");
            st.setLong(3, 1235551234);
            st.setString(4, "Cpt. Malcom Reynolds");
            st.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DataServiceImpl.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DataServiceImpl.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }*/
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
            service.connection = DriverManager.getConnection(service.url, username, password);
        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(DataServiceImpl.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
        
        return service;
    }
    
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
    public PatientDto getPatient(int PatID) {
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            st = connection.prepareStatement(
                    "SELECT * FROM Patients WHERE PatID=(?)");
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
    public List<PatientDto> queryPatientByName(String first, String last) {
        // TODO: implement
        return null;
    }
}
