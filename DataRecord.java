import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//class for CO2 data record
public class DataRecord {

    private final LocalDateTime timestamp; //time date data was recorded
    private final String userID; //userID of data being submitted 
    private final String postcode; //postcode of data location
    private final double co2ppm; //CO2 concentration

    public DataRecord(LocalDateTime timestamp, String userID,
                      String postcode, double co2ppm) {
        this.timestamp = timestamp; //set timestamp
        this.userID = userID; //set user ID
        this.postcode = postcode; //set postcode
        this.co2ppm = co2ppm; //set CO2 vale
    }

    //export as CSV with the local date and time
    public String toCSV() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME; //format of date/time
        return formatter.format(timestamp) + "," + userID + "," + postcode + "," + co2ppm; //all data into one csv string
    }

    @Override
    public String toString() {
        return "DataRecord{" +
               "timestamp=" + timestamp +
               ", userID='" + userID + '\'' +
               ", postcode='" + postcode + '\'' +
               ", co2ppm=" + co2ppm +
               '}';
    }
}
