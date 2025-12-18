import java.io.*;

public class CSVLogger {

    private static final String FILE_NAME = "co2_readings.csv";
    private static final String HEADER = "timestamp,userID,postcode,CO2 ppm";

    public static synchronized void writeRecord(DataRecord record) {
        File file = new File(FILE_NAME);

        try {
            // If file does not exist, create it and write header + record
            if (!file.exists() || file.length() == 0) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(HEADER);
                    writer.newLine();
                    writer.write(record.toCSV());
                    writer.newLine();
                }
                return;
            }

            // Check if header exists
            boolean headerExists;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String firstLine = reader.readLine();
                headerExists = firstLine != null && firstLine.equals(HEADER);
            }

            // If header is missing, create a temp file with header + old data + new record
            if (!headerExists) {
                File tempFile = new File("temp.csv");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
                     BufferedReader reader = new BufferedReader(new FileReader(file))) {

                    // Write header
                    writer.write(HEADER);
                    writer.newLine();

                    // Copy old data
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.newLine();
                    }

                    // Write new record
                    writer.write(record.toCSV());
                    writer.newLine();
                }

                // Replace old file with temp file
                if (!file.delete() || !tempFile.renameTo(file)) {
                    System.err.println("Failed to update CSV with header.");
                }
                return;
            }

            // If header exists, just append new record
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(record.toCSV());
                writer.newLine();
            }

        } catch (IOException e) {
            System.err.println("CSV write error: " + e.getMessage());
        }
    }
}
