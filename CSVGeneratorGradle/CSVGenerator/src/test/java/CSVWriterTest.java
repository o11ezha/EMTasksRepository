import org.example.CSVField;
import org.example.CSVWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CSVWriterTest {

    private CSVWriter writer;
    private Path tempFile;
    private List<BusinessObject> testData;

    @BeforeEach
    void setUp() throws IOException {
        Map<String, Object> settings = new HashMap<>();
        settings.put("withHeaders", true);
        settings.put("lineSeparator", System.lineSeparator());
        settings.put("delimiter", ';');
        settings.put("nullValue","None");

        writer = new CSVWriter(settings);

        tempFile = Files.createTempFile("testCSV", ".csv");

        testData = Arrays.asList(
                new BusinessObject(123, "Danya", "Developer", "IT", "88 888 888"),
                new BusinessObject(124, "Artem", "Manager", "Business", "88 888 887"),
                new BusinessObject(125, "Nikita", "Team Lead", "IT", "88 888 886"));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testWriteToFile() throws IOException {
        writer.writeCSV(testData, tempFile.toString());

        assertTrue(Files.exists(tempFile));

        List<String> lines = Files.readAllLines(tempFile);
        List<String> expectedLines = Arrays.asList(
                "Emp ID;Name;Title;Department;Phone",
                "123;Danya;Developer;IT;88 888 888",
                "124;Artem;Manager;Business;88 888 887",
                "125;Nikita;Team Lead;IT;88 888 886"
        );

        assertEquals(expectedLines.size(), lines.size());

        for (int i = 0; i < expectedLines.size(); i++) {
            assertEquals(expectedLines.get(i), lines.get(i));
        }
    }
}

class BusinessObject {
    @CSVField("Emp ID")
    private int id;

    @CSVField("Name")
    private String name;

    @CSVField("Title")
    private String title;

    @CSVField("Department")
    private String department;

    @CSVField("Phone")
    private String phone;

    public BusinessObject(int id, String name, String title, String department, String phone) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.department = department;
        this.phone = phone;
    }
}
