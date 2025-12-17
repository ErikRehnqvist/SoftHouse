package util;

import model.Address;
import model.Family;
import model.Person;
import model.Phone;
import model.XMLType;

import java.util.List;

public class LineParser {
    private static final int INDEX_ONE = 1;
    private static final int INDEX_TWO = 2;
    private static final int INDEX_THREE = 3;

    public void parseLine(String lineOfText,
                          List<Person> people,
                          StateTracker stateTracker) {
        if (!lineOfText.matches(".*\\|.*\\|.*")) {
            throw new IllegalArgumentException("The input does not contain the necessary split characters");
        }
        String[] lineOfTextAsArray = lineOfText.split("\\|");
        String xmlType = lineOfTextAsArray[0].toUpperCase();
        XMLType type = XMLType.fromCode(xmlType);
        createObjects(type, lineOfTextAsArray, people, stateTracker);
    }

    private void createObjects(XMLType xmlType,
                               String[] line,
                               List<Person> people,
                               StateTracker stateTracker) {
        switch (xmlType) {
            case PERSON -> {
                Person person = new Person();
                person.setFirstName(line[INDEX_ONE]);
                person.setLastName(line[INDEX_TWO]);
                people.add(person);
                stateTracker.setPerson(person);
                stateTracker.setFamily(null);
            }
            case ADDRESS -> {
                Address address = new Address();
                address.setStreet(line[INDEX_ONE]);
                address.setCity(line[INDEX_TWO]);
                if (line.length > 3) {
                    address.setPostCode(line[INDEX_THREE]);
                }
                if (stateTracker.getFamily() != null) {
                    stateTracker.getFamily().setAddress(address);
                } else if (stateTracker.getPerson() != null) {
                    stateTracker.getPerson().setAddress(address);
                } else {
                    throw new IllegalStateException("Error: Address without a belonging person or family");
                }
            }
            case PHONE -> {
                Phone phone = new Phone();
                phone.setCellPhone(line[INDEX_ONE]);
                phone.setPhone(line[INDEX_TWO]);
                if (stateTracker.getFamily() != null) {
                    stateTracker.getFamily().setPhone(phone);
                } else if (stateTracker.getPerson() != null) {
                    stateTracker.getPerson().setPhone(phone);
                } else {
                    throw new IllegalStateException("Error: Phone without a belonging person or belonging family");
                }
            }
            case FAMILY -> {
                Family family = new Family();
                family.setName(line[INDEX_ONE]);
                family.setBirthYear(line[INDEX_TWO]);
                if (stateTracker.getPerson() == null) {
                    throw new IllegalStateException("Error: Family without a belonging person");
                }
                List<Family> families = stateTracker.getPerson().getFamilies();
                families.add(family);
                stateTracker.setFamily(family);
            }
        }
    }
}
