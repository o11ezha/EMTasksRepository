import org.example.CSVField;
import org.example.CSVWriter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CSVWriterTest {

    @Test
    public void testWriteToFile() throws IOException {
        Map<String, Object> settings = new HashMap<>();
        settings.put("withHeaders", true);
        settings.put("lineSeparator", System.lineSeparator());
        settings.put("delimiter", ';');
        settings.put("nullValue","None");

        CSVWriter writer = new CSVWriter(settings);

        List<BusinessObject> list = new ArrayList<>();

        list.add(new BusinessObject(123, "Danya", "Developer", "IT", "88 888 888"));
        list.add(new BusinessObject(124, "Artem", "Manager", "Business", "88 888 887"));
        list.add(new BusinessObject(125, "Nikita", "Team Lead", "IT", "88 888 886"));

        String filePath = "example.csv";

        writer.writeCSV(list, filePath);

        File file = new File(filePath);
        assertTrue(file.exists());

        List<String> lines = Files.readAllLines(Paths.get(filePath));
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

        Files.delete(Paths.get(filePath));
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
