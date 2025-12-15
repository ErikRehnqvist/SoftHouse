package util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import model.People;
import model.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileReader {
    public void readFile(String filePath) {
        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream(filePath);

        if (inputStream == null) {
            throw new IllegalStateException("The file " + filePath + " could not be found.");
        }

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(inputStream))) {
            LineParser lineParser = new LineParser();
            String lineOfText = reader.readLine().trim();
            List<Person> people = new ArrayList<>();
            StateTracker     stateTracker = new StateTracker();
            while (lineOfText != null) {
                lineParser.parseLine(lineOfText, people, stateTracker);
                lineOfText = reader.readLine();
            }
            //System.out.println(people);
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            People peopleWrapper = new People(people);
            String xml = xmlMapper.writeValueAsString(peopleWrapper);
            System.out.println(xml);

        } catch (IOException e) {
            System.out.println("Could not read file");
        }
    }
}
